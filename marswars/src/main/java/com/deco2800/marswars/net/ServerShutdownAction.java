package com.deco2800.marswars.net;

/**
 * Network action object for when server shuts-down.
 * Not actually sent over the network.
 */
public class ServerShutdownAction implements Action {

    public ServerShutdownAction() {}

    @Override
    public String toString() {
        return "Server shutdown";
    }
}
