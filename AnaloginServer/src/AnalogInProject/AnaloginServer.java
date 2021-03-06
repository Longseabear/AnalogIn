package AnalogInProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @author LEaps This server manage to room information. PROTOCOL [HOST_NAME]
 *         [Thread-Name] [MAIN_COMMAND] [CONTENT]
 * 
 */
public class AnaloginServer {
	private static final int serverPORT = 2346; // PORT
	private static final int MAX_ROOM = 100;
	private static final int STUN_SERVER_PORT = 1111; // PORT
	private static final String STUN_SERVER_IP = "127.0.0.1";
	// information in Server
	static HashMap<String, RoomServerHandler> userInfoList = new HashMap<String, RoomServerHandler>();
	static PriorityQueue<Integer> roomNumberPool = new PriorityQueue<Integer>();

	public static Socket StunSocket = null;
	public static BufferedReader StunIn = null;
	public static PrintWriter StunOut = null;

	public AnaloginServer() throws Exception {
		System.out.println("The analogin server is running.");

		StunSocket = new Socket(STUN_SERVER_IP, STUN_SERVER_PORT);
		StunIn = new BufferedReader(new InputStreamReader(StunSocket.getInputStream()));
		StunOut = new PrintWriter(StunSocket.getOutputStream(), true);

		StunOut.println("[SERVER]");
		ServerSocket listener = new ServerSocket(serverPORT);
		init();
		try {
			while (true) {
				new RoomServerHandler(listener.accept()).start();
				System.out.println(":: Connect ::");
			}
		} finally {
			listener.close();
		}
	}

	private static void init() {
		for (int i = 0; i != MAX_ROOM; i++) {
			roomNumberPool.add(i);
		}
		// temp
		RoomInfo a = new RoomInfo();
		a.gameName = "HELLO WORL";
		a.roomName = "BUE BUE";
		RoomManager.roomInfoList.put(a.roomName, a);
	}

	private static class RoomServerHandler extends Thread {
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		public UserInfo userInfo;

		public RoomServerHandler(Socket socket) {
			this.socket = socket;
			userInfo = new UserInfo();
		}

		private boolean connectionAwithB(String u1, String u2) {
			return false;
		}

		private boolean tcpConnection(RoomInfo room) {
			for (UserInfo u : room.User) {
				// userInfoList.get(u.id).out.println("서버 연결하라");
				// out.println("너도 연결해 임마");

				// A하고 B있냐? 물어본다
				// 있을떄까지 대기.
				// A하고 B 연결 시작.
				if (!connectionAwithB(u.id, userInfo.id)) {
					return false;
				}
			}
			return true;
		}

		public void uniCast(String user, String str) {
			synchronized (userInfoList) {
				try {
					userInfoList.get(user).out.reset();
					userInfoList.get(user).out.writeObject(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void broadCast(String str) {
			synchronized (userInfoList) {
				for (String key : userInfoList.keySet()) {
					try {
						userInfoList.get(key).out.reset();
						userInfoList.get(key).out.writeObject(str);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		public void userLobbyStateChange(String user) {
			broadCast("LOBBY_STATE_CHANGE " + user);
		}

		public void stringRequest(String req) throws IOException {
			String request[] = req.split(" ", 4);
			String who = request[0];
			String handlerName = request[1];
			String command = request[2];
			String content = null;
			if (request.length == 4) {
				content = request[3];
			}
			System.out.println("REQ : " + req);

			/**
			 * User Request
			 */
			if (who.equals("USER")) {
				/**
				 * USER REQURST PROTOCOL 1 Command : JOIN_GAME Content :
				 * [String] Id response : ACK / RE_ACK When user join game
				 * firstly, register user information. and check id duplication.
				 * " Id must be primary key!! "
				 **/
				if (command.equals("JOIN_GAME")) {
					synchronized (userInfoList) {
						if (!userInfoList.containsKey(content)) {
							userInfo.id = content;
							userInfoList.put(content, this);
							out.writeObject(handlerName + " ACK");
						} else {
							out.writeObject(handlerName + " NEGACK");
							System.out.println("To " + handlerName + ", NEGACK because duplicated id");
						}
					}
				}
				/**
				 * USER REQURST PROTOCOL 2 Command : GET_ROOM_LIST Content :
				 * NULL response : Serialized Room list. [ ArrayList of RoomInfo
				 * class ]. Room list request.
				 **/

				else if (command.equals("GET_ROOM_LIST")) {
					synchronized (RoomManager.roomInfoList) {
						out.reset();
						out.writeObject(RoomManager.roomInfoList);
					}
				}
				/**
				 * USER REQURST PROTOCOL 3 Command : CREATE_ROOM Content :
				 * [RoomName] [GameName] [UserName] [RULE] response : ACK / ERR /
				 * DUP(duplication) [ Create Room ]. Create Room.
				 **/
				else if (command.equals("CREATE_ROOM")) {
					String[] requestContent = content.split("-",5);
					String roomName = requestContent[0];
					String gameName = requestContent[1];
					String userName = requestContent[2];
					String rule = requestContent[3];
					// duplication Check if
					if (RoomManager.isRoomName(roomName)) { // REACK DUP
						synchronized (RoomManager.roomInfoList) {
							out.reset();
							System.out.println("이미 방이 있음");
							out.writeObject(handlerName+" FAIL");
						}							
						return;
					}
					System.out.println("[SERVER] CREATE_ROOM -> Create room");
					RoomInfo room = new RoomInfo();
					room.roomName = roomName;
					room.gameName = gameName;
					RoomManager.roomInfoList.put(room.roomName, room);
					if (roomNumberPool.peek() != null)
						room.roomNumber = roomNumberPool.poll();
					else { // CREATE ERROR
						System.out.println("[SERVER] ERRO -> PEEK FAIL");
						out.writeObject(handlerName+" FAIL");
					}
					System.out.println("UserName = " + userName);
					room.User.add(userInfoList.get(userName).userInfo);

					synchronized (RoomManager.roomInfoList) {
						RoomManager.roomInfoList.put(roomName, room);
					}
					System.out.println("[SERVER] LobbyStateChange");
					userLobbyStateChange(userInfo.id);		
					System.out.println("[SERVER] CREATE_ROOM -> OK SEND ACK");
					out.writeObject(handlerName+" ACK");
				}

				/**
				 * USER REQURST PROTOCOL 4 Command : JOIN_ROOM Content :
				 * [RoomName] response : ACK / NAK [ Join room ] userlist to
				 * Existed user
				 **/
				else if (command.equals("JOIN_ROOM")) {
					String roomName = content;

					synchronized (RoomManager.roomInfoList) {
						RoomInfo room = RoomManager.roomInfoList.get(roomName);
						if (room == null) {
							System.out.println("To " + handlerName + ", Because roomName = " + roomName + " Null");
							out.writeObject(handlerName + " NAK");
							return;
						}
						System.out.println("[JOIN_ROOM] " + roomName + " " + room.User.size());
						if (room.User.size() >= 4) {
							// Full
							System.out.println("To " + handlerName + ", beacause user >= 4");
							out.writeObject(handlerName + " NAK");
							return;
						}
						room.User.add(userInfo);
					}
					out.writeObject(handlerName + " ACK");
					// information change all user must be know it
					userLobbyStateChange(userInfo.id);
				}
				/**
				 * USER REQURST PROTOCOL 5 Command : EXIT_ROOM Content :
				 * roomName [RoomName] response : ACK / NAK [ Join room ]
				 * userlist to Existed user
				 **/
				else if (command.equals("EXIT_ROOM")) {
					String roomName = content;

					synchronized (RoomManager.roomInfoList) {
						RoomInfo room = RoomManager.roomInfoList.get(roomName);
						if (room == null) {
							System.out.println("To " + handlerName + ", Because roomName = " + roomName + " Null");
							out.writeObject(handlerName + " NAK");
							return;
						}
						room.User.remove(userInfo);
					}
					out.writeObject(handlerName + " ACK");
					// information change all user must be know it
					userLobbyStateChange(userInfo.id);
				}
				/**
				 * USER REQURST PROTOCOL 6 Command : STUN_CONNECT Content :
				 * user1 [STUN_CONNECT] response : ACK / NAK [ Connection ] for
				 * Hole Punching, connect with STUN server. this time, user send
				 * privateIP/privatePort to STUN server
				 **/
				else if (command.equals("STUN_CONNECT")) {
					String[] userNhost = content.split("-");
					uniCast(userNhost[0], "STUN_STEP1 " + userNhost[1]);
				}
				/**
				 * USER REQURST PROTOCOL 7 Command : STUN_CONNECT_OK Content :
				 * user1 [STUN_CONNECT] response : ACK Connection OK
				 */
				else if (command.equals("STUN_CONNECT_OK")) {
					uniCast(content, handlerName + " ACK");
				} else if (command.equals("STUN_CONNECT_FALSE")) {
					uniCast(content, handlerName + " NAC");
				} else if (command.equals("STUN_CONNECTION_START")) {
					String[] users = content.split("-");
					StunOut.println("SET " + users[0] + "-" + users[1]);
					if (StunIn.readLine().equals("ACK")) {
						System.out.println("[STUN] HOLPUNCHING ACK");
						out.writeObject(handlerName + " ACK");
					} else {
						System.out.println("[STUN] HOLPUNCHING NAK");
						out.writeObject(handlerName + " NAK");
					}
				} else if (command.equals("GAME_START")) {
					uniCast(content, "GAME_START");
				}
				System.out.println("PROCESS OK");
			}

		}

		@Override
		public void run() {
			try {
				// Create character streams for the socket.
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());
				do {
					Object socketData = in.readObject();
					/**
					 * String Request
					 */
					if (socketData instanceof String) {
						stringRequest((String) socketData);
					}
				} while (true);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(userInfo.id + " is exit.");
			} finally {
				// User 나감
				try {
					synchronized (userInfoList) {
						userInfoList.remove(userInfo.id);
					}
					// also user in room must be removed.
					/***/
					// information change all user must be know it
					userLobbyStateChange(userInfo.id);
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}