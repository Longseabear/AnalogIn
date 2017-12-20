package AnalogInProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class KeyInputController extends Thread {
	// Turn Timer
	int turn = 0; // 1또는 0
	int collectionTime = 30;
	int processTime = 30;
	int collectionIncreaseValue = 1;
	int processIncreaseValue = 1;

	boolean collectionError = false;
	boolean processTimeError = false;

	/*
	 * KeyInputController Create
	 */
	public KeyInputController() {
		GIM.currentInputCollector = new KeyInputCollector(turn);
	}

	/*
	 * Sort with Number & Name
	 */
	private void sortInputBuffer(ArrayList<String> t) {
		Collections.sort(t, new Comparator<String>() {
			public int compare(String one, String other) {
				long timeOne = Long.valueOf(one.split("_")[5]);
				long timetwo = Long.valueOf(other.split("_")[5]);

				if (timeOne == timetwo) {
					return one.split("_")[0].compareTo(other.split("_")[0]);
				} else {
					return timeOne > timetwo ? 1 : -1;
				}
			}
		});
	}

	/*
	 * process Input Buffer for all user
	 */
	private void processInputBuffer(double maxF, ArrayList<String> _input) {
		ArrayList<String> input = new ArrayList<>(_input);
		for (String s : input) {
			// TYPE_OBJECTNUMBER_X_Y_OPTION
			String[] commend = s.split("_");
			// System.out.println(commend[0]+" "+commend[1]+" "+commend[2]);
			int objNum = Integer.parseInt(commend[2]);
			int x = Integer.parseInt(commend[3]);
			int y = Integer.parseInt(commend[4]);
			long time = Long.valueOf(commend[5]);

			if (time > maxF * GIM.currentInputCollector.communicationTime[turn]) {
				return;
			}
			_input.remove(s);

			if (commend[1].equals("CLICK")) {
				// System.out.println("ok");
				GIM.GameObject.remove(GIM.blockObject.get(objNum));
				GIM.GameObject.add(GIM.blockObject.get(objNum), GIM.blockPriority);
			} else if (commend[1].equals("MOVE")) {
				// System.out.println("ok2");
				GIM.blockObject.get(objNum).setLocation(x, y);
				GIM.blockObject.get(objNum).blockInfo.x = x;
				GIM.blockObject.get(objNum).blockInfo.y = y;
			}
		}
	}

	private void turnChange() {
		if (turn == 1) {
			turn = 0;
		} else {
			turn = 1;
		}
	}

	private int getNextTurn() {
		if (turn == 1) {
			return 0;
		} else {
			return 1;
		}
	}

	/*
	 * Handler Wait -> All user wait
	 */
	public void inputPropagation(ArrayList<String> input) throws InterruptedException {
		ArrayList<String> send = new ArrayList<String>(input);
		send.add(0, "GAME_INPUT_JAR");
		send.add(1, collectionError + " " + processTimeError);

		for (NetworkPeer a : NetworkPeerManager.peers) {
			a.SenderObject(send);
		}
		for (NetworkPeer a : NetworkPeerManager.peers) {
			synchronized (a.networkJar) {
				while (!a.networkJar.containsKey("GAME_INPUT_JAR")) {
					a.networkJar.wait();
				}
				ArrayList<String> temp = (ArrayList<String>) a.networkJar.get("GAME_INPUT_JAR");
				String[] error = temp.get(0).split(" ");
				temp.remove(0);
				collectionError = collectionError && Boolean.valueOf(error[0]);
				processTimeError = processTimeError && Boolean.valueOf(error[1]);

				input.addAll(temp);
				a.networkJar.remove("GAME_INPUT_JAR");
			}
		}
		// lock step valuati
		if (collectionError) {
			if (collectionIncreaseValue > 0) {
				collectionIncreaseValue = -1;
			} else {
				collectionIncreaseValue *= 2;
			}
		} else {
			if (collectionIncreaseValue <= 0) {
				collectionIncreaseValue = 1;
			} else {
				collectionIncreaseValue *= 2;
			}
		}
		collectionTime -= collectionIncreaseValue;
		if(collectionTime < 5){
			collectionTime = 5;
		}
		
		if (processTimeError) {
			if (processIncreaseValue > 0) {
				processIncreaseValue = -1;
			} else {
				processIncreaseValue *= 2;
			}
		} else {
			if (processIncreaseValue <= 0) {
				processIncreaseValue = 1;
			} else {
				processIncreaseValue *= 2;
			}
		}
		processTime -= processIncreaseValue;
		if(processTime < 5){
			processTime = 5;
		}
	}

	@Override
	public void run() {
		try {
			// 1, 2 Turn 진행 첫턴은 40 miliScenoe;

			GIM.currentInputCollector.startCollection(getNextTurn());
			Thread.sleep(40);
			GIM.currentInputCollector.communicationTime[getNextTurn()] = 40 * 1000000;
			// Loop
			do {
				// X Turn ( X > 2 )
				// Input Collect Start
				turnChange();
				// System.out.println(turn + " " + getNextTurn());

				GIM.currentInputCollector.startCollection(getNextTurn());
				long turnStartTime = System.nanoTime();
				/*
				 * COLEECTION TIME
				 */
				ArrayList<String> processInput;
				if (turn == 0) {
					processInput = new ArrayList<String>(GIM.currentInputCollector.input0);
					GIM.currentInputCollector.input0.clear();
				} else {
					processInput = new ArrayList<String>(GIM.currentInputCollector.input1);
					GIM.currentInputCollector.input1.clear();
				}
				inputPropagation(processInput);
				sortInputBuffer(processInput);
				// wihle(lockStep())
				// Thread.sleep(secondPerTurn);
				// GameTurn step
				// Thread.sleep(collectionTime);
				long collectionRunningTime = collectionTime - ((System.nanoTime() - turnStartTime) / 1000000);
				if (collectionRunningTime > 0) {
					Thread.sleep(collectionRunningTime);
					collectionError = false;
				} else {
					System.out.println("runningTime :" + collectionRunningTime);
					collectionError = true;
				}

				/*
				 * PROCESS TIME
				 */
				int remainFrame = 2;
				// System.out.println(":"+maximumTime +" "+remainFrame);

				processTimeError = false;
				for (int i = 1; i <= remainFrame; i++) {
					long start = System.nanoTime();

					processInputBuffer((double) i / remainFrame, processInput);
					long end = System.nanoTime();
					long runningTime = (end - start) / 1000000;
					if (runningTime < processTime / remainFrame) {
						// System.out.println(runningTime + " and " +
						// (processTime - runningTime));
						Thread.sleep(processTime - runningTime);
					} else {
						System.out.println("runningTime :" + runningTime);
						processTimeError = true;
					}
				}
				// System.out.println((System.nanoTime()-turnStartTime)/1000000);

				long turnEndTime = System.nanoTime();
				GIM.currentInputCollector.communicationTime[getNextTurn()] = turnEndTime - turnStartTime;
				// Status Update
				Thread.sleep(5);
//				System.out.println(collectionTime + " " + processTime);
			} while (true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
