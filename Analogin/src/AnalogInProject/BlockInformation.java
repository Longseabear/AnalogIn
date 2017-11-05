package AnalogInProject;

import java.awt.Image;

public class BlockInformation {
	public int width;
	public int height;
	public Image image;
	public int x;
	public int y;
	
	public BlockInformation(int _x, int _y, int _width, int _height, Image _image )
	{
		x = _x;
		y = _y;
		width = _width;
		height = _height;
		image = _image.getScaledInstance(_width, _height, Image.SCALE_SMOOTH);
	}
}
