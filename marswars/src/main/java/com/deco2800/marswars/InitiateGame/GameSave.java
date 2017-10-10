package com.deco2800.marswars.InitiateGame;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Treenhan on 10/10/17.
 * This class is responsible for saving and loading the game
 */
public class GameSave {

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
