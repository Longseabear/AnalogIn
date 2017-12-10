package AnalogInProject;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class Scene_GameLoading extends SceneManager {
	/************************************************
	 * Resource
	 *************************************************/
	// Music
	// private Audio introMusic;
	private JButton backButton = new JButton();

	// Image
	private Image background = ImageManager.Opening;

	/************************************************
	 * Component/object
	 *************************************************/
	private Scene_GameLoading thisInstance = this; // thisInstance

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

	public String roomName;
	
	public Scene_GameLoading() {
		GIM.currentScene = this;
		// currentSecene register
		try {
			systemHandler.add(NetworkRoomServer.systemHandler("STUN_SERVER_STEP1"));
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("[START GAME LOADING] " + GIM.me.id + " // " + GIM.playingGameRoom.User.get(0).id);
		// Loading
		if(!GIM.me.id.equals(GIM.playingGameRoom.User.get(0).id))
		{
			System.out.println("NORMAL");
			//Normal player
			synchronized(NetworkPeerManager.peers)
			{
				while(NetworkPeerManager.peers.size() != GIM.playingGameRoom.User.size()-1)
				{
					try{
						System.out.println(NetworkPeerManager.peers.size() + " " + (GIM.playingGameRoom.User.size()-1));
						NetworkPeerManager.peers.wait();					
					}catch(Exception e){}
				}
			}
			System.out.println("[GAME LOADING] STUN SERVER CONNECTION OK");
//			if((NetworkPeerManager.peers.get(0).Sender("P2P_CONNECTION_COMPLETION OK", "p2p", true)==null){
//				System.out.println("ERROR [ P@P_CONNECTION_COMPLETION REPLAY EEROR ]");
//			}
			
			NetworkPeer toMaster = NetworkPeerManager.peers.get(0);
			if(!toMaster.mapSynchronization())
			{
				System.out.println("[SCENE_GAMELOADING] ERROR mapSynchronization");
				System.exit(1);
			}
			System.out.println("[GAME LOADING] GAME INFORMATION SYNCH OK");
			if(!toMaster.imageRequest())
			{
				System.out.println("[SCENE_GAMELOADING] ERROR ImageRequest");
				System.exit(1);				
			}
			System.out.println("requyest는?");
		}else{
			System.out.println("MASTER");
			GIM.playingGameRoom = new RoomInfo(Scene_Lobby.roomInfoList.get(GIM.currentRoomName));
			ArrayList<UserInfo> users = (ArrayList<UserInfo>) GIM.playingGameRoom.User.clone();
			String master = users.get(0).id;
			while(!users.isEmpty()){
				String user = users.get(0).id;
				for(int i=1;i!=users.size();i++){
					String user2 = users.get(i).id;
					System.out.println(user+"/"+user2 + " Connection Ready");
					System.out.println(user+"/"+user2 + " Connection : "+ NetworkRoomServer.p2pConnection(user,user2));
				}
				users.remove(0);
			}
			// 모든 연결완료를 기ㅏㄷ린다.
			synchronized(NetworkPeerManager.peers)
			{
				while(NetworkPeerManager.peers.size() != GIM.playingGameRoom.User.size()-1)
				{
					try{
						System.out.println(NetworkPeerManager.peers.size() + " " + (GIM.playingGameRoom.User.size()-1));
						NetworkPeerManager.peers.wait();					
					}catch(Exception e){}
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(NetworkPeer p : NetworkPeerManager.peers)
			{
				if(p==NetworkPeerManager.peers.get(0))
					continue;
				p.mapInfoSend();
			}
			// 연결이 완료되었으니 유저의 요청을 듣는다
			// 
			for(int i=1;i!=NetworkPeerManager.peers.size();i++)
			{
				NetworkPeer p = NetworkPeerManager.peers.get(i);
				while(true){
					try {
						Handler s = p.systemHandler("P2P_INFO_SYN");
						s.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String res = null;
					// 여러처리 OK이면 break;
					synchronized (p.networkJar) {
						res = (String)p.networkJar.get("FILE_REQUEST_BI");
						p.networkJar.remove("FILE_REQUEST_BI");
					}
					if(!res.equals("ACK")){
						try {
							BufferedImage img = ImageIO.read(new File(GIM.dir + res));
							p.SenderObject(img);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						break;
					}
				}
			}
			// 끝 이제 게임을 시작하면 됨. 나머지 3명에게 게임스타트를 보내주고, 3초로 맞춘다음 스타트.
			
		}
		//Connection OK
		GIM.GameObject.repaint();
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
