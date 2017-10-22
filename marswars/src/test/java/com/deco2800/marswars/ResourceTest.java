package com.deco2800.marswars;

import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Test;

import static org.junit.Assert.*;
public class ResourceTest {
	private int initialStorage = 100;// 100 here is the default capacity
	@Test
	public void constructorTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t2 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		Resource t3 = new Resource(0, 1, 2, 3, 4, ResourceType.CRYSTAL);
		Resource t4 = new Resource(0, 1, 2, 3, 4, ResourceType.BIOMASS);
		
		assertEquals(t2.getType(), ResourceType.ROCK);
		assertEquals(t3.getType(), ResourceType.CRYSTAL);
		assertEquals(t4.getType(), ResourceType.BIOMASS);
		
		assertEquals(t2.getTexture(), "large_rock");
		assertEquals(t3.getTexture(), "large_crystal");
		assertEquals(t4.getTexture(), "large_biomass");
	}
	
	@Test
	public void harvesterTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		
		assertEquals(t1.getHarvesterCapacity(), 5);
		
		t1.setHarvestNumber(-1);
		assertEquals(t1.getHarvesterNumber(), 0);
		
		t1.setHarvestNumber(0);
		assertEquals(t1.getHarvesterNumber(), 0);
		
		t1.setHarvestNumber(1);
		assertEquals(t1.getHarvesterNumber(), 1);
		
		t1.setHarvestNumber(2);
		assertEquals(t1.getHarvesterNumber(), 2);
		
		t1.setHarvestNumber(3);
		assertEquals(t1.getHarvesterNumber(), 3);
		
		t1.setHarvestNumber(4);
		assertEquals(t1.getHarvesterNumber(), 4);
		
		t1.setHarvestNumber(5);
		assertEquals(t1.getHarvesterNumber(), 5);
		
		t1.setHarvestNumber(6);
		assertEquals(t1.getHarvesterNumber(), 5);
	}
	
	@Test
	public void storageTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		
		assertEquals(t1.getHealth(), initialStorage); 
		
		t1.setHealth(10 * initialStorage);
		assertEquals(t1.getHealth(), initialStorage); 
		
		t1.setHealth(50);
		assertEquals(t1.getHealth(), 50); 
	}
	
	@Test
	public void storageStateTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t2 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		Resource t3 = new Resource(0, 1, 2, 3, 4, ResourceType.CRYSTAL);
		Resource t4 = new Resource(0, 1, 2, 3, 4, ResourceType.BIOMASS);
		
		t2.setHealth(initialStorage - 1);
		t3.setHealth(initialStorage - 1);
		t4.setHealth(initialStorage - 1);
		assertEquals(t2.getTexture(), "large_rock");
		assertEquals(t3.getTexture(), "large_crystal");
		assertEquals(t4.getTexture(), "large_biomass");
		
		t2.setHealth(initialStorage / 2);
		t3.setHealth(initialStorage / 2);
		t4.setHealth(initialStorage / 2);
		assertEquals(t2.getTexture(), "medium_rock");
		assertEquals(t3.getTexture(), "medium_crystal");
		assertEquals(t4.getTexture(), "medium_biomass");
		
		t2.setHealth(initialStorage / 5);
		t3.setHealth(initialStorage / 5);
		t4.setHealth(initialStorage / 5);
		assertEquals(t2.getTexture(), "small_rock");
		assertEquals(t3.getTexture(), "small_crystal");
		assertEquals(t4.getTexture(), "small_biomass");
	}
	
	@Test
	public void removedTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.BIOMASS);
		Resource t2 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		GameManager.get().getWorld().addEntity(t1);
		
		//GameManager.get().getWorld().getEntities(0, 1);
		assertTrue(GameManager.get().getWorld().getEntities().contains(t1));
		
		// normally when health is 0, the resource will get removed, however it still exist in the world
		// update: it doesn't exist in the world but in that position

		t1.setHealth(0);
		assertFalse(GameManager.get().getWorld().getEntities().contains(t1));
		
		t2.setHealth(-1);
		assertFalse(GameManager.get().getWorld().getEntities().contains(t2));
	}
}
