package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TimeManager;

public class HelpWindow extends Window{
	private static final int SIDEPANEBUTTONWIDTH = 160;
	private static final int SIDEPANEBUTTONHEIGHT = 40;
	private static final int WINDOWPAD = 20;
	private static final int HOTKEYBUTTON = 40;

	private static final boolean ENABLED = true; 

	private Stage stage;
	private Window window;
	private Skin skin;
	private Table sidePane;
	private HUDView hud;
	
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	public HelpWindow(Stage stage, Skin skin) {
		super("", skin);
		this.stage = stage;
		this.skin = skin;
		
		this.setSize(600, 600);
		this.setDebug(ENABLED);
		this.align(Align.left | Align.top);
		this.pad(WINDOWPAD);
		buildWindow();
	}

	/**
	 * Builds the help window
	 */
	private void buildWindow() {
		this.add(this.sidePane()).align(Align.topLeft);
	}

	private Table sidePane() {
		sidePane = new Table();
		sidePane.setDebug(ENABLED);
		sidePane.align(Align.topLeft);
		Button gameGuide = new TextButton("GAME GUIDE", skin);
		Button hotKeys = new TextButton("HOTKEYS", skin);
		Button settings = new TextButton("SETTINGS", skin);
		Button back = new TextButton("BACK TO GAME", skin);

		gameGuide.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				clear();
			}
		});
		
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				timeManager.unPause();
				setVisible(false);
			}
		});
		
		hotKeys.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				clear();
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
		return this;
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
		hotKeysChild.add(techTree).size(HOTKEYBUTTON);
		hotKeysChild.add(techInfo).row();
		hotKeysChild.add(quit).size(HOTKEYBUTTON);
		hotKeysChild.add(quitInfo).row();
		hotKeysChild.add(displayHUD).size(HOTKEYBUTTON);
		hotKeysChild.add(dispInfo).row();
		hotKeysChild.add(hideHUD).size(HOTKEYBUTTON);
		hotKeysChild.add(hideInfo).row();
		
		hotKeysParent.add(hotkeysInfo).row();
		hotKeysParent.add(hotKeysChild);
		this.add(hotKeysParent);
		return hotKeysParent;
	}
}