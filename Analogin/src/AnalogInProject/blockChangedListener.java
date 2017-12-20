package AnalogInProject;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

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
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if(!GIM.getCheckdBlock().blockInfo.isStatic)
								scene.staticButton.setIcon(null);
							else
								scene.staticButton.setIcon(new ImageIcon(ImageManager.checkImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH)));
							if(!GIM.getCheckdBlock().blockInfo.isVisible)
								scene.visibleButton.setIcon(null);
							else
								scene.visibleButton.setIcon(new ImageIcon(ImageManager.checkImage.getScaledInstance(27, 27, Image.SCALE_SMOOTH)));
	
						}
					});
					lock.notifyAll();
				}
			} while (true);
		}
}
