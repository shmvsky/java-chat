package ru.shmvsky.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
	private static volatile Server instance;
	private ServerSocket socket;

	public Server() throws IOException {
		this.socket = new ServerSocket(ServerConfig.port);
		instance = this;
	}

	public static Server getInstance() throws IOException {
		if (instance == null) {
			synchronized (Server.class) {
				if (instance == null) {
					instance = new Server();
				}
			}
		}
		return instance;
	}

	@Override
	public void run() {
		try {
			Socket clientSocket = socket.accept();
			clientSocket.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
