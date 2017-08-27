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

public class MiniMap {

    private Image backgroundImage;
    private int width;
    private int height;

    public MiniMap(String mapId, int width, int height) {
        // TODO select appropriate background image based on mapPath
        // for now:
        TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
        backgroundImage = new Image(reg.getTexture(mapId));
        this.width = width;
        this.height = height;
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

    public void addFriendlyEntity() {

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
        float fractionWidthClick = (float) x / width;
        float fractionHeightClick = (float) y / height;

        float worldWidth = GameManager.get().getWorld().getWidth();
        float worldLength = GameManager.get().getWorld().getLength();

        float fractionWidth = worldWidth * fractionWidthClick;
        float fractionHeight = worldLength * fractionHeightClick;

        //float fractionWidth = Gdx.graphics.getWidth() * fractionWidthClick;
        //float fractionHeight = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight() * fractionHeightClick);

        Vector2 newPosition = new Vector2(fractionWidth, fractionHeight);
        newPosition.rotate(45);

        OrthographicCamera camera = GameManager.get().getCamera();
        camera.lookAt(newPosition.x, newPosition.y, 0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
