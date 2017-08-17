package com.deco2800.marswars.net;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

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
    public SpacClient(Listener connectionManager) {
        this.client = new Client();
        this.client.addListener(connectionManager);
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
     * Send object to host
     */
    public void sendObject(Object o) {
        // TODO check this.client.isConnected()
        this.client.sendTCP(o);
    }
}
