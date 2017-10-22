package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.net.ConnectionManager;
import com.deco2800.marswars.net.LobbyAction;
import com.deco2800.marswars.net.LobbyRequestAction;
import com.deco2800.marswars.net.LobbyUser;
import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * A UI element that displays all the current players connected to a multiplayer lobby and their ready status.
 *  
 * @author James McCall
 *
 */
public class LobbyPlayerList extends Table {
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
    // Number of Connected Players
    private int numConnectedPlayers;
    // Label of connected players out of max
    private Label playerInfo;
    // A list of the connected players
    private List<LobbyUser> playerList;
    // A table to display the connected players and their ready Status
    private Table lobbyList;
    // skin used to style the various elements of the HUD
    private Skin skin;
       
    // Indicator if player is ready
    private static final String READY = "R";
    // Indicator if player is not ready
    private static final String NOT_READY = "N";
    
    // Width of the players column
    private static final int PLAYERS_WIDTH = 200;
    // Width of the ready column
    private static final int READY_WIDTH = 40;
    
    public LobbyPlayerList(Skin skin) {
        this.skin = skin;
        numConnectedPlayers = 0;
        playerList = new ArrayList<LobbyUser>();
        
        setupPlayerInfo();
        setupLobbyList();
        setupLayout();

        setupCommunicationHandler();
        getLobbyInformation();
    }
    
    /**
     * Updates the internal playerList to the given playerList.
     * @param playerList The new list of players to store. 
     */
    public void updatePlayerList(List<LobbyUser> playerList) {
        this.playerList = playerList;
        this.numConnectedPlayers = playerList.size();
        updateLobbyList();
    }
    
    /**
     * Helper method that updates the lobbyList so it reflects the current state of the playerList.
     */
    private void updateLobbyList() {
        setupLobbyList();
        for (LobbyUser user : playerList) {
            addPlayerToLobbyList(user);
        }
        this.clearChildren();
        this.playerInfo.setText(getPlayerInfoString());
        setupLayout();
    }
    
    /**
     * Adds the given player to the list of players displayed.
     * @param user The player to add.
     */
    private void addPlayerToLobbyList(LobbyUser user) {
        Label playerName = new Label(user.getUsername(), skin);
        
        lobbyList.add(playerName);
        lobbyList.add(getReadyIndicator(user.isReady()));
        lobbyList.row();
    }
    
    /**
     * Retrieves the appropriate indicator for the ready status.
     * @param ready True if the player is ready, and false otherwise.
     * @return The appropriate ready indicator based on the given state.
     */
    private Label getReadyIndicator(boolean ready) {
        if (ready) {
            return new Label(READY, skin);
        } else {
            return new Label(NOT_READY, skin);
        }
    }
    
    /**
     * Send a request to the server to receive updated information about the state of the lobby.
     */
    private void getLobbyInformation() {
        LobbyRequestAction getLobby = new LobbyRequestAction();
        netManager.getNetworkClient().sendObject(getLobby);
    }
    
    /**
     * Helper method that sets up the layout of LobbyPlayerList, assumes that all other UI elements have been
     * initialized.
     */
    private void setupLayout() {
        this.add(playerInfo).align(Align.right);
        this.row();
        this.add(lobbyList);
    }
    
    /**
     * Helper method for setting up a new LobbyList, adds headers for the various fields.
     */
    private void setupLobbyList() {
        lobbyList = new Table(skin);
        lobbyList.columnDefaults(0).width(PLAYERS_WIDTH);
        lobbyList.columnDefaults(1).width(READY_WIDTH);
        
        // Add Header
        Label players = new Label("Players", skin);
        Label ready = new Label("Ready", skin);
        lobbyList.add(players);
        lobbyList.add(ready);
        lobbyList.row();
    }
    
    /**
     * Helper method to set up the listener for a Lobby Action form the server, so that updates to the lobby can be
     * reflected in the UI.
     */
    private void setupCommunicationHandler() {
        netManager.getNetworkClient().addConnectionManager(
                new ConnectionManager() {
                    @Override
                    public void received(Connection connection, Object o) {
                        if (o instanceof LobbyAction) {
                            LobbyAction action = (LobbyAction) o;
                            updatePlayerList(action.getUserList());
                            updateLobbyList();
                        }
                    }
                });
    }
    
    /**
     * Helper method that converts the stored value of the number of players into a format suitable to be 
     * displayed in the UI.
     * @return The player info in the format to be displayed on screen.
     */
    private String getPlayerInfoString() {
        return "" + Integer.toString(numConnectedPlayers) + " connected to Lobby.";
    }
    
    /**
     * Helper method that contains all the actions needed to set up the player info label.
     */
    private void setupPlayerInfo() {
        playerInfo = new Label(getPlayerInfoString(), skin);
    }
}
