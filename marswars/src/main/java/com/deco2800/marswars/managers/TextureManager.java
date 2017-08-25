package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.Texture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Texture manager acts as a cache between the file system and the renderers.
 * This allows all textures to be read into memory at the start of the game saving
 * file reads from being completed during rendering.
 *
 * With this in mind don't load textures you're not going to use.
 * Textures that are not used should probably (at some point) be removed
 * from the list and then read from disk when needed again using some type
 * of reference counting
 * @Author Tim Hadwen
 */
public class TextureManager extends Manager {

    private static final Logger LOGGER = LoggerFactory.getLogger(TextureManager.class);

    /**
     * A HashMap of all textures with string keys
     */
    private Map<String, Texture> textureMap = new HashMap<String, Texture>();

    /**
     * Constructor
     * Currently loads up all the textures but probably shouldn't/doesn't
     * need to.
     */
    public TextureManager() {
        textureMap.put("grass", new Texture("resources/placeholderassets/grass.png"));
        textureMap.put("grass2", new Texture("resources/placeholderassets/grass2.png"));
        textureMap.put("tree", new Texture("resources/placeholderassets/spacman.png"));
        textureMap.put("real_tree", new Texture("resources/placeholderassets/tree.png"));
        textureMap.put("ground_1", new Texture("resources/placeholderassets/ground-1.png"));
        textureMap.put("ground_gray", new Texture("resources/placeholderassets/line-of-sight.png"));
        textureMap.put("spacman", new Texture("resources/placeholderassets/spacman.png"));
        textureMap.put("spacman_red", new Texture("resources/placeholderassets/spacman_red.png"));
        textureMap.put("spacman_blue", new Texture("resources/placeholderassets/spacman_blue.png"));
        textureMap.put("spacman_green", new Texture("resources/placeholderassets/spacman_green.png"));
        textureMap.put("spacman_ded", new Texture("resources/placeholderassets/spacman_ded.png"));
        textureMap.put("selected", new Texture("resources/placeholderassets/selected.png"));
        textureMap.put("selected_black", new Texture("resources/placeholderassets/selected_black.png"));
        textureMap.put("base", new Texture("resources/placeholderassets/base.png"));
        textureMap.put("base2", new Texture("resources/placeholderassets/base2.png"));
        textureMap.put("memetank", new Texture("resources/placeholderassets/memetank.png"));
        this.saveTexture("tree_selected", "resources/placeholderassets/tree_selected.png");
        this.saveTexture("base", "resources/placeholderassets/base.png");
        this.saveTexture("spacman_yellow", "resources/placeholderassets/spacman_yellow.png");
        this.saveTexture("spacman", "resources/placeholderassets/spacman.png");
        this.saveTexture("spacman_red", "resources/placeholderassets/spacman_red.png");
        this.saveTexture("spacman_blue", "resources/placeholderassets/spacman_blue.png");
        this.saveTexture("spacman_green", "resources/placeholderassets/spacman_green.png");
        this.saveTexture("deded_spacman", "resources/placeholderassets/spacman_ded.png");
        this.saveTexture("spatman_blue", "resources/placeholderassets/spatman_blue.png");
        this.saveTexture("small_water", "resources/resourceAssets/water_S.png");
        this.saveTexture("medium_water", "resources/resourceAssets/water_M.png");
        this.saveTexture("large_water", "resources/resourceAssets/water_L.png");
        this.saveTexture("small_rock", "resources/resourceAssets/rock_S.png");
        this.saveTexture("medium_rock", "resources/resourceAssets/rock_M.png");
        this.saveTexture("large_rock", "resources/resourceAssets/rock_L.png");
        this.saveTexture("small_crystal", "resources/resourceAssets/crystal_S.png");
        this.saveTexture("medium_crystal", "resources/resourceAssets/crystal_M.png");
        this.saveTexture("large_crystal", "resources/resourceAssets/crystal_L.png");
        this.saveTexture("small_biomass", "resources/resourceAssets/biomass_S.png");
        this.saveTexture("medium_biomass", "resources/resourceAssets/biomass_M.png");
        this.saveTexture("large_biomass", "resources/resourceAssets/biomass_L.png");
        this.saveTexture("chat_button", "resources/HUDAssets/chatbutton.png");
        this.saveTexture("help_button", "resources/HUDAssets/helpbutton.png");
        this.saveTexture("quit_button", "resources/HUDAssets/quitbutton.png");
        this.saveTexture("arrow_button", "resources/HUDAssets/arrowbutton.png");

        this.saveTexture("map", "resources/HUDassets/map.png");
    }

    /**
     * Gets a texture object for a given string id
     * @param id Texture identifier
     * @return Texture for given id
     */
    public Texture getTexture(String id) {
        if (textureMap.containsKey(id)) {
            return textureMap.get(id);
        } else {
            return textureMap.get("spacman_ded");
        }

    }

    /**
     * Saves a texture with a given id
     * @param id Texture id
     * @param filename Filename within the assets folder
     */
    public void saveTexture(String id, String filename) {
        LOGGER.info("Saving texture" + id + " with Filename " + filename);
        if (!textureMap.containsKey(id)) {
            textureMap.put(id, new Texture(filename));
        }
    }
}
