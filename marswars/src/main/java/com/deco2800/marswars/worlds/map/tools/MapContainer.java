package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityTypes;
import com.deco2800.marswars.entities.TerrainElements.TerrainElement;
import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import com.deco2800.marswars.entities.buildings.Building;
import com.deco2800.marswars.entities.buildings.BuildingTypes;
import com.deco2800.marswars.worlds.CivilizationTypes;
import com.deco2800.marswars.worlds.MapSizeTypes;

import java.util.Random;


/**
 * Creates a container of objects for a map.
 * This class has tools to generate or set game elements into a container. This container
 * should be later loaded into a CustomizedWorld.
 */
public class MapContainer {

    // path of the .tmx map file
    private String mapPath;
    // width of the map loaded
    private int width;
    // height of the map loaded
    private int height;
    // randomizer
    private Random r = new Random();

    /**
     * Creates a new Map container from a given map with random elements.
     *
     * @param mapPath .tmx file to be loaded
     * @param random elements in the container.
     */
    public MapContainer( String mapPath, boolean random){

    }

    /**
     * Creates a new Map container from a given map.
     *
     * @param mapPath .tmx file to be loaded
     */
    public MapContainer(String mapPath){

    }

    /**
     * Creates a Map container from a random map with random elements.
     *
     * @param random map and elements in the container
     */
    public MapContainer(boolean random){

    }

    /**
     * @return the map .tmx been used
     */
    public String getMap(){

        return "";
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
    public void setstructure(Building[][] buildings, boolean random){

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

    }

    /**
     * Places a set of entities on the map
     *
     * @param entities the set of entities to be placed.
     */
    public void setEntities(BaseEntity[][] entities){

    }

    /**
     * Places an entity on the map in a random location.
     *
     * @param entity the entity to be placed.
     * @param random whether its position should be random or not.
     */
    public void setEntity(BaseEntity entity, boolean random){

    }

    /**
     * Places a set of entities on the map in a random position.
     *
     * @param entities the set of entities to be placed.
     * @param random whether their position should be random or not.
     */
    public void setEntities(BaseEntity[][] entities, boolean random){

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
    private Building getRandomBuilding(){
        BuildingTypes random = BuildingTypes.values()[r.nextInt(BuildingTypes.values().length)];
        return null;
    }

    /**
     * Creates a random group of same type of buildings.
     *
     * @return the new group of buildings.
     */
    private Building[][] getRandomStructure(){
        BuildingTypes random = BuildingTypes.values()[r.nextInt(BuildingTypes.values().length)];
        return null;
    }

    /**
     * Creates a random entity.
     *
     * @return the new entity.
     */
    private BaseEntity getRandomEntity(){
        EntityTypes random = EntityTypes.values()[r.nextInt(EntityTypes.values().length)];
        return null;
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




}
