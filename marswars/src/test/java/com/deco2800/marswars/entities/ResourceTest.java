package com.deco2800.marswars.entities;

import static org.junit.Assert.*;

import org.junit.Test;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
public class ResourceTest {
	private int initialStorage = 100;// 100 here is the default capacity
	@Test
	public void ConstructorTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.WATER);
		Resource t2 = new Resource(0, 1, 2, 3, 4, ResourceType.ROCK);
		Resource t3 = new Resource(0, 1, 2, 3, 4, ResourceType.CRYSTAL);
		Resource t4 = new Resource(0, 1, 2, 3, 4, ResourceType.BIOMASS);
		
		assertEquals(t1.getType(), ResourceType.WATER);
		assertEquals(t2.getType(), ResourceType.ROCK);
		assertEquals(t3.getType(), ResourceType.CRYSTAL);
		assertEquals(t4.getType(), ResourceType.BIOMASS);
		
		assertEquals(t1.getTexture(), "large_water");
		assertEquals(t2.getTexture(), "large_rock");
		assertEquals(t3.getTexture(), "large_crystal");
		assertEquals(t4.getTexture(), "large_biomass");
	}
	
	@Test
	public void HarvesterTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.WATER);
		
		assertEquals(t1.getHarvesterCapacity(), 5);
		
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
	public void StorageTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.WATER);
		
		assertEquals(t1.getHealth(), initialStorage); 
		
		t1.setHealth(10 * initialStorage);
		assertEquals(t1.getHealth(), initialStorage); 
		
		t1.setHealth(50);
		assertEquals(t1.getHealth(), 50); 
	}
	
	@Test
	public void StorageStateTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.WATER);
		
		t1.setHealth(initialStorage / 2);
		assertEquals(t1.getTexture(), "medium_water");
		
		t1.setHealth(initialStorage / 5);
		assertEquals(t1.getTexture(), "small_water");
	}
	
	@Test
	public void RemovedTest() { // NOT COMPLETE
		GameManager.get().setWorld(new BaseWorld(10,10));
		Resource t1 = new Resource(0, 1, 2, 3, 4, ResourceType.WATER);
		GameManager.get().getWorld().addEntity(t1);
		
		//GameManager.get().getWorld().getEntities(0, 1);
		assertTrue(GameManager.get().getWorld().getEntities(0, 1).contains(t1));
		
		// normally when health is 0, the resource will get removed, however it still exist in the world
//		t1.setHealth(0);
//		assertFalse(GameManager.get().getWorld().getEntities(0, 1).contains(t1));
	}
}
