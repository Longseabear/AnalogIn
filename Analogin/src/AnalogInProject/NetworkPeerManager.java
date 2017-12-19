package AnalogInProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkPeerManager extends Thread{
	public static ArrayList<NetworkPeer> peers = new ArrayList<NetworkPeer>();
	private static String STUN_SERVER_IP = "210.90.135.149";
	private static int STUN_SERVER_PORT = 1111;
    static BufferedReader in;
    static PrintWriter out;
    Socket socket;
    
    private InetAddress tempPrivateIp;
    private int tempPrivatePort;
    private InetAddress tempPublicIp;
    private int tempPublicPort;
    
    private int mePort = 0;
    
	public static boolean networkStart() {
		System.out.println("[peerServer] networkStart");
		NetworkPeerManager nm = new NetworkPeerManager();
		try {
			nm.socket = new Socket(STUN_SERVER_IP, STUN_SERVER_PORT);
			nm.socket.setReuseAddress(true);
			nm.mePort = nm.socket.getLocalPort();
			nm.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("[peerServer] networkStart -success");
		return true;
	}

	@Override
	public void run(){
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);	 
			out.println(GIM.me.id + "-" + InetAddress.getLocalHost().getHostAddress() + "-"
					+ socket.getLocalPort());
			System.out.println(socket.isConnected());
			// 정보 전달 / 아이디 저장.	
			do{
				String req = in.readLine();
				System.out.println("[Peer] Request : "+req);
				if(req.startsWith("setConnectionInfo")){
					String[] reqs = req.split("-");
					tempPrivateIp = InetAddress.getByName(reqs[1].substring(1));
					tempPrivatePort = Integer.parseInt(reqs[2]);
					tempPublicIp = InetAddress.getByName(reqs[3].substring(1));
					tempPublicPort = Integer.parseInt(reqs[4]);
				}
				else if(req.startsWith("RECEIVE"))
				{
					System.out.println("[RECEIVER] WAIT START");
					socket.close();
					ServerSocket rev = new ServerSocket(mePort);
					System.out.println(" Port : " + mePort);

					Socket client = rev.accept();
					rev.close();
					synchronized(peers)
					{
						peers.add(new NetworkPeer(client));
						peers.notifyAll();
					}
					System.out.println("[RECEIVER] SUCCESS");
					break;
				}
				else if(req.startsWith("CONNECT"))
				{
					System.out.println("[CONNECTER] START");
					socket.close();
					SocketAddress socketAddress = new InetSocketAddress(tempPrivateIp, tempPrivatePort);
					System.out.println(tempPrivateIp + " " + tempPrivatePort + " access...");
					try{
						socket = new Socket();
						socket.connect(socketAddress, 500);
						System.out.println("GO");
						synchronized(peers)
						{
							peers.add(new NetworkPeer(socket));
							peers.notifyAll();
						}
						System.out.println("[CONNECTER] SUCCESS_PRIATE");
						break;
					}catch(SocketTimeoutException e){
						System.out.println("ACCESS PUBLIC IP");
					}
					socketAddress = new InetSocketAddress(tempPublicIp, tempPublicPort);
					System.out.println(tempPublicIp + " " + tempPublicPort + " access...");
					try{
						socket = new Socket();
						socket.connect(socketAddress, 10000);
						synchronized(peers)
						{
							peers.add(new NetworkPeer(socket));
							peers.notifyAll();
						}
						System.out.println("[CONNECTER] SUCCESS_PUBLIC");
						break;
					}catch(SocketTimeoutException e){
						System.out.println("FAIL");
					}
					break;
				}
			}while(true);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				socket.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}finally{}
	}
}
