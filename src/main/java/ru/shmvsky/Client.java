package ru.shmvsky;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;

	private Client(String username, Socket socket) {
		try {
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
			writer.write(username);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMessageToChat(String message) {
		try {
			writer.write(message);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listenForNewMessages() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String message = null;
					while (socket.isConnected()) {
						message = reader.readLine();
						System.out.println(message);
					}
				} catch (IOException e) {
					Thread.currentThread().interrupt();
				}
			}
		}).start();
	}

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your username: ");
		String username  = sc.nextLine();
		Socket socket = new Socket(InetAddress.getLocalHost(), 1337);
		Client client = new Client(username, socket);
		client.listenForNewMessages();
		String message = sc.nextLine();
		while (!message.equals("STOP")) {
			client.sendMessageToChat(message);
			message = sc.nextLine();
		}
		sc.close();
		client.sendMessageToChat("STOP");
		client.quitChat();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
