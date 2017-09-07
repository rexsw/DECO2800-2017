package com.deco2800.marswars.managers;
import com.deco2800.marswars.MarsWars;
import com.deco2800.marswars.net.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetManager extends Manager {
    static final int SERVER_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(MarsWars.class);

    private ClientConnectionManager clientConnectionManager = new ClientConnectionManager();
    private ServerConnectionManager serverConnectionManager = new ServerConnectionManager();

    private SpacServer networkServer = new SpacServer(serverConnectionManager);
    private SpacClient networkClient = new SpacClient(clientConnectionManager);

    public SpacServer getNetworkServer(){
        return networkServer;
    }

    public SpacClient getNetworkClient(){
        return networkClient;
    }

    public ServerConnectionManager getServerConnectionManager(){
        return serverConnectionManager;
    }

    public ClientConnectionManager getClientConnectionManager(){
        return clientConnectionManager;
    }

    public void startServer(String ip) {

        //Initiate Server
        try {
            networkServer.bind(SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Error when initiating server", e);
        }

        //Join it as a Client
        try {
            networkClient.connect(5000, ip, SERVER_PORT);
        } catch (IOException e) {
            LOGGER.error("Error when joining as client", e);
        }
        JoinLobbyAction action = new JoinLobbyAction("Host");
        networkClient.sendObject(action);

        LOGGER.info(ip);

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