package AnalogInProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Scene_CreateGame extends SceneManager {
	/************************************************
	 * Resource
	 *************************************************/
	// Music
	// private Audio introMusic;

	// Image
	private Image background = ImageManager.background;
	private Image image_mainFrame = ImageManager.mainFrame;
	private Image image_subFrame = ImageManager.subFrame;

	/************************************************
	 * Component/object
	 *************************************************/
	private Scene_CreateGame thisInstance = this; // thisInstance
	private JButton createBlockButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_1.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton imageLoadButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_2.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton deleteButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_3.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton ruleButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_4.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton exitButton = new JButton(
			new ImageIcon(ImageManager.testButtonImage_5.getScaledInstance(240, 320, Image.SCALE_SMOOTH)));
	private JButton setupButton = new JButton(
			new ImageIcon(ImageManager.setupImage.getScaledInstance(350, 350, Image.SCALE_SMOOTH)));
	private JTextField blockNameTextField = new JTextField();
	private JTextField posXTextField = new JTextField();
	private JTextField posYTextField = new JTextField();
	private JTextField sizeXTextField = new JTextField();
	private JTextField sizeYTextField = new JTextField();
	private JButton staticButton = new JButton();
	private JButton visibleButton = new JButton();
	private JButton behindButton = new JButton();
	private JButton frontButton = new JButton();
	private JButton topButton = new JButton();
	private JButton bottomButton = new JButton();
	private JLabel createBlockField = new JLabel();

	/************************************************
	 * GameController
	 *************************************************/

	Font font1 = new Font("SansSerif", Font.BOLD, 11);

	/// GAME APPLICATION이 실행될 때 반드시 초기화해야하는 GIM 변수
	/// - KeyInputBuffer / GIM-currentScene / GIM-blockPriority // BlockObject
	/// -
	public Scene_CreateGame() {
		//
		GIM.currentScene = this;
		// Input 등록
		GIM.keyInputBuffer = new KeyInputController();
		GIM.keyInputBuffer.start();
		GIM.blockObject = blockObject;

		// Block의 우선순위
		GIM.blockPriority = 1;

		// setup
		setupButton.setBounds(902, 210, 350, 350);
		setupButton.setBorderPainted(false); // 버튼 배치 테스트 때문에 true로 변경
		setupButton.setContentAreaFilled(false); // 채우지마
		setupButton.setFocusPainted(false);
		setupButton.setLayout(null);
		systemObject.add(setupButton);

		// setup -> name
		blockNameTextField.setBounds(150, 5, 190, 30);
		blockNameTextField.setOpaque(false);
		blockNameTextField.setFont(font1);
		blockNameTextField.setBorder(BorderFactory.createEmptyBorder());
		blockNameTextField.setHorizontalAlignment(JTextField.CENTER);
		setupButton.add(blockNameTextField);

		// setup -> pos X Y
		posXTextField.setBounds(170, 125, 60, 30);
		posXTextField.setOpaque(false);
		posXTextField.setFont(font1);
		posXTextField.setBorder(BorderFactory.createEmptyBorder());
		posXTextField.setHorizontalAlignment(JTextField.CENTER);
		setupButton.add(posXTextField);

		posYTextField.setBounds(260, 125, 60, 30);
		posYTextField.setOpaque(false);
		posYTextField.setFont(font1);
		posYTextField.setBorder(BorderFactory.createEmptyBorder());
		posYTextField.setHorizontalAlignment(JTextField.CENTER);
		setupButton.add(posYTextField);
		// setup -> size X Y
		sizeXTextField.setBounds(170, 175, 60, 30);
		sizeXTextField.setOpaque(false);
		sizeXTextField.setFont(font1);
		sizeXTextField.setBorder(BorderFactory.createEmptyBorder());
		sizeXTextField.setHorizontalAlignment(JTextField.CENTER);
		setupButton.add(sizeXTextField);

		sizeYTextField.setBounds(260, 175, 60, 30);
		sizeYTextField.setOpaque(false);
		sizeYTextField.setFont(font1);
		sizeYTextField.setBorder(BorderFactory.createEmptyBorder());
		sizeYTextField.setHorizontalAlignment(JTextField.CENTER);
		setupButton.add(sizeYTextField);
		// setup -> static
		staticButton.setBounds(114, 278, 27, 27);
		staticButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		staticButton.setContentAreaFilled(false); // 채우지마
		staticButton.setFocusPainted(false);
		staticButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				staticButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				staticButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				staticButton.setVisible(true);
			}
		});
		setupButton.add(staticButton);
		// setup -> visible
		visibleButton.setBounds(285, 277, 27, 27);
		visibleButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		visibleButton.setContentAreaFilled(false); // 채우지마
		visibleButton.setFocusPainted(false);
		visibleButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				visibleButton.setVisible(true);
			}
		});
		setupButton.add(visibleButton);

		// setup -> back
		behindButton.setBounds(271, 315, 74, 27);
		behindButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		behindButton.setContentAreaFilled(false); // 채우지마
		behindButton.setFocusPainted(false);
		behindButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				visibleButton.setVisible(true);
			}
		});
		setupButton.add(behindButton);
		// setup -> bottom
		bottomButton.setBounds(187, 315, 74, 27);
		bottomButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		bottomButton.setContentAreaFilled(false); // 채우지마
		bottomButton.setFocusPainted(false);
		bottomButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				visibleButton.setVisible(true);
			}
		});
		setupButton.add(bottomButton);
		// setup -> bottom
		frontButton.setBounds(102, 315, 74, 27);
		frontButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		frontButton.setContentAreaFilled(false); // 채우지마
		frontButton.setFocusPainted(false);
		frontButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				visibleButton.setVisible(true);
			}
		});
		setupButton.add(frontButton);
		// setup -> TOP
		topButton.setBounds(12, 315, 74, 27);
		topButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		topButton.setContentAreaFilled(false); // 채우지마
		topButton.setFocusPainted(false);
		topButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				visibleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				visibleButton.setVisible(true);
			}
		});
		setupButton.add(topButton);
		
		// button
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
				if (GIM.checkedBlock != null) {
					String path = ImageManager.loadImage();
					if (path != null && GIM.checkedBlock != null) {
						GIM.checkedBlock.blockInfo.setImagePath(path);
						GIM.checkedBlock.synBlockInfo();
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
				deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 이벤트
																		// 처리는
																		// createBlock이랑
																		// 똑같이
																		// 설정되어있음
																		// 누르면
																		// rule이
																		// 뜨게
																		// 해야함
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

		createBlockField.setBounds(0, 20, 1280, 700); // 크기 설정 자유자재 아니였음? 테스트
														// 버전?
		createBlockField.setVisible(false);
		createBlockField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		createBlockField.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e.getX() + " " + e.getY());
				Block b = new Block(new BlockInformation(e.getX(), e.getY(), 300, 300, ""));
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
		// blockObject.add(new Block(new
		// BlockInformation(400,300,300,300,ImageManager.testButtonImage)));
		// blockObject.add(new Block(new
		// BlockInformation(500,100,300,300,ImageManager.testButtonImage)));

		for (Component b : systemObject) {
			GIM.GameObject.add(b);
		}
		GIM.blockPriority = systemObject.size();
		for (Block b : blockObject) {
			GIM.GameObject.add(b);
		}

		GIM.GameObject.repaint();

		// introMusic = new Audio("testMusic.mp3", true); 자꾸 에러나서 주석처리 해놓음
		// introMusic.start();
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
		// introMusic.close();
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, 1280, 720, null);
		g.drawImage(image_subFrame, 890, 35, 375, 670, null);
		g.drawImage(image_mainFrame, 15, 35, 860, 670, null);

		GIM.GameObject.paintComponents(g);
		GIM.GameObject.repaint();
	}
}
