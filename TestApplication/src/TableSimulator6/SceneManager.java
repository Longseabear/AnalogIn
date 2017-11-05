package TableSimulator6;

import java.awt.Graphics;

public class SceneManager {
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
		System.out.println("Error");
		return null;	

	}
}
