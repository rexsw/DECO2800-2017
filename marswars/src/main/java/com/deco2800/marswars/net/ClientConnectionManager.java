package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This is the listener for SpacClient, it will handle network events.
 */
public class ClientConnectionManager extends ConnectionManager {
	protected String formatLogMessage(String original) {
		return "| " + original;
	}
	//private Set<MessageAction> messageActions = new HashSet<>();
	private List<ChatAction> messageActions = new ArrayList<>();
	public boolean toggle;

	@Override
	public void disconnected(Connection connection) {
		this.logAction(new ServerShutdownAction());
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof ChatAction) {
			ChatAction action = (ChatAction) o;
			this.messageActions.add(action);
			this.logAction(action);
		}
	}

//	public List<MessageAction> getMessageActions() {
//		return messageActions;
//	}
}
