package com.deco2800.marswars.mainMenu;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.hud.HUDView;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.net.JoinLobbyAction;
import com.deco2800.marswars.net.ServerConnectionManager;
import com.deco2800.marswars.net.SpacClient;
import com.deco2800.marswars.net.SpacServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyButton{
	private Skin skin; 
	private Stage stage; 
	private Window mainmenu; 
	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);
	private Dialog ipDiag;

	private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
	
	public LobbyButton(Skin skin, Window mainmenu, Stage stage){
		this.skin = skin;
		this.stage = stage;
		this.mainmenu = mainmenu;
		System.out.println("Lobby instantiated"); //$NON-NLS-1$
	}
	
	/**
	 * Creates a 'Join Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @param menuScreen 
	 * @return Join Server button
	 */
	public Button addJoinServerButton(MenuScreen menuScreen){
		LOGGER.debug("attempt to add join lobby button"); //$NON-NLS-1$
		/* Join server button */
		Button joinServerButton = new TextButton("Join Server", this.skin); //$NON-NLS-1$
		joinServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menuScreen.setJoinedServer(); //set the game status to joined server
				menuScreen.selectCharacter(LobbyButton.this.mainmenu, LobbyButton.this.stage);

				// Construct inside of dialog
				Table inner = new Table(LobbyButton.this.skin);
				Label ipLabel = new Label("IP", LobbyButton.this.skin); //$NON-NLS-1$
				TextField ipInput = new TextField("localhost", LobbyButton.this.skin); //$NON-NLS-1$
				Label usernameLabel = new Label("Username", LobbyButton.this.skin); //$NON-NLS-1$
				TextField usernameInput = new TextField("wololo", LobbyButton.this.skin); //$NON-NLS-1$

				inner.add(ipLabel);
				inner.add(ipInput);
				inner.row();
				inner.add(usernameLabel);
				inner.add(usernameInput);
				inner.row();

				LobbyButton.this.ipDiag = new Dialog("IP", LobbyButton.this.skin, "dialog") { //$NON-NLS-1$ //$NON-NLS-2$
					@Override
					protected void result(Object o) {
						if(o != null) {
							String username = usernameInput.getText();
							String ip = ipInput.getText();

							netManager.startClient(ip, username);
						}
					}
				};

				LobbyButton.this.ipDiag.getContentTable().add(inner);
				LobbyButton.this.ipDiag.button("Join", true); //$NON-NLS-1$
				LobbyButton.this.ipDiag.button("Cancel" , null); //$NON-NLS-1$
				LobbyButton.this.ipDiag.show(LobbyButton.this.stage);
			}
		});
		
		return joinServerButton;
	}
	
	/**
	 * Creates a 'Start Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @param menuScreen 
	 * @return Start Server button
	 */
	public Button addStartServerButton(MenuScreen menuScreen){
		/* Start server button */
		Button startServerButton = new TextButton("Start Server", this.skin); //$NON-NLS-1$
		startServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				menuScreen.selectWorldMode(LobbyButton.this.mainmenu, LobbyButton.this.stage);

				Dialog ipDiag = new Dialog("Local IP", LobbyButton.this.skin, "dialog") {}; //$NON-NLS-1$ //$NON-NLS-2$

				try {
					InetAddress ipAddr = InetAddress.getLocalHost();
					String ip = ipAddr.getHostAddress();
					ipDiag.text("IP Address: " + ip); //$NON-NLS-1$
					LOGGER.info(ip);

					netManager.startServer();
				} catch (UnknownHostException ex) {
					ipDiag.text("Something went wrong"); //$NON-NLS-1$
					LOGGER.error("Unknown Host", ex); //$NON-NLS-1$
				}
				ipDiag.button("Close", null); //$NON-NLS-1$
				ipDiag.show(LobbyButton.this.stage);
			}

		});
		return startServerButton; 
	}		
	
	public void check(){
		System.out.println("just let me die"); //$NON-NLS-1$
	}
}