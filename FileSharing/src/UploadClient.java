import java.net.*;
import java.util.Date;
import java.io.*;
public class UploadClient {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket("127.0.0.1", 7777);
        File file = new File("C:/Test");

        File[] fileList = file.listFiles();
        String filename = null;
        BufferedOutputStream toServer = null;
        BufferedInputStream bis = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            for (int i = 0; i < fileList.length; i++) {
                System.out.println(new Date());
                File files = fileList[i];
                System.out.println(files.getName());
                filename = files.getPath();
                toServer = new BufferedOutputStream(s.getOutputStream());
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(filename);

                fis = new FileInputStream(filename);
                bis = new BufferedInputStream(fis);

                dos.writeInt(bis.available());
                int ch = 0;
                while ((ch = bis.read()) != -1) {
                    toServer.write(ch);
                }
                toServer.flush();
                new Thread().sleep(1000);

            }
            fis.close();
            s.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}