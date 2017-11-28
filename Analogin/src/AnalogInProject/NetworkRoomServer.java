package AnalogInProject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author LEaps
 *
 *         Consumer Provider
 * 
 *         Receiver / Sender Receiver -> only out Sender -> only in
 */
public class NetworkRoomServer {
	private static final int PORT = 2346;
	static final String serverAddress = "127.0.0.1";
	static ObjectInputStream ois;

	private static Socket socket = null;
	private static ObjectOutputStream out = null;
	public static HashMap<String, Object> networkJar = new HashMap<String, Object>();
	private static ArrayList<Handler> handlers = new ArrayList<Handler>();

	private static Object senderLock = new Object();
	private static Object roomLock = new Object();

	/**
	 * @return Ensure Connection about Network
	 */
	public static boolean networkStart() {
		System.out.println("[ReqFun] networkStart");
		try {
			socket = new Socket(serverAddress, PORT);
			new Receiver().start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("[ReqFun] networkStart -end");
		return true;

	}

	public static boolean networkClose() {
		System.out.println("[ReqFun] networkClose");
		try {
			socket.close();
			synchronized (handlers) {
				for (Handler s : handlers) {
					s.interrupt();
				}
				handlers.clear();
			}
			synchronized (networkJar) {
				networkJar.clear();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static boolean joinRoom(String content) {
		// Synchronized
		System.out.println("[reqFun] joinRoom");
		String response;
		if ((response = (String) Sender("JOIN_ROOM " + content, "object", true)) == null) {
			return false;
		}
		if (response.equals("ACK"))
			return true;
		else
			return false;
	}
	public static boolean exitRoom(String content) {
		// Synchronized
		System.out.println("[reqFun] exitRoom");
		String response;
		if ((response = (String) Sender("EXIT_ROOM " + content, "object", true)) == null) {
			return false;
		}
		if (response.equals("ACK"))
			return true;
		else
			return false;
	}

	// game first entrance
	public static boolean setGameRegister(String content) {
		// Synchronized
		System.out.println("[reqFun] setGameRegister");
		String response;
		if ((response = (String) Sender("JOIN_GAME " + content, "object", true)) == null) {
			return false;
		}
		if (response.equals("ACK"))
			return true;
		else
			return false;
	}

	// room info
	public static HashMap<String, RoomInfo> getRoomInfo() {
		// Synchronized
		synchronized (roomLock) {
			System.out.println("[reqFun] getRoomInfo");
			HashMap<String, RoomInfo> response = null;
			if ((response = (HashMap<String, RoomInfo>) Sender("GET_ROOM_LIST", "ROOM_INFO", true)) == null) {
				return null;
			}
	//		printAllUser(response);
			return response;
		}
	}
	public static void printAllUser(HashMap<String,RoomInfo> roomInfoList){
		for(String rn : roomInfoList.keySet()){
			System.out.println(rn);
			for(UserInfo u : roomInfoList.get(rn).User){
				System.out.println(u.id);
			}
		}
	}
	public static Object Sender(String commend, String jobType, boolean isSyn) {
		Handler h = new Handler(jobType);
		synchronized (handlers) {
			handlers.add(h);
		}
		String handlerName;
		if (jobType.equals("object"))
			handlerName = h.getName();
		else
			handlerName = jobType;
		h.start();
		String req = "USER " + h.getName() + " " + commend;
		System.out.println("[Sender] request : " + req);
		synchronized (senderLock) {
			try {
				out.writeObject(req);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				synchronized (handlers) {
					h.interrupt();
					handlers.remove(h);
				}
				return null;
			}
		}
		if (isSyn) {
			try {
				h.join(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			h.interrupt();
		}
		Object res = null;
		synchronized (networkJar) {
			res = networkJar.get(handlerName);
			networkJar.remove(handlerName);
		}
		synchronized (handlers) {
			h.interrupt();
			handlers.remove(h);
		}
		return res;
	}
	public static Handler systemHandler(String jobType){
		Handler h = new Handler(jobType);
		synchronized (handlers) {
			handlers.add(h);
		}
		h.start();
		System.out.println("[SystemHandler] request : " + jobType);
		return h;
	}
	/**
	 * @author LEaps Response Pattern -> Thread-Name Content
	 */

	private static class Receiver extends Thread {
		static ObjectInputStream in;

		Receiver() {
			System.out.println("[Response] On");
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				in = new ObjectInputStream(socket.getInputStream());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void stringResponse(String reqFromServer) {
			System.out.println("[Receiver] : " + reqFromServer);
			String[] res = reqFromServer.split(" ", 2);
			synchronized (networkJar) {
				networkJar.put(res[0], res[1]);
				networkJar.notifyAll();
			}
			System.out.println("[Receiver] put [" + res[1] + "] in networkJar of " + res[0]);
		}

		public void hashResponse(Object reqFromServer) {
			System.out.println("[Receiver] : HashData Receive");
			synchronized (networkJar) {
				networkJar.put("ROOM_INFO", reqFromServer);
				networkJar.notifyAll();
			}
			System.out.println("[Receiver] put [RoomInfo] in networkJar of ROOM_INFO");
		}

		@Override
		public void run() {
			do {
				try {
					Object reqFromServer = in.readObject();
					// String
					if (reqFromServer instanceof String) {
						stringResponse((String) reqFromServer);
					} else if (reqFromServer instanceof HashMap) {
						hashResponse(reqFromServer);
					} else {
						System.out.println("[Receiver] Don't know data");
						break;
					}
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
					System.out.println("[Receiver] Socket IO Exception");
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				}
				System.out.println("[Receiver] Complete Request");
			} while (true);
			System.out.println("[Receiver] Receiver End");
		}
	}
}
