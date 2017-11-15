package AnalogInProject;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.Border;

public class Block extends JLabel {
	/*********************************
	 * Information *
	 *********************************/
	BlockInformation blockInfo;
	Analogin GameObject = GIM.GameObject;
	Block gameObject = this;

	private int mouseX, mouseY;

	public Block(BlockInformation b) {
		blockInfo = b;
		synBlockInfo();

		// initialize
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				// System.out.println(blockObject.indexOf(gameObject));
				mouseX = e.getX();
				mouseY = e.getY();

				GIM.keyInputBuffer
						.playIn("CLICK_" + GIM.blockObject.indexOf(gameObject) + "_" + mouseX + "_" + mouseY);
				// GameObject.remove(gameObject);
				// GameObject.add(gameObject, 0);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if(GIM.checkedBlock!=gameObject)
					{
						BlockManager.checkedBlockUpdate(gameObject);
						gameObject.setBorder(BorderFactory.createLineBorder(Color.RED, 2,true));
						GIM.checkedBlock = gameObject;
					}
					else
					{
						gameObject.setBorder(null);
						GIM.checkedBlock = null;
						
					}
						
				}
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
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				// System.out.println(x+" "+y+" mouse Pos :
				// "+getLocationOnScreen().getX()+ " " +
				// getLocationOnScreen().getY());
				// setLocation(x-GameObject.getLocationOnScreen().x-mouseX,
				// y-GameObject.getLocationOnScreen().y-mouseY);
				GIM.keyInputBuffer.playIn("MOVE_" + GIM.blockObject.indexOf(gameObject) + "_"
						+ (x - GameObject.getLocationOnScreen().x - mouseX) + "_"
						+ (y - GameObject.getLocationOnScreen().y - mouseY));
			}
		});
	}

	public void synBlockInfo() {
		//ImageIcon은 NULL이 아님을 보장받는다.
		this.setBounds(blockInfo.x, blockInfo.y, blockInfo.width, blockInfo.height);
		ImageIcon icon = new ImageIcon(blockInfo.image);
		icon.getImage().flush();
		this.setIcon(icon);
		this.revalidate();
		this.repaint();
	}
}
