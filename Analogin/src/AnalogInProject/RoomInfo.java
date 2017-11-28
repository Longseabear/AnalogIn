package AnalogInProject;
import java.util.ArrayList;

public class RoomInfo implements java.io.Serializable,Comparable<RoomInfo> {

	int roomNumber;
	String gameName;
	String roomName; // primary key
	String content;
	ArrayList<UserInfo> User = new ArrayList<UserInfo>();

	@Override
	public int compareTo(RoomInfo obj){
		if(this.roomNumber<obj.roomNumber)
			return -1;
		return 1;
	}
	
	@Override
	public String toString(){
		String suffix = String.format("%03d", roomNumber); 
		return suffix + ". " + roomName + " (" + User.size() + "/4)";
	}
}
