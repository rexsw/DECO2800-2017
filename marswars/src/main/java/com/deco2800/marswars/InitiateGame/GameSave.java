package com.deco2800.marswars.InitiateGame;

import com.deco2800.marswars.entities.AbstractEntity;
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
     */
    //gray fog of war
    private Array2D<Integer> fogOfWar;

    //black fog of war
    private Array2D<Integer> blackFogOfWar;

    //list of entities
    List<AbstractEntity> entities = new ArrayList<>();
    
    //list of walkables
    List<AbstractEntity> walkables = new ArrayList<>();

    //the map


    /**
     * this function is saving a game by writing data to the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public static void saveGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Output output = new Output(new FileOutputStream("save.bin"));



        output.close();
    }

    /**
     * this function is loading a game by reading data from the file save.bin
     * @throws java.io.FileNotFoundException
     */
    public static void loadGame() throws java.io.FileNotFoundException{
        Kryo kryo = new Kryo();
        Input input = new Input(new FileInputStream("save.bin"));


        input.close();
    }

    /**
     * this vunction will fetch the game with loaded data from the function loadGame()
     */
    public static void fetchGame(){

    }
}
