package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class ClientConnectionManager extends Listener {
	@Override
	public void disconnected(Connection connection) {

	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof JoinLobbyAction) {
			JoinLobbyAction action = (JoinLobbyAction) o;
			String username = action.getUsername();
			System.out.println("| *" + username + " joined the lobby.*");
		} else if (o instanceof MessageAction) {
			MessageAction action = (MessageAction) o;
			System.out.println("| " + action.getUsername() + ": " + action.getMessage());
		}
	}

	@Override
	public void idle(Connection connection) {

	}
}
