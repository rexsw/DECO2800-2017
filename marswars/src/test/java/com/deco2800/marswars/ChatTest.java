package com.deco2800.marswars;

import com.deco2800.marswars.net.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


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
	// SpacServer server;

	// class MockedSpacServer extends SpacServer {
	// 	Set<Listener> listeners = new HashSet<>();

	// 	// We don't want to create a Kryonet Server
	// 	public MockedSpacServer() {}

	// 	public void bind(int port) {
	// 		// Set active server to this one.
	// 		server = this;
	// 	}

	// 	// We don't have a Kryonet server, so we can't stop it
	// 	public void stop() {}

	// 	public void addConnectionManager(Listener listener) {
	// 		this.listeners.add(listener)
	// 	}
	// }

	// class MockedSpacClient extends SpacClient {
	// 	Set<Listener> listeners = new HashSet<>();

	// 	// We don't want to create a Kryonet Client
	// 	public MockedSpacClient() {}

	// 	// We don't really 'connect'
	// 	public void connect(int timeout, String host, int port) throws IOException {}

	// 	// Don't have a client, can't stop it
	// 	public void stop() {}

	// 	public void addConnectionManager(Listener listener) {
	// 		this.listeners.add(listener)
	// 	}
	// }
	ServerConnectionManager servManager;
	LoggingConnectionManager aliceManager;
	LoggingConnectionManager bobManager;
	
    // Correct line separator for executing machine 
    private final static String LS = System.getProperty(
            "line.separator");
    
	public Connection createConnection(Listener listener) {
		MockConnection connection = new MockConnection();
		connection.addListener(listener);
		return connection;
	}

	@Before
	public void setup() {
		servManager = new ServerConnectionManager();
		aliceManager = new LoggingConnectionManager();
		bobManager = new LoggingConnectionManager();
	}

	@Test
	public void BasicJoinTest() {
		Connection alice = createConnection(aliceManager);
		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);
		LobbyAction lobby = new LobbyAction(servManager.getUserList());
		String log = aliceJoin.toString() + LS + lobby.toString();
		// assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), log);
	}

	@Test
	public void TwoJoinTest() {
		Connection alice = createConnection(aliceManager);
		Connection bob = createConnection(bobManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");

		servManager.received(alice, aliceJoin);
		LobbyAction aliceLobby = new LobbyAction(servManager.getUserList());
		servManager.received(bob, bobJoin);
		
		// assertEquals(
		// 		servManager.getLog(),
		// 		"*Alice* joined the lobby.\n*Bob* joined the lobby."
		// );
		LobbyAction bobLobby = new LobbyAction(servManager.getUserList());
		String aliceLog = aliceJoin.toString() + LS + aliceLobby.toString() + LS + bobJoin.toString() + LS + bobLobby.toString();
		String bobLog = bobJoin.toString() + LS + bobLobby.toString() ;
		
		assertEquals(aliceManager.getLog(), aliceLog);
		assertEquals(bobManager.getLog(), bobLog);
	}

	@Test
	public void MessageTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		MessageAction aliceMessage = new MessageAction("Alice", "new phone who dis");

		servManager.received(alice, aliceJoin);
		servManager.received(alice, aliceMessage);
		LobbyAction lobby = new LobbyAction(servManager.getUserList());
		String log = aliceJoin.toString() + LS + lobby.toString() + LS + aliceMessage.toString();

		// assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), log);
	}

	@Test
	public void ClientLeaveTest() {
		Connection alice = createConnection(aliceManager);
		Connection bob = createConnection(bobManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		JoinLobbyAction bobJoin = new JoinLobbyAction("Bob");

		servManager.received(alice, aliceJoin);
		LobbyAction lobby1 = new LobbyAction(servManager.getUserList());
		servManager.received(bob, bobJoin);
	    LobbyAction lobby2= new LobbyAction(servManager.getUserList());
		servManager.disconnected(alice);
	    LobbyAction lobby3 = new LobbyAction(servManager.getUserList());
	    LeaveLobbyAction dc = new LeaveLobbyAction("Alice");
		
		// assertEquals(
		// 		servManager.getLog(),
		// 		"*Alice* joined the lobby.\n" +
		// 				"*Bob* joined the lobby.\n" +
		// 				"*Alice* left the lobby."
		// 		);
		
		String aliceLog = aliceJoin.toString() + LS + lobby1.toString() + LS + bobJoin.toString() + LS +
		        lobby2.toString();
		String bobLog = bobJoin.toString() + LS + lobby2.toString() + LS + dc.toString() + LS + lobby3.toString(); 
		
		assertEquals(aliceManager.getLog(), aliceLog);
		assertEquals(bobManager.getLog(), bobLog);
	}

	@Test
	public void ServerShutdownTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);
		LobbyAction lobby = new LobbyAction(servManager.getUserList());
		aliceManager.disconnected(null);
		// assertEquals(
		// 		servManager.getLog(),
		// 		"*Alice* joined the lobby."
		// );
		ServerShutdownAction shutdown = new ServerShutdownAction();
		String aliceLog = aliceJoin.toString() + LS + lobby.toString() + LS + shutdown.toString();
		assertEquals(aliceManager.getLog(), aliceLog);
	}

	@Test
	public void JoinLobbyTwiceTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");

		servManager.received(alice, aliceJoin);
		servManager.received(alice, aliceJoin);

		// assertEquals(
		// 		servManager.getLog(),
		// 		"*Alice* joined the lobby."
		// );
		LobbyAction lobby = new LobbyAction(servManager.getUserList());
		String aliceLog = aliceJoin.toString() + LS + lobby.toString();
		assertEquals(aliceManager.getLog(), aliceLog);
	}

	@Test
	public void MessageBeforeJoinTest() {
		Connection alice = createConnection(aliceManager);

		JoinLobbyAction aliceJoin = new JoinLobbyAction("Alice");
		MessageAction aliceMessage = new MessageAction("new phone who dis");

		servManager.received(alice, aliceMessage);
		servManager.received(alice, aliceJoin);
		LobbyAction lobby = new LobbyAction(servManager.getUserList());
		String aliceLog = aliceJoin.toString() + LS + lobby.toString();

		// assertEquals(servManager.getLog(), log);
		assertEquals(aliceManager.getLog(), aliceLog);
	}
}
