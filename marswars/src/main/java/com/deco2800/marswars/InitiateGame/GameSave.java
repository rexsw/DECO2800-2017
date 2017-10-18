package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.util.Array2D;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Created by Treenhan on 10/10/17.
 * This class is responsible for saving and loading the game
 */
public class GameSave {

    public Data data = new Data();
    /**
     * blank constructor to correctly load game
     * this is used for loading
     */
    public GameSave(){}

    /**
     * the constructor to save map type and size
     * only used when initiate game saving instance
     */
    public GameSave(int aITeams, int playerTeams){
        data.aITeams = aITeams;
        data.playerTeams = playerTeams;

        File delete = new File("./resources/mapAssets/loadmap.tmx");
        delete.delete();

        //copying the map
        File source = new File("./resources/mapAssets/tmap.tmx");
        File dest = new File("./resources/mapAssets/loadmap.tmx");

        try {
            Files.copy(source.toPath(), dest.toPath());
        }catch (java.io.IOException e){}

    }

    /**
     * this function is saving a game by writing data to the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public void writeGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("save.bin"));
        fillData();

        kryo.writeClassAndObject(output, data.fogOfWar);
        kryo.writeClassAndObject(output, data.blackFogOfWar);
        kryo.writeClassAndObject(output, data.entities);
        kryo.writeClassAndObject(output, data.resource);
        kryo.writeClassAndObject(output, data.building);
        kryo.writeClassAndObject(output, data.aITeams);
        kryo.writeClassAndObject(output, data.playerTeams);
        kryo.writeClassAndObject(output, data.aIStats);
        kryo.writeClassAndObject(output, data.playerStats);
        output.close();
    }

    /**
     * this function will read the saved data out of a file
     * @throws java.io.FileNotFoundException
     */
    public void readGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("save.bin"));

        data.fogOfWar  = (Array2D<Integer>)kryo.readClassAndObject(input);
        data.blackFogOfWar  = (Array2D<Integer>)kryo.readClassAndObject(input);
        data.entities  = (ArrayList<SavedEntity>)kryo.readClassAndObject(input);
        data.resource  = (ArrayList<Resource>)kryo.readClassAndObject(input);
        data.building  = (ArrayList<SavedBuilding>)kryo.readClassAndObject(input);
        data.aITeams  = (int)kryo.readClassAndObject(input);
        data.playerTeams  = (int)kryo.readClassAndObject(input);
        data.aIStats = (ArrayList<ArrayList<Integer>>)kryo.readClassAndObject(input);
        data.playerStats = (ArrayList<ArrayList<Integer>>)kryo.readClassAndObject(input);


        input.close();
    }



    /**
     * this function will get all the entities and fills in the arrays
     */
    public void fillData(){
        data.fogOfWar = FogManager.getFog();
        data.blackFogOfWar = FogManager.getBlackFog();

        //getting all the entities
        List<BaseEntity> renderables_be = GameManager.get().getWorld().getEntities();

        //converting these base entities to abstract entities
        // Tutor approved workaround to avoid changing whole structure of game
        List<AbstractEntity> renderables = new ArrayList<>();
        for (BaseEntity e : renderables_be) {
            renderables.add(e);
        }

        //Sort entities into walkables and entities
        for (AbstractEntity r : renderables) {
             if(r instanceof Resource){
                data.resource.add((Resource)r);
            }
            else if(r instanceof BuildingEntity){
                fillBuilding(r);
            }
            else {
                fillEntities(r);
            }
        }

        ResourceManager rm = (ResourceManager) GameManager.get()
                .getManager(ResourceManager.class);
        //filling stats for AI
        //biomass-rocks-crystal-water-population
        for(int i=1;i<data.aITeams+1;i++){
            ArrayList<Integer> stats = new ArrayList<>();
            stats.add(rm.getBiomass(i));
            stats.add(rm.getRocks(i));
            stats.add(rm.getCrystal(i));
            stats.add(rm.getWater(i));
            stats.add(rm.getPopulation(i));
            data.aIStats.add(stats);
        }

        //filling stats for AI
        //biomass-rocks-crystal-water-population
        for(int i=1;i<data.playerTeams+1;i++){
            ArrayList<Integer> stats = new ArrayList<>();
            stats.add(rm.getBiomass(-i));
            stats.add(rm.getRocks(-i));
            stats.add(rm.getCrystal(-i));
            stats.add(rm.getWater(-i));
            stats.add(rm.getPopulation(-i));
            data.playerStats.add(stats);
        }

    }

    /**
     * this function fill the data with saved buildings
     * @param b
     */
    public void fillBuilding(AbstractEntity b){
        BuildingEntity bE = (BuildingEntity)b;
        if(bE.getbuilding().equals("Turret")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.TURRET,bE.getOwner(),bE.getHealth()));
        }
        else if (bE.getbuilding().equals("Base")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BASE,bE.getOwner(),bE.getHealth()));
        }
        else if (bE.getbuilding().equals("Barracks")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BARRACKS,bE.getOwner(),bE.getHealth()));
        }
        else if (bE.getbuilding().equals("Bunker")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BUNKER,bE.getOwner(),bE.getHealth()));
        }
        else if(bE.getbuilding().equals("Hero Factory")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.HEROFACTORY,bE.getOwner(),bE.getHealth()));
        }
        else if(bE.getbuilding().equals("TechBuilding")){
            data.building.add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.TECHBUILDING,bE.getOwner(),bE.getHealth()));
        }
    }

    /**
     * this function fills the data with proper entities
     * @param e
     */
    public void fillEntities(AbstractEntity e){
        if(e instanceof Astronaut){
            data.entities.add(new SavedEntity("Astronaut",e.getPosX(),e.getPosY(),((Astronaut) e).getOwner(),((Astronaut) e).getHealth()));
        }else if(e instanceof Base){
            data.entities.add(new SavedEntity("Base",e.getPosX(),e.getPosY(),((Base) e).getOwner(),((Base) e).getHealth()));
        }else if(e instanceof Tank){
            data.entities.add(new SavedEntity("Tank",e.getPosX(),e.getPosY(),((Tank) e).getOwner(),((Tank) e).getHealth()));
        }else if(e instanceof Carrier){
            data.entities.add(new SavedEntity("Carrier",e.getPosX(),e.getPosY(),((Carrier) e).getOwner(),((Carrier) e).getHealth()));
        }else if(e instanceof Commander){
            data.entities.add(new SavedEntity("Commander",e.getPosX(),e.getPosY(),((Commander) e).getOwner(),((Commander) e).getHealth()));
        }else if(e instanceof Medic){
            data.entities.add(new SavedEntity("Medic",e.getPosX(),e.getPosY(),((Medic) e).getOwner(),((Medic) e).getHealth()));
        }else if(e instanceof Hacker){
            data.entities.add(new SavedEntity("Hacker",e.getPosX(),e.getPosY(),((Hacker) e).getOwner(),((Hacker) e).getHealth()));
        }else if (e instanceof Soldier){
            data.entities.add(new SavedEntity("Soldier",e.getPosX(),e.getPosY(),((Soldier) e).getOwner(),((Soldier) e).getHealth()));
        }

    }
}
