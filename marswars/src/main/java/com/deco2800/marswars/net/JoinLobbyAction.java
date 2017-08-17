package com.deco2800.marswars.net;


public class JoinLobbyAction {
    private String username;

    public JoinLobbyAction() {}

    public JoinLobbyAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
