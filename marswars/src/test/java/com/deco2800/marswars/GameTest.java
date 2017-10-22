package com.deco2800.marswars;

import com.deco2800.marswars.buildings.*;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.initiategame.GameSave;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * Junit Tests for code in package initiateGame
 * includes: gameSave, game, SavedEntity, SavedBuilding, SavedAnimal
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
	}
	
	/**
	 * Tests gameSave and saving all types of entities upon saving.
	 * 
	 * @throws FileNotFoundException when savefile is not found
	 */
	@Test
	public void testGameSave() throws FileNotFoundException {
		GameSave testSave = new GameSave(1, 1, AiManager.Difficulty.EASY,true);
		testSave.fillData();
		testSave.writeGame();
		testSave.readGame();
		
		Turret turret = new Turret(null, 0, 0, 0, 1);
		Base base = new Base(null, 0, 0, 0, 1);
		Barracks barracks = new Barracks(null, 0, 0, 0, 1);
		Bunker bunker = new Bunker(null, 0, 0, 0, 1);
		HeroFactory heroFact = new HeroFactory(null, 0, 0, 0, 1);
		TechBuilding techBuild = new TechBuilding(null, 0, 0, 0, 1);
		
		testSave.fillBuilding(turret);
		testSave.fillBuilding(base);
		testSave.fillBuilding(barracks);
		testSave.fillBuilding(bunker);
		testSave.fillBuilding(heroFact);
		testSave.fillBuilding(techBuild);
		
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
		
		Corn corn = new Corn(0, 0, 0, 1);
		Snail snail = new Snail(0, 0, 0, 1);
		Dino dino = new Dino(0, 0, 0, 1);
		
		testSave.fillAnimals(corn);
		testSave.fillAnimals(snail);
		testSave.fillAnimals(dino);
	}	
	
	@Test
	public void testGame() {
		
	}
}
