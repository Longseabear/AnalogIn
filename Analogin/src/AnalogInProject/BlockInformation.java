package AnalogInProject;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BlockInformation {
	public int width;
	public int height;
	public Image image;
	public String imagePath = null;
	public String blockName = "";
	public int x;
	public int y;
	
	public BlockInformation(int _x, int _y, int _width, int _height, Image _image )
	{
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		if(_image != null)
			image = _image.getScaledInstance(_width, _height, Image.SCALE_SMOOTH);
		else
			image = ImageManager.nullImage.getScaledInstance(_width, _height, Image.SCALE_SMOOTH);;
	}
	public BlockInformation(int _x, int _y, int _width, int _height, String _imagePath )
	{
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		imagePath = _imagePath;
		try{
			image = new ImageIcon(_imagePath).getImage();
		}catch(Exception e){
			e.printStackTrace();
		}finally{			
			image = ImageManager.nullImage.getScaledInstance(_width, _height, Image.SCALE_SMOOTH);;
		}
	}
	public void setImagePath(String _imagePath)
	{
		System.out.println(image);
		imagePath = _imagePath;
		try{
			image = new ImageIcon(_imagePath).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
		}catch(Exception e){
			image = ImageManager.nullImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			e.printStackTrace();
		}
	}
}
