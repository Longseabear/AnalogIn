import java.util.HashMap;

public class RoomManager {
	static HashMap<String,RoomInfo> roomInfoList = new HashMap<String,RoomInfo>();
	
	public static boolean isRoomName(String name){
		return roomInfoList.containsKey(name);
	}
}