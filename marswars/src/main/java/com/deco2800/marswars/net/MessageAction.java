package com.deco2800.marswars.net;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

/**
 * Network action object for when somebody sends a message.
 */
public class MessageAction extends ChatAction {
    private String username;
    private String message;
    private String time;

    public MessageAction() {
        // Blank constructor needed due to Kryonet
    }

    /**
     * Constructor that clients use, they can't set the username.
     */
    public MessageAction(String msg) {
        this.message = msg;
        this.time = this.getTimeString();
    }

    /**
     * Constructor that the server uses.
     */
    public MessageAction(String username, String msg) {
        this.username = username;
        this.message = msg;
        this.time = this.getTimeString();
    }

    private String getTimeString() {
        TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);
        return (
                "[" + formatTime(timeManager.getPlayHours()) + ":"
                    + formatTime(timeManager.getPlayMinutes()) + ":"
                    + formatTime(timeManager.getPlaySeconds()) + "]");
    }

    public String getMessage() {
        return this.message;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTime() {
        return time;
    }
    @Override
    public String toString() {
        return time + " " + this.username + ": " + this.message;
    }
    
    /**
     * Returns the given time as a string containing a leading 0 if the time is less than 10.
     * @param time The time to convert.
     * @return the formatted time string.
     */
    private String formatTime(long time) {
        String result;
        String stringTime = Long.toString(time);
        if (stringTime.length() < 2) {
            result = "0" + stringTime;
        } else {
            result = stringTime;
        }
        return result;
    }
}
