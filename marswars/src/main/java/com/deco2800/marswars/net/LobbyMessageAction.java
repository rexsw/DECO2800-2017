package com.deco2800.marswars.net;

public class LobbyMessageAction extends ChatAction {
    private String username;
    private String message;
    
    public LobbyMessageAction() {
        // Blank construtor for kryonet
    }
    
    public LobbyMessageAction(String message) {
        this.message = message;
    }
    
    public LobbyMessageAction(String username, String message) {
        this.username = username;
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override 
    public String toString() {
        return username + ": " + message;
    }
}