import java.net.*;
import java.io.*;
import java.util.StringTokenizer;


public class UploadServer {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(7777);
            System.out.println("Server ready..");
             while (true) {
                Socket s = ss.accept();
                System.out.println("Sucess!");
                System.out.println(s.getInetAddress());// IP address
                ServerThread st = new ServerThread(s);
                st.start();
             }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}// end of class
class ServerThread extends Thread {
    Socket socket;
    public ServerThread(Socket s) {
        socket = s;
    }
    public void run() {
        try {
            while( true ) {
                BufferedInputStream up = new BufferedInputStream(socket.getInputStream());
                DataInputStream fromClient = new DataInputStream(up);
                System.out.println("wait...");
                String filename = fromClient.readUTF();
                System.out.println(filename + "\t received.");
                int len = fromClient.readInt();
                System.out.println(len + "\t received.");
                System.out.println("filename is : " + filename);
                
                StringTokenizer st = new StringTokenizer(filename,"\\"); // 문자열과 매개값을 정함
                int countTokens = st.countTokens();

                for(int i=0; i<countTokens; i++) { // 구분자 수만큼 반복
                	String token = st.nextToken(); // 구분자를 하나씩 꺼냄
                	filename = token; // 하나씩 꺼내는 걸 확인
                	}
                
                
                FileOutputStream toFile = new FileOutputStream("Test2/" + filename);
                BufferedOutputStream outFile = new BufferedOutputStream(toFile);
                int ch = 0;
//            while ((ch = up.read()) != -1) {
//                outFile.write(ch);
//            }
                for (int i = 0; i < len; i++) {
                    outFile.write(up.read());
                }
                outFile.flush();
                outFile.close();
//            fromClient.close();
//            socket.close();
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            try { socket.close(); } catch (Exception e) {}


        }
    }
}