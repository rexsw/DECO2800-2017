package com.deco2800.marswars.mainMenu;

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
import com.deco2800.marswars.net.RequestGameInfoAction;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;

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
    private final Array<MapTypes> MAPS = getMaps();
    private final Array<MapSizeTypes> SIZES = getSizes();
    
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
            
        mapSelect.setItems(MAPS);
        sizeSelect.setItems(SIZES);
        
        setupMapSelectionHandler();
        setupMapSizeSelectionHandler();
        setupLayout();
        sendGameInfo();
    }
    
    private void setupMapSelectionHandler() {
        mapSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               MapTypeAction action = new MapTypeAction(mapSelect.getSelected());
               netManager.getNetworkClient().sendObject(action);
           }
        });
    }

    private void sendGameInfo() {
        MapSizeAction sizeAction = new MapSizeAction(sizeSelect.getSelected());
        MapTypeAction typeAction = new MapTypeAction(mapSelect.getSelected());
        netManager.getNetworkClient().sendObject(sizeAction);
        netManager.getNetworkClient().sendObject(typeAction);
    }
    private void setupMapSizeSelectionHandler() {
        sizeSelect.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
               MapSizeAction action = new MapSizeAction(sizeSelect.getSelected());
               netManager.getNetworkClient().sendObject(action);
           }
        });
    }
    
    private Array<MapTypes> getMaps() {
        Array<MapTypes> result = new Array<MapTypes>();
        for (MapTypes map : MapTypes.values()) {
            result.add(map);
        }
        return result;
    }
    
    private Array<MapSizeTypes> getSizes() {
        Array<MapSizeTypes> result = new Array<MapSizeTypes>();
        for (MapSizeTypes size : MapSizeTypes.values()) {
            result.add(size);
        }
        return result;
    }
    
    private void setupLayout() {
        this.add(mapLabel).left();
        this.add(mapSelect);
        this.row().pad(10);
        this.row();
        this.add(sizeLabel).left();
        this.add(sizeSelect);
               
    }
}
