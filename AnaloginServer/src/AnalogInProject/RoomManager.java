package AnalogInProject;
import java.util.HashMap;

public class RoomManager {
	public static HashMap<String,RoomInfo> roomInfoList = new HashMap<String,RoomInfo>();
	
	public static boolean isRoomName(String name){
		return roomInfoList.containsKey(name);
	}
	public static void printAllUser(){
		for(String rn : roomInfoList.keySet()){
			System.out.println(rn);
			for(UserInfo u : roomInfoList.get(rn).User){
				System.out.println(u.id);
			}
		}
	}
}