package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;

import java.util.HashMap;
import java.util.Map;


/**
 * This is the listener for SpacServer. It will handle network events
 */
public class ServerConnectionManager extends ConnectionManager {
	/**
	 * Utility class for storing username and connection
	 */
	private class User {
		private String username;
		private Connection connection;

		public User(String username, Connection conn) {
			this.username = username;
			this.connection = conn;
		}

		public String getUsername() {
			return this.username;
		}

		public Connection getConnection() {
			return this.connection;
		}
	}

	// Lookup of connection id to User
	private Map<Integer, User> idToUser = new HashMap<>();

	/**
	 * Helper function to send an action to all users
	 */
	private void broadcastAction(Object o) {
		for (Map.Entry<Integer, User> entry : this.idToUser.entrySet()) {
			entry.getValue().getConnection().sendTCP(o);
		}
	}

	@Override
	public void disconnected(Connection connection) {
		int id = connection.getID();
		User from = this.idToUser.get(id);
		this.idToUser.remove(id);
		LeaveLobbyAction action = new LeaveLobbyAction(from.getUsername());
		this.logAction(action);
		this.broadcastAction(action);
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof JoinLobbyAction) {
			int conId = connection.getID();
			if (this.idToUser.containsKey(conId)) {
				// Already received from them, ignore
				return;
			}

			JoinLobbyAction action = (JoinLobbyAction) o;
			String username = action.getUsername();
			this.logAction(action);
			this.idToUser.put(connection.getID(), new User(username, connection));

			this.broadcastAction(action);
		} else if (o instanceof MessageAction) {
			MessageAction action = (MessageAction) o;
			User from = this.idToUser.get(connection.getID());

			if (from == null) {
				return; // We don't know them
			}

			MessageAction newAction = new MessageAction(from.getUsername(), action.getMessage());
			this.logAction(newAction);

			this.broadcastAction(newAction);
		}
	}
}
