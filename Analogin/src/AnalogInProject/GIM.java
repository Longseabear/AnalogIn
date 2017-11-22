package AnalogInProject;

import java.util.ArrayList;

public class GIM {
	// 부동 오브젝트
	public static Analogin GameObject;
	
	// STATUS 게임 외부 변수
	public static int turnTime = 10;
	public static final int FPS = 60;
	
	// 유동 오브젝트 [ 각종 Manager ]
	public static KeyInputController keyInputBuffer;
		
	// GAME 진행시 필요한 변수목록
	public static SceneManager currentScene = null;
	public static int blockPriority = 1;
	public static Block checkedBlock = null;
	public static ArrayList<Block> blockObject = null;
	public static KeyInputCollector currentInputCollector = null;
	// 유동 변수 소거
	public static void removeGIM()
	{
		currentScene = null;
		blockPriority = 0;
		checkedBlock = null;
		blockObject = null;
		if(keyInputBuffer!=null)
			keyInputBuffer.interrupt();
		keyInputBuffer = null;
	}
}
