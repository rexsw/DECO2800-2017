package com.deco2800.marswars.net;

import com.deco2800.marswars.net.Action;

/**
 * Network action object for when somebody sends a message.
 */
public class MessageAction implements Action {
    private String username;
    private String message;

    public MessageAction() {
        // Blank constructor needed due to Kryonet
    }

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

    @Override
    public String toString() {
        return this.username + ": " + this.message;
    }
}
