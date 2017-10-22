package com.deco2800.marswars.heros;

import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.items.Item;
import com.deco2800.marswars.entities.items.Special;
import com.deco2800.marswars.entities.items.SpecialType;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.entities.units.Soldier;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommanderTest {
	Commander c;
	@Before
	public void setUp() {
		c = new Commander(1,1,1,1);
	}
	
	/**
	 * This test checks following things:
	 * 1. does this commander has an inventory
	 * 2. does it has the right owner
	 * 3. since it inherited from Soldier class, does the override method works
	 */
	@Test
	public void constructorTest() {
		assertTrue(c.getInventory() instanceof Inventory);
		assertFalse(c.getOwner() == 2);
		assertTrue(c.getOwner() == 1);
		assertFalse(c.toString().equals("Soldier"));
		assertTrue(c.toString().equals("Commander"));
	}
	
	/**
	 * This test check some simple function on inventory
	 * The detailed checking has been handled in the inventory test
	 * This test just to make sure the right item gets passed through to the inventory object
	 */
	@Test
	public void inventoryTest() {
		// more detailed test should be handled in inventoryTest.java
		assertTrue(c.getInventory().getWeapon() == null);
		assertTrue(c.getInventory().getWeapon() == null);
		assertTrue(c.getInventory().getSpecials().size() == 0);
		
		Item item = new Special(SpecialType.HEALTHSHOT);
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertTrue(c.addItemToInventory(item));
		assertFalse(c.addItemToInventory(item));
		
		assertTrue(c.removeItemFromInventory(item));
		Item item2 = new Special(SpecialType.MISSILE);
		assertFalse(c.removeItemFromInventory(item2));
	}
	
	/**
	 * Test the equals method of the Commander class
	 * 
	 */
	@Test
	public void equalTest() {
		Commander c2 = new Commander(1,1,1,2);
		assertTrue(c.equals(c));
		assertFalse(c.equals(c2));
		assertTrue(c.hashCode() == c.hashCode());
		assertFalse(c.hashCode() == c2.hashCode());
		
		assertFalse(c.equals(new Soldier(1,1,1,1)));
	}
	
	/**
	 * statsTest test the commander entityType, toString, action, current and max health
	 * resourceCarried. Pretty much everything should be passed to EntityStats
	 */
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
	
	/**
	 * The loyalty of Commander should be unaffected by the hacker's setLoyalty method
	 * or any setLoytalty method
	 */
	@Test
	public void loyaltyTest() {
		int currentLoyalty = c.getLoyalty();
		c.setLoyalty(currentLoyalty - 10);
		
		assertFalse(c.getLoyalty() == currentLoyalty - 10);
		assertTrue(c.getLoyalty() == currentLoyalty);
	}
}
