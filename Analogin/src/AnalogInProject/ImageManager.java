package AnalogInProject;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageManager {
	static final Image background = new ImageIcon(Main.class.getResource("../image/testBackground.jpg")).getImage();
	static final Image mainFrame = new ImageIcon(Main.class.getResource("../image/mainFrame.jpg")).getImage();
	static final Image subFrame = new ImageIcon(Main.class.getResource("../image/subFrame.jpg")).getImage();
	static final Image nullImage = new ImageIcon(Main.class.getResource("../image/subFrame.jpg")).getImage();
	static final Image testButtonImage = new ImageIcon(Main.class.getResource("../image/2.jpg")).getImage();

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
