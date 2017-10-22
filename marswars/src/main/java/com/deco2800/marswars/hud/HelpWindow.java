package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
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

/**
 * Creates a Help Window instance 
 * @author Naziah Siddique
 * 23/10/17
 *
 */
public class HelpWindow extends Window{
	private static final int SIDEPANEBUTTONWIDTH = 160;
	private static final int SIDEPANEBUTTONHEIGHT = 40;
	private static final int WINDOWPAD = 20;
	private static final int HOTKEYHEIGHT = 40;
	private static final int HOTKEYWIDTH = 80;
	private static final int HOTKEYPAD = 40; 

	private static final boolean ENABLED = false; 

	private Skin skin;			//game skin
	private Table sidePane;		//help side pane buttons
	
	private TimeManager timeManager = (TimeManager)
			GameManager.get().getManager(TimeManager.class);

	/**
	 * Constructor for the help window 
	 */
	public HelpWindow(Stage stage, Skin skin, String string) {
		super("", skin, string);
		this.skin = skin;
		
		this.setHeight(600);
		this.setWidth(600);
		
		this.setDebug(ENABLED);
		this.align(Align.left | Align.top);
		this.pad(WINDOWPAD);
		buildWindow();
		gameInfo();
	}

	/**
	 * Builds the help window
	 */
	private void buildWindow() {
		this.add(this.sidePane()).align(Align.topLeft);
	}

	/**
	 * Returns the help window side pane 
	 * @return help window 
	 */
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
				buildWindow();
				gameInfo();
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
	
	/**
	 * Returns the help window instance 
	 * @return
	 */
	public Window getHelpWindow() {
		return this;
	}
	
	private Table gameInfo() {
		Table gameInfoTable = new Table();
		gameInfoTable.padLeft(20);
		gameInfoTable.align(Align.topLeft);
		Label gameInfo = new Label("Spacwars", this.skin, "menusubheading");
		
		
		Label text = new Label("", this.skin);
		text.setText("A Space based Real Time Strategy game \n"
				+ "made for the DECO2800 Design \n" 
				+ "Computing Studio 2 course at the \n" 
				+ "University of Queensland.\n\n" 
				+ "To learn more about the game\n"
				+ "feaures and rules, click below!");
		text.setWrap(true);
		
		Button gameRules = new TextButton("Read Game Rules", this.skin);
		
		gameRules.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.net.openURI("https://github.com/UQdeco2800/deco2800-2017-spacwars/wiki/SpacWars:-The-Player-Guide");
			}
		});
		
		gameInfoTable.add(gameInfo).align(Align.left).row();
		gameInfoTable.add(text).align(Align.left).row();
		gameInfoTable.add(gameRules);
		this.add(gameInfoTable).expand().align(Align.topLeft);
		return gameInfoTable;
	}
	
	/**
	 * Builds the hotkey table display
	 * @return hotkey map out table
	 */
	private Table hotKeysInfo() {
		Table hotKeysParent = new Table();
		hotKeysParent.padLeft(20);
		hotKeysParent.align(Align.topLeft);

		Table hotKeysChild = new Table();
		Label hotkeysInfo = new Label("Key Shortcuts", skin, "menusubheading");
		hotKeysChild.align(Align.left);
		
		Button escape = new TextButton("ESC", skin);
		Button quit = new TextButton("Q", skin);
		Button chatButton = new TextButton("C", skin);
		Button closeChatButton = new TextButton("ctrl-c", skin);
		Button techTree = new TextButton("T", skin);
		Button zoomIn = new TextButton("+", skin);
		Button zoomOut = new TextButton("-", skin);
		Button toggleHelp = new TextButton("H", skin);
		Button toggleHUD = new TextButton("E", skin);
		Button camLeft = new TextButton("A", skin);
		Button camRight = new TextButton("D", skin);
		Button camDown = new TextButton("S", skin);
		Button camUp = new TextButton("W", skin);

		Label escInfo = new Label("Toggle pause menu", skin);
		Label quitInfo = new Label("Quit game", skin);
		Label chatInfo = new Label("Open chat window", skin);
		Label chatCloseInfo = new Label("Close chat window", skin);
		Label zoomInInfo = new Label("Zoom in map", skin);
		Label zoomOutInfo = new Label("Zoom in map", skin);
		Label techInfo = new Label("Open Technology Tree menu", skin);
		Label helpInfo = new Label("Toggle help menu", skin);
		Label toggleHUDInfo = new Label("Toggle Heads Up Display", skin);
		Label camDownInfo = new Label("Shift view down", skin);
		Label camLeftInfo = new Label("Shift view left", skin);
		Label camUpInfo = new Label("Shift view up", skin);
		Label camRightInfo = new Label("Shift view right", skin);

		hotKeysChild.add(escape).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(escInfo).align(Align.left).align(Align.left).row();
		hotKeysChild.add(toggleHelp).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(helpInfo).align(Align.left).row();
		hotKeysChild.add(chatButton).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(chatInfo).align(Align.left).row();
		hotKeysChild.add(closeChatButton).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(chatCloseInfo).align(Align.left).row();
		hotKeysChild.add(techTree).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(techInfo).align(Align.left).row();
		hotKeysChild.add(quit).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(quitInfo).align(Align.left).row();
		hotKeysChild.add(toggleHUD).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(toggleHUDInfo).align(Align.left).row();
		hotKeysChild.add(camLeft).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(camLeftInfo).align(Align.left).row();
		hotKeysChild.add(camRight).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(camRightInfo).align(Align.left).row();
		hotKeysChild.add(camUp).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(camUpInfo).align(Align.left).row();
		hotKeysChild.add(camDown).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(camDownInfo).align(Align.left).row();
		hotKeysChild.add(zoomIn).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(zoomInInfo).align(Align.left).row();
		hotKeysChild.add(zoomOut).size(HOTKEYWIDTH, HOTKEYHEIGHT).padRight(HOTKEYPAD);
		hotKeysChild.add(zoomOutInfo).align(Align.left).row();

		hotKeysParent.add(hotkeysInfo).align(Align.left).row();
		hotKeysParent.add(hotKeysChild);
		this.add(hotKeysParent);
		return hotKeysParent;
	}
}