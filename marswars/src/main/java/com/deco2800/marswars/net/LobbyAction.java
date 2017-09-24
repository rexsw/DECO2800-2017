package com.deco2800.marswars.net;

import java.util.ArrayList;

/**
 * Action for communicating the state of the server and clients lobby.
 * 
 * @author James McCall
 *
 */
public class LobbyAction implements Action {
    // A list of users connected to the server
    private ArrayList<LobbyUser> userList;
    // Correct line separator for executing machine 
    private final static String LINE_SEPARATOR = System.getProperty(
            "line.separator");
    
    public LobbyAction() {
        // Blank constructor needed due to Kryonet
    }
    
    /**
     * Constructor to send the current state of the lobby.
     * @param userList A list of the current users in the lobby. 
     */
    public LobbyAction(ArrayList<LobbyUser> userList) {
        this.userList = userList;
    }
    
    /**
     * 
     * @return the list of users in the lobby.
     */
    public ArrayList<LobbyUser> getUserList() {
        return this.userList;
    }
    
    @Override
    public String toString() {
        String result = "";
        for (LobbyUser user: userList) {
            result += user.toString() + LINE_SEPARATOR;
         }
        return result;
    }
}
