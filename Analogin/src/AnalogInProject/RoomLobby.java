package AnalogInProject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class RoomLobby extends JFrame {
	public JPanel userPanel;
	public JPanel buttonPanel;
	public String roomName;

	public void roomLobbyClose() {
		NetworkRoomServer.exitRoom(roomName);
		this.dispose();
	}

	public boolean refresh() {

		userPanel.removeAll();
		synchronized (Scene_Lobby.roomInfoList) {
			for (UserInfo user : Scene_Lobby.roomInfoList.get(roomName).User) {
				userPanel.add(new JButton(user.id));
			}
		}
		while (userPanel.getComponentCount() < 4) {
			userPanel.add(new JButton());
		}
		userPanel.revalidate();
		userPanel.repaint();
		return true;
	}

	public RoomLobby(String _roomName) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		System.out.println("[ROOM] User entrance into " + _roomName);

		roomName = _roomName;
		setTitle(roomName);
		setSize(500, 300);
		setResizable(false);
		setLocationRelativeTo(null);

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		setLayout(gbl);

		userPanel = new JPanel();
		buttonPanel = new JPanel();
		userPanel.setLayout(new GridLayout(2, 2));
		userPanel.setBackground(new Color(0, 0, 0, 100));

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.setBackground(new Color(0, 0, 0, 125));
		buttonPanel.setAlignmentY(RIGHT_ALIGNMENT);
		JButton startBtn = new JButton("START");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomLobbyClose();
			}
		});
		JButton exitBtn = new JButton("EXIT");
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				roomLobbyClose();
			}
		});
		buttonPanel.add(startBtn);
		buttonPanel.add(exitBtn);
		addGrid(gbl, gbc, userPanel, 0, 0, 1, 1, 100, 100);
		addGrid(gbl, gbc, buttonPanel, 0, 1, 1, 2, 1, 1);
		setSize(500, 500);

		this.setPreferredSize(new Dimension(500, 300));
		refresh();
		pack();
		setVisible(true);
	}

	private void addGrid(GridBagLayout gbl, GridBagConstraints gbc, Component c, int gridx, int gridy, int gridwidth,
			int gridheight, int weightx, int weighty) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbl.setConstraints(c, gbc);
		add(c);
	}
}
