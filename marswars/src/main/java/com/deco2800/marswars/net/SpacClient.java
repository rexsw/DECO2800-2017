package com.deco2800.marswars.net;


import com.esotericsoftware.kryonet.Client;

import java.io.IOException;


/**
 * Helpful wrapper around Kryonet's Client.
 */
public class SpacClient {
    private Client client;

    /**
     * Create a new SpacClient with the specified connection manager (listener)
     * see: ClientConnectionManager
     */
    public SpacClient() {
        this.client = new Client();
    }

    /**
     * Connect to host
     */
    public void connect(int timeout, String host, int port) throws IOException {
        this.client.start();
        ActionUtils.registerActions(this.client.getKryo());
        this.client.connect(timeout, host, port);
    }

    /**
     * Stop client / disconnect
     */
    public void stop() {
        this.client.stop();
    }

    /**
     * Send object to host
     */
    public void sendObject(Object o) {
        if (this.client.getID() >= 0) {
            this.client.sendTCP(o);
        }
    }

    /**
     * Adds connection manager
     */
    public void addConnectionManager(ConnectionManager manager) {
        this.client.addListener(manager);
    }
}
