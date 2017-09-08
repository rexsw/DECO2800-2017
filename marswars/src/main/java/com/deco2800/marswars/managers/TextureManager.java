package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.units.Soldier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    	//Select Building zone
    	textureMap.put("greenSelect", new Texture("resources/buildSelect/greenSelect.png"));
    	textureMap.put("redSelect", new Texture("resources/buildSelect/redSelect.png"));
        this.saveTexture("selected", "resources/placeholderassets/selected.png");
        this.saveTexture("selected_black", "resources/placeholderassets/selected_black.png");
        //Buildings
        textureMap.put("homeBase1", new Texture("resources/BuildingAssets/homeBase1.png"));
        textureMap.put("homeBase2", new Texture("resources/BuildingAssets/FinalDraft_Homebase5.png"));
    	textureMap.put("homeBase", new Texture("resources/BuildingAssets/FinalDraft_Homebase4.png"));
        textureMap.put("barracks",new Texture("resources/BuildingAssets/Draft_Barracks1.png"));
        textureMap.put("turret",new Texture("resources/BuildingAssets/turret.png"));
        textureMap.put("bunker",new Texture("resources/BuildingAssets/bunker.png"));
        //Environment
        textureMap.put("grass", new Texture("resources/placeholderassets/grass.png"));
        textureMap.put("grass2", new Texture("resources/placeholderassets/grass2.png"));
        textureMap.put("tree", new Texture("resources/placeholderassets/spacman.png"));
        textureMap.put("real_tree", new Texture("resources/placeholderassets/tree.png"));
        textureMap.put("ground_1", new Texture("resources/placeholderassets/ground-1.png"));
        textureMap.put("transparent_tile",new Texture("resources/placeholderassets/transparent_tile.png"));
        textureMap.put("black_tile",new Texture("resources/placeholderassets/black_tile.png"));
        //Units

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
        this.saveTexture("base", "resources/placeholderassets/base.png");
        this.saveTexture("base2", "resources/placeholderassets/base2.png");
        this.saveTexture("memetank", "resources/placeholderassets/memetank.png");
        textureMap.put("tree1", new Texture("resources/placeholderassets/tree1.png"));
        textureMap.put("tree1sele", new Texture("resources/placeholderassets/tree1sele.png"));
        this.saveTexture("tree_selected", "resources/placeholderassets/tree_selected.png");
        this.saveTexture("rock", "resources/placeholderassets/ground-1.png");
        this.saveTexture("tree", "resources/placeholderassets/tree.png");
        this.saveTexture("base", "resources/placeholderassets/base.png");
        this.saveTexture("spacman_yellow", "resources/placeholderassets/spacman_yellow.png");
        this.saveTexture("spacman", "resources/placeholderassets/spacman.png");
        this.saveTexture("spacman_red", "resources/placeholderassets/spacman_red.png");
        this.saveTexture("spacman_blue", "resources/placeholderassets/spacman_blue.png");
        this.saveTexture("spacman_green", "resources/placeholderassets/spacman_green.png");
        this.saveTexture("spacman_ded", "resources/placeholderassets/spacman_ded.png");
        this.saveTexture("spatman_blue", "resources/placeholderassets/spatman_blue.png");
        //everything above here must be replaced
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
        this.saveTexture("large_tree", "resources/resourceAssets/tree1sele.png");
        this.saveTexture("medium_tree", "resources/resourceAssets/treem.png");
        this.saveTexture("small_tree", "resources/resourceAssets/treesl.png");
        this.saveTexture("large_mine", "resources/resourceAssets/minel.png");
        this.saveTexture("medium_mine", "resources/resourceAssets/minem.png");
        this.saveTexture("small_mine", "resources/resourceAssets/mines.png");       
        this.saveTexture("large_flower", "resources/resourceAssets/flowerl.png");
        this.saveTexture("medium_flower", "resources/resourceAssets/flowerm.png");
        this.saveTexture("small_flower", "resources/resourceAssets/flowers.png");   
        this.saveTexture("chat_button", "resources/HUDAssets/chatbutton.png");
        this.saveTexture("help_button", "resources/HUDAssets/helpbutton.png");
        this.saveTexture("quit_button", "resources/HUDAssets/quitbutton.png");
        this.saveTexture("plus_button", "resources/HUDAssets/plusbutton.png");
        this.saveTexture("minus_button", "resources/HUDAssets/minusbutton.png");
        this.saveTexture("arrow_button", "resources/HUDAssets/arrowbutton.png");
        this.saveTexture("tech_button", "resources/HUDAssets/techtreebutton.png");
        this.saveTexture("map", "resources/HUDAssets/map.png");
        this.saveTexture("friendly_unit", "resources/HUDAssets/friendlyMinimapUnit.png");
        
        //----------Unit Assets:
        //Soldier:
        this.saveTexture("bullet", "resources/UnitAssets/Neutral/bullet_1.png");
        this.saveTexture("soldier", "resources/UnitAssets/Neutral/Soldier_1.png");
        this.saveTexture("soldierSelected", "resources/UnitAssets/Neutral/Soldier_2.png");
        //Tank:
        
        this.saveTexture("missile", "resources/UnitAssets/Neutral//Missile_3.png");
        this.saveTexture("tank", "resources/UnitAssets/Neutral/Tank_1.png");
        this.saveTexture("tankSelected", "resources/UnitAssets/Neutral/Tank_2.png");
        
        
        //Backgrounds:
        this.saveTexture("dawn_Bg", "resources/Backgrounds/dawn_Bg.png");
        this.saveTexture("day_Bg", "resources/Backgrounds/day_Bg.png");
        this.saveTexture("dusk_Bg", "resources/Backgrounds/dusk_Bg.png");
        this.saveTexture("night_Bg1", "resources/Backgrounds/night_Bg1.png");
        this.saveTexture("night_Bg2", "resources/Backgrounds/night_Bg2.png");
    }
    /*
     *
     */
    public String loadUnitSprite(AbstractEntity unit, String textureType){//use soldier as the base class
        if(textureType == null){
            textureType = "default";
        }
        //currently only implemented for entities that extend Soldier (i.e. all 
        //player controllable units)
        if(unit instanceof Soldier){
            Soldier soldier = (Soldier) unit;
            String path;
            //Determine the unit type
            String unitType = unit.getClass().toString();
            //filter out class name qualifier, this may be changed later to extend it 
            // to other entity types
            Scanner sc = new Scanner (unitType);
            sc.useDelimiter("units.").next();
            unitType=sc.next();
            sc.close();
            //find the team colour of the owner:
            String teamColour = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(soldier.getOwner());
            path = String.format("resources/UnitAssets/%s/%s/%s.png",
                    unitType,teamColour,textureType);
			//try to load the texture into the textureMap
            LOGGER.info(String.format("Loading texture %s for %s from %s", 
            		textureType, unitType, path));
            String retVal = textureType + teamColour + unitType;
            saveTexture(retVal,path);
            return retVal;
        } else {
        	return null;
        }
        
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
        try{
            textureMap.put(id, new Texture(filename));
        }
        catch(Exception e){
            LOGGER.error(String.format("Failed to load texture %s from %s", id,filename));
            throw e;//we don't want to mask the fact that a texture failed to load
        }
    }
}
