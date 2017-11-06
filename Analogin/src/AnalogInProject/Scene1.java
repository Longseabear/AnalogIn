package AnalogInProject;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Scene1 extends SceneManager {
	private Analogin GameObject; // Root Class
	
	/************************************************
	   Resource
	*************************************************/
	// Music
	private Audio introMusic;
	
	// Image
	private Image background = ImageManager.background;
	
	/************************************************
	   Component/object
	*************************************************/	
	private SceneManager thisInstance = this; // thisInstance
	
	private JButton testButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage.getScaledInstance(400, 100, Image.SCALE_SMOOTH)));
	
	public Scene1(Analogin _tableSimulator) {
		GameObject = _tableSimulator;
		GameObject.currentScene = this;
		//Input 등록
		GameInformationMaster.keyInputBuffer = new KeyInputBuffer();
		GameInformationMaster.keyInputBuffer.start();
		// Icon normal
		testButton.setBounds(500, 500, 400, 100);
		testButton.setBorderPainted(false);
		testButton.setContentAreaFilled(false); // 채우지마
		testButton.setFocusPainted(false);
		testButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				testButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				testButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// 눌렀을때 처리 화면을 바꿔보자.
				GameObject.changeScene(thisInstance, 2);
			}
		});
		GameObject.add(testButton); // JFrame에 버튼을 추가.
		systemObject.add(testButton);

		blockObject.add(new Block(new BlockInformation(400,300,300,300,ImageManager.testButtonImage)));
		blockObject.add(new Block(new BlockInformation(500,100,300,300,ImageManager.testButtonImage)));

		for(Block b : blockObject){
			GameObject.add(b);	
		}
		
		introMusic = new Audio("testMusic.mp3", true);
		introMusic.start();
		
		GameObject.repaint();
	}
	@Override
	public void removeScene()
	{
		for(Component c : systemObject){
			GameObject.remove(c);			
		}
		for(Block b : blockObject){
			GameObject.remove(b);			
		}
		introMusic.close();
	}
	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		GameObject.paintComponents(g);
		GameObject.repaint();
	}
}
