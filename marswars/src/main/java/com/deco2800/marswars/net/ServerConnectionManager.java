package com.deco2800.marswars.net;

import com.deco2800.marswars.util.ServerGameInformation;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This is the listener for SpacServer. It will handle network events
 */
public class ServerConnectionManager extends ConnectionManager {
	// Lookup of connection id to User
	private Map<Integer, ServerUser> idToUser = new HashMap<>();
	// Game information to start a new game
	private ServerGameInformation gameInfo = new ServerGameInformation();

	
	/**
	 * Helper method used to retrieve a list of all users connected to the server.
	 * Set to public for testing purposes.
	 * @return A list of all users connected to the server.
	 */
	public ArrayList<LobbyUser> getUserList() {
	    ArrayList<LobbyUser> result = new ArrayList<LobbyUser>();
	    for (ServerUser user : idToUser.values()) {
	        result.add(new LobbyUser(user.getUsername(), user.isReady()));
	    }
	    return result;
	}
	
	/**
	 * Helper function to send an action to all users
	 */
	private void broadcastAction(Object o) {
		for (Map.Entry<Integer, ServerUser> entry : this.idToUser.entrySet()) {
			entry.getValue().getConnection().sendTCP(o);
		}
	}

	/**
	 * Helper method that handles a JoinLobbyAction when it is received. 
	 * Adds the given user to the lobby and broadcasts to all users that the lobby has been altered.
	 * 
	 * @param connection The connection the action was received from.
	 * @param action The JoinLobbyAction that was received.
	 */
	private void handleJoinLobbyAction(Connection connection, JoinLobbyAction action) {
        int conId = connection.getID();
        if (this.idToUser.containsKey(conId)) {
            // Already received from them, ignore
            return;
        }
        
        String username = action.getUsername();
        this.idToUser.put(connection.getID(), new ServerUser(username, connection));
        this.broadcastAction(action);
        
        // update people on the state of the lobby.
        LobbyAction lobbyAction = new LobbyAction(getUserList());
        this.broadcastAction(lobbyAction);
	}
	
	/**
	 * Helper method that handles a MessageAction when it is received. 
	 * Broadcasts the message received to all users. 
	 * 
	 * @param connection The connection the action was received from.
     * @param action The MessageAction that was received.
	 */
	private void handleChatAction(Connection connection, ChatAction action) {
        ServerUser from = this.idToUser.get(connection.getID());

        if (from == null) {
            return; // We don't know them
        }
        
        if (action instanceof MessageAction) {
            // Cast from ChatAction to MessageAction so message can be extracted.
            MessageAction msg = (MessageAction) action;
            // Send message with username
            MessageAction newAction = new MessageAction(from.getUsername(), msg.getMessage());
            this.broadcastAction(newAction);
        } else if (action instanceof LobbyMessageAction) {
            LobbyMessageAction msg = (LobbyMessageAction) action;
            LobbyMessageAction lobbyAction = new LobbyMessageAction(from.getUsername(), msg.getMessage());
            this.broadcastAction(lobbyAction);
        }
	}
	
	/**
     * Helper method that handles a ReadyAction when it is received. 
     * Broadcasts the new ready state of the user it was received from to all users in the lobby. 
     * 
     * @param connection The connection the action was received from.
     * @param action The ReadyAction that was received.
     */
	private void handleReadyAction(Connection connection, ReadyAction action) {
        int connectionID = connection.getID();
        ServerUser from = this.idToUser.get(connectionID);
        boolean readyStatus = action.getReadyStatus();
        
        if (from == null) {
            return; // We don't know them
        }
        
        idToUser.get(connectionID).setReady(readyStatus);
        LobbyAction lobbyAction = new LobbyAction(getUserList());
        this.broadcastAction(lobbyAction);
    } 
	
	private void handleLobbyRequestAction(Connection connection) {
	    LobbyAction lobby = new LobbyAction(getUserList());
	    this.broadcastAction(lobby);
	}
	
	private void handleStartRequestAction() {
	    StartGameAction action;
	    if (arePlayersReady() && gameInfo.isNewGameValid()) {
	        action = new StartGameAction(true, gameInfo);
	    } else {
	        action = new StartGameAction(false, gameInfo);
	    }
	    this.broadcastAction(action);
	}
	
	private void handleMapSizeAction(MapSizeAction action) {
	    gameInfo.setMapSize(action.getMapSize());
	    GameInfoAction toSend = new GameInfoAction(gameInfo);
	    this.broadcastAction(toSend);
	}
	
	private void handleMapTypeAction(MapTypeAction action) {
	    gameInfo.setMapType(action.getMapType());
	    GameInfoAction toSend = new GameInfoAction(gameInfo);
	    this.broadcastAction(toSend);
	}
	
	private void handleGameInfoRequest() {
	    GameInfoAction toSend = new GameInfoAction(gameInfo);
	    this.broadcastAction(toSend);
	}
	
	private boolean arePlayersReady() {
	    for (ServerUser user : idToUser.values()) {
	        if (!user.isReady()) {
	            return false;
	        }
	    }
	    return true;
	}
	@Override
	public void disconnected(Connection connection) {
		int id = connection.getID();
		ServerUser from = this.idToUser.get(id);
		this.idToUser.remove(id);
		LeaveLobbyAction action = new LeaveLobbyAction(from.getUsername());
		this.broadcastAction(action);
		// update people on the state of the lobby.
		LobbyAction lobbyAction = new LobbyAction(getUserList());
		this.broadcastAction(lobbyAction);
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof JoinLobbyAction) {
		    JoinLobbyAction action = (JoinLobbyAction) o;
		    handleJoinLobbyAction(connection, action);
		    
		} else if (o instanceof ChatAction) {
		    ChatAction action = (ChatAction) o;
		    handleChatAction(connection, action);

		} else if (o instanceof ReadyAction) {
		    ReadyAction action = (ReadyAction) o;
		    handleReadyAction(connection, action);
		    
		} else if (o instanceof LobbyRequestAction) {
		    handleLobbyRequestAction(connection);
		    
		} else if (o instanceof StartRequestAction) {
		    handleStartRequestAction();
		    
		} else if (o instanceof MapSizeAction) {
		    MapSizeAction action = (MapSizeAction) o;
		    handleMapSizeAction(action);
		    
		} else if (o instanceof MapTypeAction) {
		    MapTypeAction action = (MapTypeAction) o;
		    handleMapTypeAction(action);
		    
		} else if (o instanceof RequestGameInfoAction) {
		    handleGameInfoRequest();
		    
		}
	}
}
