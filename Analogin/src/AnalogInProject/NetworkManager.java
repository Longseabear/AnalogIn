package AnalogInProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

// Consumer Provider 구조로 제작.
public class NetworkManager extends Thread{

	private static final int PORT = 1555;
	public NetworkHandler myServer = null;
    public ArrayList<NetworkHandler> connectionList = new ArrayList<NetworkHandler>();
	
	public void run(){
        System.out.println("The client socket is on");
        ServerSocket listener = null;
		try {
			listener = new ServerSocket(PORT);
			
			while (true) {
                myServer = new NetworkHandler(listener.accept());
                connectionList.add(myServer);
                myServer.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
            System.exit(1);
		}finally{
			try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
			}
		}
    }	
}
