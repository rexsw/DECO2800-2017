package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class MiniMap {

    private Image backgroundImage;
    private int width;
    private int height;
    private List<Entity> entitiesOnMap;

    public MiniMap(String mapId, int width, int height) {
        // TODO select appropriate background image based on mapPath
        // for now:
        TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
        backgroundImage = new Image(reg.getTexture(mapId));
        this.width = width;
        this.height = height;
        entitiesOnMap = new ArrayList<Entity>();
    }
    
    public void updateMap() {
    	TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
    	backgroundImage = new Image(reg.getTexture("minimap"));
    }

    public Image getBackground() {
        return backgroundImage;
    }
    
	public void render(HUDView view) {
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		PixmapIO.writePNG(Gdx.files.local("resources/HUDAssets/minimap.png"), pixmap);
		pixmap.dispose();
	}

    public void addFriendlyEntity(int x, int y) {
        TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
        reg.getTexture("friendly_unit");

    }

    public void removeEntity() {

    }

    /**
     * Determine if a click was on the minimap and move the map to that position if it was, otherwise do nothing
     * @param x x-coordinate of the click
     * @param y y-coordinate of the click
     * @return true if the click was on the minimap, false otherwise.
     */
    public boolean clickedOn(int x, int y) {
        if (x < width && y > Gdx.graphics.getHeight() - height) {
            moveMap(x, Gdx.graphics.getHeight() - y);
            return true;
        }
        return false;
    }

    /**
     * Moves the map to the location clicked on on the minimap
     * @param x 0 <= x < this.width
     * @param y 0 <= y < this.height
     */
    private void moveMap(int x, int y) {
        // What portion of the minimap screen was clicked
        float fractionWidthClick = (float) x / width;
        float fractionHeightClick = (float) y / height;

        // Width and height of the game world
        int mapWidth = GameManager.get().getWorld().getWidth()*58;
        int mapLength = GameManager.get().getWorld().getLength()*36;

        OrthographicCamera camera = GameManager.get().getCamera();

        camera.position.x = fractionWidthClick * mapWidth;
        camera.position.y = (fractionHeightClick * mapLength) - (mapLength / 2);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

class Entity {
    public int team; // 0, player's team, 1 allied, 2 enemy
    public int x; // x coordinate of the entity in pixels: 0 <= x < width
    public int y; // y coordinate of the entity in pixels: window height - height <= y < window height

}