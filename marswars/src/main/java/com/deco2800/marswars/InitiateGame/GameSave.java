package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.worlds.MapSizeTypes;
import com.deco2800.marswars.worlds.map.tools.MapTypes;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
    public GameSave(MapTypes mapType, MapSizeTypes mapSize, int aITeams, int playerTeams){
        data.mapType = mapType;
        data.mapSize = mapSize;
        data.aITeams = aITeams;
        data.playerTeams = playerTeams;
    }

    /**
     * this function is saving a game by writing data to the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public void writeGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("save.bin"));
        fillData();

        kryo.writeObject(output, data.fogOfWar);
        kryo.writeObject(output, data.blackFogOfWar);
        kryo.writeObject(output, data.entities);
        kryo.writeObject(output, data.walkables);
        kryo.writeObject(output, data.mapType);
        kryo.writeObject(output, data.mapSize);
        kryo.writeObject(output, data.aITeams);
        kryo.writeObject(output, data.playerTeams);
        output.close();
    }

    public void readGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("save.bin"));
        data  = kryo.readObject(input, Data.class);
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
            if (r.canWalOver()) {
                data.walkables.add(r);
            } else {
                data.entities.add(r);
            }
        }
    }
}
