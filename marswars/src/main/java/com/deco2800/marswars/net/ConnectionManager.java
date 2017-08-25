package com.deco2800.marswars.net;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Class for Connection Managers.
 */
public class ConnectionManager extends Listener {
    // List of actions received
    protected List<Action> actionLog = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

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
        LOGGER.debug(this.formatLogMessage(action.toString()));
    }

    /**
     * Get the action log in string format
     */
    public String getLog() {
        return this.actionLog.stream()
                .map(Action::toString)
                .collect(Collectors.joining("\n"));
    }
}
