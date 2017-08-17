package com.deco2800.marswars;

import com.deco2800.marswars.net.*;
import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.IOException;

/**
 * Test chat functionality
 */
public class ChatTest {
    @Test
    public void ConnectionTest() {
	ServerConnectionManager servManager = new ServerConnectionManager();
	SpacServer server = new SpacServer(servManager);

	ClientConnectionManager aliceManager = new ClientConnectionManager();
	SpacClient alice = new SpacClient(aliceManager);

	ClientConnectionManager bobManager = new ClientConnectionManager();
	SpacClient bob = new SpacClient(bobManager);

	try {
		server.bind(9876);
	} catch (IOException e) {
		fail("Server failed to bind");
	}

	try {
		alice.connect(5000, "localhost", 9876);
	} catch (IOException e) {
		fail("Alice failed to connect");
	}

	try {
		bob.connect(5000, "localhost", 9876);
	} catch(IOException e) {
		fail("Bob failed to connect");
	}
    }
}
