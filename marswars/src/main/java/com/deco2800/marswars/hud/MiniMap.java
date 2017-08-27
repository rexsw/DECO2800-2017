package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;


/**
 * Holds the minimap information, and provides useful methods
 * for manipulating the map.
 */
public class MiniMap {

    private Image backgroundImage; //the image of the map
    private int width; //the width of the minimap in pixels
    private int height; //the height of the minimap in pixels

    /**
	 * Creates a new minimap instance 
	 * @param mapId the name of the image file within the texture manager
	 * @param width the width of the minimap
	 * @param height the height of the minimap
	 */
    public MiniMap(String mapId, int width, int height) {
        TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
        backgroundImage = new Image(reg.getTexture(mapId));
        this.width = width;
        this.height = height;
    }
    
    /**
	 * refreshes the minimap.png file in memory
	 */
    public void updateMap(TextureManager reg) {
    	reg.saveTexture("minimap", "resources/HUDAssets/minimap.png");
    	backgroundImage = new Image(reg.getTexture("minimap"));
    }

    /**
     * @return the minimap image
	 */
    public Image getBackground() {
        return backgroundImage;
    }
    
    /**
     * Takes a clear screenshot of the game screen, for use as a minimap
     */
	public void render() {
		//get the current screen state from the frame buffer
		byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
		Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
		BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
		//save the buffer into a png
		PixmapIO.writePNG(Gdx.files.local("resources/HUDAssets/minimap.png"), pixmap);
		//clear the pixmap from memory
		pixmap.dispose();
	}

	/**
	 * Work in progress method for hotswapping entity textures within the minimap
	 * @param x
	 * the entity x location
	 * @param y
	 * the entity y location
	 */
    public void addFriendlyEntity(int x, int y) {
        TextureManager reg = (TextureManager)(GameManager.get().getManager(TextureManager.class));
        reg.getTexture("friendly_unit");

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