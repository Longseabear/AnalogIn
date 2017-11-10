package AnalogInProject;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Scene_CreateGame extends SceneManager {
	/************************************************
	   Resource
	*************************************************/
	// Music
	private Audio introMusic;
	
	// Image
	private Image background = ImageManager.background;
	private Image image_mainFrame = ImageManager.mainFrame;
	private Image image_subFrame = ImageManager.subFrame;
	
	/************************************************
	   Component/object
	*************************************************/	
	private Scene_CreateGame thisInstance = this; // thisInstance
	private JButton createBlockButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage.getScaledInstance(400, 100, Image.SCALE_SMOOTH)));
	private JButton imageLoadButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage.getScaledInstance(400, 100, Image.SCALE_SMOOTH)));

	
	private JLabel createBlockField = new JLabel();
	
	/************************************************
	   GameController
	*************************************************/	
	
	
	///GAME APPLICATION이 실행될 때 반드시 초기화해야하는 GIM 변수
	/// - KeyInputBuffer / GIM-currentScene / GIM-blockPriority // BlockObject
	/// - 
	public Scene_CreateGame() {
		// 
		GIM.currentScene = this;
		// Input 등록
		GIM.keyInputBuffer = new KeyInputBuffer();
		GIM.keyInputBuffer.start();		
		GIM.blockObject = blockObject;
		
		// Block의 우선순위
		GIM.blockPriority = 1;
		
		// button
		createBlockButton.setBounds(900, 200, 35, 35);
		createBlockButton.setBorderPainted(false);
		createBlockButton.setContentAreaFilled(false); // 채우지마
		createBlockButton.setFocusPainted(false);
		createBlockButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				createBlockButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				createBlockButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				createBlockField.setVisible(true);
			}
		});
		systemObject.add(createBlockButton);
		// inage Upload button
		imageLoadButton.setBounds(950, 200, 35, 35);
		imageLoadButton.setBorderPainted(false);
		imageLoadButton.setContentAreaFilled(false); // 채우지마
		imageLoadButton.setFocusPainted(false);
		imageLoadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				imageLoadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				imageLoadButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(GIM.checkedBlock!=null)
				{
					String path = ImageManager.loadImage();
					if(path!=null && GIM.checkedBlock!=null)
					{
						GIM.checkedBlock.blockInfo.setImagePath(path);
						GIM.checkedBlock.synBlockInfo();				
					}
				}
			}
		});
		systemObject.add(imageLoadButton);
		
		createBlockField.setBounds(0, 20, 1280, 700);
		createBlockField.setVisible(false);
		createBlockField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		createBlockField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getX() + " " + e.getY());
				Block b = new Block(new BlockInformation(e.getX(),e.getY(),50,50,""));
				blockObject.add(b);
				GIM.GameObject.add(b,GIM.blockPriority);
				createBlockField.setVisible(false);
			}
		});
		systemObject.add(0,createBlockField);
		
//		blockObject.add(new Block(new BlockInformation(400,300,300,300,ImageManager.testButtonImage)));
//		blockObject.add(new Block(new BlockInformation(500,100,300,300,ImageManager.testButtonImage)));		
		

		for(Component b : systemObject){
			GIM.GameObject.add(b);	
		}
		GIM.blockPriority = systemObject.size();
		for(Block b : blockObject){
			GIM.GameObject.add(b);	
		}

		GIM.GameObject.repaint();
		
		introMusic = new Audio("testMusic.mp3", true);
		introMusic.start();	
	}
	@Override
	public void removeScene()
	{
		GIM.removeGIM();
		for(Component c : systemObject){
			GIM.GameObject.remove(c);			
		}
		for(Block b : blockObject){
			GIM.GameObject.remove(b);			
		}
		introMusic.close();
	}
	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(image_subFrame, 890, 35, 375, 670, null);
		g.drawImage(image_mainFrame, 15, 35, 860, 670, null);
		
		GIM.GameObject.paintComponents(g);
		GIM.GameObject.repaint();
	}
}
