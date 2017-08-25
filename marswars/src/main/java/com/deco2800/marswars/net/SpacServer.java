package com.deco2800.marswars.net;


import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

/**
 * Helpful wrapper around Kryonet's Server.
 */
public class SpacServer {
    private Server server;

    /**
     * Creates a SpacServer with the specified connection manager (listener)
     * see: ServerConnectionManager
     */
    public SpacServer(Listener connectionManager) {
        this.server = new Server();
        this.server.addListener(connectionManager);
    }

    /**
     * Start listening on port
     */
    public void bind(int port) throws IOException {
        this.server.start();
        ActionUtils.registerActions(this.server.getKryo());
        this.server.bind(port);
    }

    /**
     * Stop server
     */
    public void stop() {
        this.server.stop();
    }
}
