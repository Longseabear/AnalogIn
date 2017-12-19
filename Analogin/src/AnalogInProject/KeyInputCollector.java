package AnalogInProject;

import java.util.ArrayList;
import java.util.LinkedList;

public class KeyInputCollector extends Thread {
	// 턴을 수집하는 담당.
	// Turn Timer
	int turn = 0;
//	int currentTime = (int)System.nanoTime();
	public ArrayList<String> input0 = new ArrayList<String>();
	public ArrayList<String> input1 = new ArrayList<String>();
	public int[] currentTime = new int[2];
	public int[] communicationTime = new int[2];
	
	public void startCollection(int _turn){
		turn = _turn;
		currentTime[_turn] = (int)System.nanoTime(); // new Turn Collection start
	}
	public KeyInputCollector(int _turn){
		turn = _turn;
		input0 = new ArrayList<String>();
		input1 = new ArrayList<String>();
	}
	public void setTurn(int _turn)
	{
		turn = _turn;
	}
	public synchronized void playIn(String s) {
		if(turn==0){
			input0.add(GIM.me.id+"_"+s + "_" + ((int) System.nanoTime() - currentTime[turn]));			
		}
		else{
			input1.add(GIM.me.id+"_"+s + "_" + ((int) System.nanoTime() - currentTime[turn]));			
		}
	}
}
