package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.HashMap;
import java.util.Map;


public class ServerConnectionManager extends Listener {
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

	private Map<Integer, User> idToUser = new HashMap<>();

	private void broadcastAction(Object o) {
		for (Integer id : this.idToUser.keySet()) {
			User to = this.idToUser.get(id);
			to.getConnection().sendTCP(o);
		}
	}

	@Override
	public void disconnected(Connection connection) {

	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof JoinLobbyAction) {
			JoinLobbyAction action = (JoinLobbyAction) o;
			String username = action.getUsername();
			System.out.println("*" + username + " joined the lobby.*");
			this.idToUser.put(connection.getID(), new User(username, connection));

			this.broadcastAction(action);
		} else if (o instanceof MessageAction) {
			MessageAction action = (MessageAction) o;
			User from = this.idToUser.get(connection.getID());
			System.out.println(from.getUsername() + ": " + action.getMessage());

			this.broadcastAction(new MessageAction(from.getUsername(), action.getMessage()));
		}
	}

	@Override
	public void idle(Connection connection) {

	}
}
