package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Array2D;
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

    /**
     * this is the list of data will be saved
     * the order of saving will follow this order
     */
    //gray fog of war
    private Array2D<Integer> fogOfWar;

    //black fog of war
    private Array2D<Integer> blackFogOfWar;

    //list of entities
    List<AbstractEntity> entities = new ArrayList<>();

    //list of walkables
    List<AbstractEntity> walkables = new ArrayList<>();

    //TODO: the map


    /**
     * this function is saving a game by writing data to the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public void saveGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("save.bin"));
        fillData();




        output.close();
    }

    /**
     * this function is loading a game by reading data from the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public void loadGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("save.bin"));


        input.close();
    }

    /**
     * this vunction will fetch the game with loaded data from the function loadGame()
     */
    public void fetchGame(){

    }


    /**
     * this function will get all the entities and fills in the arrays
     */
    public void fillData(){
        fogOfWar = FogManager.getFog();
        blackFogOfWar = FogManager.getBlackFog();

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
                walkables.add(r);
            } else {
                entities.add(r);
            }
        }
    }
}
