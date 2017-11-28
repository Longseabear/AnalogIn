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
	public static SceneManager createScene(String str){
		System.out.println("Change into " + str);
		switch(str){
		case "CreateGame":
			return new Scene_CreateGame();
		case "Start":
			return new Scene_Start();
		case "Lobby":
			return new Scene_Lobby();
		}

		System.out.println("ERROR " + str);
		return null;
	}
}
