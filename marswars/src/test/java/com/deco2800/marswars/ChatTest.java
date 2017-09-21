package com.deco2800.marswars;

import com.deco2800.marswars.net.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that mocks Kryonet's Connection class for testing
 */
class MockConnection extends Connection {
	// Static counter to ensure unique id
	private static int currentConnectionId = 0;
	// Identification integer
	private int id;
	private List<Listener> listeners;

	public MockConnection() {
		this.id = currentConnectionId++;
		this.listeners = new ArrayList<>();
	}

	public int getID() {
		return this.id;
	}

	/**
	 * Add a listener to this connection
	 */
	public void addListener(Listener listener) {
		this.listeners.add(listener);
	}

	/**
	 * This is used to send an object _to_ this connection
	 */
	public int sendTCP(Object object) {
		for(Listener listener : this.listeners) {
			listener.received(this, object);
		}
		return 0;
	}
}

/**
 * Test chat functionality
 */
public class ChatTest {
	ServerConnectionManager servManager;
	ClientConnectionManager aliceManager;
	ClientConnectionManager bobManager;

	public Connection createConnection(Listener listener) {
		MockConnection connection = new MockConnection();
		connection.addListener(listener);
		return connection;
	}

	@Before
	public void setup() {
		servManager = new ServerConnectionManager();
		aliceManager = new ClientConnectionManager();
		bobManager = new ClientConnectionManager();
	}

	@Test
	public void BasicJoinTest() {
		Connection alice = createConnection(aliceManager);
		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);

		String log = "*Alice* joined the lobby.";
		assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), log);
	}

	@Test
	public void TwoJoinTest() {
		Connection alice = createConnection(aliceManager);
		Connection bob = createConnection(bobManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");

		servManager.received(alice, aliceJoin);
		servManager.received(bob, bobJoin);

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby.\n*Bob* joined the lobby."
		);
		assertEquals(
				aliceManager.getLog(),
				"*Alice* joined the lobby.\n*Bob* joined the lobby."
		);
		assertEquals(
				bobManager.getLog(),
				"*Bob* joined the lobby."
		);
	}

	@Test
	public void MessageTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		MessageAction aliceMessage = new MessageAction("Alice", "new phone who dis");

		servManager.received(alice, aliceJoin);
		servManager.received(alice, aliceMessage);

		String log = "*Alice* joined the lobby.\n" + aliceMessage.toString();

		System.out.println(log);
		System.out.println(servManager.getLog());
		System.out.println(aliceManager.getLog());
		assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), log);
	}

	@Test
	public void ClientLeaveTest() {
		Connection alice = createConnection(aliceManager);
		Connection bob = createConnection(bobManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");

		servManager.received(alice, aliceJoin);
		servManager.received(bob, bobJoin);
		servManager.disconnected(alice);

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby.\n" +
						"*Bob* joined the lobby.\n" +
						"*Alice* left the lobby."
				);

		assertEquals(
				aliceManager.getLog(),
				"*Alice* joined the lobby.\n" +
						"*Bob* joined the lobby."
		);

		assertEquals(
				bobManager.getLog(),
						"*Bob* joined the lobby.\n" +
						"*Alice* left the lobby."
		);
	}

	@Test
	public void ServerShutdownTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);
		aliceManager.disconnected(null);

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby."
		);

		assertEquals(
				aliceManager.getLog(),
				"*Alice* joined the lobby.\n" +
						"Server shutdown"
		);
	}

	@Test
	public void JoinLobbyTwiceTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);
		servManager.received(alice, aliceJoin);

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby."
		);

		assertEquals(
				aliceManager.getLog(),
				"*Alice* joined the lobby."
		);
	}

	@Test
	public void MessageBeforeJoinTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		MessageAction aliceMessage = new MessageAction("new phone who dis");

		servManager.received(alice, aliceMessage);
		servManager.received(alice, aliceJoin);

		String log = "*Alice* joined the lobby.";

		assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), log);
	}
}
