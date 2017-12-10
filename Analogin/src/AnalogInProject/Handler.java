package AnalogInProject;

import java.util.ArrayList;
import java.util.HashMap;

class Handler extends Thread {
	String jobType = "";
	String content;
	HashMap<String, Object> networkJar = null;

	Handler(String job) {
		jobType = job;
	}

	Handler(String job, String _content) {
		jobType = job;
		content = _content;
	}

	Handler(String job, HashMap<String, Object> jar) {
		jobType = job;
		networkJar = jar;
	}

	public void job1() {
		try {
			synchronized (NetworkRoomServer.networkJar) {
				while (!NetworkRoomServer.networkJar.containsKey(this.getName())) {
					NetworkRoomServer.networkJar.wait();
				}
				NetworkRoomServer.networkJar.notifyAll();
			}
		} catch (InterruptedException e) {
			e.getStackTrace();
			System.out.println("1 error");
		}
	}

	// RoomInfo
	public void job2() {
		try {
			synchronized (NetworkRoomServer.networkJar) {
				while (!NetworkRoomServer.networkJar.containsKey("ROOM_INFO")) {
					NetworkRoomServer.networkJar.wait();
				}
				NetworkRoomServer.networkJar.notifyAll();
			}
		} catch (InterruptedException e) {
			e.getStackTrace();
			System.out.println(" 2error");
		}

	}

	// LOBBY_STATE_CHANGE
	public void job3() {
		try {
			while (true) {
				synchronized (NetworkRoomServer.networkJar) {
					while (!NetworkRoomServer.networkJar.containsKey("LOBBY_STATE_CHANGE")) {
						NetworkRoomServer.networkJar.wait();
					}
					NetworkRoomServer.networkJar.remove("LOBBY_STATE_CHANGE");
					NetworkRoomServer.networkJar.notifyAll();

				}
				Scene_Lobby.synRoomInfoWithServer();
			}
		} catch (InterruptedException e) {
			e.getStackTrace();
			System.out.println("error");
		}
	}

	// STUN_SERVER_STEP1
	public void job4() {
		try {
			while (true) {
				synchronized (NetworkRoomServer.networkJar) {
					while (!NetworkRoomServer.networkJar.containsKey("STUN_STEP1")) {
						NetworkRoomServer.networkJar.wait();
					}
					String masterName = (String) NetworkRoomServer.networkJar.get("STUN_STEP1");
					NetworkRoomServer.networkJar.remove("STUN_STEP1");
					NetworkRoomServer.networkJar.notifyAll();

					if (NetworkPeerManager.networkStart()) {
						NetworkRoomServer.Sender("STUN_CONNECT_OK " + masterName, "STUN_CONNECT", false);
					} else {
						NetworkRoomServer.Sender("STUN_CONNECT_FALSE " + masterName, "STUN_CONNECT", false);
					}

				}
			}
		} catch (InterruptedException e) {
			e.getStackTrace();
			System.out.println("4 error");
		}
	}

	// FILE_JAR
	public void job5() {
		try {
			synchronized (NetworkRoomServer.networkJar) {
				while (!networkJar.containsKey(this.getName())) {
					networkJar.wait();
				}
				networkJar.notifyAll();
			}
		} catch (InterruptedException e) {
			e.getStackTrace();
			System.out.println("5 error");
		}
	}
	// FILE_JAR
		public void job6() {
			try {
				synchronized (NetworkRoomServer.networkJar) {
					while (!networkJar.containsKey(this.getName())) {
						networkJar.wait();
					}
					networkJar.notifyAll();
				}
			} catch (InterruptedException e) {
				e.getStackTrace();
				System.out.println("5 error");
			}
		}
	@Override
	public void run() {
		switch (jobType) {
		case "object":
			job1();
			break;
		case "ROOM_INFO":
			job2();
			break;
		case "LOBBY_STATE_CHANGE":
			job3();
			break;
		case "STUN_SERVER_STEP1":
			job4();
			break;
		case "p2p":
			job5();
			break;
		case "P2P_INFO_SYN":
			job6();
			break;
		default:
			if (networkJar == null)
				job1();
			else
				job5();
			break;
		}
		System.out.println("[" + getName() + "]" + " is Handle ok");
	}
}
