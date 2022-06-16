package ru.shmvsky;

import java.util.ArrayList;
import java.util.List;

public class HandlersRepository {
	private List<ClientHandler> clientHandlers;
	private static HandlersRepository instance;

	private HandlersRepository() {
		clientHandlers = new ArrayList<>();
	}

	public static synchronized HandlersRepository getInstance() {
		if (instance == null) {
			instance = new HandlersRepository();
		}
		return instance;
	}

	public void addClientHandler(ClientHandler clientHandler) {
		clientHandlers.add(clientHandler);
	}

	public void removeHandler(ClientHandler handler) {
		clientHandlers.remove(handler);
	}

	public List<ClientHandler> getClientHandlers() {
		return clientHandlers;
	}

}
