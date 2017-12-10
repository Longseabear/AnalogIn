package AnalogInProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

/**
 * @author LEaps This server manage to communication for each other peer. for
 *         this, we use 'TCP hole punching' through this server, user ... name
 *         -> primary key
 * 
 * 
 *         [HOST] [HANDLER_NAME] [COMMAND] [CONTENT]
 * 
 *         HOST : USER OR SERVER
 * 
 */

public class ConnectionServer {

	static final int serverPORT = 1111;

	public  ConnectionServer() throws Exception {
		System.out.println("The analogin holepunching server is running.");
		ServerSocket listener = new ServerSocket(serverPORT);
		listener.setReuseAddress(true);
		try {
			while (true) {
				new HolePunchingServer(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	public static HashMap<String, HolePunchingServer> userList = new HashMap<String, HolePunchingServer>();

	public static class HolePunchingServer extends Thread {
		Socket socket;
		String userName;
		private BufferedReader in;
		private PrintWriter out;

		InetAddress userPrivateIp = null;
		InetAddress userPublicIp = null;
		int userPrivatePort;
		int userPublicPort;

		public HolePunchingServer(Socket _socket) {
			socket = _socket;
			try {
				socket.setReuseAddress(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public String userConnectionInfo() {
			return "setConnectionInfo" + "-" + userPrivateIp + "-" + userPrivatePort + "-" + userPublicIp + "-"
					+ userPublicPort;
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				// 처음으로 이름-privateIp-port;
				String[] firstReq = in.readLine().split("-");
				userName = firstReq[0];
				System.out.println("[STUN] Access " + userName);
				if(!userName.equals("[SERVER]"))
				{
					userPrivateIp = InetAddress.getByName(firstReq[1]);
					userPrivatePort = Integer.parseInt(firstReq[2]);
					userPublicIp = socket.getInetAddress();
					userPublicPort = socket.getPort();					
				}

				System.out.println("[STUN] " + userName + " in list");
				userList.put(userName, this);

				if (userName.equals("[SERVER]")) {
					do {
						String[] req = in.readLine().split(" ");
						String command = req[0];
						String content = req[1];

						/**
						 * CONNECTION USERNAME USERNAME Content : USER USER
						 * response : ACK / NAK. [ INPUT_EEROR / NOT_FIND_USER ]
						 * connect two user.
						 **/
						if (command.startsWith("SET")) {
							String[] user = content.split("-");
							System.out.println("[STUN] " + userName + " SET User : " + user[0] + " "+ user[1]);
							if (!userList.containsKey(user[0]) || !userList.containsKey(user[1])){
								System.out.println("[STUN] " + userName + "SET FAILE : because userName Not in list");
								out.println("NAK");
								continue;
							}
							HolePunchingServer receiver = userList.get(user[0]);
							HolePunchingServer connector = userList.get(user[1]);
							connector.out.println(receiver.userConnectionInfo());
							System.out.println("[STUN] Ip setup Ok");
							receiver.out.println("RECEIVE");
							connector.out.println("CONNECT");
							userList.remove(user[0]);
							userList.remove(user[1]);
							out.println("ACK");
						}
					} while (true);
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					socket.close();
					System.out.println("[STUN] exit " + userName);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} 
		}
	}
}
