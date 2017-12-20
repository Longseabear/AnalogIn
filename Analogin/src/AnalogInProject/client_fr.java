package AnalogInProject;

import javax.swing.*;
import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.logging.*;

@SuppressWarnings("serial")
public class client_fr {
	public static int port_server = 8888;
	public static String add_server = "127.0.0.1";
	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat (sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
	static TargetDataLine audio_in;
	
	public static void init_audio() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			if(!AudioSystem.isLineSupported(info)) {
				System.out.println("not support");
				System.exit(0);
			}
			audio_in = (TargetDataLine) AudioSystem.getLine(info);
			audio_in.open(format);
			audio_in.start();
			recorder_thread r= new recorder_thread();
			InetAddress inet = InetAddress.getByName(add_server);
			r.audio_in = audio_in;
			r.dout= new DatagramSocket();
			r.server_ip = inet;
			r.server_port = port_server;
			Client_voice.calling = true;
			r.start();
			Scene_GamePlaying.VoicechatButton.setEnabled(false);
			Scene_GamePlaying.VoicechatButton.setEnabled(true);
		} catch (LineUnavailableException | UnknownHostException | SocketException ex) {
			Logger.getLogger(client_fr.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
