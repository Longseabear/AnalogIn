package AnalogInProject;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import java.awt.Font;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class server_fr extends JFrame {
	
	//public static int port= 8888;
	
	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat (sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
	public static SourceDataLine audio_out;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					server_fr frame = new server_fr();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void init_audio() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format); 
			if(!AudioSystem.isLineSupported(info_out)) {
				System.out.println("not supported");
				System.exit(0);
			}
			System.out.println("Hello");
			audio_out = (SourceDataLine)AudioSystem.getLine(info_out);
			audio_out.open(format);
			audio_out.start();
			player_thread p = new player_thread();
			p.din = new DatagramSocket(Scene_GamePlaying.port);
			p.audio_out = audio_out;
			Server_voice.calling = true;
			p.start();
			//Scene_GamePlaying.VoicechatButton.setEnabled(false);
			
		} catch (LineUnavailableException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
