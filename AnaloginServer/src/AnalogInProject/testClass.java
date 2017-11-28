package AnalogInProject;

public class testClass extends Thread{
	public static void main(String[] argv){
		testClass test = new testClass();
		test.start();
		try {
			Thread.sleep(2000);
			synchronized(test){
				test.notify();				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public void run(){
		try {
			synchronized(this){
				this.wait();
				System.out.println("test1");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("test2");
		}
	}
}
