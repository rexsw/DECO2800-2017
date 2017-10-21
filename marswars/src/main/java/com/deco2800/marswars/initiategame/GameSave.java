package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.TerrainElements.Obstacle;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.*;
import com.deco2800.marswars.util.Array2D;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Treenhan on 10/10/17.
 * This class is responsible for saving and loading the game
 */
public class GameSave {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSave.class);

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
        data.setaITeams(aITeams);
        data.setPlayerTeams(playerTeams);

        File delete = new File("./resources/mapAssets/loadmap.tmx");
        delete.delete();

        //copying the map
        File source = new File("./resources/mapAssets/tmap.tmx");
        File dest = new File("./resources/mapAssets/loadmap.tmx");

        try {
            Files.copy(source.toPath(), dest.toPath());
        }catch (java.io.IOException e){
            LOGGER.info("Game save: No file found - " + e);
        }

    }

    /**
     * this function is saving a game by writing data to the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public void writeGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("save.bin"));
        fillData();

        kryo.writeClassAndObject(output, data.getFogOfWar());
        kryo.writeClassAndObject(output, data.getBlackFogOfWar());
        kryo.writeClassAndObject(output, data.getEntities());
        kryo.writeClassAndObject(output, data.getResource());
        kryo.writeClassAndObject(output, data.getBuilding());
        kryo.writeClassAndObject(output, data.getObstacles());
        kryo.writeClassAndObject(output, data.getIndex());
        kryo.writeClassAndObject(output, data.getaITeams());
        kryo.writeClassAndObject(output, data.getPlayerTeams());
        kryo.writeClassAndObject(output, data.getaIStats());
        kryo.writeClassAndObject(output, data.getPlayerStats());
        kryo.writeClassAndObject(output, data.getHour());
        kryo.writeClassAndObject(output, data.getMin());
        kryo.writeClassAndObject(output, data.getSec());
        output.close();
    }

    /**
     * this function will read the saved data out of a file
     * @throws java.io.FileNotFoundException
     */
    public void readGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("save.bin"));

        data.setFogOfWar((Array2D<Integer>)kryo.readClassAndObject(input));
        data.setBlackFogOfWar((Array2D<Integer>)kryo.readClassAndObject(input));
        data.setEntities((ArrayList<SavedEntity>)kryo.readClassAndObject(input));
        data.setResource((ArrayList<Resource>)kryo.readClassAndObject(input));
        data.setBuilding((ArrayList<SavedBuilding>)kryo.readClassAndObject(input));
        data.setObstacles((ArrayList<Obstacle>)kryo.readClassAndObject(input));
        data.setIndex((int)kryo.readClassAndObject(input));
        data.setaITeams((int)kryo.readClassAndObject(input));
        data.setPlayerTeams((int)kryo.readClassAndObject(input));
        data.setaIStats((ArrayList<ArrayList<Integer>>)kryo.readClassAndObject(input));
        data.setPlayerStats((ArrayList<ArrayList<Integer>>)kryo.readClassAndObject(input));
        data.setHour((long)kryo.readClassAndObject(input));
        data.setMin((long)kryo.readClassAndObject(input));
        data.setSec((long)kryo.readClassAndObject(input));
        input.close();
    }



    /**
     * this function will get all the entities and fills in the arrays
     */
    public void fillData(){
        data.setFogOfWar(FogManager.getFog());
        data.setBlackFogOfWar(FogManager.getBlackFog());

        //getting all the entities
        List<BaseEntity> renderablesBe = GameManager.get().getWorld().getEntities();

        //converting these base entities to abstract entities
        // Tutor approved workaround to avoid changing whole structure of game
        List<AbstractEntity> renderables = new ArrayList<>();
        for (BaseEntity e : renderablesBe) {
            renderables.add(e);
        }

        //Sort entities into walkables and entities
        for (AbstractEntity r : renderables) {
             if(r instanceof Resource){
                data.getResource().add((Resource)r);
            }
            else if(r instanceof BuildingEntity){
                fillBuilding(r);
            }
            else if(r instanceof Obstacle){
                 data.getObstacles().add((Obstacle)r);
             }
            else {
                fillEntities(r);
            }
        }

        ResourceManager rm = (ResourceManager) GameManager.get()
                .getManager(ResourceManager.class);
        //filling stats for AI
        //biomass-rocks-crystal-water-population
        for(int i=1;i<data.getaITeams()+1;i++){
            ArrayList<Integer> stats = new ArrayList<>();
            stats.add(rm.getBiomass(i));
            stats.add(rm.getRocks(i));
            stats.add(rm.getCrystal(i));
            stats.add(rm.getPopulation(i));
            data.getaIStats().add(stats);
        }

        //filling stats for player
        //biomass-rocks-crystal-water-population
        for(int i=1;i<data.getPlayerTeams()+1;i++){
            ArrayList<Integer> stats = new ArrayList<>();
            stats.add(rm.getBiomass(-i));
            stats.add(rm.getRocks(-i));
            stats.add(rm.getCrystal(-i));
            stats.add(rm.getPopulation(-i));
            data.getPlayerStats().add(stats);
        }


        //fill the time
        TimeManager timeManager = (TimeManager)
                GameManager.get().getManager(TimeManager.class);
        data.setHour(timeManager.getPlayHours());
        data.setMin(timeManager.getPlayMinutes());
        data.setSec(timeManager.getPlaySeconds());

    }

    /**
     * this function fill the data with saved buildings
     * @param b
     */
    public void fillBuilding(AbstractEntity b){
        BuildingEntity bE = (BuildingEntity)b;
        if("Turret".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.TURRET,bE.getOwner(),bE.getHealth()));
        }
        else if ("Base".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BASE,bE.getOwner(),bE.getHealth()));
        }
        else if ("Barracks".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BARRACKS,bE.getOwner(),bE.getHealth()));
        }
        else if ("Bunker".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.BUNKER,bE.getOwner(),bE.getHealth()));
        }
        else if("Hero Factory".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.HEROFACTORY,bE.getOwner(),bE.getHealth()));
        }
        else if("TechBuilding".equals(bE.getbuilding())){
            data.getBuilding().add(new SavedBuilding(bE.getPosX(),bE.getPosY(),BuildingType.TECHBUILDING,bE.getOwner(),bE.getHealth()));
        }
    }

    /**
     * this function fills the data with proper entities
     * @param e
     */
    public void fillEntities(AbstractEntity e){
        if(e instanceof Astronaut){
            data.getEntities().add(new SavedEntity("Astronaut",e.getPosX(),e.getPosY(),((Astronaut) e).getOwner(),((Astronaut) e).getHealth()));
        }else if(e instanceof Base){
            data.getEntities().add(new SavedEntity("Base",e.getPosX(),e.getPosY(),((Base) e).getOwner(),((Base) e).getHealth()));
        }else if(e instanceof Tank){
            data.getEntities().add(new SavedEntity("Tank",e.getPosX(),e.getPosY(),((Tank) e).getOwner(),((Tank) e).getHealth()));
        }else if(e instanceof Carrier){
            data.getEntities().add(new SavedEntity("Carrier",e.getPosX(),e.getPosY(),((Carrier) e).getOwner(),((Carrier) e).getHealth()));
        }else if(e instanceof Commander){
            data.getEntities().add(new SavedEntity("Commander",e.getPosX(),e.getPosY(),((Commander) e).getOwner(),((Commander) e).getHealth()));
        }else if(e instanceof Medic){
            data.getEntities().add(new SavedEntity("Medic",e.getPosX(),e.getPosY(),((Medic) e).getOwner(),((Medic) e).getHealth()));
        }else if(e instanceof Hacker){
            data.getEntities().add(new SavedEntity("Hacker",e.getPosX(),e.getPosY(),((Hacker) e).getOwner(),((Hacker) e).getHealth()));
        }else if (e instanceof Soldier){
            data.getEntities().add(new SavedEntity("Soldier",e.getPosX(),e.getPosY(),((Soldier) e).getOwner(),((Soldier) e).getHealth()));
        }

    }
}
