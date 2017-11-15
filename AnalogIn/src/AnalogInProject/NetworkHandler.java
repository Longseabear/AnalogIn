package AnalogInProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class NetworkHandler extends Thread {
	private Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	private Player a;

	private String[] outputBuffer = new String[10];
	private Queue<String> inputBuffer = new LinkedList<String>();
	/**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
	
	// Consumer Provider 구조
	// 한 쓰레드는 입력버퍼를 대기하다가 처리한다.
	
	public NetworkHandler(Socket socket) {
            this.socket = socket;
    }
	public synchronized void setInputBuffer(int outputPort, String str){
		if(outputPort>=10){
			System.out.println("ERROR!!!!");
			return;
		}
		inputBuffer.add(outputPort+"/"+str);
	}

	public void run() {
		// 여기 try Catch 구문에서 폴스값을 리턴하면 그 플레이어는 빠져나간다.
		try{
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
			
			while(true)
			{
				if(!inputBuffer.isEmpty()){
					inputBuffer.remove();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			// 플레이어 접속종료
		}
	}
}
