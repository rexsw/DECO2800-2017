package com.deco2800.marswars.net;

import com.esotericsoftware.kryonet.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This is the listener that will log all received actions
 */
public class LoggingConnectionManager extends ConnectionManager {
	// Prefix for all logged actions
	protected String prefix;
	// List of actions received
    protected List<Action> actionLog = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
    // Correct line separator for executing machine 
    private final static String LINE_SEPARATOR = System.getProperty(
            "line.separator");
    /**
     * Instatiate a connection manager to log all actions.
     * @param prefix String to prefix all logs with
     */
	public LoggingConnectionManager(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Instantiate a connection manager to log all actions without a prefix;
	 */
	public LoggingConnectionManager() {
		this.prefix = "";
	}

	/**
     * Helper function to log actions received
     */
    protected void logAction(Action action) {
        this.actionLog.add(action);
        LOGGER.debug(this.prefix + action.toString());
    }

    /**
     * Get the action log in string format
     */
    public String getLog() {
        return this.actionLog.stream()
                .map(Action::toString)
                .collect(Collectors.joining(LINE_SEPARATOR));
    }

	@Override
	public void disconnected(Connection connection) {
		this.logAction(new ServerShutdownAction());
	}

	@Override
	public void received(Connection connection, Object o) {
		if (o instanceof Action) {
			Action action = (Action) o;
			this.logAction(action);
		}
	}
}
