package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.net.MapSizeAction;
import com.deco2800.marswars.net.MapTypeAction;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

/**
 * A UI element that allows you to select the options for a new multiplayer game and communicate that to the server.
 * 
 * @author James McCall
 *
 */
public class OptionSelectionPanel extends Table {
    // The Net Manager so you can communicate with the server
    private NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
    //Map selection label
    private Label mapLabel;
    // Map selection
    private SelectBox<MapTypes> mapSelect;
    // Size Selection label
    private Label sizeLabel;
    // Size selection
    private SelectBox<MapSizeTypes> sizeSelect;
    
    // The skin for styling elements
    private Skin skin;
    
    // Arrays of map sizes and types, to be displayed in select boxes.
    private final Array<MapTypes> maps = getMaps();
    private final Array<MapSizeTypes> sizesArray = getSizes();
    
    // Strings for messages
    private static final String MAP_SELECT_TEXT = "Select a map to play on:";
    private static final String SIZE_SELECT_TEXT = "Select a map size:";
    
    public OptionSelectionPanel(Skin skin) {
        this.skin = skin;
        // Initiate elements
        mapLabel = new Label(MAP_SELECT_TEXT, this.skin);
        sizeLabel = new Label(SIZE_SELECT_TEXT, this.skin);
        mapSelect = new SelectBox<MapTypes>(this.skin);
        sizeSelect = new SelectBox<MapSizeTypes>(this.skin);
            
        mapSelect.setItems(maps);
        sizeSelect.setItems(sizesArray);
        
        setupMapSelectionHandler();
        setupMapSizeSelectionHandler();
        setupLayout();
        sendGameInfo();
    }
    
    /**
     * Sends the currently selected map and type to the server. Used when initiating a new OptionSelectionPanel
     * to send the default selected options.
     */
    private void sendGameInfo() {
        MapSizeAction sizeAction = new MapSizeAction(sizeSelect.getSelected());
        MapTypeAction typeAction = new MapTypeAction(mapSelect.getSelected());
        netManager.getNetworkClient().sendObject(sizeAction);
        netManager.getNetworkClient().sendObject(typeAction);
    }
    
    /**
     * Set up a handler so that when a new map type is selected that information is communicated to the server.
     */
    private void setupMapSelectionHandler() {
        mapSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               MapTypeAction action = new MapTypeAction(mapSelect.getSelected());
               netManager.getNetworkClient().sendObject(action);
           }
        });
    }

    /**
     * Set up a handler so that when a new map size if selected that information is communicated to the server.
     */
    private void setupMapSizeSelectionHandler() {
        sizeSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               MapSizeAction action = new MapSizeAction(sizeSelect.getSelected());
               netManager.getNetworkClient().sendObject(action);
           }
        });
    }
    
    /**
     * Return an array of the available map types that can be stored within the SelectBox.
     * @return An array of the available map types.
     */
    private Array<MapTypes> getMaps() {
        Array<MapTypes> result = new Array<MapTypes>();
        for (MapTypes map : MapTypes.values()) {
            result.add(map);
        }
        return result;
    }
    
    /**
     * Return an array of the available sizes that can be stored within the SelectBox.
     * @return An array of the available sizes.
     */
    private Array<MapSizeTypes> getSizes() {
        Array<MapSizeTypes> result = new Array<MapSizeTypes>();
        for (MapSizeTypes size : MapSizeTypes.values()) {
            result.add(size);
        }
        return result;
    }
    
    /**
     * Helper method for setting up the layout of the UI element. 
     */
    private void setupLayout() {
        this.add(mapLabel).left();
        this.add(mapSelect);
        this.row().pad(10);
        this.row();
        this.add(sizeLabel).left();
        this.add(sizeSelect);
               
    }
}
