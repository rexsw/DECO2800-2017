package com.deco2800.marswars;

import com.deco2800.marswars.net.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

/**
 * Test chat functionality
 */
public class ChatTest {
	int TEST_PORT = 9876;

	SpacServer server;
	SpacClient alice;
	SpacClient bob;

	ServerConnectionManager servManager;
	ClientConnectionManager aliceManager;
	ClientConnectionManager bobManager;

	@Before
	public void setup() {
		servManager = new ServerConnectionManager();
		server = new SpacServer(servManager);

		aliceManager = new ClientConnectionManager();
		alice = new SpacClient(aliceManager);

		bobManager = new ClientConnectionManager();
		bob = new SpacClient(bobManager);
	}

	@After
	public void teardown() {
		alice.stop();
		bob.stop();
		server.stop();
	}

	/**
	 * Helper function to wait for messages. This is horrible, I hate it
	 */
	void sleepThread() {
		try {
			Thread.sleep(1000);
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Helper function to connect the clients to the server
	 */
	void startServerAndClients() {
		try {
			server.bind(TEST_PORT);
		} catch (IOException e) {
			fail("Server failed to bind");
		}

		try {
			alice.connect(5000, "localhost", TEST_PORT);
		} catch (IOException e) {
			fail("Alice failed to connect");
		}

		try {
			bob.connect(5000, "localhost", TEST_PORT);
		} catch(IOException e) {
			fail("Bob failed to connect");
		}
	}

	@Test
	public void ConnectionTest() {
		startServerAndClients();

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		alice.sendObject(aliceJoin);

		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");
		bob.sendObject(bobJoin);

		sleepThread();

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby.\n" +
				"*Bob* joined the lobby."
		);
	}

	// TODO: Test JoinLobbyAction sent twice
}
