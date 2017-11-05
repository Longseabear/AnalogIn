package TableSimulator6;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Scene1 extends SceneManager {
	private TableSimulator GameObject; // Root Class
	
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
	private Block testBlock;
	
	
	
	
	public Scene1(TableSimulator _tableSimulator) {
		GameObject = _tableSimulator;
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

		testBlock = new Block(new BlockInformation(400,300,300,300,ImageManager.testButtonImage));
		GameObject.add(testBlock);
		
		
		introMusic = new Audio("testMusic.mp3", true);
		introMusic.start();
		
		GameObject.repaint();
	}
	@Override
	public void removeScene()
	{
		GameObject.remove(testButton);
		GameObject.remove(testBlock);
		introMusic.close();
	}
	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		GameObject.paintComponents(g);
		GameObject.repaint();
	}
}
