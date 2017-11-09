package server_voice;

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
	
	public int port= 8888;
	
	public static AudioFormat getAudioFormat() {
		float sampleRate = 8000.0F;
		int sampleSizeInbits = 16;
		int channel = 2;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat (sampleRate, sampleSizeInbits, channel, signed, bigEndian);
	}
	public SourceDataLine audio_out;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					server_fr frame = new server_fr();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public server_fr() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		jLabell = new JLabel("Server Voice");
		jLabell.setFont(new Font("210 ∏«πﬂ¿«√ª√· L", Font.BOLD, 16));
		jLabell.setBounds(152, 10, 127, 40);
		contentPane.add(jLabell);
		
		btn_start = new JButton("Start");
		btn_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				init_audio();
			}
		});
		btn_start.setBounds(165, 136, 97, 23);
		contentPane.add(btn_start);
	}
	
	public void init_audio() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info_out = new DataLine.Info(SourceDataLine.class, format); 
			if(!AudioSystem.isLineSupported(info_out)) {
				System.out.println("not supported");
				System.exit(0);
			}
			audio_out = (SourceDataLine)AudioSystem.getLine(info_out);
			audio_out.open(format);
			audio_out.start();
			player_thread p = new player_thread();
			p.din = new DatagramSocket(port);
			p.audio_out = audio_out;
			Server_voice.calling = true;
			p.start();
			btn_start.setEnabled(false);
			
		} catch (LineUnavailableException | SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private javax.swing.JButton btn_start;
	private javax.swing.JLabel jLabell;
}
