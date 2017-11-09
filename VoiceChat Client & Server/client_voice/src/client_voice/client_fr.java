package client_voice;

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
public class client_fr extends JFrame {
	

	public int port_server = 8888;
	public String add_server = "127.0.0.1";
	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat (sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
	TargetDataLine audio_in;


	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client_fr frame = new client_fr();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public client_fr() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		jLabell = new JLabel("                              Client Voice");
		jLabell.setFont(new Font("210 ∏«πﬂ¿«√ª√· B", Font.PLAIN, 17));
		contentPane.add(jLabell, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		btn_start = new JButton("Start");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				init_audio();
				
			}
		});
		panel.add(btn_start);
		
		btn_stop = new JButton("Stop");
		btn_stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client_voice.calling = false;
				btn_start.setEnabled(true);
				btn_stop.setEnabled(false);
			}
		});
		panel.add(btn_stop);
	}
	public void init_audio() {
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
			btn_start.setEnabled(false);
			btn_stop.setEnabled(true);
		} catch (LineUnavailableException | UnknownHostException | SocketException ex) {
			Logger.getLogger(client_fr.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private javax.swing.JButton btn_start;
	private javax.swing.JButton btn_stop;
	private javax.swing.JLabel jLabell;
}
