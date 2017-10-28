package com.deco2800.marswars.worlds.map.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.terrainelements.*;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.initiategame.GameSave;
import com.deco2800.marswars.worlds.CivilizationTypes;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
        mapPath = RandomMapWriter.FILENAME;//getRandomMap();
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
            if (this.length>15) placeCliffs("grey");

            placeTrees("", true);

            this.generateResourcePattern();
            for (int i = 0; i < 2; i++) {
            	//I removed random entities and buildings for now, don't think they make sense in a strategy game
              // this.getRandomBuilding();
              // this.getRandomEntity();
               //this.getRandomResource();
            }
        }
    }


    /**
     * populate the world with cliffs
     * @param colour grey or red cliffs, null for random
     */
    private void placeCliffs(String colour) {
        Random r = new Random();
        for(int i =0; i<this.length/5; i++) {
            try {
                placeCliff(r.nextInt(this.length), r.nextInt(this.width), r.nextFloat() > 0.5, colour);
            } catch (Exception e) {}
        }

    }

    /**
     * place a series of cliff entities to create a cliff
     * @param x start x
     * @param y start y
     * @param direction 0 is up-left, 1 is up-right
     * @param colour colour of the cliff "grey" or "red"
     */
    private void placeCliff(int x, int y, boolean direction, String colour) {
        Random r = new Random();
        int maxCliffLength = 10;
        float linearTerminateFactor = 0.08f; //how much more likely a cliff is to end after each segment
        boolean cont = true;
        for (int i = 0; i < maxCliffLength && cont; i++) {
            cont = r.nextFloat()>(linearTerminateFactor*i);
            if (x<0||x>this.length||y<0||y>this.width) {
                cont = false;
            } else {
                Obstacle cliff;
                cliff = new Obstacle(x, y, 0, 2, 2,
                        direction?ObstacleType.CLIFF_R:ObstacleType.CLIFF_L, colour, true);
                world.addEntity(cliff);
            }
            x += direction?1:0;
            y += direction?0:1;
        }
    }

    /**
     * places the trees on the map
     * @param colour the colour of the tree to place (red, green, blue, yellow)
     * @param randomColour whether to randomly choose a colour for ea tree (& disregard colour parameter)
     */
    private void placeTrees(String colour, boolean randomColour) {
        Random r = new Random();
        float rf;
        ObstacleType type;
        Obstacle tree;
        for (int i = 0; i<this.length*1.5; i++) {
            if (randomColour) {
                rf = r.nextFloat();
                if (rf<0.25) {
                    colour = "red";
                }
                else if (rf<0.5) {
                    colour = "blue";
                }
                else if (rf<0.75) {
                    colour = "green";
                }
                else {
                    colour= "yellow";
                }
            }
            rf = r.nextFloat();
            if (rf<0.33) {
                type = ObstacleType.TREE1;
            }
            else if (rf<0.66) {
                type = ObstacleType.TREE2;
            }
            else {
                type = ObstacleType.TREE3;
            }
            tree = new Obstacle(r.nextInt(this.length), r.nextInt(this.width), 0, 4, 4,
                    type, colour, false);
            if(r.nextInt(200) < 20) {
            	int x = r.nextInt(101);
            	AmbientAnimal animal;
            	if(x == 42) {
            		animal = new Corn(r.nextInt(this.length), r.nextInt(this.width), 0, 0);
            	} else if (x > 42) {
            		animal = new Dino(r.nextInt(this.length), r.nextInt(this.width), 0, 0);
            	} else {
            		animal = new Snail(r.nextInt(this.length), r.nextInt(this.width), 0, 0);
            	}
            	world.addEntity(animal);
            }
            world.addEntity(tree);
        }
        Dinoking king = new Dinoking(r.nextInt(this.length), r.nextInt(this.width), 0, 0);
        world.addEntity(king);
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
     */
    public void setEntity(EntityID entityType, float x, float y, float z){
        BaseEntity entity = null;
        switch (entityType){

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
     * Creates random pattern of resources. The chance of the game being stuck in an infinite loop is inversely proportional
     * to the size of the map, use sensibly large map sizes!
     */
    protected void generateResourcePattern(){
        ResourceType resource = ResourceType.ROCK;
        for (int i =0; i<8; i++){
            generateResourcePatternFor(resource);
            switch (resource) {
                case ROCK:
                    resource = ResourceType.BIOMASS;
                    break;
                case BIOMASS:
                    resource = ResourceType.CRYSTAL;
                    break;
                case CRYSTAL:
                    resource = ResourceType.ROCK;
                    break;
            }
        }
    }

    /**
     * generates the resource pattern for a single resource
     * @param resource the resource to generate
     */
    protected void generateResourcePatternFor(ResourceType resource) {
        Random r  = new Random();
        double length;
        double direction;
        double xOrigin = this.length/2d;
        double yOrigin = this.width/2d;
        //maximum radial length
        double maxLength = Math.floor(Math.sqrt(Math.pow(this.width/2d,2)+Math.pow(this.width/2d,2)));
        int divisions = 4; //how many distinct circular divisions are used in the radial distribution
        int frequency = (int)Math.sqrt(this.width/2d+this.length/2d)/4; //how many resource groups there are
        for (int i = 0; i < divisions; i++) {
            //maximum and mininum rangle for angles in this divisions
            double radMin = ((Math.PI*2)/divisions)*i;
            for (int ii=0; ii<frequency; ii++) {
                direction = r.nextFloat()*(Math.PI*2/divisions) + radMin;
                //make sure resources aren't bunched up in the middle!
                length = maxLength*(1-Math.pow(r.nextFloat(),4));
                double xOff = length*Math.cos(direction);
                double yOff = length*Math.sin(direction);
                double x = xOrigin+xOff;
                double y = yOrigin+yOff;
                if ((int)x<=0 || (int)x>=this.length || (int)y<=0 || (int)y>=this.width) {
                    //try place it again if it lands outside the map
                    ii--;
                    continue;
                }
                //if the clump failed to generate, try again
                if (!placeClumpAt(resource, 5, 4, (int)x, (int)y)) {
                    ii--;
                }
            }
        }
    }

    /**
     * places a 'clump' of resources of a given size and type around a given coordinate. Complicated algorithm ensures
     * shapes of resources conform to certain constraints, including parameters. if the clump cannot be generated, the
     * function will return false.
     *
     * @param resource resource to place in clump
     * @param clumpSize amount of resource per clump
     * @param maxWidth maximum width of a clump (to stop long veins)
     * @param x x position of resource clump
     * @param y y position of resource clump
     *
     * @return returns false if a clump could not be generated at this position
     */
    protected boolean placeClumpAt(ResourceType resource, int clumpSize, int maxWidth, int x, int y){
        Random r = new Random();
        int maxTries = 90; //maximum amount of times to try generate the clump. Will fail after this
        int tries=0; //how many attempts have been made at generating this clump
        float chanceReset = 0.25f; //the chance that the clump generator will reset to xy after passing chanceResetDist
        int chanceResetDistance = (int) (maxWidth*0.5);
        int newX = x;
        int newY = y;
        int i;
        List toAdd = new ArrayList<>();
        //generate clumpsize resources
        for (i = 0; (i < clumpSize)&&(tries<maxTries); i++) {
            //make sure we dont go out of bounds of our clump OR the map
            if (newX>x+maxWidth||newX<x-maxWidth||newX<0||newX>=this.length||newY<0||newY>=this.width) {
                newX = x;
                newY = y;
            }
            //check our position agianst our chanceResetDistance
            if (Math.abs(newX-x)>=chanceResetDistance||Math.abs(newY-y)>=chanceResetDistance) {
                if (r.nextFloat()>chanceReset) {
                    //we're out of range of the reset distance  AND we rolled a reset
                    newX = x;
                    newY = y;
                }
            }
            if (checkForEntity(newX, newY)){
                //put the good stuff down
                toAdd.add(new Resource(newX, newY, 0, 1f, 1f, resource));
                //world.addEntity(new Resource(newX, newY, 0, 1f, 1f, resource));
            }
            else {//if the entity couldnt be placed, decrement i and retry
                i--;
            }
            //randomly move the next tile to be generated in a random direction
            newX += r.nextInt(3)-1;
            newY += r.nextInt(3)-1;
            //increment how many attempts have been made
            tries+=1;
        }
//        System.out.printf("generated "+i+" resources taking "+tries+"tries\n");
        if (tries!=maxTries) {
            for(i=0; i<toAdd.size(); i++) {
                world.addEntity((BaseEntity) toAdd.get(i));
            }
            return true;
        }
        return false;
    }

    /**
     * Creates a random entity.
     */
    protected void getRandomEntity(){
        EntityID random = EntityID.SPACMAN;
        //please add consturtors for the other enities to this methoad before useing their IDs
        LOGGER.info("chosen entity type: " + random);
        BaseEntity entity = null;
        int x = r.nextInt(width-1);
        int y = r.nextInt(length-1);
        if(!checkForEntity(x, y)){
            return;
        }
        switch (random){
            case SPACMAN:
                entity = new Astronaut(x, y, 0, -1);
            default:
                LOGGER.error("Unhandled Case, Entity not supported");
        }

        world.addEntity(entity);
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
     * this will reload resource from gamesave
     */
    public void loadResourceEntities(GameSave loadedGame){
        for(Resource each : loadedGame.data.getResource()){
            world.addEntity(each);
        }
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
        else{
            return;
        }
        world.addEntity(newEntity);
    }

    /**
     * Chooses a random map (.tmx file) of a random size
     * @return the new map file path.
     */
    protected String getRandomMap(){
        MapSizeTypes randomSize = MapSizeTypes.values()[r.nextInt(MapSizeTypes.values().length)];
        MapTypes randomType = MapTypes.values()[r.nextInt(MapTypes.values().length)];
        LOGGER.info("chosen map type: " + randomType + " map size: " + randomSize);

        //add the tiles we want the map to contain - IN ORDER
        List tilesToAdd= new ArrayList<Integer>();
        int size;
        switch (randomSize){
            case TINY:
                size = 40;
                mapSizeType = MapSizeTypes.TINY;
                break;
            case SMALL:
                size = 60;

                mapSizeType = MapSizeTypes.SMALL;
                break;
            case MEDIUM:
                size = 100;
                mapSizeType = MapSizeTypes.MEDIUM;
                break;
            case LARGE:
                size = 150;
                mapSizeType = MapSizeTypes.LARGE;
                break;
            case VERY_LARGE:
                size = 250;
                mapSizeType = MapSizeTypes.VERY_LARGE;
                break;
            default:
                size=100;
        }
        switch (randomType){
            case MARS:
                tilesToAdd.add(new Integer(11));
                tilesToAdd.add(new Integer(12));
                tilesToAdd.add(new Integer(13));
                tilesToAdd.add(new Integer(14));
                tilesToAdd.add(new Integer(15));
                tilesToAdd.add(new Integer(16));
                tilesToAdd.add(new Integer(40));
                tilesToAdd.add(new Integer(41));
                tilesToAdd.add(new Integer(42));
                mapType = MapTypes.MARS;
                break;
            case MOON:
                tilesToAdd.add(new Integer(31));
                tilesToAdd.add(new Integer(32));
                tilesToAdd.add(new Integer(44));
                tilesToAdd.add(new Integer(35));
                tilesToAdd.add(new Integer(36));
                tilesToAdd.add(new Integer(37));
                mapType = MapTypes.MOON;
                break;
            case SUN:
                //UPDATE THESE TILES
                tilesToAdd.add(new Integer(33));
                tilesToAdd.add(new Integer(46));
                tilesToAdd.add(new Integer(38));
                tilesToAdd.add(new Integer(39));
                tilesToAdd.add(new Integer(34));
                mapType = MapTypes.SUN;
                break;
            default:
                LOGGER.error("Unknown Map type");
        }
        RandomMapWriter randomTiles = new RandomMapWriter(100, 100, tilesToAdd, new NoiseMap(size,size, 18));
        try{
            randomTiles.writeMap();
        }catch(Exception e){
            //oh no what do we do? (file IO error)
            //we should probably throw some error and crash the game if this fails :[] ?
        }
        return randomTiles.FILENAME;
    }

    /**
     * Chooses a fixed map (.tmx file) of a fixed size
     *
     * @return the new map file path.
     */
    protected String getFixedMap(MapTypes type, MapSizeTypes size){
        //add the tiles we want the map to contain - IN ORDER
        List tilesToAdd= new ArrayList<Integer>();
        int mapSize;
        switch (size){
            case TINY:
                mapSize = 40;
                mapSizeType = MapSizeTypes.TINY;
                break;
            case SMALL:
                mapSize = 60;
                mapSizeType = MapSizeTypes.SMALL;
                break;
            case MEDIUM:
                mapSize = 100;
                mapSizeType = MapSizeTypes.MEDIUM;
                break;
            case LARGE:
                mapSize = 150;
                mapSizeType = MapSizeTypes.LARGE;
                break;
            case VERY_LARGE:
                mapSize = 250;
                mapSizeType = MapSizeTypes.VERY_LARGE;
                break;
            default:
                mapSize = 100;
        }
        switch (type){
            case MARS:
                tilesToAdd.add(new Integer(11));
                tilesToAdd.add(new Integer(12));
                tilesToAdd.add(new Integer(13));
                tilesToAdd.add(new Integer(14));
                tilesToAdd.add(new Integer(15));
                tilesToAdd.add(new Integer(16));
                tilesToAdd.add(new Integer(40));
                tilesToAdd.add(new Integer(41));
                tilesToAdd.add(new Integer(42));
                mapType = MapTypes.MARS;
                break;
            case MOON:
                tilesToAdd.add(new Integer(31));
                tilesToAdd.add(new Integer(32));
                tilesToAdd.add(new Integer(44));
                tilesToAdd.add(new Integer(35));
                tilesToAdd.add(new Integer(36));
                tilesToAdd.add(new Integer(37));
                mapType = MapTypes.MOON;
                break;
            case SUN:
                //UPDATE THESE TILES
                tilesToAdd.add(new Integer(33));
                tilesToAdd.add(new Integer(46));
                tilesToAdd.add(new Integer(38));
                tilesToAdd.add(new Integer(39));
                tilesToAdd.add(new Integer(34));
                mapType = MapTypes.SUN;
                break;
            default:
                LOGGER.error("Unknown Map type");
        }
        RandomMapWriter randomTiles = new RandomMapWriter(mapSize, mapSize, tilesToAdd, new NoiseMap(mapSize ,mapSize,
                8+mapSize/10));
        try{
            randomTiles.writeMap();
        }catch(Exception e){
            //oh no what do we do? (file IO error)
            //we should probably throw some error and crash the game if this fails :[ ?
        }
        return randomTiles.FILENAME;
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
