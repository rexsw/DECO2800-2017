package com.deco2800.marswars.net;


/**
 * Network action object for when somebody sends a message.
 */
public class MessageAction {
    private String username;
    private String message;

    public MessageAction() {}

    /**
     * Constructor that clients use, they can't set the username.
     */
    public MessageAction(String msg) {
        this.message = msg;
    }

    /**
     * Constructor that the server uses.
     */
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