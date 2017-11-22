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
 * @author LEaps
 *  This server manage to communication for each other peer.
 *	for this, we use 'TCP hole punching'			
 *  through this server, user ...
 *  name -> primary key
 *  
 *  
 *  [HOST] [COMMAND] [CONTENT]
 *  
 *  HOST : USER OR SERVER
 *  
 */

public class ConnectionServer {

	static final int serverPORT = 9876;
	
	public static void main(String[] args) throws Exception {
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
	public static HashMap<String,HolePunchingServer> userList = new HashMap<String,HolePunchingServer>(); 
	
	public static class HolePunchingServer extends Thread{
		Socket socket; 
		String userName;
		private BufferedReader in;
		private PrintWriter out;
		InetAddress userPrivateIp = null;
		InetAddress userPublicIp = null;
		int userPrivatePort;
		int userPublicPort;
		
		public HolePunchingServer(Socket _socket){
			socket = _socket;
			try {
				socket.setReuseAddress(true);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run(){
			try{
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);	
				do{
					String[] req = in.readLine().split("-");
					String host = req[0];
					String command = req[1];
					String content = null;
					if(req.length>2){
						content = req[2];
					}
			
					/**
					 * USER-CONNECTION_SERVER-PROTOCOL
					 **/
					if(host.equals("USER"))
					{
						/**
						 * JOIN USERNAME
						 * Content : NULL 
						 * response : NULL.
						 * register user_name to HolePunchingServer.
						 **/
						if(command.equals("JOIN"))
						{
							userName = content;
							userList.put(userName, this);
						}
						/**
						 * SET_MYIP privateIp privatePort
						 * Content : 
						 * response : NULL.
						 * register user_name to HolePunchingServer.
						 **/
						if(command.equals("SET_MYIP"))
						{
							String userInfo[] = content.split(" ");
							userPrivateIp = InetAddress.getByName(userInfo[0]);
							userPrivatePort = Integer.parseInt(userInfo[1]);
							userPublicIp = socket.getInetAddress();
							userPublicPort = socket.getPort();
						}	
					}
					/**
					 * SERVER-CONNECTION_SERVER-PROTOCOL
					 **/
					else if(host.equals("SERVER")){
						/**
						 * CONNECTION USERNAME USERNAME
						 * Content : USER USER
						 * response : ACK / NAK. [ INPUT_EEROR / NOT_FIND_USER ]
						 * connect two user. 
						 **/
						if(command.equals("CONNECTION"))
						{
							String[] user = content.split(" ");
							if(user.length!=2){
								out.println("NAK_INPUT_ERROR");
								continue;
							}
							if(!userList.containsKey(user[0]+"_"+user[1]) || !userList.containsKey(user[1]+"_"+user[0])){
								out.println("NAK_NOT_FIND_USER");
								continue;
							}
							HolePunchingServer receiver = userList.get(user[0]+"_"+user[1]);
							HolePunchingServer connector = userList.get(user[1]+"_"+user[0]);
							connector.out.println("접속해야되는 정보");
							receiver.out.println("방만들어라");
							connector.out.println("참가해라");
						} 
					}
				}while(true);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
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
