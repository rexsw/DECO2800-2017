package com.deco2800.marswars.worlds.map.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.TerrainElement;
import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import com.deco2800.marswars.entities.buildings.Building;
import com.deco2800.marswars.entities.buildings.BuildingTypes;
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
    // width of the map loaded
    private int width;
    // height of the map loaded
    private int length;
    // randomizer
    private Random r = new Random();
    // the world that will hold the content of the container
    private CustomizedWorld world;
    /**
     * Creates a new Map container from a given map with random elements.
     *
     * @param width the width of the map.
     * @param length the length of the map.
     */
    public MapContainer(String mapPath, int width, int length){
        this.width = width;
        this.length = length;
        this.mapPath = mapPath;
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
            for (int i = 0; i < 4; i++) {
                this.getRandomBuilding();
                this.getRandomEntity();
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
        //Waiting on terrain
    }

    /**
     * Places the terrain element on the map in a random position.
     *
     * @param terrainElement the element to be placed.
     * @param random whether the position of the terrain element should be random.
     */
    public void setTerrainElement(TerrainElement terrainElement, boolean random){
        //Waiting on terrain
    }

    /**
     * Places a single building on the map.
     *
     * @param building the building to be placed.
     */
    public void setStructure(Building building){
        //Waiting on building
    }

    /**
     * Places a group buildings on the map.
     *
     * @param buildings the buildings to be placed.
     */
    public void setStructure(Building[][] buildings){
        //Waiting on building
    }

    /**
     * Places a single building on the map in a random position.
     *
     * @param building the building to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(Building building, boolean random){
        //Waiting on building
    }

    /**
     * Places a group buildings on the map in a random position.
     *
     * @param buildings the buildings to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(Building[][] buildings, boolean random){
        //Waiting on building
    }

    /**
     * Places a set of elements that make up a civilization
     *
     * @param civilizationContainer the container with all the civilization elements.
     */
    public void setCivilization(CivilizationContainer civilizationContainer){
        //Waiting on civilization
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
        // this method has not been implemented yet!!!!!!!!!!!
    }

    /**
     * Creates a civilization container from a random type of civilization.
     *
     * @return the new CivilizationContainer.
     */
    public CivilizationContainer createRandomCivilization(){
        return new CivilizationContainer(this);
        // this method has not been implemented yet!!!!!!!!!!!
    }

    /**
     * Creates a random TerrainElement.
     *
     * @return the new TerrainElement.
     */
    private TerrainElement getRandomTerrainElement(){
        TerrainElementTypes random = TerrainElementTypes.values()[r.nextInt(TerrainElementTypes.values().length)];
        return null;
    }

    /**
     * Creates a random building object.
     *
     */
    private void getRandomBuilding(){
        BuildingTypes random = BuildingTypes.values()[r.nextInt(BuildingTypes.values().length)];
        LOGGER.info("chosen building type: " + random);
        BaseEntity newBuilding;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        if(random == BuildingTypes.BASE){
            newBuilding = new Base(world, x,y,0);
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
    public Building[][] getRandomStructure(){
        BuildingTypes random = BuildingTypes.values()[r.nextInt(BuildingTypes.values().length)];
        LOGGER.info("chosen building type: " + random);
        return null;
    }

    /**
     * Creates random pattern of resources
     */
    private void generateResourcePattern(){
        int xLength = this.length;
        int yWidth = this.width;
        NoiseMap noise = new NoiseMap(xLength, yWidth, 14);
        for (int ix=0; ix<this.length; ix++){
            for (int iy=0; iy<this.width; iy++){
                double n = noise.getNoiseAt(ix,iy);
                if (n>0.4){
                    if(r.nextInt(10) > 5 && checkForEntity(ix, iy)){
                        this.getRandomResource(ix, iy);
                    }
                }
            }
        }
    }

    /**
     * Creates a random entity.
     */
    private void getRandomEntity(){
        EntityTypes random = EntityTypes.values()[r.nextInt(EntityTypes.values().length)];
        LOGGER.info("chosen entity type: " + random);
        BaseEntity newEntity;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        if(random == EntityTypes.SPACMAN){
            newEntity = new Spacman(x, y,0);
        }
        else if(random == EntityTypes.ENEMYSPACMAN){
            newEntity = new EnemySpacman(x, y,0);
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
    private void getRandomResource(int x, int y){
        ResourceType random = ResourceType.values()[r.nextInt(ResourceType.values().length)];
        LOGGER.info("chosen resource type: " + random);
        BaseEntity newEntity;
        if(!checkForEntity(x, y)){
            return;
        }
        if(random == ResourceType.BIOMASS){
            newEntity = new Resource(x, y, 0, 1f, 1f, ResourceType.BIOMASS);
        }
        else if(random == ResourceType.CRYSTAL) {
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
     * Creates a random resource in a random position
     */
    private void getRandomResource(){
        ResourceType random = ResourceType.values()[r.nextInt(ResourceType.values().length)];
        LOGGER.info("chosen resource type: " + random);
        BaseEntity newEntity;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
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
    private String getRandomMap(){
        MapSizeTypes randomSize = MapSizeTypes.values()[r.nextInt(MapSizeTypes.values().length)];
        MapTypes randomType = MapTypes.values()[r.nextInt(MapTypes.values().length)];
        LOGGER.info("chosen map type: " + randomType + " map size: " + randomSize);
        String newPath = "";
        String mapDoesntExit = "The given Map type doesn't exits";
        if(randomSize == MapSizeTypes.TINY){
            if(randomType == MapTypes.MARS){
                newPath = "resources/mapAssets/tinyMars.tmx";
            } else if (randomType == MapTypes.MOON){
                newPath = "resources/mapAssets/tinyMoon.tmx";
            } else if (randomType == MapTypes.SUN){
                newPath = "resources/mapAssets/tinySun.tmx";
            } else {
                LOGGER.error(mapDoesntExit);
            }
        }else if(randomSize == MapSizeTypes.SMALL){
            if(randomType == MapTypes.MARS){
                newPath = "resources/mapAssets/smallMars.tmx";
            } else if (randomType == MapTypes.MOON){
                newPath = "resources/mapAssets/smallMoon.tmx";
            } else if (randomType == MapTypes.SUN){
                newPath = "resources/mapAssets/smallSun.tmx";
            } else {
                LOGGER.error(mapDoesntExit);
            }
        }else if(randomSize == MapSizeTypes.MEDIUM){
            if(randomType == MapTypes.MARS){
                newPath = "resources/mapAssets/mediumMars.tmx";
            } else if (randomType == MapTypes.MOON){
                newPath = "resources/mapAssets/mediumMoon.tmx";
            } else if (randomType == MapTypes.SUN){
                newPath = "resources/mapAssets/mediumSun.tmx";
            } else {
                LOGGER.error(mapDoesntExit);
            }
        }else if(randomSize == MapSizeTypes.LARGE){
            if(randomType == MapTypes.MARS){
                newPath = "resources/mapAssets/largeMars.tmx";
            } else if (randomType == MapTypes.MOON){
                newPath = "resources/mapAssets/largeMoon.tmx";
            } else if (randomType == MapTypes.SUN){
                newPath = "resources/mapAssets/largeSun.tmx";
            } else {
                LOGGER.error(mapDoesntExit);
            }
        }else if(randomSize == MapSizeTypes.VERY_LARGE){
            if(randomType == MapTypes.MARS){
                newPath = "resources/mapAssets/veryLargeSun.tmx";
            } else if (randomType == MapTypes.MOON){
                newPath = "resources/mapAssets/veryLargeSun.tmx";
            } else if (randomType == MapTypes.SUN){
                newPath = "resources/mapAssets/veryLargeSun.tmx";
            } else {
                LOGGER.error(mapDoesntExit);
            }
        } else {
            LOGGER.error(mapDoesntExit);
        }
        return newPath;
    }
}
