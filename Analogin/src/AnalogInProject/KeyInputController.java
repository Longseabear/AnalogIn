package AnalogInProject;

import java.util.ArrayList;
import java.util.LinkedList;

public class KeyInputController extends Thread {
	//
	public LinkedList<KeyInputCollector> turnCollection = new LinkedList<KeyInputCollector>();

	// Turn Timer
	int turn = 1;
	int secondPerTurn = 30;
	KeyInputCollector currentTurn;
	
	private void processInputBuffer(double maxF) {
		ArrayList<String> temp = (ArrayList<String>) currentTurn.input.clone();
		
		for (String s : temp) {
			// TYPE_OBJECTNUMBER_X_Y_OPTION
			String[] commend = s.split("_");
			// System.out.println(commend[0]+" "+commend[1]+" "+commend[2]);
			int objNum = Integer.parseInt(commend[1]);
			int x = Integer.parseInt(commend[2]);
			int y = Integer.parseInt(commend[3]);
			int time = Integer.parseInt(commend[4]);

			if(time > maxF * currentTurn.communicationTime){
				return;
			}

			currentTurn.input.remove(s);
			if (commend[0].equals("CLICK")) {
				// System.out.println("ok");
				GIM.GameObject.remove(GIM.blockObject.get(objNum));
				GIM.GameObject.add(GIM.blockObject.get(objNum), GIM.blockPriority);
			} else if (commend[0].equals("MOVE")) {
				// System.out.println("ok2");
				GIM.blockObject.get(objNum).setLocation(x, y);
				GIM.blockObject.get(objNum).blockInfo.x = x;
				GIM.blockObject.get(objNum).blockInfo.y = y;
			}
		}
	}

	@Override
	public void run() {
		try {
			// 여기서 한번 서버타임 동기화.
			// 1, 2 Turn 진행
			GIM.currentInputCollector = new KeyInputCollector(turn++);
			turnCollection.add(GIM.currentInputCollector);
			Thread.sleep(40);
			turnCollection.getLast().communicationTime = 40*1000000;
			GIM.currentInputCollector = null;
			do {
				// X Turn ( X > 2 )
				// Input Collect Start
				GIM.currentInputCollector = new KeyInputCollector(turn++);
				turnCollection.add(GIM.currentInputCollector);				
				currentTurn = turnCollection.getFirst();
				turnCollection.removeFirst();

				int maximumTime = currentTurn.communicationTime / 1000000;
				int turnStartTime = (int) System.nanoTime();
				// LockStep - 여기서 주어진 시간은 input 시간
				// ---player에게 전송, 확인, RTT를 계산해서 기록
				// wihle(lockStep())
				//Thread.sleep(secondPerTurn);
				// GameTurn step
				Thread.sleep(secondPerTurn);
				int remainFrame = 2;
//				System.out.println(":"+maximumTime +" "+remainFrame);
				for(int i=1;i<=remainFrame; i++)
				{
					int start = (int)System.nanoTime();
					processInputBuffer((double)i/remainFrame);
					
					int end = (int)System.nanoTime();
					int runningTime = (int) (end - start) / 1000000;
					if (runningTime < secondPerTurn) {
						Thread.sleep(secondPerTurn - runningTime);
					} else {
						System.out.println("error :" + runningTime);
					}	
				}
				
				int turnEndTime = (int)System.nanoTime();
				turnCollection.getLast().communicationTime = turnEndTime - turnStartTime;
				GIM.currentInputCollector = null;
				//Status Update
			} while (true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
