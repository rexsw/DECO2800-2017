package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.TerrainElement;
import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import com.deco2800.marswars.entities.buildings.Building;
import com.deco2800.marswars.entities.buildings.BuildingTypes;
import com.deco2800.marswars.worlds.CivilizationTypes;
import com.deco2800.marswars.worlds.CustomizedWorld;
import com.deco2800.marswars.worlds.MapSizeTypes;
import java.util.Random;


/**
 * Creates a container of objects for a map.
 * This class has tools to generate or set game elements into a container. This container
 * should be later loaded into a CustomizedWorld.
 */
public class MapContainer {

    // path of the .tmx map file
    private String mapPath = "";
    // width of the map loaded
    private int width;
    // height of the map loaded
    private int length;
    // randomizer
    private Random r = new Random();
    //
    private CustomizedWorld world;
    /**
     * Creates a new Map container from a given map with random elements.
     *
     * @param width the width of the map.
     * @param length the length of the map.
     * @param random elements in the container.
     */
    public MapContainer(String mapPath, int width, int length, boolean random){
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
    }

    /**
     * Generates entities for world
     */
    public void generateEntities(){
        for (int i = 0; i < 20; i ++){
            this.getRandomBuilding();
            this.getRandomEntity();
            this.getRandomResource();
        }

    }

    /**
     * @return the map .tmx been used
     */
    public String getMap(){
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

    }

    /**
     * Places the terrain element on the map in a random position.
     *
     * @param terrainElement the element to be placed.
     * @param random whether the position of the terrain element should be random.
     */
    public void setTerrainElement(TerrainElement terrainElement, boolean random){
    }

    /**
     * Places a single building on the map.
     *
     * @param building the building to be placed.
     */
    public void setStructure(Building building){

    }

    /**
     * Places a group buildings on the map.
     *
     * @param buildings the buildings to be placed.
     */
    public void setStructure(Building[][] buildings){
    }

    /**
     * Places a single building on the map in a random position.
     *
     * @param building the building to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(Building building, boolean random){

    }

    /**
     * Places a group buildings on the map in a random position.
     *
     * @param buildings the buildings to be placed.
     * @param random whether the position should be random.
     */
    public void setStructure(Building[][] buildings, boolean random){

    }

    /**
     * Places a set of elements that make up a civilization
     *
     * @param civilizationContainer the container with all the civilization elements.
     */
    public void setCivilization(CivilizationContainer civilizationContainer){

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
        for (BaseEntity e: entities
             ) {
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
        CivilizationContainer civilizationContainer = new CivilizationContainer(this, civilizationTypes);
        // this method has not been implemented yet!!!!!!!!!!!
        return civilizationContainer;
    }

    /**
     * Creates a civilization container from a random type of civilization.
     *
     * @return the new CivilizationContainer.
     */
    public CivilizationContainer createRandomCivilization(){
        CivilizationContainer civilizationContainer = new CivilizationContainer(this);
        // this method has not been implemented yet!!!!!!!!!!!
        return civilizationContainer;
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
     * @return the new Building.
     */
    private void getRandomBuilding(){
        BuildingTypes random = BuildingTypes.values()[r.nextInt(BuildingTypes.values().length)];
        BaseEntity newBuilding;
        if(random == BuildingTypes.BASE){
            newBuilding = new Base(world, r.nextInt(width-1),r.nextInt(length-1),1);
        }
        //Doesn't exist yet
//        if(random == BuildingTypes.FORT){
//            newBuilding = new Fort(world, r.nextInt(width-1),r.nextInt(length-1),1);
//        }
//        if(random == BuildingTypes.HOUSE){
//            newBuilding = new House(world, r.nextInt(width-1),r.nextInt(length-1),1);
//        }
//        if(random == BuildingTypes.CAMP){
//            newBuilding = new Camp(world, r.nextInt(width-1),r.nextInt(length-1),1);
//        }
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
        return null;
    }

    /**
     * Creates a random entity.
     */
    private void getRandomEntity(){
        EntityTypes random = EntityTypes.values()[r.nextInt(EntityTypes.values().length)];
        BaseEntity newEntity;
        if(random == EntityTypes.SPACMAN){
            newEntity = new Spacman(r.nextInt(width-1),r.nextInt(length-1),1);
        }
        else if(random == EntityTypes.ENEMYSPACMAN){
            newEntity = new EnemySpacman(r.nextInt(width-1),r.nextInt(length-1),1);
        }
        //Does not exist yet
//        if(random == EntityTypes.ALIEN){
//            newEntity = new Alien(r.nextInt(width-1),r.nextInt(length-1),1);
//        }
//        if(random == EntityTypes.PIG){
//            newEntity = new Pig(r.nextInt(width-1),r.nextInt(length-1),1);
//        }
        else {
            return;
        }

        world.addEntity(newEntity);
    }

    /**
     * Creates a random entity.
     */
    private void getRandomResource(){
        ResourceType random = ResourceType.values()[r.nextInt(ResourceType.values().length)];
        BaseEntity newEntity;
        if(random == ResourceType.BIOMASS){
            newEntity = new Resource(r.nextInt(length-1), r.nextInt(width-1), 0, 1f, 1f, ResourceType.BIOMASS);
        }
        else if(random == ResourceType.CRYSTAL){
            newEntity = new Resource(r.nextInt(length-1), r.nextInt(width-1), 0, 1f, 1f, ResourceType.CRYSTAL);
        }
        else if(random == ResourceType.ROCK){
            newEntity = new Resource(r.nextInt(length-1), r.nextInt(width-1), 0, 1f, 1f, ResourceType.ROCK);
        }
        else if(random == ResourceType.WATER){
            newEntity = new Resource(r.nextInt(length-1), r.nextInt(width-1), 0, 1f, 1f, ResourceType.WATER);
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
        return "";
    }

//    /**
//     * Adds an entity to the temporary collision map
//     * @param entity the entity to be added
//     */
//    public void addEntity(BaseEntity entity) {
//
//        //Add to the collision map
//        int left = (int)entity.getPosX();
//        int right = (int)Math.ceil(entity.getPosX() + entity.getXLength());
//        int bottom = (int)entity.getPosY();
//        int top = (int)Math.ceil(entity.getPosY() + entity.getYLength());
//        for (int x = left; x < right; x++) {
//            for (int y = bottom; y < top; y++) {
//                newCollisionMap.get(x, y).add(entity);
//            }
//        }
//    }




}
