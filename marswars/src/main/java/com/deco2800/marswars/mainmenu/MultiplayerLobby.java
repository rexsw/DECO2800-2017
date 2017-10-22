package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.net.ConnectionManager;
import com.deco2800.marswars.net.StartGameAction;
import com.deco2800.marswars.net.StartRequestAction;
import com.esotericsoftware.kryonet.Connection;

/**
 * The UI for the multiplayer lobby.
 * 
 * @author James McCall
 *
 */
public class MultiplayerLobby extends Table {
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
    
    // ChatBox
    private LobbyChatBox chatBox;
    // IP of the Connected server
    private String hostIP;
    // is the player the host
    private boolean host;
    // Connected Server label
    private Label connectionLabel;
    // playerList
    private LobbyPlayerList playerList;
    // Ready Up Button
    private ReadyButton readyButton;
    // Start Button, only used on host player
    private Button startButton;
    // Left Panel, wrapper to allow for formatting
    private Table leftPanel;
    // Game options
    private OptionSelectionPanel options;
    // The currently selected options for the game
    private SelectedOptions selectedOptions;

    // Skin for styling the various aspects
    Skin skin;
    
    /**
     * Create the layout for the multiplayer lobby screen.
     * 
     * @param skin The skin for styling the various elements of the Lobby.
     * @param hostIP The IP address of the host of the lobby.
     * @param host True if this player is the host.
     */
    public MultiplayerLobby(Skin skin, String hostIP, boolean host) {
        this.skin = skin;
        this.hostIP = hostIP;
        this.host = host;
        // Set up various aspects
        readyButton = new ReadyButton(this.skin);
        chatBox = new LobbyChatBox(this.skin);
        connectionLabel = new Label(createConnectedString(), this.skin);
        playerList = new LobbyPlayerList(this.skin);
        leftPanel = new Table();
           
        selectedOptions = new SelectedOptions(this.skin);

        setupLeftPanel();
        setupLayout(); 
        setupStartGameHandler();
    }
    
    /**
     * Helper method to set up a handler that will initiate the start of a new game when it receives the
     * signal to from the server.  
     */
    private void setupStartGameHandler() {
        netManager.getNetworkClient().addConnectionManager(
                new ConnectionManager() {
                    @Override
                    public void received(Connection connection, Object o) {
                        if (o instanceof StartGameAction) {
                            StartGameAction action = (StartGameAction) o;
                            if (action.canGameStart()) {
                                //TODO start new game
                            }
                        }
                    }
                }
        );
    }
       
    /**
     * Helper mehtod for setting up the layout of the leftPanel. This layout changes dependign if the player is
     * the host or client. If the player is the host they have access to changing the game options. Whereas if 
     * the player is a client they can only see the currently selected game options. 
     */
    private void setupLeftPanel() {
        if (host) {
            options = new OptionSelectionPanel(this.skin); 
            leftPanel.add(options).expand(); //GameOptions
            leftPanel.row();
        }
        leftPanel.add(selectedOptions).expand();
        leftPanel.row();
        leftPanel.add(chatBox).bottom();
    }
    
    /**
     * Helper method that places all elements in their correct positions in the UI layout.
     * Assumes that all UI elements have been instantiated.
     */
    private void setupLayout() {
        if (host) {
            startButton = new TextButton("Start", skin);
            setupStartButton();
        }
        this.add(leftPanel).expand().fill(); 
        this.add(playerList).size(200, 300).pad(20).top().expand();
        this.row();
        this.add(connectionLabel).right();
        this.add(readyButton).right();
        if (host) {
            this.add(startButton).right();
        }
    }
    
    /**
     * Helper method for setting up handler for the start button.
     */
    private void setupStartButton() {
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                StartRequestAction canStart = new StartRequestAction();
                netManager.getNetworkClient().sendObject(canStart);
            }
        });
    }
    /**
     * Creates a string in a format to be displayed within the UI.
     * @return A string in the format, "Connected to: <Server IP>"
     */
    private String createConnectedString() {
        return "Connected to: " + hostIP;
    }
}
