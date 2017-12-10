package AnalogInProject;

public class ServerMain {
	public static void main(String[] argv){
		try {
			new Thread(){
				@Override
				public void run(){
					try {
						new ConnectionServer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			}.start();
			Thread.sleep(100);
			new Thread(){
				@Override
				public void run(){
					try {
						new AnaloginServer();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			}.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
