package AnalogInProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class Scene_Lobby extends SceneManager {
	/************************************************
	 * Resource
	 *************************************************/
	// Music
	// private Audio introMusic;
	private JButton backButton = new JButton();

	// Image
	private Image background = ImageManager.Lobby;

	/************************************************
	 * Component/object
	 *************************************************/
	private Scene_Lobby thisInstance = this; // thisInstance
	private JList roomList = new JList();
	public static HashMap<String, RoomInfo> roomInfoList = new HashMap<String, RoomInfo>();
	public static DefaultListModel dlm = new DefaultListModel();
	public static RoomLobby currentRoom = null;

	/************************************************
	 * GameController
	 *************************************************/
	private ArrayList<Thread> backendObject = new ArrayList<Thread>();
	private ArrayList<Handler> systemHandler = new ArrayList<Handler>();
	
	/// GAME APPLICATION이 실행될 때 반드시 초기화해야하는 GIM 변수
	/// - KeyInputBuffer / GIM-currentScene / GIM-blockPriority // BlockObject
	/// -
	// ETC..
	Font font1 = new Font("SansSerif", Font.BOLD, 20);

	/*
	 * room join
	 */
	/*
	 * room refresh
	 */
	private static void roomRef() {
		synchronized (dlm) {
			dlm.clear();
			for (String roomName : roomInfoList.keySet()) {
				dlm.addElement(roomInfoList.get(roomName));
			}
		}
	}
	
	/*
	 * set RoomInfoList
	 */
	public static void synRoomInfoWithServer() {
		System.out.println("[synRoomInfoWithServer] Syn Start");
		synchronized (roomInfoList) {
			roomInfoList = NetworkRoomServer.getRoomInfo();
			if (roomInfoList != null) {
				roomRef();
			}
		}
		if(currentRoom!=null){
			System.out.println("REFRESH");
			currentRoom.refresh();
		}
	}

	/**
	 * init() 1) Room Server register / USER ID AND USER 2) receive Room list 3)
	 * roomList initialization
	 */
	private void init() {
		synRoomInfoWithServer();
		
		for (Thread t : backendObject) {
			t.start();
		}
		systemHandler.add(NetworkRoomServer.systemHandler("LOBBY_STATE_CHANGE"));
	}

	public Scene_Lobby() {
		// currentSecene register
		GIM.currentScene = this;

		// initialization
		init();

		// Room List Panel
		roomList = new JList(dlm);
		roomList.setFixedCellWidth(600);
		roomList.setFont(font1);
		roomList.setOpaque(false);
		roomList.setVisibleRowCount(9);
		roomList.setCellRenderer(new TransparentListCellRenderer());
		roomList.setFixedCellHeight(60);
		roomList.setSelectedIndex(0);
		roomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// JOIN GAME ROOM
					if (currentRoom != null) {
						currentRoom.roomLobbyClose();
						currentRoom = null;
					}
					RoomInfo room = (RoomInfo) roomList.getSelectedValue();
					// User 참가 보내고 -> 다시 방 초기화
					if(!NetworkRoomServer.joinRoom(room.roomName)){
						return;
					}
					synRoomInfoWithServer();
					currentRoom = new RoomLobby(room.roomName);
				}
			}
		});
		JScrollPane j = new JScrollPane();
		j.setViewportView(roomList);
		j.setBounds(42, 144, 625, 540);
		j.getViewport().setOpaque(false);
		j.setOpaque(false);
		systemObject.add(j);

		// Back Button
		backButton.setBorderPainted(true); // 버튼 배치 테스트 때문에 true로 변경
		backButton.setContentAreaFilled(false); // 채우지마
		backButton.setBounds(1182, 20, 52, 47);
		backButton.setFocusPainted(false);
		backButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// button.setIcon으로 아이콘변경
				backButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				NetworkRoomServer.networkClose();
				GIM.GameObject.changeScene(thisInstance, "Start");
			}
		});
		systemObject.add(backButton);
		// Input 등록

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
		for (Thread t : backendObject) {
			t.interrupt();
		}
		for (Handler t : systemHandler) {
			t.interrupt();
		}
		// introMusic.close();
	}

	public void screenDraw(Graphics g) {
		g.drawImage(background, 0, 0, 1280, 720, null);

		GIM.GameObject.paintComponents(g);
		GIM.GameObject.repaint();
	}
}
