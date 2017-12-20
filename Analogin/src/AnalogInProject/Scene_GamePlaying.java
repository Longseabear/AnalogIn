
package AnalogInProject;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Scene_GamePlaying extends SceneManager {
	/************************************************
	 * Resource
	 *************************************************/
	// Music
	// private Audio introMusic;

	public static int port= 8888;
	
	// get audio format for voice chat
	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat (sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
	
	
	
	public SourceDataLine audio_out;
	
	// Image
	private Image background = ImageManager.background;
	private Image image_mainFrame = ImageManager.mainFrame;
	private Image image_subFrame = ImageManager.subFrame;

	/************************************************
	 * Component/object
	 *************************************************/
	private Scene_GamePlaying thisInstance = this; // thisInstance
	private JButton createBlockButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_1.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton imageLoadButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_2.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton deleteButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_3.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton ruleButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_4.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton saveButton = new JButton();
	private JButton loadButton = new JButton();
	private JButton exitButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_5.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	public JButton setupButton = new JButton(
			new ImageIcon(ImageManager.setupImage.getScaledInstance(350, 350, Image.SCALE_SMOOTH)));
	public static JButton VoicechatButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_6.getScaledInstance(50,  50,  Image.SCALE_SMOOTH)));
	private JLabel createBlockField = new JLabel();

	/************************************************
	 * GameController
	 *************************************************/
	private ArrayList<Thread> backendObject = new ArrayList<Thread>();
	blockChangedListener blockChangeStateListener;
	Font font1 = new Font("SansSerif", Font.BOLD, 11);

	public void init() throws IOException {
		server_fr.init_audio();
		System.out.println("******Voice chat server start**********");
	}

	/// GAME APPLICATION이 실행될 때 반드시 초기화해야하는 GIM 변수
	/// - KeyInputBuffer / GIM-currentScene / GIM-blockPriority // BlockObject
	public Scene_GamePlaying() {
		//
		GIM.currentScene = this;
		// Input 등록
		GIM.keyInputBuffer = new KeyInputController();
		GIM.keyInputBuffer.start();
		GIM.blockObject = blockObject;

		// Block의 우선순위
		GIM.blockPriority = 1;

		// init
		try {
			init();
		} catch (Exception e) {
			System.out.println("INIT ERRORR");
		}
		;
		// setup
		setupButton.setVisible(false);
		setupButton.setBounds(902, 210, 350, 350);
		setupButton.setBorderPainted(false); // 버튼 배치 테스트 때문에 true로 변경
		setupButton.setContentAreaFilled(false); // 채우지마
		setupButton.setFocusPainted(false);
		setupButton.setLayout(null);
		systemObject.add(setupButton);
		
		createBlockButton.setBounds(910, 90, 100, 100);
		createBlockButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
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

		// image Upload button
		imageLoadButton.setBounds(1027, 90, 100, 100);
		imageLoadButton.setBorderPainted(true); // 버튼 범위 측정 때문에 true로 변경 및 테스트함
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
				if (GIM.getCheckdBlock() != null) {
					String path = ImageManager.loadImage();
					if (path != null && GIM.getCheckdBlock() != null) {
						GIM.getCheckdBlock().blockInfo.setImagePath(path);
						GIM.getCheckdBlock().synBlockInfo();
					}
				}
			}
		});
		systemObject.add(imageLoadButton);

		// delete button
		deleteButton.setBounds(1146, 90, 100, 100);
		deleteButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		deleteButton.setContentAreaFilled(false);
		deleteButton.setFocusPainted(false);
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				deleteButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				deleteButton.setVisible(true);
			}
		});
		systemObject.add(deleteButton);

		createBlockField.setBounds(0, 20, 1280, 700); // 
		createBlockField.setVisible(false);
		createBlockField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		createBlockField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
//				System.out.println(e.getX() + " " + e.getY());
				Block b = new Block(new BlockInformation(e.getX(), e.getY(), 30, 30, ""));
				blockObject.add(b);
				GIM.GameObject.add(b, GIM.blockPriority);
				createBlockField.setVisible(false);
			}
		});
		systemObject.add(0, createBlockField);

		// rule button
		ruleButton.setBounds(910, 580, 175, 115);
		ruleButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		ruleButton.setContentAreaFilled(false); // 채우지마
		ruleButton.setFocusPainted(false);
		ruleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				ruleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				ruleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ruleButton.setVisible(true);
			}
		});
		systemObject.add(ruleButton);
		
		// save button
		saveButton.setBounds(1090, 580, 175, 50);
		saveButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		saveButton.setContentAreaFilled(false); // 채우지마
		saveButton.setFocusPainted(false);
		saveButton.setText("SAVE");

		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				saveButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				//save
				String gameName = Scene_GamePlaying.setGameName();
				String saveforder = SaveLoadManager.getSaveDirectory();
						
				if(saveforder!=null && !gameName.equals(""))
				{
					if(SaveLoadManager.saveMap(blockObject, saveforder, "Chess rule", gameName))
						System.out.println("Save Success");
					else
						System.out.println("Save Fail");
				}
			}
		});
		systemObject.add(saveButton);

		// save button
		loadButton.setBounds(1090, 645, 175, 50);
		loadButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		loadButton.setContentAreaFilled(false); // 채우지마
		loadButton.setFocusPainted(false);
		loadButton.setText("LOAD");
		loadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				loadButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				ArrayList<BlockInformation> blockInfo = null;
				String loadpath = SaveLoadManager.getLoadDirectory();
				if(loadpath!=null)
					blockInfo = SaveLoadManager.loadMap(loadpath);
				GIM.loadedBlockInfo = blockInfo;
				GIM.GameObject.changeScene(thisInstance, "CreateGame");
			}
		});
		systemObject.add(loadButton);

		// exit button
		exitButton.setBounds(1220, 8, 29, 29);
		exitButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		exitButton.setContentAreaFilled(false); // 채우지마
		exitButton.setFocusPainted(false);
		exitButton.addMouseListener(new MouseAdapter() {
			// 누르면 나가게 이벤트 처리해줘야 함
			@Override
			public void mouseEntered(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				exitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				exitButton.setVisible(true);
			}
		});
		systemObject.add(exitButton);
		
		VoicechatButton.setBounds(1146, 33, 50, 50);
		VoicechatButton.setBorderPainted(true);
		VoicechatButton.setContentAreaFilled(false);
		VoicechatButton.setFocusPainted(false);
		VoicechatButton.setVisible(true);
		VoicechatButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				VoicechatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				VoicechatButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			boolean audioStart = true;
			public void mousePressed(MouseEvent e) {
				client_fr.init_audio();
			}
		});
		systemObject.add(VoicechatButton);
		
		
		
		// CACHE
		System.out.println("loadedBlockInfo load start");
		if(GIM.loadedBlockInfo!=null){
			for(BlockInformation f : GIM.loadedBlockInfo){
				blockObject.add(new Block(f));
			}
			System.out.println("Success okokok");
			GIM.loadedBlockInfo = null;
		}
		
		
		for (Component b : systemObject) {
			GIM.GameObject.add(b);
		}
		GIM.blockPriority = systemObject.size();
		for (Block b : blockObject) {
			GIM.GameObject.add(b);
		}
		for (Thread t : backendObject) {
			t.start();
		}
		GIM.GameObject.repaint();

		// introMusic = new Audio("testMusic.mp3", true); 자꾸 에러나서 주석처리 해놓음
		// introMusic.start();
		System.out.println("REAL START");
	}

	@Override
	public void removeScene() {
		GIM.removeGIM();
		for (Component c : systemObject) {
			GIM.GameObject.remove(c);
		}
		for (Block b : blockObject) {
			GIM.GameObject.remove(b);
		}
		for (Thread t : backendObject) {
			t.interrupt();
		}
		// introMusic.close();
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, 1280, 720, null);
		g.drawImage(image_subFrame, 890, 35, 375, 670, null);
		g.drawImage(image_mainFrame, 15, 35, 860, 670, null);

		GIM.GameObject.paintComponents(g);
		GIM.GameObject.repaint();
	}
	private static String setGameName() {
        return JOptionPane.showInputDialog(
            GIM.GameObject,
            "Input Game Name",
            "Welcome to the making!",
            JOptionPane.QUESTION_MESSAGE);
    }
	// UTIL
	public static boolean isNum(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
