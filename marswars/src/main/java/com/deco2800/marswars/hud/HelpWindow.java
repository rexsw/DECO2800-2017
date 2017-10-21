package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class HelpWindow {
	private static final int SIDEPANEBUTTONWIDTH = 160;
	private static final int SIDEPANEBUTTONHEIGHT = 40;
	private static final int WINDOWPAD = 20;
	private static final int HOTKEYBUTTON = 40;

	private static final boolean enabled = true; 

	private Stage stage;
	private Window window;
	private Skin skin;
	private Table sidePane;

	public HelpWindow(Stage stage, Skin skin) {
		this.stage = stage;
		this.skin = skin;

		this.window = new Window("Help", skin);
		window.setSize(600, 600);
		window.setDebug(enabled);
		window.align(Align.left | Align.top);
		window.pad(WINDOWPAD);
		stage.addActor(window);
		window.setPosition(stage.getWidth()/2 - window.getWidth(), stage.getHeight()/2 - window.getHeight()/2);
		buildWindow();
	}

	/**
	 * Builds the help window
	 */
	private void buildWindow() {
		window.add(this.sidePane()).align(Align.topLeft);
	}

	private Table sidePane() {
		sidePane = new Table();
		sidePane.setDebug(enabled);
		sidePane.align(Align.topLeft);
		Button gameGuide = new TextButton("GAME GUIDE", skin);
		Button hotKeys = new TextButton("HOTKEYS", skin);
		Button settings = new TextButton("SETTINGS", skin);
		Button back = new TextButton("BACK TO GAME", skin);
		
		gameGuide.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				window.clear();
			}
		});
		
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				window.setVisible(false);
			}
		});
		
		hotKeys.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				window.clear();
				buildWindow();
				hotKeysInfo();
			}
		});
		
		sidePane.add(gameGuide).size(SIDEPANEBUTTONWIDTH, SIDEPANEBUTTONHEIGHT).row();
		sidePane.add(hotKeys).size(SIDEPANEBUTTONWIDTH, SIDEPANEBUTTONHEIGHT).row();
		sidePane.add(settings).size(SIDEPANEBUTTONWIDTH, SIDEPANEBUTTONHEIGHT).row();
		sidePane.add(back).size(SIDEPANEBUTTONWIDTH, SIDEPANEBUTTONHEIGHT).row();
				
		return sidePane;
	}
	
	public Window getHelpWindow() {
		return this.window;
	}
	
	private Table hotKeysInfo() {
		Table hotKeysParent = new Table();
		Table hotKeysChild = new Table();
		Label hotkeysInfo = new Label("HOTKEYS", skin);
		
		Button escape = new TextButton("ESC", skin);
		Button quit = new TextButton("Q", skin);
		Button chatButton = new TextButton("C", skin);
		Button techTree = new TextButton("T", skin);
		Button displayHUD = new TextButton("+", skin);
		Button hideHUD = new TextButton("-", skin);
		
		Label escInfo = new Label("Pause game", skin);
		Label quitInfo = new Label("Quit game", skin);
		Label chatInfo = new Label("Open chat window", skin);
		Label techInfo = new Label("Open Technology Tree menu", skin);
		Label hideInfo = new Label("Hide Heads Up Display", skin);
		Label dispInfo = new Label("Show Heads Up Display", skin);

		hotKeysChild.add(escape).size(HOTKEYBUTTON);
		hotKeysChild.add(escInfo).row();
		hotKeysChild.add(chatButton).size(HOTKEYBUTTON);
		hotKeysChild.add(chatInfo).row();
		hotKeysChild.add(quit).size(HOTKEYBUTTON);
		hotKeysChild.add(quitInfo).row();
		hotKeysChild.add(displayHUD).size(HOTKEYBUTTON);
		hotKeysChild.add(dispInfo).row();
		hotKeysChild.add(hideHUD).size(HOTKEYBUTTON);
		hotKeysChild.add(hideInfo).row();
		
		hotKeysParent.add(hotkeysInfo).row();
		hotKeysParent.add(hotKeysChild);
		window.add(hotKeysParent);
		return hotKeysParent;
	}
}