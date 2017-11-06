package AnalogInProject;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;

public class SceneManager {
	public ArrayList<Block> blockObject = new ArrayList<Block>();
	public ArrayList<Component> systemObject = new ArrayList<Component>();
	
	//override
	public void screenDraw(Graphics g) {
	}
	public void removeScene(){
		System.exit(0);
	}
	public static SceneManager createScene(int i){
		switch(i){
		case 1:
			return new Scene1(GameInformationMaster.GameObject);
		case 2:
			return new Scene2(GameInformationMaster.GameObject);
		}
		return null;
	}
}
