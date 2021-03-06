package com.deco2800.marswars.renderers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.List;

/**
 * Render2D is a 2D renderer for side scrolling games.
 * It impliments the Renderer interface in order to talk to LibGDX
 * @author Tim Hadwen
 */
public class Render2D implements Renderer {

    OrthogonalTiledMapRenderer tiledMapRenderer;

    /**
     * A multiplier to convert from 3D worlds into 2D worlds.
     * (x,y) position * COORDINATEMULTIPLIER = Screen position
     */
    private int COORDINATEMULTIPLIER = 64;

    /**
     * Simply renders things onto the viewport in 2D
     * @param batch Batch to render onto
     */
    @Override
    public void render(SpriteBatch batch, Camera camera) {
        List<BaseEntity> renderables = GameManager.get().getWorld().getEntities();

        batch.begin();

        for (Renderable e : renderables) {
            TextureManager reg = (TextureManager) GameManager.get().getManager(TextureManager.class);
            Texture tex = reg.getTexture(e.getTexture());
            batch.draw(tex, e.getPosY()*COORDINATEMULTIPLIER, e.getPosZ()*COORDINATEMULTIPLIER,
                    e.getXRenderLength()*COORDINATEMULTIPLIER, e.getYRenderLength()*COORDINATEMULTIPLIER);
        }

        batch.end();
    }

    /**
     * Returns the correct tile renderer for this rendering engine
     * @param batch The current sprite batch
     * @return TiledMapRenderer for the 2D engine
     */
    @Override
    public BatchTiledMapRenderer getTileRenderer(SpriteBatch batch) {
        return new OrthogonalTiledMapRenderer(GameManager.get().getWorld().getMap(), 1, batch);
    }
}