package com.deco2800.marswars.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.FogOfWarManager;
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
        List<BaseEntity> renderables_be = GameManager.get().getWorld().getEntities();

        // Tutor approved workaround to avoid changing whole structure of game
        List<AbstractEntity> renderables = new ArrayList<>();
        for (BaseEntity e : renderables_be) {
            renderables.add(e);
        }

        List<AbstractEntity> entities = new ArrayList<>();

        List<AbstractEntity> walkables = new ArrayList<>();

        // Tutor approved workaround to avoid changing whole structure of game
        List<FogOfWarLayer> fogs_temp = FogWorld.getFogMap();
        List<AbstractEntity> fogs = new ArrayList<>();
        for (FogOfWarLayer e : fogs_temp) {
            fogs.add(e);
        }

        // Tutor approved workaround to avoid changing whole structure of game
        List<FogOfWarLayer> blackFogs_temp = FogWorld.getBlackFogMap();
        List<AbstractEntity> blackFogs = new ArrayList<>();
        for (FogOfWarLayer e : blackFogs_temp) {
            blackFogs.add(e);
        }

        /* Sort entities into walkables and entities */
        for (AbstractEntity r : renderables) {
            if (r.canWalOver()) {
                walkables.add(r);
            } else {
                entities.add(r);
            }
        }

        batch.begin();

        renderEntities(walkables, batch, camera,0);
        renderEntities(entities, batch, camera,0);

        renderEntities(fogs,batch,camera,0);
        renderEntities(blackFogs,batch,camera,0);
        renderEntities(walkables, batch, camera,1);

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



    /**
     * Renders entities given by entities onto the batch and camera
     * @param entities list of entities to be rendered.
     * @param batch the batch that is going to contain all the sprites
     * @param camera the camera being use to display the game.

     */

    private void renderEntities(List<AbstractEntity> entities, SpriteBatch batch, Camera camera,int iteration) {

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

            //fog of war: leave the CheckSelect on top of everything
            if(iteration==1 && !(entity instanceof CheckSelect)) continue;

            //fog of war: if it is turned off, don't render any fog of war tiles
            if(entity instanceof FogOfWarLayer && !FogOfWarManager.getToggleFog()) continue;


            //this function is for the blackFog to omit the tiles that are revealed
            if (entity instanceof BlackTile)
                if (FogOfWarManager.getBlackFog((int) entity.getPosX(), (int) entity.getPosY()) == 1)
                    continue;
            //omit the tiles that are in sight
            if (entity instanceof GrayTile)
                if (FogOfWarManager.getFog((int) entity.getPosX(), (int) entity.getPosY()) == 2) continue;


            //fog of war part of the game: eliminate enemies outside fog of war if fog of war is on
            if (entity instanceof BaseEntity && FogOfWarManager.getToggleFog()) {
                BaseEntity baseEntity = (BaseEntity) entity;
                if (baseEntity.getEntityType() == BaseEntity.EntityType.UNIT) {
                    if (FogOfWarManager.getFog((int) entity.getPosX(), (int) entity.getPosY()) == 0) continue;
                }
            }

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
