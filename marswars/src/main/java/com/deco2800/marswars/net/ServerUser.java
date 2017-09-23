package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;

/**
 * A class for storing information about user's on the server.
 * @author James McCall
 *
 */
public class ServerUser extends LobbyUser {
    private Connection connection;
    
    public ServerUser(String username, Connection conn) {
        super(username);
        this.connection = conn;
    }
    
    /**
     * Used to retrieve the user's current connection status.
     * @return The user's current connection.
     */
    public Connection getConnection() {
        return this.connection;
    }
  
}
