package com.deco2800.marswars.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Render2D;
import com.deco2800.marswars.renderers.Renderer;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

/**
 * Creates the map view of the entire game world
 */
public class MapWorld extends BaseWorld {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapWorld.class);
    private Renderer renderer = new Render2D();
    private OrthographicCamera camera = new OrthographicCamera();

    public MapWorld() {
        constructMap();
        render();
    }

    /**
     * constructs a map
     */
    private void constructMap() {
        // TODO link the two maps being loaded somehow
        this.map = new TmxMapLoader().load("resources/placeholderassets/mega200.tmx");
        //LOGGER.info("Length: " + world.getLength() + ", Width: " + world.getWidth());
    }

    private void render() {
        BaseWorld gameWorld = GameManager.get().getWorld(); // save the old world
        GameManager.get().setWorld(this);

        SpriteBatch batch = new SpriteBatch();

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        BatchTiledMapRenderer tileRenderer = renderer.getTileRenderer(batch);
        tileRenderer.setView(camera);
        tileRenderer.render();

        renderer.render(batch, camera);
        handleClick();
    }

    private void handleClick() {

    }

}
