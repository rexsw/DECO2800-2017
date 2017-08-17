package com.deco2800.marswars;

import com.deco2800.marswars.net.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Test chat functionality
 */
public class ChatTest {
	int TEST_PORT = 9876;

	@Test
	public void ConnectionTest() {
		ServerConnectionManager servManager = new ServerConnectionManager();
		SpacServer server = new SpacServer(servManager);

		ClientConnectionManager aliceManager = new ClientConnectionManager();
		SpacClient alice = new SpacClient(aliceManager);

		ClientConnectionManager bobManager = new ClientConnectionManager();
		SpacClient bob = new SpacClient(bobManager);

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

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		alice.sendObject(aliceJoin);

		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");
		bob.sendObject(bobJoin);

		assertEquals(
				servManager.getLog(),
				"*Alice* joined the lobby.\n" +
				"*Bob* joined the lobby."
		);
	}

	// TODO: Test JoinLobbyAction sent twice
}
