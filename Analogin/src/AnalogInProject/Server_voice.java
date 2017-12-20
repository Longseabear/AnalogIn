package AnalogInProject;

public class Server_voice {
	public static boolean calling = false;
	
	public static void main(String[] args) {
		server_fr.init_audio();
		server_fr voiceServer = new server_fr();
	}
}
