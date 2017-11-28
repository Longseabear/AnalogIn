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
import java.util.Scanner;

/**
 * 
 * @author LEaps
 * 
 */
public class AnaloginServer2 {
	/**
	 * The port that the server listens on.
	 */
	private static final int PORT = 2346; // PORT
											
	public static void main(String[] args) throws Exception {
		System.out.println("The analogin server is running.");
		new Thread(){
			@Override
			public void run(){
				try {
					Thread.sleep(15000);
					try {
						p2pTcpConnection(new String[]{"a","b"});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		ServerSocket listener = new ServerSocket(PORT);
		listener.setReuseAddress(true);
		try {
			while (true) {
				Socket s = listener.accept();
				new Handler(s).start();
			}
		} finally {
			listener.close();
		}
	}

	public static void peerConnection(UserInfo user1, UserInfo user2) throws IOException{
		Scanner p = new Scanner(System.in); // test
		user1.pingMode = false;
		user2.pingMode = false;
		
		user1.sendBuffer.println("CONNECTION_SETTING");
		user2.sendBuffer.println("CONNECTION_SETTING");
		user1.inputBuffer.readLine();
		user2.inputBuffer.readLine();
		user1.sendBuffer.println(user2.printInfo());
		user2.sendBuffer.println(user1.printInfo());
		user1.inputBuffer.readLine();
		user2.inputBuffer.readLine();	
		
		user1.sendBuffer.println("CONNECTION_LISTEN");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user2.sendBuffer.println("CONNECTION_SENDER");
	}
	public static void p2pTcpConnection(String users[]) throws IOException {
		peerConnection(userList.get(users[0]),userList.get(users[1]));
	}

	public static HashMap<String, UserInfo> userList = new HashMap<String, UserInfo>();

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;
		private UserInfo user = new UserInfo();

		/**
		 * Constructs a handler thread, squirreling away the socket. All the
		 * interesting work is done in the run method.
		 */
		public Handler(Socket socket) {
			this.socket = socket;
			try {
				socket.setReuseAddress(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				String respone; // 엔터까지 받으니 조심
				String userInfo[];

				// Create character streams for the socket.
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				while (true) {
					out.println("REQUEST_INFO"); // 연결 성공 후 Request 요구
					// 이름-사설Ip-사설Port를 보내야만 한다.
					respone = in.readLine(); // 엔터까지 받으니 조심
					userInfo = respone.split("-");
					name = userInfo[0];
					System.out.println(name + "_response : " + respone);
					if (!userList.containsKey(name)) {
						break;
					}
				}
				user.privateIp = InetAddress.getByName(userInfo[1]);
				user.privatePort = Integer.parseInt(userInfo[2]);
				user.publicIp = socket.getInetAddress();
				user.publicPort = socket.getPort();
				user.sendBuffer = out;
				user.socket = socket;
				user.inputBuffer = in;
				System.out.println(user.print());
				userList.put(name, user);
				while (true) {
					if (user.pingMode) {
						out.println("PING");
						in.readLine();
						Thread.sleep(1000);
					}
				}
			} catch (Exception e) {
				System.out.println("3?");
				e.printStackTrace();
			} finally {
				if (name != null)
					userList.remove(name);
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("THREAD END");
			}

		}
	}
}