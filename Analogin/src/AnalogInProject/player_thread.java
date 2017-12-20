package AnalogInProject;

import java.io.IOException;
import java.io.PrintWriter;
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
	private static HashSet<DatagramPacket> writers = new HashSet<DatagramPacket>();
	@Override
	public void run() {
		
		DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
		writers.add(incoming);
		while(Server_voice.calling) {
			try {
				for(DatagramPacket writer : writers) {
					din.receive(writer);
					buffer = writer.getData();
					audio_out.write(buffer, 0, buffer.length);
				}
			} catch (IOException e) {
				// TODO Auto-generated ca	tch block
				Logger.getLogger(player_thread.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		audio_out.close();
		audio_out.drain();
		System.out.println("voice chat stop");
	}
}
