package com.deco2800.marswars.worlds.map.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.TerrainElement;
import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.entities.units.UnitTypes;
import com.deco2800.marswars.managers.AbstractPlayerManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.CivilizationTypes;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Random;


/**
 * Creates a container of objects for a map.
 * This class has tools to generate or set game elements into a container. This container
 * should be later loaded into a CustomizedWorld.
 */
public class MapContainer {

    // logger of the class
    private static final Logger LOGGER = LoggerFactory.getLogger(MapContainer.class);
   
    // path of the .tmx map file
    private String mapPath = "";
    
    // width of the map loaded
    private int width;
    
    // height of the map loaded
    private int length;
    
    // randomizer
    private Random r = new Random();
    
    // the world that will hold the content of the container
    protected CustomizedWorld world;

    /**
     * Creates a new Map container from a given map.
     *
     * @param mapPath .tmx file to be loaded
     */
    public MapContainer(String mapPath){
        this.mapPath = mapPath;
    }

    /**
     * Creates a Map container from a random map with random elements.
     */
    public MapContainer(){
        mapPath = RandomMapWriter.FILENAME;//getRandomMap();

        //add the tiles we want the map to contain - IN ORDER
        List tilesToAdd= new ArrayList<Integer>();
        tilesToAdd.add(new Integer(11));
        tilesToAdd.add(new Integer(16));
        tilesToAdd.add(new Integer(16));
        tilesToAdd.add(new Integer(16));
        tilesToAdd.add(new Integer(12));
        tilesToAdd.add(new Integer(12));
        tilesToAdd.add(new Integer(18));
        RandomMapWriter randomTiles = new RandomMapWriter(100, 100, tilesToAdd, new NoiseMap(100,100,4));
        randomTiles.addTile(1,1,10);
        try{
            randomTiles.writeMap();
        }catch(Exception e){
            //oh shit what the fuck do we do? (file IO error)
            //we should probably throw some error and crash the game?
        }
        TiledMap mockMap = new TmxMapLoader().load(randomTiles.FILENAME);
        width = mockMap.getProperties().get("width", Integer.class);
        length = mockMap.getProperties().get("height", Integer.class);
        LOGGER.info("Random Map: " + mapPath + " width: " + width + " length: " + length);
    }

    /**
     * Checks if an x y position on the grid is empty
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if empty
     */
    public boolean checkForEntity(int x, int y){
        if(world.getCollisionMap().get(x, y).isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Generates entities for world
     */
    public void generateEntities(boolean random){
        if(random) {
            this.generateResourcePattern();
            for (int i = 0; i < 2; i++) {
                this.getRandomBuilding();
            }
        }
    }

    /**
     * @return the map .tmx been used
     */
    public String getMap(){
        LOGGER.info("Map: " + mapPath);
        return this.mapPath;
    }


    /**
     * Passed world to map container so entities can be accessed
     */
    public void passWorld(CustomizedWorld world){
        this.world = world;
        this.length = world.getLength();
        this.width = world.getWidth();
    }

    /**
     * Places the terrain element on the map if possible.
     *
     * @param terrainElement the terrain element to be placed.
     */
    public void setTerrainElement(TerrainElement terrainElement){
        //Waiting on terrain (Not yet implemented)
    }

    /**
     * Places the terrain element on the map in a random position.
     *
     * @param terrainElement the element to be placed.
     * @param random whether the position of the terrain element should be random.
     */
    public void setTerrainElement(TerrainElement terrainElement, boolean random){
        //Waiting on terrain (Not yet implemented)
    }

    /**
     * Places a single building on the map.
     *
     * @param building the building to be placed.
     */
    public void setStructure(BuildingEntity building){
        //Waiting on building (Not yet implemented)
    }

    /**
     * Places a group buildings on the map.
     *
     * @param buildings the buildings to be placed.
     */
    public void setStructure(BuildingEntity[][] buildings){
        //Waiting on building (Not yet implemented)
    }

    /**
     * Places a single building on the map in a random position.
     *
     * @param building the building to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(BuildingEntity building, boolean random){
        //Waiting on building (Not yet implemented)
    }

    /**
     * Places a group buildings on the map in a random position.
     *
     * @param buildings the buildings to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(BuildingEntity[][] buildings, boolean random){
        //Waiting on building (Not yet implemented)
    }

    /**
     * Places a set of elements that make up a civilization
     *
     * @param civilizationContainer the container with all the civilization elements.
     */
    public void setCivilization(CivilizationContainer civilizationContainer){
        //Waiting on civilization (Not yet implemented)
    }

    /**
     * Places an entity on the map.
     *
     * @param entity the entity to be placed.
     */
    public void setEntity(BaseEntity entity){
        this.world.addEntity(entity);
    }

    /**
     * Places a set of entities on the map
     *
     * @param entities the set of entities to be placed.
     */
    public void setEntities(BaseEntity[] entities){
        for (BaseEntity e: entities) {
            this.world.addEntity(e);
        }
    }

    /**
     * Places an entity on the map in a random location.
     *
     * @param entity the entity to be placed.
     */
    public void setRandomEntity(BaseEntity entity){
        //Can't implement as entity requires x/y before being passed if random
    }

    /**
     * Place an entity on the map in a fixed location.
     *
     * @param entityType the type of the new entity to be placed
     * @param x its x coordinate
     * @param y its y coordinate
     * @param z its z coordinate
     * @param player the owner
     */
    public void setEntity(UnitTypes entityType, float x, float y, float z, AbstractPlayerManager player){
        BaseEntity entity = null;
        switch (entityType){
            case ASTRONAUT:
                entity = new Astronaut(x,y,z, player);
                break;
            case TANK:
                entity = new Tank(x,y,z, player);
                break;
            case SPACMAN:
                return;
            case SOLDIER:
                entity = new Soldier(x,y,z, player);
                break;
            default:
                LOGGER.error("Unhandled Case, Entity not supported");
        }
        this.world.addEntity(entity);
    }

    /**
     * Places a set of entities on the map in a random position.
     *
     * @param entities the set of entities to be placed.
     */
    public void setRandomEntities(BaseEntity[] entities){
        //Can't implement as entity requires x/y before being passed if random
    }


    /**
     * Creates a civilization container from a type of civilization.
     *
     * @param civilizationTypes the civilization type to be used.
     * @return the new CivilizationContainer.
     */
    public CivilizationContainer createCivilization(CivilizationTypes civilizationTypes){
        return new CivilizationContainer(this, civilizationTypes);
        // this method has not been implemented yet
    }

    /**
     * Creates a civilization container from a random type of civilization.
     *
     * @return the new CivilizationContainer.
     */
    public CivilizationContainer createRandomCivilization(){
        return new CivilizationContainer(this);
        // this method has not been implemented yet
    }

    /**
     * Creates a random TerrainElement.
     *
     * @return the new TerrainElement.
     */
    protected TerrainElement getRandomTerrainElement(){
        TerrainElementTypes random = TerrainElementTypes.values()[r.nextInt(TerrainElementTypes.values().length)];
        LOGGER.info("chosen terrain type: " + random);
        return new TerrainElement();
    }

    /**
     * Creates a random building object.
     *
     */
    protected void getRandomBuilding(){
        BuildingType random = BuildingType.values()[r.nextInt(BuildingType.values().length)];
        LOGGER.info("chosen building type: " + random);
        BuildingEntity newBuilding;
        int x = r.nextInt(width-3);
        int y = r.nextInt(length-3);
        if(!checkForEntity(x, y)){
            return;
        }
        if(random == BuildingType.BASE){
            newBuilding = new Base(world, x,y,0);
        } else if(random == BuildingType.TURRET){
            newBuilding = new Turret(world, x,y,0);
        } else if(random == BuildingType.BUNKER){
            newBuilding = new Bunker(world, x,y,0);
        } else if(random == BuildingType.BARRACKS){
            newBuilding = new Barracks(world, x,y,0);
        }
        else {
            return;
        }
        world.addEntity(newBuilding);
    }


    /**
     * Creates a random group of same type of buildings.
     *
     * @return the new group of buildings.
     */
    protected void getRandomStructure(){
        BuildingType random = BuildingType.values()[r.nextInt(BuildingType.values().length)];
        LOGGER.info("chosen building type: " + random);
        return;
    }

    /**
     * Creates random pattern of resources
     */
    protected void generateResourcePattern(){
        int xLength = this.length;
        int yWidth = this.width;
        int featureSize = 5;
        int scale = 2;
        if (xLength * yWidth > 110){
            featureSize = 14;
            scale = 5;
        }
        NoiseMap noise = new NoiseMap(xLength, yWidth, featureSize);
        for (int ix=0; ix<this.length; ix++){
            for (int iy=0; iy<this.width; iy++){
                double n = noise.getNoiseAt(ix,iy);
                if (n>0.4 && r.nextInt(10) > scale && checkForEntity(ix, iy)){
                        this.getRandomResource(ix, iy);
                }
            }
        }
    }

    /**
     * Creates a random entity.
     */
    protected void getRandomEntity(){
        UnitTypes random = UnitTypes.values()[r.nextInt(UnitTypes.values().length)];
        LOGGER.info("chosen entity type: " + random);
        BaseEntity entity = null;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        PlayerManager playerManager = new PlayerManager();
        playerManager.setColour("blue");
        GameManager.get().addManager(playerManager);
        switch (random){
            case ASTRONAUT:
                entity = new Astronaut(x,y,0, playerManager);
                break;
            case TANK:
                entity = new Tank(x,y,0, playerManager);
                break;
            case SPACMAN:
                return;
            case SOLDIER:
                entity = new Soldier(x,y,0, playerManager);
                break;
            default:
                LOGGER.error("Unhandled Case, Entity not supported");
        }

        world.addEntity(entity);
        System.out.println(random + " " + entity.toString());
    }

    /**
     * Creates a random resource in a given position.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    protected void getRandomResource(int x, int y){
        ResourceType random = ResourceType.values()[r.nextInt(ResourceType.values().length)];
        LOGGER.info("chosen resource type: " + random);
        if(!checkForEntity(x, y)){
            return;
        }
        setRandomResource(random, x, y);
    }

    /**
     * Creates a random resource in a random position
     */
    protected void getRandomResource(){
        ResourceType random = ResourceType.values()[r.nextInt(ResourceType.values().length)];
        LOGGER.info("chosen resource type: " + random);
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        setRandomResource(random, x, y);
    }

    /**
     * Sets a random resource in specific x y
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setRandomResource(ResourceType random, int x, int y){
        BaseEntity newEntity;
        if(random == ResourceType.BIOMASS){
            newEntity = new Resource(x, y, 0, 1f, 1f, ResourceType.BIOMASS);
        }
        else if(random == ResourceType.CRYSTAL){
            newEntity = new Resource(x, y, 0, 1f, 1f, ResourceType.CRYSTAL);
        }
        else if(random == ResourceType.ROCK){
            newEntity = new Resource(x, y, 0, 1f, 1f, ResourceType.ROCK);
        }
        else if(random == ResourceType.WATER){
            newEntity = new Resource(x, y, 0, 1f, 1f, ResourceType.WATER);
        }
        else{
            return;
        }
        world.addEntity(newEntity);
    }

    /**
     * Chooses a random map (.tmx file) of a random size
     *
     * @return the new map file path.
     */
    protected String getRandomMap(){
        MapSizeTypes randomSize = MapSizeTypes.values()[r.nextInt(MapSizeTypes.values().length)];
        MapTypes randomType = MapTypes.values()[r.nextInt(MapTypes.values().length)];
        LOGGER.info("chosen map type: " + randomType + " map size: " + randomSize);
        String newPath = "resources/mapAssets/";
        switch (randomSize){
            case TINY:
                newPath+="tiny";
                break;
            case SMALL:
                newPath+="small";
                break;
            case MEDIUM:
                newPath+="medium";
                break;
            case LARGE:
                newPath+="large";
                break;
            case VERY_LARGE:
                newPath+="veryLarge";
                break;
            default:
                LOGGER.error("Unknown Map Size type");
        }
        switch (randomType){
            case MARS:
                newPath+="Mars.tmx";
                break;
            case MOON:
                newPath+="Moon.tmx";
                break;
            case SUN:
                newPath+="Sun.tmx";
                break;
            default:
                LOGGER.error("Unknown Map type");
        }
        return newPath;
    }
}
