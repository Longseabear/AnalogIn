package AnalogInProject;

import java.io.*;
import java.net.*;
import java.util.logging.*;

import javax.sound.sampled.TargetDataLine;

public class recorder_thread extends Thread{
	public TargetDataLine audio_in = null;
	public DatagramSocket dout;
	byte byte_buff[] = new byte[512];
	public InetAddress server_ip;
	public int server_port;
	
	@Override
	public void run() {
		int num = 1;
		while(Client_voice.calling) {
			try {
				audio_in.read(byte_buff, 0, byte_buff.length);
				DatagramPacket data = new DatagramPacket(byte_buff, byte_buff.length, server_ip, server_port);
				System.err.println("send #"+ (num++));
				dout.send(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(recorder_thread.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		audio_in.close();
		audio_in.drain();
		System.out.println("Thread Stop");
	}
}
