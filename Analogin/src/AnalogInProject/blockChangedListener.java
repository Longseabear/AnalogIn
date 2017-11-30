package AnalogInProject;

public class blockChangedListener extends Thread{
		public boolean update = false;
		public Object lock = new Object();
		private Scene_CreateGame scene = null;
		blockChangedListener(Scene_CreateGame c){
			scene = c;
		}
		public void setUpdaate() {
			synchronized (lock) {
				while(update){
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				update = true;
				lock.notifyAll();
			}
		}
		@Override
		public void run() {
			do {
				synchronized (lock) {
					while (!update) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							System.out.println("Thread End");
						}
					}
					Block block = GIM.getCheckdBlock();
					update = false;
					
					if (block == null)
					{
						scene.setupButton.setVisible(false);
						lock.notifyAll();
						continue;
					}
					scene.setupButton.setVisible(true);
					scene.blockNameTextField.setText(block.blockInfo.blockName);
					scene.sizeXTextField.setText("" + block.blockInfo.width);
					scene.sizeYTextField.setText("" + block.blockInfo.height);
					scene.posXTextField.setText("" + block.blockInfo.x);
					scene.posYTextField.setText("" + block.blockInfo.y);
					lock.notifyAll();
				}
			} while (true);
		}
}
