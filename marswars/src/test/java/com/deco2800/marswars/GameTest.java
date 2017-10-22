package com.deco2800.marswars;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.initiategame.GameSave;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import com.deco2800.marswars.entities.units.*;

/**
 * Junit Tests for code in package initiateGame
 * includes: gameSave, game
 * 
 * Created by jdtran21 on 10/22/2017
 *
 */
public class GameTest {
	
	@Before
	public void initialise() {
		GameManager.get().setWorld(new BaseWorld(50, 50));
	}
	
	@Test
	public void emptyTest() throws FileNotFoundException {
		GameSave testSave = new GameSave();
		//Game testGame = new Game(1, 1);
	}
	
	@Test
	public void testGameSave() throws FileNotFoundException {
		GameSave testSave = new GameSave(1, 1);
		testSave.fillData();
		testSave.writeGame();
		testSave.readGame();
		Astronaut astronaut = new Astronaut(0, 0, 0, 1);
		Tank tank = new Tank(0, 0, 0, 1);
		Carrier carrier = new Carrier(0, 0, 0, 1);
		Spacman spacman = new Spacman(0, 0, 0, 1);
		Sniper sniper = new Sniper(0, 0, 0, 1);
		TankDestroyer tankDes = new TankDestroyer(0, 0, 0, 1);
		Spatman spatman = new Spatman(0, 0, 0, 1);
		Commander commander = new Commander(0, 0, 0, 1);
		Medic medic = new Medic(0, 0, 0, 1);
		Hacker hacker = new Hacker(0, 0, 0, 1);
		Soldier soldier = new Soldier(0, 0, 0, 1);
		testSave.fillEntities(astronaut);
		testSave.fillEntities(tank);
		testSave.fillEntities(carrier);
		testSave.fillEntities(spacman);
		testSave.fillEntities(sniper);
		testSave.fillEntities(tankDes);
		testSave.fillEntities(spatman);
		testSave.fillEntities(commander);
		testSave.fillEntities(medic);
		testSave.fillEntities(hacker);
		testSave.fillEntities(soldier);

	}	
}
