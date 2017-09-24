package com.deco2800.marswars.managers;

import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NetManager extends Manager {
    static final int SERVER_PORT = 8081;
    private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);


    private ServerConnectionManager serverConnectionManager = new ServerConnectionManager();

    private SpacServer networkServer = new SpacServer();
    private SpacClient networkClient = new SpacClient();

    public NetManager() {
        networkServer.addConnectionManager(serverConnectionManager);
        networkServer.addConnectionManager(new LoggingConnectionManager("| "));

        networkClient.addConnectionManager(new LoggingConnectionManager());
    }

    public SpacServer getNetworkServer(){
        return networkServer;
    }

    public SpacClient getNetworkClient(){
        return networkClient;
    }

    public void startServer() {
        //Initiate Server
        try {
            networkServer.bind(SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Error when initiating server", e);
        }

        //Join it as a Client
        try {
            networkClient.connect(5000, "localhost", SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Error when joining as client", e);
        }
        JoinLobbyAction action = new JoinLobbyAction("Host");
        networkClient.sendObject(action);
    }

    public void startClient(String ip, String username){
        try {
            networkClient.connect(5000, ip, SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Join server error", e);
        }
        JoinLobbyAction action = new JoinLobbyAction(username);
        networkClient.sendObject(action);
    }
}