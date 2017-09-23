package com.deco2800.marswars.worlds.map.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.entities.TerrainElements.ResourceType;
import com.deco2800.marswars.entities.TerrainElements.TerrainElement;
import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import com.deco2800.marswars.worlds.CivilizationTypes;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // default map type
    private MapTypes mapType = MapTypes.MARS;

    // default map size
    private MapSizeTypes mapSizeType = MapSizeTypes.MEDIUM;
    
    // width of the map loaded
    private int width;
    
    // height of the map loaded
    private int length;
    
    // randomizer
    private Random r = new Random();
    
    // the world that will hold the content of the container
    protected CustomizedWorld world;
    /**
     * Creates a new Map container from a given map with random elements.
     *
     * @param width the width of the map.
     * @param length the length of the map.
     */
    public MapContainer(String mapPath, int width, int length){
        //Not yet implemented
    }

    /**
     * Creates a new Map container from a given map with random elements.
     *
     * @param type the type of the map.
     * @param size the size of the map.
     */
    public MapContainer(MapTypes type, MapSizeTypes size){
        mapPath = getFixedMap(type, size);
        TiledMap mockMap = new TmxMapLoader().load(mapPath);
        width = mockMap.getProperties().get("width", Integer.class);
        length = mockMap.getProperties().get("height", Integer.class);
        LOGGER.info("Random Map: " + mapPath + " width: " + width + " length: " + length);
    }


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
        mapPath = getRandomMap();
        TiledMap mockMap = new TmxMapLoader().load(mapPath);
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
            	//I removed random entities and buildings for now, don't think they make sense in a strategy game
              // this.getRandomBuilding();
              // this.getRandomEntity();
               this.getRandomResource();
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
     * @param random whether its position should be random or not.
     */
    public void setEntity(BaseEntity entity, boolean random){
        //Can't implement as entity requires x/y before being passed if random
    }

    /**
     * Places a set of entities on the map in a random position.
     *
     * @param entities the set of entities to be placed.
     * @param random whether their position should be random or not.
     */
    public void setEntities(BaseEntity[] entities, boolean random){
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
        if(random == BuildingType.BASE && world.checkValidPlace(BuildingType.BASE, x, y, random.getBuildSize(), 0f)){
            newBuilding = new BuildingEntity(x,y,0,BuildingType.BASE, 0);
        } else if(random == BuildingType.TURRET && world.checkValidPlace(BuildingType.TURRET, x, y, random.getBuildSize(), .5f)){
            newBuilding = new BuildingEntity(x,y,0,BuildingType.TURRET, 0);
        } else if(random == BuildingType.BUNKER && world.checkValidPlace(BuildingType.BUNKER, x, y, random.getBuildSize(), .5f)){
            newBuilding = new BuildingEntity(x,y,0,BuildingType.BUNKER, 0);
        } else if(random == BuildingType.BARRACKS && world.checkValidPlace(BuildingType.BARRACKS, x, y, random.getBuildSize(), 0f)){
            newBuilding = new BuildingEntity(x,y,0,BuildingType.BARRACKS, 0);
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
        EntityID random = EntityID.SPACMAN;
        //please add consturtors for the other enities to this methoad before useing their IDs
        LOGGER.info("chosen entity type: " + random);
        BaseEntity newEntity;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        random=EntityID.SPACMAN;
        if(random == EntityID.SPACMAN){
            newEntity = new Spacman(x, y, 0);
            newEntity.setOwner(0);
        }
        else {
            return;
        }

        world.addEntity(newEntity);
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
                mapSizeType = MapSizeTypes.TINY;
                break;
            case SMALL:
                newPath+="small";
                mapSizeType = MapSizeTypes.SMALL;
                break;
            case MEDIUM:
                newPath+="medium";
                mapSizeType = MapSizeTypes.MEDIUM;
                break;
            case LARGE:
                newPath+="large";
                mapSizeType = MapSizeTypes.LARGE;
                break;
            case VERY_LARGE:
                newPath+="veryLarge";
                mapSizeType = MapSizeTypes.VERY_LARGE;
                break;
            default:
                LOGGER.error("Unknown Map Size type");
        }
        switch (randomType){
            case MARS:
                newPath+="Mars.tmx";
                mapType = MapTypes.MARS;
                break;
            case MOON:
                newPath+="Moon.tmx";
                mapType = MapTypes.MOON;
                break;
            case SUN:
                newPath+="Sun.tmx";
                mapType = MapTypes.SUN;
                break;
            default:
                LOGGER.error("Unknown Map type");
        }
        return newPath;
    }

    /**
     * Chooses a fixed map (.tmx file) of a fixed size
     *
     * @return the new map file path.
     */
    protected String getFixedMap(MapTypes type, MapSizeTypes size){
        String newPath = "resources/mapAssets/";
        switch (size){
            case TINY:
                newPath+="tiny";
                mapSizeType = MapSizeTypes.TINY;
                break;
            case SMALL:
                newPath+="small";
                mapSizeType = MapSizeTypes.SMALL;
                break;
            case MEDIUM:
                newPath+="medium";
                mapSizeType = MapSizeTypes.MEDIUM;
                break;
            case LARGE:
                newPath+="large";
                mapSizeType = MapSizeTypes.LARGE;
                break;
            case VERY_LARGE:
                newPath+="veryLarge";
                mapSizeType = MapSizeTypes.VERY_LARGE;
                break;
            default:
                LOGGER.error("Unknown Map Size type");
        }
        switch (type){
            case MARS:
                newPath+="Mars.tmx";
                mapType = MapTypes.MARS;
                break;
            case MOON:
                newPath+="Moon.tmx";
                mapType = MapTypes.MOON;
                break;
            case SUN:
                newPath+="Sun.tmx";
                mapType = MapTypes.SUN;
                break;
            default:
                LOGGER.error("Unknown Map type");
        }
        return newPath;
    }

    /**
     * @return returns the current map type
     */
    public MapTypes getMapType() {
        return mapType;
    }

    /**
     * @return returns the current map size
     */
    public MapSizeTypes getMapSizeType() {
        return mapSizeType;
    }
}
