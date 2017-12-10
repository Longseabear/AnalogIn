package AnalogInProject;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Map extends JFrame implements Serializable{
	static final ArrayList<BlockInformation> bi = new ArrayList<BlockInformation>();
	static String str = new String();
	
	public void printStr() {
		System.out.println(str);
	}
}
	