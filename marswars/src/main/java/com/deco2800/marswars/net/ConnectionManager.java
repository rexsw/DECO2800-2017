package com.deco2800.marswars.net;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * Base Class for Connection Managers.
 */
public class ConnectionManager extends Listener {
    protected List<Action> actionLog = new ArrayList<>();

    /**
     * Helper function to format the log message before outputting it
     */
    protected String formatLogMessage(String original) {
        return original;
    }

    /**
     * Helper function to log actions received
     */
    protected void logAction(Action action) {
        this.actionLog.add(action);
        System.out.println(this.formatLogMessage(action.toString()));
    }

    /**
     * Get the action log in string format
     */
    public String getLog() {
        return this.actionLog.stream()
                .map(Action::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public void disconnected(Connection connection) {}

    @Override
    public void received(Connection connection, Object o) {}

    @Override
    public void idle(Connection connection) {}
}
