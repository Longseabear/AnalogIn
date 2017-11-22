import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * @author LEaps This server manage to room information. PROTOCOL
 *         [SENDER_STATUS]-[MAIN_COMMAND]-[CONTENT]
 * 
 */
public class AnaloginServer {
	private static final int serverPORT = 2346; // PORT
	private static final int MAX_ROOM = 100;
	// information in Server
	static HashMap<String, RoomServerHandler> userInfoList = new HashMap<String, RoomServerHandler>();
	static PriorityQueue<Integer> roomNumberPool = new PriorityQueue<Integer>();
	
	public static void main(String[] args) throws Exception {
		System.out.println("The analogin server is running.");
		ServerSocket listener = new ServerSocket(serverPORT);
		init();
		try {
			while (true) {
				new RoomServerHandler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}
	private static void init(){
		for(int i=0;i!=MAX_ROOM;i++){
			roomNumberPool.add(i);
		}
	}
	private static class RoomServerHandler extends Thread {
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private ObjectOutputStream oos;
		public UserInfo userInfo;

		public RoomServerHandler(Socket socket) {
			this.socket = socket;
			userInfo = new UserInfo();
		}

		private boolean connectionAwithB(String u1,String u2){
			return false;
		}
		private boolean tcpConnection(RoomInfo room){
			for(UserInfo u : room.User){
				
				userInfoList.get(u.id).out.println("서버 연결하라");
				out.println("너도 연결해 임마");
				
				//A하고 B있냐? 물어본다
				//있을떄까지 대기. 
				// A하고 B 연결 시작.
				if(!connectionAwithB(u.id, userInfo.id)){
					return false;
				}
			}
			return true;
		}
		@Override
		public void run() {
			try {
				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				oos = new ObjectOutputStream(socket.getOutputStream());
				do {
					String socketData = in.readLine();
					String request[] = socketData.split("-");
					String who = request[0];
					String command = request[1];
					String content = null;
					if(request.length>2){
						content = request[2];						
					}

					/**
					 * User Request
					 */
					if (who.equals("USER")) {
						/**
						 * USER REQURST PROTOCOL 1 Command : JOIN_GAME 
						 * Content : [String] Id 
						 * response : ACK / RE_ACK When user join
						 * game firstly, register user information. and check id
						 * duplication. " Id must be primary key!! "
						 **/
						if (command.equals("JOIN_GAME")) {
							if (!userInfoList.containsKey(content)) {
								userInfo.id = content;
								userInfoList.put(content, this);
								out.println(command+"-ACK");
							} else {
								out.println(command+"-RE_ACK");
							}

						}
						/**
						 * USER REQURST PROTOCOL 2 Command : GET_ROOM_LIST
						 * Content : NULL 
						 * response : Serialized Room list.
						 * [ ArrayList of RoomInfo class ]. Room list request.
						 **/
						else if (command.equals("GET_ROOM_LIST")) {
							out.println(command + "_PREDICT_SAND");
							oos.writeObject(RoomManager.roomInfoList);
						}
						/**
						 * USER REQURST PROTOCOL 3 Command : CREATE_ROOM
						 * Content : [RoomName] [GameName] [UserName]
						 *  response : ACK / ERR / DUP(duplication)
						 * [ Create Room ]. Create Room.
						 **/
						else if (command.equals("CREATE_ROOM")) {
							String req[] = content.split(" ");
							String roomName = req[0];
							String gameName = req[1];
							String userName = req[2];
							// duplication Check
							if(RoomManager.isRoomName(roomName)){
								// REACK DUP
								out.println("DUP");
								continue;
							}
							RoomInfo room = new RoomInfo();
							room.roomName = roomName;
							room.gameName = gameName;
							if(roomNumberPool.peek()!=null)
								room.roomNumber = roomNumberPool.poll();
							else
							{
								// CREATE ERROR
								out.println("ERR");
								continue;
							}
							room.User.add(userInfoList.get(userName).userInfo);
							RoomManager.roomInfoList.put(roomName,room);
							out.println("ACK");
						}
						/**
						 * USER REQURST PROTOCOL 4 Command : JOIN_ROOM
						 * Content : [RoomName]
						 *  response : ACK / NAK
						 * [ Join room ] 
						 * TCP HolePunching connection with existed user.
						 * transfer new userlist to Existed user
						 **/
						else if (command.equals("JOIN_ROOM")) {
							String req[] = content.split(" ");
							String roomName = req[0];
							
							RoomInfo room = RoomManager.roomInfoList.get(roomName);
							if(!RoomManager.isRoomName(roomName) || room.User.size()>=4 || !this.tcpConnection(room)){
								// Full
								out.println("NAK");
								continue;
							}
							room.User.add(userInfo);
							out.println("ACK");
						}
					}

				} while (true);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// User 나감
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}