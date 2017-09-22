package com.deco2800.marswars.heros;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.SpecialType;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.entities.units.Soldier;

public class CommanderTest {
	Commander c;
	@Before
	public void setUp() {
		c = new Commander(1,1,1,1);
	}
	
	@Test
	public void constructorTest() {
		assertTrue(c.getInventory() instanceof Inventory);
		assertFalse(c.getOwner() == 2);
		assertTrue(c.getOwner() == 1);
		assertFalse(c.toString().equals("Soldier"));
		assertTrue(c.toString().equals("Commander"));
	}
	
	@Test
	public void inventoryTest() {
		// more detailed test should be handled in inventoryTest.java
		assertTrue(c.getInventory().getWeapon() == null);
		assertTrue(c.getInventory().getWeapon() == null);
		assertTrue(c.getInventory().getSpecials().size() == 0);
		
		Item item = new Special(SpecialType.AOEHEAL1);
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertFalse(c.addItemToInventory(item));
		
		assertTrue(c.removeItemFromInventory(item));
		Item item2 = new Special(SpecialType.BOMB);
		assertFalse(c.removeItemFromInventory(item2));
	}
	
	@Test
	public void equalTest() {
		Commander c2 = new Commander(1,1,1,2);
		assertTrue(c.equals(c));
		assertFalse(c.equals(c2));
		assertTrue(c.hashCode() == c.hashCode());
		assertFalse(c.hashCode() == c2.hashCode());
		
		assertFalse(c.equals(new Soldier(1,1,1,1)));
	}
	
	@Test
	public void statsTest() {
		EntityStats stats = c.getStats();
		assertTrue(stats.getType().equals(Selectable.EntityType.HERO));
		assertTrue(stats.getName().equals(c.toString()));
		assertTrue(stats.getCurrentAction().equals(c.getAction()));
		assertTrue(stats.getHealth() == c.getHealth());
		assertTrue(stats.getResourceCarried() == null);
		assertTrue(stats.getMaxHealth() == c.getMaxHealth());
	}
	
	@Test
	public void loyaltyTest() {
		int currentLoyalty = c.getLoyalty();
		c.setLoyalty(currentLoyalty - 10);
		
		assertFalse(c.getLoyalty() == currentLoyalty - 10);
		assertTrue(c.getLoyalty() == currentLoyalty);
	}
}
