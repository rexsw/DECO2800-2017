package com.deco2800.marswars.managers;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.Turret;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.units.AmbientAnimal;
import com.deco2800.marswars.entities.units.Dinoking;
import com.deco2800.marswars.entities.units.Soldier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    
    private String spacmandedString = "spacman_ded";
    private String nighbgString = "resources/Backgrounds/nighbg.png";
    
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
    	//Select indicators
    	textureMap.put("greenSelect1", new Texture("resources/buildSelect/greenSelect1.png"));
    	textureMap.put("redSelect1", new Texture("resources/buildSelect/redSelect1.png"));
    	textureMap.put("greenSelect2", new Texture("resources/buildSelect/greenSelect2.png"));
    	textureMap.put("redSelect2", new Texture("resources/buildSelect/redSelect2.png"));
    	textureMap.put("greenSelect3", new Texture("resources/buildSelect/greenSelect3.png"));
    	textureMap.put("redSelect3", new Texture("resources/buildSelect/redSelect3.png"));
    	textureMap.put("greenSelect4", new Texture("resources/buildSelect/greenSelect4.png"));
    	textureMap.put("redSelect4", new Texture("resources/buildSelect/redSelect4.png"));
    	textureMap.put("greenSelect5", new Texture("resources/buildSelect/greenSelect5.png"));
    	textureMap.put("redSelect5", new Texture("resources/buildSelect/redSelect5.png"));
    	textureMap.put("greenSelect6", new Texture("resources/buildSelect/greenSelect6.png"));
    	textureMap.put("redSelect6", new Texture("resources/buildSelect/redSelect6.png"));
        textureMap.put("tileSelectGreen", new Texture("resources/shopAssets/greenSelect.png"));
        textureMap.put("tileSelectRed", new Texture("resources/shopAssets/redSelect.png"));
        //default stuff
        textureMap.put("base",new Texture("resources/Base/Blue/3.png"));
        textureMap.put("bunker",new Texture("resources/Bunker/Blue/3.png"));
        textureMap.put("wall",new Texture("resources/WallHorizontal/Blue/1.png"));
        textureMap.put("turret",new Texture("resources/Turret/Blue/3.png"));
        textureMap.put("barracks",new Texture("resources/Barracks/Blue/3.png"));
        textureMap.put("missileturret", new Texture("resources/Turret/missile.png"));
        //Load walls
        loadAllBuildingTextures();
	    textureMap.put("mainmenubg", new Texture("resources/MainMenu/final.png"));
	    //Environment
        textureMap.put("grass", new Texture("resources/placeholderassets/grass.png"));
        textureMap.put("grass2", new Texture("resources/placeholderassets/grass2.png"));
        //environmental obstacles
        textureMap.put("tree1_blue", new Texture("resources/EnvironmentalAssets/treestyle1-blue.png"));
        textureMap.put("tree1_green", new Texture("resources/EnvironmentalAssets/treestyle1-green.png"));
        textureMap.put("tree1_yellow", new Texture("resources/EnvironmentalAssets/treestyle1-yellow.png"));
        textureMap.put("tree1_red", new Texture("resources/EnvironmentalAssets/treestyle1-red.png"));
        textureMap.put("tree2_blue", new Texture("resources/EnvironmentalAssets/treestyle3-blue.png"));
        textureMap.put("tree2_green", new Texture("resources/EnvironmentalAssets/treestyle3-green.png"));
        textureMap.put("tree2_yellow", new Texture("resources/EnvironmentalAssets/treestyle3-yellow.png"));
        textureMap.put("tree2_red", new Texture("resources/EnvironmentalAssets/treestyle3-red.png"));
        textureMap.put("tree3_blue", new Texture("resources/EnvironmentalAssets/treestyle4-blue.png"));
        textureMap.put("tree3_green", new Texture("resources/EnvironmentalAssets/treestyle4-green.png"));
        textureMap.put("tree3_yellow", new Texture("resources/EnvironmentalAssets/treestyle4-yellow.png"));
        textureMap.put("tree3_red", new Texture("resources/EnvironmentalAssets/treestyle4-red.png"));

        textureMap.put("cliff_left_red", new Texture("resources/EnvironmentalAssets/CLIFF-bottom-left-MARS.png"));
        textureMap.put("cliff_right_red", new Texture("resources/EnvironmentalAssets/CLIFF-bottom-right-MARS.png"));
        textureMap.put("cliff_right_grey", new Texture("resources/EnvironmentalAssets/CLIFF-bottom-right.png"));
        textureMap.put("cliff_left_grey", new Texture("resources/EnvironmentalAssets/CLIFF-bottom-left.png"));

        textureMap.put("real_tree", new Texture("resources/placeholderassets/tree.png"));
        textureMap.put("ground_1", new Texture("resources/placeholderassets/ground-1.png"));
        textureMap.put("transparent_tile",new Texture("resources/placeholderassets/transparent_tile.png"));
        textureMap.put("black_tile",new Texture("resources/placeholderassets/black_tile.png"));
        //Units
        
        textureMap.put("spacman", new Texture("resources/placeholderassets/spacman.png"));
        textureMap.put("spacman_red", new Texture("resources/placeholderassets/spacman_red.png"));
        textureMap.put("spacman_blue", new Texture("resources/placeholderassets/spacman_blue.png"));
        textureMap.put("spacman_green", new Texture("resources/placeholderassets/spacman_green.png"));
        textureMap.put(spacmandedString, new Texture("resources/placeholderassets/spacman_ded.png"));
        textureMap.put("memetank", new Texture("resources/placeholderassets/memetank.png"));
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
        this.saveTexture(spacmandedString, "resources/placeholderassets/spacman_ded.png");
        this.saveTexture("spatman_blue", "resources/placeholderassets/spatman_blue.png");
        //everything above here must be replaced
        this.saveTexture("small_water", "resources/resourceAssets/water_S.png");
        this.saveTexture("medium_water", "resources/resourceAssets/water_M.png");
        this.saveTexture("large_water", "resources/resourceAssets/water_L.png");
        this.saveTexture("water_HUD", "resources/resourceAssets/water_HUD.png");
        this.saveTexture("small_rock", "resources/resourceAssets/rock_S.png");
        this.saveTexture("medium_rock", "resources/resourceAssets/rock_M.png");
        this.saveTexture("large_rock", "resources/resourceAssets/rock_L.png");
        this.saveTexture("rock_HUD", "resources/resourceAssets/rock_HUD.png");
        this.saveTexture("small_crystal", "resources/resourceAssets/crystal_S.png");
        this.saveTexture("medium_crystal", "resources/resourceAssets/crystal_M.png");
        this.saveTexture("large_crystal", "resources/resourceAssets/crystal_L.png");
        this.saveTexture("crystal_HUD", "resources/resourceAssets/crystal_HUD.png");
        this.saveTexture("small_biomass", "resources/resourceAssets/biomass_S.png");
        this.saveTexture("medium_biomass", "resources/resourceAssets/biomass_M.png");
        this.saveTexture("large_biomass", "resources/resourceAssets/biomass_L.png");
        this.saveTexture("biomass_HUD", "resources/resourceAssets/biomass_HUD.png");
        this.saveTexture("large_tree", "resources/resourceAssets/tree1sele.png");
        this.saveTexture("medium_tree", "resources/resourceAssets/treem.png");
        this.saveTexture("small_tree", "resources/resourceAssets/treesl.png");
        this.saveTexture("large_mine", "resources/resourceAssets/minel.png");
        this.saveTexture("medium_mine", "resources/resourceAssets/minem.png");
        this.saveTexture("small_mine", "resources/resourceAssets/mines.png");       
        this.saveTexture("large_flower", "resources/resourceAssets/flowerl.png");
        this.saveTexture("medium_flower", "resources/resourceAssets/flowerm.png");
        this.saveTexture("small_flower", "resources/resourceAssets/flowers.png");
        
        // ------------HUD Assets
        //Header Buttons:
        this.saveTexture("chat_button", "resources/HUDAssets/chatbutton.png");
        this.saveTexture("help_button", "resources/HUDAssets/helpbutton.png");
        this.saveTexture("quit_button", "resources/HUDAssets/quitbutton.png");
        this.saveTexture("plus_button", "resources/HUDAssets/plusbutton.png");
        this.saveTexture("minus_button", "resources/HUDAssets/minusbutton.png");
        this.saveTexture("arrow_button", "resources/HUDAssets/arrowbutton.png");
        //Footer Buttons:
        this.saveTexture("tech_button", "resources/HUDAssets/techtreebutton.png");
        this.saveTexture("shop_button", "resources/shopAssets/items/shop_button.png");
        this.saveTexture("menu_button", "resources/HUDAssets/menubutton.png");
        //Other Assets
        this.saveTexture("map", "resources/HUDAssets/map.png");
        this.saveTexture("friendly_unit", "resources/HUDAssets/friendlyMinimapUnit.png");
        this.saveTexture("clock", "resources/HUDAssets/clock_label.png");
        this.saveTexture("AI_unit", "resources/HUDAssets/AIMiniMapUnit.png");
        this.saveTexture("actions_window", "resources/HUDAssets/actions_window.png");
        this.saveTexture("actions_window_cropped", "resources/HUDAssets/actions_window_cropped.png");
        this.saveTexture("actions_window_top", "resources/HUDAssets/actions_window_top.png");
        this.saveTexture("stats", "resources/HUDAssets/stats.png");
        this.saveTexture("header", "resources/HUDAssets/header.png");

        //----------- Technology Assets:

        //----------- MainMenu Assets:
        this.saveTexture("menubackground", "resources/Mainmenu/background.png");   
        this.saveTexture("mars_map", "resources/mapAssets/tileset/mars007.png");
        this.saveTexture("moon_map", "resources/mapAssets/tileset/moon007.png");
        this.saveTexture("desert_map", "resources/mapAssets/tileset/moon010.png");
        this.saveTexture("astro_blue", "resources/UnitAssets/astronaut/blue/default.png");
        this.saveTexture("astro_green", "resources/UnitAssets/astronaut/green/default.png");
        this.saveTexture("astro_pink", "resources/UnitAssets/astronaut/pink/default.png");
        this.saveTexture("astro_purple", "resources/UnitAssets/astronaut/purple/default.png");
        this.saveTexture("astro_red", "resources/UnitAssets/astronaut/red/default.png");
        this.saveTexture("astro_yellow", "resources/UnitAssets/astronaut/yellow/default.png");
        this.saveTexture("play_button", "resources/MainMenu/playButton.png");        
        this.saveTexture("back_button", "resources/MainMenu/backButton.png");
        
        //----------Unit Assets:
        //Soldier:
        this.saveTexture("bullet", "resources/UnitAssets/neutral/bullet_1.png");
        //Tank:

        this.saveTexture("missile", "resources/UnitAssets/neutral//missile_3.png");

        //Carrier:

        //Commander:

        //Backgrounds:
        this.saveTexture("dawn_Bg", "resources/Backgrounds/daybg.png");
        this.saveTexture("day_Bg", "resources/Backgrounds/daybg.png");
        this.saveTexture("dusk_Bg", nighbgString);
        this.saveTexture("night_Bg1", nighbgString);
        this.saveTexture("night_Bg2", nighbgString);

        //Tiles:
        this.saveTexture("water_draft", "resources/tileAssets/water_draft.png");
        this.saveTexture("water_final", "resources/tileAssets/water_final80.png");
        this.saveTexture("lava_final", "resources/tileAssets/lava_final.png");
        this.saveTexture("multi_selection", "resources/placeholderassets/multiselection.png");


        // Item icon in shop dialog
        	//Weapons and Armour
        this.saveTexture("helmet_1", "resources/shopAssets/items/helmet_1.png");
        this.saveTexture("gun_1", "resources/shopAssets/items/gun_1.png");
        this.saveTexture("rifle_1", "resources/shopAssets/items/rifle_1.png");
        this.saveTexture("goggle_1", "resources/shopAssets/items/goggle_1.png");
        this.saveTexture("helmet_2", "resources/shopAssets/items/helmet_2.png");
        this.saveTexture("gun_2", "resources/shopAssets/items/gun_2.png");
        this.saveTexture("rifle_2", "resources/shopAssets/items/rifle_2.png");
        this.saveTexture("goggle_2", "resources/shopAssets/items/goggle_2.png");
        this.saveTexture("helmet_3", "resources/shopAssets/items/helmet_3.png");
        this.saveTexture("gun_3", "resources/shopAssets/items/gun_3.png");
        this.saveTexture("rifle_3", "resources/shopAssets/items/rifle_3.png");
        this.saveTexture("goggle_3", "resources/shopAssets/items/goggle_3.png");
        	//specials
        this.saveTexture("floating_boots", "resources/shopAssets/items/floating_boots.png");
        this.saveTexture("air_strike", "resources/shopAssets/items/missile.png");
        this.saveTexture("nuke", "resources/shopAssets/items/nuke.png");
        this.saveTexture("healing_bless", "resources/shopAssets/items/healing_bless.png");
        this.saveTexture("teleboots", "resources/shopAssets/items/teleboots.png");
        this.saveTexture("penetration", "resources/shopAssets/items/penetration.png");
        this.saveTexture("health_shot", "resources/shopAssets/items/health_shot.png");
        this.saveTexture("health_station", "resources/shopAssets/items/health_station.png");
        this.saveTexture("barrier_gloves", "resources/shopAssets/items/barrier_gloves.png");
        this.saveTexture("snipper_shot", "resources/shopAssets/items/snipper_shot.png");
        
        this.saveTexture("military_command", "resources/shopAssets/items/hero_button.png");
        this.saveTexture("hero_button_off", "resources/shopAssets/items/hero_button_off.png");
        
        this.saveTexture("locked_inventory", "resources/shopAssets/items/lock.png");
        
        
        // stats icon
        this.saveTexture("armour_stats", "resources/statsAssets/armor_stats.png");
        this.saveTexture("health_stats", "resources/statsAssets/health_stats.png");
        this.saveTexture("attack_speed_stats", "resources/statsAssets/attack_speed_stats.png");
        this.saveTexture("range_stats", "resources/statsAssets/range_stats.png");
        this.saveTexture("move_speed_stats", "resources/statsAssets/move_speed_stats.png");
        this.saveTexture("attack_stats", "resources/statsAssets/attack_stats.png");

        //Health Bars
        for (int i = 0; i < 21; i++) {
            this.saveTexture("Health"+i , "resources/UnitAssets/healthbar/health"+i+".png");
        }
        
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
            unitType=sc.next().toLowerCase();
            sc.close();
            if (soldier instanceof AmbientAnimal) {
            	if(soldier instanceof Dinoking) {
            		unitType  = "dino";
            	}
            	String mapcolour = GameManager.get().getMapType().toSColour();
            	 path = String.format("resources/UnitAssets/%s/%s/%s.png",
                         unitType,mapcolour,textureType);
            	String retVal = textureType  + unitType + mapcolour;
            	saveTexture(retVal, path);
            	return retVal;
            }
            if (textureType.equals("missile")) {
            	 path = String.format("resources/UnitAssets/%s/%s.png",
                         unitType,textureType);
            	String retVal = textureType + unitType;
            	saveTexture(retVal, path);
            	return retVal;
            }
            //find the team colour of the owner:
            String teamColour = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(soldier.getOwner());
            path = String.format("resources/UnitAssets/%s/%s/%s.png",
                    unitType,teamColour.toLowerCase(),textureType);
			//try to load the texture into the textureMap
            String retVal = textureType + teamColour + unitType;
            if (!textureMap.containsKey(retVal)) {
                LOGGER.info(String.format("Loading texture %s for %s from %s",
                        textureType, unitType, path));
                saveTexture(retVal,path);
            }
            return retVal;
        } else {
        	return null;
        }
		
        
    }
    
    /**
     * This method takes an EntityId and returns the default sprite in the players colour
     * @param unit the unit to get sprite of
     * @param owner  the team id of the team to load the sprite for
     * @return
     */
    public String loadUnitSprite(EntityID unit, int owner){//use soldier as the base class
        String textureType = "default";
        String path;
        //Determine the unit type
        String unitType = unit.name().toLowerCase();
        String teamColour = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(owner);
        path = String.format("resources/UnitAssets/%s/%s/%s.png", unitType, teamColour.toLowerCase(),textureType);

        //try to load the texture into the textureMap
        String retVal = textureType + unitType;
        if (!textureMap.containsKey(retVal)) {
            saveTexture(retVal,path);
        }

        return retVal;
    }
    
    /**
     * This method loads building textures into the game
     * 
     */
    public void loadAllBuildingTextures(){
    	List<String> BuildingEntity =  Arrays.asList("Base", "Wall", "Gate", 
    			"Turret", "Bunker", "Barracks", "HeroFactory", "TechBuilding");
    	for (String b : BuildingEntity) {
    		switch(b) {
    		case "Wall":
    			loadBuildingtextures("WallHorizontal", 1);
    			loadBuildingtextures("WallVertical", 1);
    			break;
    		case "Gate":
    	        textureMap.put("gate1",new Texture("resources/Gate/1.png"));
    	        textureMap.put("gate2",new Texture("resources/Gate/2.png"));
    			break;
    		case "Turret":
    			loadBuildingtextures("Turret", 5);
    			break;
    		case "Base":
    			loadBuildingtextures("Base", 5);
    			break;
    		case "Barracks":
    			loadBuildingtextures("Barracks", 5);
    			break;
    		case "Bunker":
    			loadBuildingtextures("Bunker", 5);
    			break;
    		case "HeroFactory":
    			loadBuildingtextures("HeroFactory", 5);
    			break;
    		case "TechBuilding":
    			loadBuildingtextures("TechBuilding", 5);
    			break;
    		default:
    			break;
    		}
    	}
    }
    
    /**
     * Gets a sprite for building
     * @param building the entity to load sprite for
     * @param numbTex how many sprites to load for building
     */
    public void loadBuildingtextures(String building, int numbTex){
    	List<String> colours;
    	colours =  Arrays.asList("Blue", "Red", "Yellow", "Green", "Purple", "Pink");
    	for (int count = 1; count < (numbTex+1); count++) {
    		for (String colour : colours) {
   			 	String path = String.format("resources/%s/%s/%s.png",
   					 building,colour,count);
   			 	String id = count + colour + building;
   			 	if (!textureMap.containsKey(id)) {
   			 		saveTexture(id, path);
   			 	}
    		}
    	}

    }
    
    /**
     * Gets a sprite for building
     * @param unitType the entity to get sprite for
     * @param textureType which sprite number to load
     * @return String name of sprite
     */
    public String getBuildingSprite(String textureType, String colour, String unitType){
        String retVal = unitType + colour + textureType;
        return retVal;
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
            return textureMap.get(spacmandedString);
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
        	if (textureMap.get(id) == null) {
        		textureMap.put(id, new Texture(filename));
        	}
        }
        catch(Exception e){
            LOGGER.error(String.format("Failed to load texture %s from %s", id,filename));
            throw e;//we don't want to mask the fact that a texture failed to load
        }
    }
}
