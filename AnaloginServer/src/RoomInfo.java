import java.util.ArrayList;

public class RoomInfo implements java.io.Serializable,Comparable<RoomInfo> {
	int roomNumber;
	String gameName;
	String roomName; // primary key
	ArrayList<UserInfo> User;

	@Override
	public int compareTo(RoomInfo obj){
		if(this.roomNumber<obj.roomNumber)
			return -1;
		return 1;
	}
}
