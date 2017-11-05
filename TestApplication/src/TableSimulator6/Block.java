package TableSimulator6;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Block extends JLabel{
	BlockInformation blockInfo;
	TableSimulator GameObject;
	private int mouseX, mouseY;
	public Block(BlockInformation b)
	{
		blockInfo = b;
		synBlockInfo();
		
		//initialize
		GameObject = GameInformationMaster.GameObject;
		
		this.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				mouseX = e.getX();
				mouseY = e.getY();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// Move Cursor
				setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter(){
			@Override
			public void mouseDragged(MouseEvent e){
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				//System.out.println(x+" "+y+" mouse Pos : "+getLocationOnScreen().getX()+ " " + getLocationOnScreen().getY());
				setLocation(x-GameObject.getLocationOnScreen().x-mouseX, y-GameObject.getLocationOnScreen().y-mouseY);
			}
		});
	}
	public void synBlockInfo()
	{
		this.setIcon(new ImageIcon(blockInfo.image));
		this.setBounds(blockInfo.x, blockInfo.y, blockInfo.width, blockInfo.height);
	}
}
