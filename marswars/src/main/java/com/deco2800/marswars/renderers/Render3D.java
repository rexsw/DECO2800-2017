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
import com.deco2800.marswars.entities.units.MissileEntity;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.mainmenu.MainMenu;
import com.deco2800.marswars.managers.*;
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

    private static int battleFlag = 0;

    private static final Logger LOGGER = LoggerFactory.getLogger(Render3D.class);

    /**
     * Renders onto a batch, given a renderables with entities
     * It is expected that BaseWorld contains some entities and a Map to read tiles from
     * @param batch Batch to render onto
     */
    @Override
    public void render(SpriteBatch batch, Camera camera) {

        List<BaseEntity> renderablesBe = GameManager.get().getWorld().getEntities();
        // Tutor approved workaround to avoid changing whole structure of game
        List<AbstractEntity> renderables = new ArrayList<>();
        for (BaseEntity e : renderablesBe) {
            if (e != null) {
                if (e instanceof Soldier && ((Soldier) e).getHealth() > 0) {
                    e.getHealthBar();
                    renderables.add(e);
                } else if (!(e instanceof Soldier)) {
                    renderables.add(e);
                }
            }
        }


        List<AbstractEntity> entities = new ArrayList<>();

        List<AbstractEntity> walkables = new ArrayList<>();

        List<AbstractEntity> hpBars = new ArrayList<>();

        List<AbstractEntity> fogs = FogWorld.getFogMap();

        List<AbstractEntity> blackFogs = FogWorld.getBlackFogMap();

        List<AbstractEntity> multiSelection = FogWorld.getMultiSelectionTile();

        FogManager fogManager = (FogManager) GameManager.get().getManager(FogManager.class);


        /* Sort entities into walkables and entities */
        for (AbstractEntity r : renderables) {
            int x = (int) Math.floor(r.getPosX());
            int y = (int) Math.floor(r.getPosY());
            if (FogManager.getBlackFog(Math.round(r.getPosX()), Math.round(r.getPosY())) != 0 ||!FogManager.getToggleFog()) {
                if (r.canWalOver()) {
                    walkables.add(r);
                } else if (r instanceof HealthBar) {
                    hpBars.add(r);
                } else {
                    entities.add(r);
                }
            }
        }

        batch.begin();

        //render multiselection
        renderEntities(multiSelection,batch,camera,0);

        //render the entities
        renderEntities(walkables, batch, camera,0);
        renderEntities(entities, batch, camera,0);
        renderEntities(hpBars, batch, camera,0);

        if (FogManager.getToggleFog()) {
            renderEntities(fogs, batch, camera, 0);
            renderEntities(blackFogs, batch, camera, 0);
        }

        //rerender the clickSelection on top of everything
        renderEntities(walkables, batch, camera,1);

        WeatherManager weather = (WeatherManager)
                GameManager.get().getManager(WeatherManager.class);
        if (weather.isRaining()) {
            weather.render(batch);
        }

        batch.end();
        weather.renderOverlay();

        if(battleFlag==1)
            MainMenu.player.playBattleSoundTrack();
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
     * @param iteration identifies the order of rendering

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

            if(entity instanceof MissileEntity && !MainMenu.player.battleTheme.isPlaying()) {
                setBattleFlag(1);
            }

            //multi selection entities
            if(entity instanceof MultiSelectionTile){
                if(MultiSelection.getSelectedTiles((int) entity.getPosX(), (int) entity.getPosY())==0)
                continue;
            }

            //carrier unit: if the entity is loaded, don't render him
            if(entity instanceof Soldier && ((Soldier)entity).getLoadStatus()==1)
        	continue;

            if (entity instanceof BlackTile)
                if (FogManager.getBlackFog((int) entity.getPosX(), (int) entity.getPosY()) == 1)
                    continue;
            //omit the tiles that are in sight
            if (entity instanceof GrayTile)
                if (FogManager.getFog((int) entity.getPosX(), (int) entity.getPosY()) == 2)
                continue;


            //fog of war part of the game: eliminate enemies outside fog of war if fog of war is on
            if (entity instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) entity;
                if (baseEntity.getEntityType() == BaseEntity.EntityType.UNIT || baseEntity.getEntityType() == BaseEntity.EntityType.HERO) {
//                    if (FogManager.getFog((int) entity.getPosX(), (int) entity.getPosY()) == 0)
//                        continue;
                }
            }

            String textureString = entity.getTexture();
            TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(textureString);

            float cartX = entity.getPosX()-entity.getXoff();
            float cartY = (worldWidth-1) - (entity.getPosY()-entity.getYoff());

            float isoX = baseX + ((cartX - cartY) / 2.0f * tileWidth);
            float isoY = baseY + ((cartX + cartY) / 2.0f) * tileHeight + tileHeight*entity.getPosZ();

            // We want to keep the aspect ratio of the image so...
            float aspect = (float)(tex.getWidth())/(float)(tileWidth);

            Vector3 pos = camera.position;
            OrthographicCamera cam = (OrthographicCamera) camera;

            if (isoX < pos.x + camera.viewportWidth*cam.zoom*autoRenderValue && isoX > pos.x - camera.viewportWidth*cam.zoom*autoRenderValue
                    && isoY < pos.y + camera.viewportHeight*cam.zoom*autoRenderValue && isoY > pos.y - camera.viewportHeight*cam.zoom*autoRenderValue) {
                //this will make the fog of war and multiselection looks better without lines between each tile
                if(entity instanceof BlackTile || entity instanceof GrayTile || entity instanceof MultiSelectionTile){
                    batch.draw(tex, isoX, isoY, tileWidth * entity.getXRenderLength()*1.05f,
                            (tex.getHeight() / aspect) * entity.getYRenderLength()*1.05f);
                }
                batch.draw(tex, isoX, isoY, tileWidth * entity.getXRenderLength(),
                        (tex.getHeight() / aspect) * entity.getYRenderLength());
            }
        }
    }

    /**
     * Set the new balttle flag
     *
     * @param battleFlag the new battle flag to use
     */
    public static void setBattleFlag(int battleFlag) {
        Render3D.battleFlag = battleFlag;
    }

    /**
     * Get the battle flag
     *
     * @return the battle flag
     */
    public static int getBattleFlag() {
        return battleFlag;
    }
}
