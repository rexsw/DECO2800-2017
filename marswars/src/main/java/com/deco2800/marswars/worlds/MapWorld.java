package com.deco2800.marswars.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Render2D;
import com.deco2800.marswars.renderers.Render3D;
import com.deco2800.marswars.renderers.Renderer;
import com.deco2800.marswars.util.Array2D;
import com.sun.org.apache.regexp.internal.RE;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Creates the map view of the entire game world
 */
public class MapWorld extends BaseWorld {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapWorld.class);
    private OrthographicCamera camera = new OrthographicCamera();
    //private int state; // 1 is active 0 is inactive
    //private BaseWorld oldWorld; // the old game world
    //private Array2D<List<BaseEntity>> oldCollisionMap; // the old game entities


    public MapWorld(String filename) {
        this.map = new TmxMapLoader().load(filename);
    }

    public void toggle() {
        if (GameManager.get().getActiveView() == 0) {
            toggleOn();
        } else {
            toggleOff();
        }
        GameManager.get().toggleActiveView();
    }

    private void toggleOn() {
        LOGGER.info("toggle on");
        GameManager.get().setMapWorld((BaseWorld) GameManager.get().getWorld());
        GameManager.get().setWorld(this);
    }

    private void toggleOff() {
        GameManager.get().setWorld(GameManager.get().getMapWorld());
        GameManager.get().setMapWorld(this);
        LOGGER.info("toggle off");
    }

}
