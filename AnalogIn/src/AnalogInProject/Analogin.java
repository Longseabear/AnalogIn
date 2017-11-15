package AnalogInProject;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Analogin extends JFrame {
	private Image screenImage;
	private Graphics screenGraphic;

 // 크기에 맞추어 처리한 예
	
	public Analogin() {
		//GameInformationMaster Init
		GIM.GameObject = this;
		
		setUndecorated(true);
		setTitle("Table Simulator");
		setSize(Main.SCREEN_SIZE_X, Main.SCREEN_SIZE_Y);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setBackground(new Color(0, 0, 0, 0));
		setLayout(null);

		//init key
		SceneManager.createScene("CreateGame");
	}
	
	public void changeScene(SceneManager preScene, String str){
		preScene.removeScene();
		SceneManager.createScene(str);
	}
	
	// double buffer
	public void paint(Graphics g) { // 가장 첫번째로 화면을 그려주는 함수. 약속된 함수.
		screenImage = createImage(Main.SCREEN_SIZE_X, Main.SCREEN_SIZE_Y);
		screenGraphic = screenImage.getGraphics();
		
		if(GIM.currentScene != null)
			GIM.currentScene.screenDraw(screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}
}