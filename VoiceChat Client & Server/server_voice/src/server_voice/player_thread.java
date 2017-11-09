package server_voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.SourceDataLine;

public class player_thread extends Thread{
	public DatagramSocket din;
	public SourceDataLine audio_out;
	byte[] buffer = new byte[512];
	
	@Override
	public void run() {
		DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
		while(Server_voice.calling) {
			try {
				din.receive(incoming);
				buffer = incoming.getData();
				audio_out.write(buffer, 0, buffer.length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		audio_out.close();
		audio_out.drain();
		System.out.println("stop");
	}
}
