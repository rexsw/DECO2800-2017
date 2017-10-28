package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Holds the minimap information, and provides useful methods
 * for manipulating the map.
 */
public class MiniMap {

    private Image backgroundImage; //the image of the map
    private int width; //the width of the minimap in pixels
    private int height; //the height of the minimap in pixels
    private List<MiniMapEntity> entitiesOnMap; //a list of every entity displayed on the minimap
    public int[][] miniMapDisplay; //every 'pixel' on the minimap and whether it has something on it, 0 is empty. (x,y)
    public Image[][] entitiesOnMiniMap; // grid of the entities on the minimap, (x,y)
    public Window stageReference;

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
        entitiesOnMap = new ArrayList<MiniMapEntity>();

        miniMapDisplay = new int[height][width];
        entitiesOnMiniMap = new Image[height][width];

    }

    /**
     * Constructor for testing only
     */
    public MiniMap() {
        entitiesOnMap = new ArrayList<MiniMapEntity>();
        miniMapDisplay = new int[220][220];
        entitiesOnMiniMap = new Image[220][220];
    }

    /**
     * gets the MiniMapEntity at the minimap coordinates (x,y)
     * @param x
     * @param y
     */
    public MiniMapEntity getEntity(int x, int y) {
        for (int i = 0; i < entitiesOnMap.size(); i++) {
            if (entitiesOnMap.get(i).samePosition(x, y)) {
                return entitiesOnMap.get(i);
            }
        }
        return null;
    }

    /**
     * refreshes the minimap.png file in memory
     */
    public void updateMap(TextureManager reg) {
        reg.saveTexture("minimap", "resources/HUDAssets/minimap_box.png");
        backgroundImage = new Image(reg.getTexture("minimap"));
    }

    /**
     * @return the minimap image
     */
    public Image getBackground() {
        return backgroundImage;
    }

    /**
     * Adds the entity to the MiniMap
     * @param entity the entity to be added to the miniMap
     */
    public void addEntity(BaseEntity entity) {
        Vector2 coordinates = convertCoordinates(entity);
        entitiesOnMap.add(new MiniMapEntity((AttackableEntity) entity, (int) coordinates.x, (int) coordinates.y));
        miniMapDisplay[(int) coordinates.x][(int) coordinates.y] = 1;
    }


    /**
     * remove an entity from the minimap display
     * @param entity the entity to be removed
     */
    public void removeEntity(BaseEntity entity) {
        entitiesOnMap.remove(entity);
        Vector2 coordinates = convertCoordinates(entity);
        if (miniMapDisplay[(int) coordinates.x][(int) coordinates.y] > 0) {
            miniMapDisplay[(int) coordinates.x][(int) coordinates.y] = 0;
        }
        try {
            entitiesOnMiniMap[(int) coordinates.x][(int) coordinates.y].remove();
        } catch (NullPointerException e) {
            //actor was already removed
        }
        catch (IllegalStateException e) {
            //occurs when there is no entity there
        }
        entitiesOnMiniMap[(int) coordinates.x][(int) coordinates.y] = null;
    }

    /**
     * Convert the given entity's game world coordinates to the minimap corrdinates and rotates them to the correct
     * orrientation
     *
     * @param entity
     * @return Vector2 of the minimap coordinates. return.x < width, return.y < height
     */
    private Vector2 convertCoordinates(BaseEntity entity) {
        float posY = entity.getPosY();
        int gameY = GameManager.get().getWorld().getLength();
        float posX = entity.getPosX();
        int gameX = GameManager.get().getWorld().getWidth();
        float newX = (posX / gameX) * width;
        float newY = height - ((posY / gameY) * height);

        newX -= width / 2; // move the origin for the rotation
        newY -= height / 2;

        Vector2 coordinates = new Vector2(newX, newY);
        coordinates.rotate(45);
        coordinates.x *= 0.70710678; // multiply by 1/root(2) to make up for the shorter distance
        coordinates.y *= 0.70710678;
        coordinates.x += (width / 2); // add back the initial offset
        coordinates.y += (height / 2);
        coordinates.x = (int) coordinates.x;
        coordinates.y = (int) coordinates.y;
        return coordinates;
    }

    /**
     * Gets the entities on the map.
     * @return List of MiniMapEntities.
     */
    public List<MiniMapEntity> getEntitiesOnMap() {
        return entitiesOnMap;
    }

    /**
     * Determine if a click was on the minimap and move the map to that position if it was, otherwise do nothing
     * @param x x-coordinate of the click
     * @param y y-coordinate of the click
     * @return true if the click was on the minimap, false otherwise.
     */
    public boolean clickedOn(int x, int y) {
        stageReference.toBack(); // move the minimap to the back, so that the eneties on the minimap can be seen
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