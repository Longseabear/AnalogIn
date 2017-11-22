package AnalogInProject;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageManager {
	static final Image background = new ImageIcon(Main.class.getResource("../image/background.jpg")).getImage();
	static final Image mainFrame = new ImageIcon(Main.class.getResource("../image/subFrame.jpg")).getImage();
	static final Image subFrame = new ImageIcon(Main.class.getResource("../image/sub.jpg")).getImage();
	static final Image nullImage = new ImageIcon(Main.class.getResource("../image/subFrame.jpg")).getImage();
	static final Image testButtonImage_1 = new ImageIcon(Main.class.getResource("../image/New.png")).getImage();
	static final Image testButtonImage_2 = new ImageIcon(Main.class.getResource("../image/Insert.png")).getImage();
	static final Image testButtonImage_3 = new ImageIcon(Main.class.getResource("../image/Delete.png")).getImage();
	static final Image testButtonImage_4 = new ImageIcon(Main.class.getResource("../image/Rule.png")).getImage();
	static final Image testButtonImage_5 = new ImageIcon(Main.class.getResource("../image/exit.png")).getImage();
	
	public static String loadImage() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "gif","png");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(GIM.GameObject);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			String str = chooser.getSelectedFile().getAbsolutePath();
			String returnValue = str.replace("\\", "\\\\");
			System.out.println(returnValue);
			return returnValue;
		}
		return null;
	}
}
