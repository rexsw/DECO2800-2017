package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.net.ConnectionManager;
import com.deco2800.marswars.net.GameInfoAction;
import com.deco2800.marswars.net.RequestGameInfoAction;
import com.deco2800.marswars.util.NewGameInformation;
import com.deco2800.marswars.util.ServerGameInformation;
import com.esotericsoftware.kryonet.Connection;

/**
 * A UI element that displays the currently selected options for a multiplayer game.
 * 
 * @author James McCall
 *
 */
public class SelectedOptions extends Table {
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
    
    // Label Map selected
    private Label selectedMap;
    // Label size selected
    private Label selectedSize;
    // The skin used to style elements
    private Skin skin;
    
    // Information about the game.
    private NewGameInformation gameInfo;
    
    // Strings for map and size selection
    private static final String MAP_SELECT_TEXT = "Selected Map: ";
    private static final String SIZE_SELECT_TEXT = "Selected Size: ";
    
    
    public SelectedOptions(Skin skin) {
        this.skin = skin;
        gameInfo = new NewGameInformation();
        // Initiate UI elements
        
        selectedMap = new Label(getMapText(), this.skin);
        selectedSize = new Label(getSizeText(), this.skin);
        
        setupCommunicationHandler();
        getInitialGameInformation();
        setupLayout();
    }
    
    /**
     * Send a request to the server to receive updated information about the state of the lobby.
     */
    private void getInitialGameInformation() {
        RequestGameInfoAction getInfo = new RequestGameInfoAction();
        netManager.getNetworkClient().sendObject(getInfo);
    }

    /**
     * Updates the internally stored state of the game information to the provided information.
     * 
     * @param newInfo The new information to be stored.
     */
    private void updateGameInfo(ServerGameInformation newInfo) {
        gameInfo.setMapSize(newInfo.getMapSize());
        gameInfo.setMapType(newInfo.getMapType());
    }
    
    /**
     * Update the UI to display the currently stored game information.
     */
    private void updateUI() {
        selectedMap.setText(getMapText());
        selectedSize.setText(getSizeText());
    }
    
    /**
     * 
     * @return A string in the format that is suitable to be displayed on the UI.
     */
    private String getMapText() {
        String mapName;
        if (gameInfo.getMapType() == null) {
            mapName = "None";
        } else {
            mapName = gameInfo.getMapType().toString();
        }
        return MAP_SELECT_TEXT + mapName;
    }

    /**
     * 
     * @return A string in the format that is suitable to be displayed on the UI.
     */
    private String getSizeText() {
        String mapSize;
        if (gameInfo.getMapSize() == null) {
            mapSize = "None";
        } else {
            mapSize = gameInfo.getMapSize().toString();
        }
        return SIZE_SELECT_TEXT + mapSize;
    }
    
    /**
     * Sets up a handler that updates the UI when ever new game information is received from the server.
     */
    private void setupCommunicationHandler() {
        netManager.getNetworkClient().addConnectionManager(
                new ConnectionManager() {
                    @Override
                    public void received(Connection connection, Object o) {
                        if (o instanceof GameInfoAction) {
                            GameInfoAction action = (GameInfoAction) o;
                            updateGameInfo(action.getGameInfo());
                            updateUI();
                        }
                    }
                });
    }
    
    /**
     * Helper mehtod for setting up the layout of the UI element.
     */
    private void setupLayout() {
        this.add(selectedMap).pad(10);
        this.row();
        this.add(selectedSize).pad(10);
    }
}
