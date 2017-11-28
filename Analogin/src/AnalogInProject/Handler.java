package AnalogInProject;

class Handler extends Thread {
	String jobType = "";

	Handler(String job) {
		jobType = job;
	}

	public void job1() {
		synchronized (NetworkRoomServer.networkJar) {
			while (!NetworkRoomServer.networkJar.containsKey(this.getName())) {
				try {
					NetworkRoomServer.networkJar.wait();
				} catch (InterruptedException e) {
					e.getStackTrace();
					System.out.println("error");
				}
			}
			NetworkRoomServer.networkJar.notifyAll();
		}

	}

	// RoomInfo
	public void job2() {
		synchronized (NetworkRoomServer.networkJar) {
			while (!NetworkRoomServer.networkJar.containsKey("ROOM_INFO")) {
				try {
					NetworkRoomServer.networkJar.wait();
				} catch (InterruptedException e) {
					e.getStackTrace();
					System.out.println("error");
				}
			}
			NetworkRoomServer.networkJar.notifyAll();
		}

	}

	// RoomInfo
	public void job3() {
		while (true) {
			synchronized (NetworkRoomServer.networkJar) {
				while (!NetworkRoomServer.networkJar.containsKey("LOBBY_STATE_CHANGE")) {
					try {
						NetworkRoomServer.networkJar.wait();
					} catch (InterruptedException e) {
						e.getStackTrace();
						System.out.println("error");
						break;
					}
				}
				NetworkRoomServer.networkJar.remove("LOBBY_STATE_CHANGE");
				NetworkRoomServer.networkJar.notifyAll();
			}
			Scene_Lobby.synRoomInfoWithServer();

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
		}
		System.out.println("[" + getName() + "]" + " is Handle ok");
	}
}
