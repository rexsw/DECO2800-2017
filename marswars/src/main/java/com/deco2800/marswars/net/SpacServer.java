package com.deco2800.marswars.net;


import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class SpacServer {
    private Server server;

    public SpacServer(Listener connectionManager) {
        this.server = new Server();
        this.server.addListener(connectionManager);
    }

    public void bind(int port) throws IOException {
        this.server.start();
        ActionUtils.registerActions(this.server.getKryo());
        this.server.bind(port);
    }
}
