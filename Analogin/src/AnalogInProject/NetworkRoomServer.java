package AnalogInProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkRoomServer {
	private static final int PORT = 2346;	
	static final String serverAddress = "127.0.0.1";
    static BufferedReader in;
    static PrintWriter out;
    static ObjectInputStream ois;
    static boolean waitSwitch = false;
    static boolean result = false;
    static ArrayList<RoomInfo> roomList;
    NetworkRoomServer(){
    	try {
			Socket socket = new Socket(serverAddress,PORT);
			new handler(socket).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //Write
    public boolean getGameRegister(){
    	out.println("JOIN_GAME");
    	waitSwitch = true;
    	while(waitSwitch){};
    	return result;
    }
    public boolean getRoomInfo(){
    	out.println("GET_ROOM_LIST");
    	waitSwitch = true;
    	while(waitSwitch){};
    	return result;
    }
    //Read
	private static class handler extends Thread{
		Socket _socket = null;

		handler(Socket socket){
	        try {
	        	_socket = socket;
				in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
				out = new PrintWriter(_socket.getOutputStream(), true);
				ois = new ObjectInputStream(_socket.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		public void run(){
			do
			{
				try {
					String reqFromServer = in.readLine();
					
					/**
					 * response about JOIN_GAME Request
					 * ACK or RE_ACK
					 * 
					 * if ACK = return true
					 * if RE_ACK = return false
					 */
					if(reqFromServer.startsWith("JOIN_GAME")){
						String res = reqFromServer.split("-")[1];
						if(res.equals("ACK"))
						{
							result = true;
							waitSwitch = false;
						}else{
							result = false;
							waitSwitch = false;	
						}
						
					}
					/**
					 * response about GET_ROOM_LIST Request
					 * RoomInfo list
					 * 
					 * if ACK = return true
					 * if IO ERROR = return false
					 */
					if(reqFromServer.startsWith("GET_ROOM_LIST")){
						try {
							roomList = (ArrayList<RoomInfo>) ois.readObject();
							result = true;
							waitSwitch = false;	
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							result = false;
							waitSwitch = false;	
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(true);
		}
	}
}
