package AnalogInProject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Audio extends Thread {
	private Player player;
	private boolean isLoop;
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	
	public Audio(String name, boolean isLoop){
		try{
			this.isLoop = isLoop;
			file = new File(Main.class.getResource("../Music/" + name).toURI());
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	public int getTime(){ // 진행시간.
		if(player == null){
			return 0;
		}
		return player.getPosition();
	}
	public void close(){
		isLoop = false;
		player.close();
		this.interrupt();
	}
	@Override
	public void run(){
		try{
			do{
				player.play(); // 한번 플레이되면 종료.
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			}while(isLoop);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
