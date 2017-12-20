package AnalogInProject;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Block extends JLabel {
	/*********************************
	 * Information *
	 *********************************/
	BlockInformation blockInfo;
	Analogin GameObject = GIM.GameObject;
	Block gameObject = this;

	//Option
	int op = 0;
	// Option 0 -> normal
	// Option 1 -> making
	
	private int mouseX, mouseY;

	public Block(BlockInformation b) {
		blockInfo = b;
		if(blockInfo.image==null){
			blockInfo.setImagePath(blockInfo.imagePath);
		}
		synBlockInfo();
		
		// initialize
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				if (GIM.currentInputCollector != null)
					GIM.currentInputCollector.playIn("CLICK_" + GIM.blockObject.indexOf(gameObject) + "_" + mouseX + "_" + mouseY);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (GIM.getCheckdBlock() != gameObject) {
						BlockManager.checkedBlockUpdate(gameObject);
						gameObject.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
						GIM.setCheckedBlock(gameObject);
					} else {
						gameObject.setBorder(null);
						GIM.setCheckedBlock(null);
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
				if (GIM.currentInputCollector != null) {
					GIM.currentInputCollector.playIn("MOVE_" + GIM.blockObject.indexOf(gameObject) + "_"
							+ (x - GameObject.getLocationOnScreen().x - mouseX) + "_"
							+ (y - GameObject.getLocationOnScreen().y - mouseY));
				}
			}
		});

	}
	public Block(BlockInformation b , int _op) {
		blockInfo = b;
		op = _op;
		if(blockInfo.image==null){
			blockInfo.setImagePath(blockInfo.imagePath);
		}
		synBlockInfo();
		// initialize
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// System.out.println(blockObject.indexOf(gameObject));
				mouseX = e.getX();
				mouseY = e.getY();
				if(!blockInfo.isStatic)
				{
					GameObject.remove(gameObject);
					GameObject.add(gameObject, 0);
					GIM.blockObject.remove(gameObject);
					GIM.blockObject.add(0,gameObject);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (GIM.getCheckdBlock() != gameObject) {
						BlockManager.checkedBlockUpdate(gameObject);
						gameObject.setBorder(BorderFactory.createLineBorder(Color.RED, 2, true));
						GIM.setCheckedBlockCreateRoom(gameObject);
					} else {
						gameObject.setBorder(null);
						GIM.setCheckedBlockCreateRoom(null);
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
				//System.out.println(x+" "+y+" mouse Pos : "+getLocationOnScreen().getX()+ " " + getLocationOnScreen().getY());
				blockInfo.x = x-GameObject.getLocationOnScreen().x-mouseX;
				blockInfo.y = y-GameObject.getLocationOnScreen().y-mouseY;
				setLocation(x-GameObject.getLocationOnScreen().x-mouseX,y-GameObject.getLocationOnScreen().y-mouseY);	
				GIM.updateData();
			}
		});
	}
	private int imageSizeX = 0;
	private int imageSizeY = 0;
	
	public void synBlockImage(){
		if(imageSizeX!=blockInfo.width || imageSizeY!=blockInfo.height)
		{
			imageSizeX = blockInfo.width;
			imageSizeY = blockInfo.height;
			blockInfo.setImagePath(blockInfo.imagePath);
			synBlockInfo();
			
		}
	}
	public void synBlockPosition(){
		setLocation(blockInfo.x,blockInfo.y);
	}
	public void synBlockInfo() {
		this.setBounds(blockInfo.x, blockInfo.y, blockInfo.width, blockInfo.height);
		ImageIcon icon = new ImageIcon(blockInfo.image);
		icon.getImage().flush();
		this.setIcon(icon);
		this.revalidate();
		this.repaint();
	}

}