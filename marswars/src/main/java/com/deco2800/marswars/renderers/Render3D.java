package com.deco2800.marswars.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.FogOfWarLayer;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.FogWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple isometric renderer for DECO2800 games
 * @author Tim Hadwen
 */
public class Render3D implements Renderer {
    
    float autoRenderValue = 0.55f;

    /* Enables debugging text to show the entity rendering order */
    private static final boolean DEBUG = false;

    BitmapFont font;

    private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

    /**
     * Renders onto a batch, given a renderables with entities
     * It is expected that BaseWorld contains some entities and a Map to read tiles from
     * @param batch Batch to render onto
     */
    @Override
    public void render(SpriteBatch batch, Camera camera) {
        List<BaseEntity> renderables = GameManager.get().getWorld().getEntities();

        List<BaseEntity> entities = new ArrayList<>();
        List<BaseEntity> walkables = new ArrayList<>();

        List<FogOfWarLayer> fogs = FogWorld.getFogMap();

        /* Sort entities into walkables and entities */
        for (BaseEntity r : renderables) {
            if (r.canWalOver()) {
                walkables.add(r);
            } else {
                entities.add(r);
            }
        }

        batch.begin();

        renderEntities(walkables, batch, camera);
        renderEntities(entities, batch, camera);
        renderFog(fogs,batch,camera);
        batch.end();

    }

    /**
     * Returns the correct tile renderer for the given rendering engine
     * @param batch The current sprite batch
     * @return A TiledMapRenderer for the current engine
     */
    @Override
    public BatchTiledMapRenderer getTileRenderer(SpriteBatch batch) {
        return new IsometricTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
    }

    /**
     * It looks similiar to renderEntities but it will be modified later to fulfill the fog's functionality
     * Renders fog-of-war tiles onto the batch and camera
     * @param entities list of entities to be rendered.
     * @param batch the batch that is going to contain all the sprites
     * @param camera the camera being use to display the game.

     */


    public void renderFog(List<FogOfWarLayer>entities, SpriteBatch batch, Camera camera){
        Collections.sort(entities);
        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(0.25f);
        }
        int worldLength = GameManager.get().getWorld().getLength();
        int worldWidth = GameManager.get().getWorld().getWidth();

        int tileWidth = (int)GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int)GameManager.get().getWorld().getMap().getProperties().get("tileheight");

        float baseX = tileWidth*(worldWidth/2.0f - 0.5f);

        float baseY = -tileHeight/2*worldLength + tileHeight/2f;

        /* Render each entity (backwards) in order to retain objects at the front */
        for (int index = 0; index < entities.size(); index++) {
            Renderable entity = entities.get(index);

            String textureString = entity.getTexture();
            TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(textureString);

            float cartX = entity.getPosX();
            float cartY = (worldWidth-1) - entity.getPosY();

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;

            // We want to keep the aspect ratio of the image so...
            float aspect = (float)(tex.getWidth())/(float)(tileWidth);

            Vector3 pos = camera.position;
            OrthographicCamera cam = (OrthographicCamera) camera;

            if (isoX < pos.x + camera.viewportWidth*cam.zoom*autoRenderValue && isoX > pos.x - camera.viewportWidth*cam.zoom*autoRenderValue
                    && isoY < pos.y + camera.viewportHeight*cam.zoom*autoRenderValue && isoY > pos.y - camera.viewportHeight*cam.zoom*autoRenderValue) {
                batch.draw(tex, isoX, isoY, tileWidth * entity.getXRenderLength(),
                        (tex.getHeight() / aspect) * entity.getYRenderLength());
            }
        }
    }


    /**
     * Renders entities given by entities onto the batch and camera
     * @param entities list of entities to be rendered.
     * @param batch the batch that is going to contain all the sprites
     * @param camera the camera being use to display the game.

     */

    private void renderEntities(List<BaseEntity> entities, SpriteBatch batch, Camera camera) {
        Collections.sort(entities);
        if (font == null) {
            font = new BitmapFont();
            font.getData().setScale(0.25f);
        }
        int worldLength = GameManager.get().getWorld().getLength();
        int worldWidth = GameManager.get().getWorld().getWidth();

        int tileWidth = (int)GameManager.get().getWorld().getMap().getProperties().get("tilewidth");
        int tileHeight = (int)GameManager.get().getWorld().getMap().getProperties().get("tileheight");

        float baseX = tileWidth*(worldWidth/2.0f - 0.5f);

        float baseY = -tileHeight/2*worldLength + tileHeight/2f;

        /* Render each entity (backwards) in order to retain objects at the front */
        for (int index = 0; index < entities.size(); index++) {
            Renderable entity = entities.get(index);

            String textureString = entity.getTexture();
            TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(textureString);

            float cartX = entity.getPosX();
            float cartY = (worldWidth-1) - entity.getPosY();

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight;

            // We want to keep the aspect ratio of the image so...
            float aspect = (float)(tex.getWidth())/(float)(tileWidth);

            Vector3 pos = camera.position;
            OrthographicCamera cam = (OrthographicCamera) camera;

            if (isoX < pos.x + camera.viewportWidth*cam.zoom*autoRenderValue && isoX > pos.x - camera.viewportWidth*cam.zoom*autoRenderValue
                    && isoY < pos.y + camera.viewportHeight*cam.zoom*autoRenderValue && isoY > pos.y - camera.viewportHeight*cam.zoom*autoRenderValue) {
                batch.draw(tex, isoX, isoY, tileWidth * entity.getXRenderLength(),
                        (tex.getHeight() / aspect) * entity.getYRenderLength());
            }
        }
    }
}
