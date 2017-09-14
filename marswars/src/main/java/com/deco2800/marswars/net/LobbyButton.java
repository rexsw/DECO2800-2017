package com.deco2800.marswars.net;

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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.hud.HUDView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyButton{
	private Skin skin; 
	private Stage stage; 
	private static final Logger LOGGER = LoggerFactory.getLogger(HUDView.class);
	Dialog ipDiag;
	
	static final int SERVER_PORT = 8080;
	SpacClient networkClient;
	SpacServer networkServer;
	
	public LobbyButton(Skin skin, Stage stage){
		this.skin = skin;
		this.stage = stage;
		System.out.println("Lobby instantiated");
	}
	
	/**
	 * Creates a 'Join Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @return Join Server button
	 */
	public Button addJoinServerButton(){
		LOGGER.debug("attempt to add join lobby button");
		/* Join server button */
		Button joinServerButton = new TextButton("Join Server", skin);
		joinServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// Construct inside of dialog
				Table inner = new Table(skin);
				Label ipLabel = new Label("IP", skin);
				TextField ipInput = new TextField("localhost", skin);
				Label usernameLabel = new Label("Username", skin);
				TextField usernameInput = new TextField("wololo", skin);

				inner.add(ipLabel);
				inner.add(ipInput);
				inner.row();
				inner.add(usernameLabel);
				inner.add(usernameInput);
				inner.row();
				
				System.out.println("Just let me die xD");

				ipDiag = new Dialog("IP", skin, "dialog") {
					@Override
					protected void result(Object o) {
						if(o != null) {
							String username = usernameInput.getText();
							String ip = ipInput.getText();

							ClientConnectionManager connectionManager = new ClientConnectionManager();
							networkClient = new SpacClient(connectionManager);

							try {
								networkClient.connect(5000, ip, SERVER_PORT);
							} catch (IOException e) {
								LOGGER.error("Join server error", e);
							}
							JoinLobbyAction action = new JoinLobbyAction(username);
							networkClient.sendObject(action);
						}
					}
				};

				ipDiag.getContentTable().add(inner);
				ipDiag.button("Join", true);
				ipDiag.button("Cancel" , null);
				ipDiag.show(stage);
			}
		});
		
		return joinServerButton;
	}
	
	/**
	 * Creates a 'Start Server' button and event listener for multiplayer features.
	 * It is intended for the button to be added into the main menu. 
	 * @return Start Server button
	 */
	public Button addStartServerButton(){
		/* Start server button */
		Button startServerButton = new TextButton("Start Server", skin);
		startServerButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Dialog ipDiag = new Dialog("Local IP", skin, "dialog") {};
				try {
					InetAddress ipAddr = InetAddress.getLocalHost();
					String ip = ipAddr.getHostAddress();
					ipDiag.text("IP Address: " + ip);

					ServerConnectionManager serverConnectionManager = new ServerConnectionManager();
					networkServer = new SpacServer(serverConnectionManager);

					ClientConnectionManager clientConnectionManager = new ClientConnectionManager();
					networkClient = new SpacClient(clientConnectionManager);
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
						LOGGER.error("Error when joinging as client", e);
					}
					JoinLobbyAction action = new JoinLobbyAction("Host");
					networkClient.sendObject(action);

					LOGGER.info(ip);
				} catch (UnknownHostException ex) {
					ipDiag.text("Something went wrong");
					LOGGER.error("Unknown Host", ex);
				}
				ipDiag.button("Close", null);
				ipDiag.show(stage);
			}
		});
		
		return startServerButton; 
	}		
	
	public void check(){
		System.out.println("just let me die");
	}
}