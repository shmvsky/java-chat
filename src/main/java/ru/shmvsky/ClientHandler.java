package ru.shmvsky;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable {
	private UUID id;
	private String username;
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	public ClientHandler(Socket socket) {
		try {
			id = UUID.randomUUID();
			reader = new BufferedReader(
				new InputStreamReader(
					socket.getInputStream()
				)
			);
			writer = new BufferedWriter(
				new OutputStreamWriter(
					socket.getOutputStream()
				)
			);
			this.socket = socket;
			this.username = reader.readLine();
			HandlersRepository.getInstance().addClientHandler(this);
			broadcastMessage(String.format("User %s has joined the chat!", username));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		try {
			String line = reader.readLine();
			while (!line.equals("STOP")) {
				String message = String.format("%s: %s", username, line);
				broadcastMessage(message);
				line = reader.readLine();
			}
			quitChat();
		} catch (IOException e) {
			quitChat();
		}
	}

	public void quitChat() {
		try {
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			HandlersRepository.getInstance().removeHandler(this);
			broadcastMessage(String.format("%s left the chat", username));
			Thread.currentThread().interrupt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcastMessage(String message) {

		try {
			System.out.println(message);
			for (ClientHandler handler : HandlersRepository.getInstance().getClientHandlers()) {
				if (!handler.getId().equals(id)) {
					handler.writer.write(message, 0, message.length());
					handler.writer.newLine();
					handler.writer.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public UUID getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

}
