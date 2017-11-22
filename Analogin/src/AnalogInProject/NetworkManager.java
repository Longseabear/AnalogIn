package AnalogInProject;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

// 
public class NetworkManager extends Thread{

	private static final int PORT = 1555;
    public ArrayList<NetworkPeerManager> connectionList = new ArrayList<NetworkPeerManager>();
	
    public void set(){
    	
    }
	public void run(){
        System.out.println("The client socket is on");
        ServerSocket listener = null;
        
    }	
}
