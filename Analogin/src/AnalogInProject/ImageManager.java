package AnalogInProject;

import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageManager {
	
	static final Image background = new ImageIcon(Main.class.getResource("../image/background.jpg")).getImage();
	static final Image mainFrame = new ImageIcon(Main.class.getResource("../image/subFrame.jpg")).getImage();
	static final Image subFrame = new ImageIcon(Main.class.getResource("../image/sub.jpg")).getImage();
	static final Image setupImage = new ImageIcon(Main.class.getResource("../image/setup.png")).getImage();
	static final Image nullImage = new ImageIcon(Main.class.getResource("../image/sub.jpg")).getImage();
	static final Image testButtonImage_1 = new ImageIcon(Main.class.getResource("../image/New.png")).getImage();
	static final Image testButtonImage_2 = new ImageIcon(Main.class.getResource("../image/Insert.png")).getImage();
	static final Image testButtonImage_3 = new ImageIcon(Main.class.getResource("../image/Delete.png")).getImage();
	static final Image testButtonImage_4 = new ImageIcon(Main.class.getResource("../image/Rule.png")).getImage();
	static final Image testButtonImage_5 = new ImageIcon(Main.class.getResource("../image/exit.png")).getImage();
	static final Image checkImage = new ImageIcon(Main.class.getResource("../image/check.png")).getImage();
	
	//Opening
	static final Image Opening = new ImageIcon(Main.class.getResource("../image/opening.jpg")).getImage();	
	
	//Lobby
	static final Image Lobby = new ImageIcon(Main.class.getResource("../image/lobby.jpg")).getImage();	
	
	//cache
	static String cache = null;
	public static String loadImage() {
		JFileChooser chooser = null;
		if(cache==null)
			chooser = new JFileChooser(cache);// 梓端 持失
		else
			chooser = new JFileChooser(System.getProperty("user.home")+"/Desktop");// 梓端 持失
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF & PNG Images", "jpg", "gif","png");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(GIM.GameObject);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String str = chooser.getSelectedFile().getAbsolutePath();
			String returnValue = str.replace("\\", "\\\\");
			System.out.println(returnValue);
			cache = returnValue;
			return returnValue;
		}
		return null;
	}
}
