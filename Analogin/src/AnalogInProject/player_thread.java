package AnalogInProject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.SourceDataLine;
import java.util.HashSet;

public class player_thread extends Thread{
	public DatagramSocket din;
	public SourceDataLine audio_out;
	byte[] buffer = new byte[512];
	private static HashSet<DatagramSocket> writers = new HashSet<DatagramSocket>();
	@Override
	public void run() {
		writers.add(din);
		DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
		while(true) {
			try {
				for(DatagramSocket writer : writers) {
					writer.receive(incoming);
					buffer = incoming.getData();
					audio_out.write(buffer, 0, buffer.length);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
}
