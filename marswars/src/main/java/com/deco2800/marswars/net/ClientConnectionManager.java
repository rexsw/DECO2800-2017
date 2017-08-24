package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;


/**
 * This is the listener for SpacClient, it will handle network events.
 */
public class ClientConnectionManager extends ConnectionManager {
	protected String formatLogMessage(String original) {
		return "| " + original;
	}

	@Override
	public void disconnected(Connection connection) {

	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof JoinLobbyAction) {
			JoinLobbyAction action = (JoinLobbyAction) o;
			this.logAction(action);
		} else if (o instanceof MessageAction) {
			MessageAction action = (MessageAction) o;
			this.logAction(action);
		}
	}

	@Override
	public void idle(Connection connection) {

	}
}
