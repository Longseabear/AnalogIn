package AnalogInProject;

import java.util.ArrayList;

public class KeyInputBuffer extends Thread {
	// 이 클래스는 현재 서버시간과 현재 컴퓨터의 시간을 알고 있어야 한다.
	// 모든 클라이언트의 시간을 맞추어 행동을 정의 해주어야 함.
	// 즉 쓰레드로 동작해야만 한다.
	// 5초에 한번씩 인풋명령을 수행
	Analogin GameObject = GIM.GameObject;
	public ArrayList<String> input = new ArrayList<String>();
	
	//Turn Timer
	int timer = 0;
	private void processInputBuffer()
	{
		ArrayList<String> temp = (ArrayList<String>) input.clone();
		 synchronized(input){
				input.clear();
		 }
		for(String s : temp){
			// TYPE_OBJECTNUMBER_X_Y_OPTION
			String[] commend = s.split("_");
	//		System.out.println(commend[0]+" "+commend[1]+" "+commend[2]);
			int objNum = Integer.parseInt(commend[1]);
			int x = Integer.parseInt(commend[2]);
			int y = Integer.parseInt(commend[3]);
			if(commend[0].equals("CLICK")){
	//			System.out.println("ok");
				GIM.GameObject.remove(GIM.blockObject.get(objNum));
				GIM.GameObject.add(GIM.blockObject.get(objNum),GIM.blockPriority);
			}
			else if(commend[0].equals("MOVE")){
	//			System.out.println("ok2");
				GIM.blockObject.get(objNum).setLocation(x, y);
			}
		}
	}
	public synchronized void playIn(String s)
	{
		 synchronized(input){
				input.add(s + "_" + (int)System.nanoTime()/1000000);
		 }
	}
	
	@Override
	public void run(){
		try{
			do{
				long start = System.nanoTime();
				processInputBuffer();
				long end = System.nanoTime();
				int runningTime = (int)(end-start)/1000000;
				if(runningTime<GIM.turnTime)
					Thread.sleep(GIM.turnTime-runningTime);
				else
				{
					Thread.sleep(100);
					System.out.println("error");
				}
				// 일단은 그냥 대기지만, 처리하는 방식을 바꾸어 주어야 한다.
			}while(true);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
