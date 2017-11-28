package AnalogInProject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class testClient {

	// TEMP INFORMAITON
	static Socket CandidateSocket = null;

	static String tempPrivatePort = "";
	static String tempPrivateIp = "";
	static String tempPublicPort = "";
	static String tempPublicIp = "";

	public static void main(String argv[]) throws Exception {
		String ip = "127.0.0.1";
		String port = "2346";
		String command;
		ArrayList<Socket> otherPlayerSocket = new ArrayList<Socket>();
		// Access Server
		Socket clientSocket = new Socket(ip, Integer.parseInt(port));
		clientSocket.setReuseAddress(true);
		
		System.out.println("Connection ok.");
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		int localPort = clientSocket.getLocalPort();
		
		// register
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					clientSocket.close();
					System.out.println("The server is shut down!");
				} catch (IOException e) {
					/* failed */ }
			}
		});
		try {
			do {
				System.out.println("Command Wait...");
				command = in.readLine();
				if (command == null)
					throw new Exception();

				System.out.println("Command : " + command);
				if (command.equals("REQUEST_INFO")) {
					Scanner p = new Scanner(System.in);
					System.out.println("Your name? ");
					String name = p.nextLine();
					out.println(name + "-" + InetAddress.getLocalHost().getHostAddress() + "-"
							+ clientSocket.getLocalPort());
				} else if (command.equals("PING")) {
					out.println("ok 200");
				} else if (command.equals("CONNECTION_SETTING")) {
					out.println("CONNECTION_SETTING_READY");
					String info = in.readLine();
					String userInfo[] = info.split("-");

					tempPrivateIp = userInfo[0];
					tempPrivatePort = userInfo[1];
					tempPublicIp = userInfo[2];
					tempPublicPort = userInfo[3];
					out.println("CONNECTION_SETTING_OK");	
				} else if (command.equals("CONNECTION_LISTEN")) {
					// 문제 예상 -> 소켓 = 연산자 불가능 / 중복
					clientSocket.close();
					ServerSocket listener = null;
					try {
						System.out.println(localPort);
						listener = new ServerSocket(localPort);
						listener.setReuseAddress(true);
						listener.setSoTimeout(30000);
						CandidateSocket = listener.accept();
					}catch(Exception e)
					{
						System.out.println("TIMEOUT OR ERROR!");
						e.printStackTrace();
					}
					finally {
						try {
							if (listener != null)
								listener.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					otherPlayerSocket.add(CandidateSocket);
					out = new PrintWriter(CandidateSocket.getOutputStream(), true);
					out.println("hello");
					CandidateSocket = null;
				} else if (command.equals("CONNECTION_SENDER")) {
					// 문제 예상 -> 소켓 = 연산자 불가능 / 중복
					clientSocket.close();
					Thread t = new Thread(){
						@Override
						public void run(){
							try {
								System.out.println(tempPublicIp+" "+tempPublicPort);
								CandidateSocket = new Socket("127.0.0.1", Integer.parseInt(tempPublicPort));
								CandidateSocket.setReuseAddress(true);
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					t.start();
					Thread.sleep(500);
					if(CandidateSocket!=null){
						System.out.println("success");
						in = new BufferedReader(new InputStreamReader(CandidateSocket.getInputStream()));
						System.out.println(in.readLine());
					}else
					{
						System.out.println("Fail");
					}
				}
				
			} while (true);
		} catch (Exception e) {
			e.getMessage();
		} finally {
			System.out.println("Client End");
			clientSocket.close();
		}
	}
}
