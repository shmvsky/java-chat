package ru.shmvsky;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	private ServerSocket socket;

	private Server(ServerSocket socket) {
		this.socket = socket;
	}

	private void run() {
		while (true) {
			try {
				new Thread(new ClientHandler(socket.accept())).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(1337);
			System.out.printf("Start listening on port %d\n", 1337);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (socket != null) {
			new Server(socket).run();
		}
	}

}
