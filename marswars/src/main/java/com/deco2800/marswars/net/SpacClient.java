package com.deco2800.marswars.net;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class SpacClient {
    private Client client;

    public SpacClient(Listener connectionManager) {
        this.client = new Client();
        this.client.addListener(connectionManager);
    }

    public void connect(int timeout, String host, int port) throws IOException {
        this.client.start();
        ActionUtils.registerActions(this.client.getKryo());
        this.client.connect(timeout, host, port);
    }

    public void sendObject(Object o) {
        this.client.sendTCP(o);
    }
}
