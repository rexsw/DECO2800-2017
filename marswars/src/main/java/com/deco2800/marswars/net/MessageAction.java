package com.deco2800.marswars.net;

public class MessageAction {
    private String username;
    private String message;

    public MessageAction() {}

    public MessageAction(String msg) {
        this.message = msg;
    }

    // Should only be used by server
    public MessageAction(String username, String msg) {
        this.username = username;
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUsername() {
        return this.username;
    }
}
