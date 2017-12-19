package AnalogInProject;

import java.util.ArrayList;
import java.util.LinkedList;

public class KeyInputController extends Thread {
	//
	KeyInputCollector meCollection;
	public ArrayList<ArrayList<String>> otherUserCollection = new ArrayList<ArrayList<String>>();
	// Turn Timer
	int turn = 0; // 1또는 0
	int collectionTime = 30;
	int processTime = 30;

	
	private boolean inputEmpty(){
		if(!meCollection.input[turn].isEmpty())
			return false;
		for(ArrayList<String> otherUser : otherUserCollection){
			if(!otherUser.isEmpty()){
				return false;
			}
		}
		return true;
	}
	private void processInputBuffer(double maxF) {
		ArrayList<String> me = meCollection.input[turn];
		ArrayList<Integer> eachTime =  new ArrayList<Integer>();
		for(int i=0;i!=GIM.playingGameRoom.User.size();i++)
			eachTime.add(i, null);			
		
		while(!inputEmpty()){
			for(int i=0;i!=GIM.playingGameRoom.User.size();i++){
				if(eachTime.get(i)==null)
				{
					String s;
					if(i==0){ 
						s = me.get(0);
					}
					else{
						s = otherUserCollection.get(i-1).get(0);
					}
					String[] commend = s.split("_");
					// System.out.println(commend[0]+" "+commend[1]+" "+commend[2]);
					int time = Integer.parseInt(commend[4]);
					eachTime.set(i, time);
				}
			}
			
			
		}
		for (String s : me) {
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
	private int turnChange(){
		if(turn==1){
			turn = 0;
		}else{
			turn = 1;
		}
		return 204871571;
	}
	@Override
	public void run() {
		try {
			// 1, 2 Turn 진행 첫턴은 40 miliScenoe; 
			GIM.currentInputCollector = new KeyInputCollector(turn);
			turnCollection.add(GIM.currentInputCollector);
			Thread.sleep(40);
			turnCollection.getLast().communicationTime = 40*1000000;
			GIM.currentInputCollector = null;
			
			
			//Loop
			do {
				// X Turn ( X > 2 )
				// Input Collect Start
				turnChange(); // Turn Change
				GIM.currentInputCollector = new KeyInputCollector(turn); // new Turn
				turnCollection.add(GIM.currentInputCollector);				
				currentTurn = turnCollection.getFirst();
				turnCollection.removeFirst();

				int maximumTime = currentTurn.communicationTime / 1000000;
				int turnStartTime = (int) System.nanoTime();
				// LockStep - 여기서 주어진 시간은 input 시간
				// input 정규화 후 전송
				// ---player에게 전송, 확인, RTT를 계산해서 기록
				// wihle(lockStep())
				//Thread.sleep(secondPerTurn);
				// GameTurn step
				Thread.sleep(collectionTime);
				int remainFrame = 2;
//				System.out.println(":"+maximumTime +" "+remainFrame);
				for(int i=1;i<=remainFrame; i++)
				{
					int start = (int)System.nanoTime();
					processInputBuffer((double)i/remainFrame);
					int end = (int)System.nanoTime();
					int runningTime = (int) (end - start) / 1000000;
					if (runningTime < processTime/remainFrame) {
						Thread.sleep(processTime - runningTime);
					} else {
						System.out.println("runningTime :" + runningTime);
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
