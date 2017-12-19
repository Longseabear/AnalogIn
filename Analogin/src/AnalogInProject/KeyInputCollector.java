package AnalogInProject;

import java.util.ArrayList;
import java.util.LinkedList;

public class KeyInputCollector extends Thread {
	// 턴을 수집하는 담당.
	// Turn Timer
	int turn = 0;
	int currentTime = (int)System.nanoTime();
	public ArrayList<String>[] input = new ArrayList[2];
	public int communicationTime;
	
	public KeyInputCollector(int _turn){
		turn = _turn;
		input[0] = new ArrayList<String>();
		input[1] = new ArrayList<String>();
	}
	public void setTurn(int _turn)
	{
		turn = _turn;
	}
	public synchronized void playIn(String s) {
		synchronized (input) {
				input[turn].add(s + "_" + ((int) System.nanoTime() - currentTime));				
		}
	}
}
