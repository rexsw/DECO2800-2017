package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LobbyButton{
	private Skin skin; 

	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);
	private Dialog ipDiag;

	private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
	
	public LobbyButton(Skin skin, Window mainmenu, Stage stage){
		this.skin = skin;
		LOGGER.info("Lobby instantiated"); 
	}
	
	/**
	 * Creates a 'Join Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @param menuScreen 
	 * @return Join Server button
	 */

	public Table addJoinServerButton(MenuScreen menuScreen){
		LOGGER.debug("attempt to add join lobby button"); //$NON-NLS-1$
        // Construct inside of dialog
        Table inner = new Table(LobbyButton.this.skin);
        Label ipLabel = new Label("IP", LobbyButton.this.skin); //$NON-NLS-1$
        TextField ipInput = new TextField("localhost", LobbyButton.this.skin); //$NON-NLS-1$
        Label usernameLabel = new Label("Username", LobbyButton.this.skin); //$NON-NLS-1$
        TextField usernameInput = new TextField("The Spacinator", LobbyButton.this.skin); //$NON-NLS-1$

        inner.add(ipLabel);
        inner.add(ipInput);
        inner.row();
        inner.add(usernameLabel);
        inner.add(usernameInput);
        inner.row();
		/* Join server button */
		Button joinServerButton = new TextButton("Join Server", this.skin); //$NON-NLS-1$
		inner.add(joinServerButton).right();
		
		joinServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    String username = usernameInput.getText();
		        String ip = ipInput.getText();

		        netManager.startClient(ip, username);
		        menuScreen.setJoinedServer(); //set the game status to joined server 
		        menuScreen.multiplayerLobby(ip, false);
			}
		});

		return inner;
	}
	
	/**
	 * Creates a 'Start Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @param menuScreen 
	 * @return Start Server button
	 */
	public Button addStartServerButton(MenuScreen menuScreen){
		/* Start server button */
		Button startServerButton = new TextButton("Start Server", this.skin);
		startServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			    String ip = "";
				try {
					InetAddress ipAddr = InetAddress.getLocalHost();
					ip = ipAddr.getHostAddress();
					LOGGER.info(ip);

					netManager.startServer();
				} catch (UnknownHostException ex) {
					ipDiag.text("Something went wrong");
					LOGGER.error("Unknown Host", ex);
				}
				
				menuScreen.multiplayerLobby(ip, true);
			}

		});
		return startServerButton;
	}		
}