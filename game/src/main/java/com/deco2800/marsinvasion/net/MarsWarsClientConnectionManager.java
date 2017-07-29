package com.deco2800.marsinvasion.net;

import com.esotericsoftware.kryonet.Connection;
import uq.deco2800.soom.client.game.GameClientConnectionManager;
import uq.deco2800.soom.server.lobby.Lobby;

/**
 * Created by timhadwen on 29/7/17.
 */
public class MarsWarsClientConnectionManager extends GameClientConnectionManager {
	@Override
	public void disconnected(Connection connection) {

	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof Lobby) {
			System.out.println(o.toString());
		}
	}

	@Override
	public void idle(Connection connection) {

	}
}
