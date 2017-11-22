package AnalogInProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkPeerManager extends Thread{
	public static ArrayList<NetworkPeerManager> peers = new ArrayList<NetworkPeerManager>();
	private static String STUN_SERVER_IP = "127.0.0.1";
	private static int STUN_SERVER_PORT = 9999;
    static BufferedReader in;
    static PrintWriter out;
    private String discriminatorUserName;
    Socket socket;
    
    private InetAddress tempPrivateIp;
    private int tempPrivatePort;
    private InetAddress tempPublicIp;
    private int tempPublicPort;
    NetworkPeerManager(String userName){
    	//userName is 'Sender_Reciver'
    	discriminatorUserName = userName;
    }
	@Override
	public void run(){
		try {
			socket = new Socket(STUN_SERVER_IP, STUN_SERVER_PORT);
			socket.setReuseAddress(true);
			// 정보 전달 / 아이디 저장.
			
			do{
				String req = in.readLine();
				if(req.equals("RECEIVER")){
					int port = socket.getLocalPort();
					socket.close();
					ServerSocket sr = new ServerSocket(port);
					//timeout
					socket = sr.accept();
					break;
				}
			}while(true);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
