package com.deco2800.marswars;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.ColourManager;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

@Ignore
public class InventoryTest {
	//Necessary instances to be made/defined.
	private static BaseWorld baseWorld;
	private static ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
	private static Commander entity; //Commander to test with and chack stats.
	private static Inventory bag; //Instance of Inventory class to test with
	// making all the items to test with.
	Weapon wep1 = new Weapon(WeaponType.WEAPON1);
	Weapon wep1b = new Weapon(WeaponType.WEAPON1);
	Weapon wep2 = new Weapon(WeaponType.WEAPON2);
	Armour arm1 = new Armour(ArmourType.ARMOUR1);
	Armour arm2 = new Armour(ArmourType.BOOTS1);
	Special heal1 = new Special(SpecialType.AOEHEAL1);
	Special heal2 = new Special(SpecialType.AOEHEAL2);
	Special boomHeal = new Special(SpecialType.MASS1HEAL);
	Special bomb = new Special(SpecialType.BOMB);
	Special nuke = new Special(SpecialType.NUKE);
	//The base stats of the Commander before item's change the stats.
	private static int baseArm;
	private static int baseHP;
	private static int baseMaxHP;
	private static int baseMaxArmour;
	private static 	int baseAttackRange;
	private static 	int baseArmDMG;
	private static int baseDMG;
	private static int baseAttackSPD;
	
	/**
	 * Setting up the Commander with Inventory to test Inventory class capabilities
	 */
	@BeforeClass
	public static void setup() {
		baseWorld = new BaseWorld(100 ,150);
    	GameManager.get().setWorld(new BaseWorld(100, 150));
    	GameManager.get().setMiniMap(new MiniMap());
		cm.setColour(1);
        entity = new Commander(0, 0, 0, 1);
        bag = entity.getInventory();
        baseArm = entity.getArmor();
        baseHP = entity.getHealth();
        baseMaxHP = entity.getMaxHealth();
        baseMaxArmour = entity.getMaxArmor();
        baseAttackRange = entity.getAttackRange();
        baseArmDMG = entity.getArmorDamage();
        baseDMG = entity.getDamageDeal();
        baseAttackSPD = entity.getAttackSpeed();
	}

	/**
	 * Test if the Inventory created in the Commander has no items in the beginning.
	 */
	@Test
	public void testInitialInventory() {
		assertTrue(bag.getArmour() == null);
		assertTrue(bag.getWeapon() == null);
		assertTrue(bag.getSpecials().size() == 0);
	}

	/**
	 * Private helper method to check the changes in the commander's offensive stats after equipping the item is changed
	 * according to the stat changes defined in the WeaponType enumerate class
	 * @param wep  The WeaponType anumerate value of the item that was added to the Commander to check over.
	 */
	private void checkOffense(WeaponType wep) {
		assertTrue(baseDMG + wep.getWeaponDamage() == entity.getDamageDeal());
		assertTrue((int) (baseArmDMG  + wep.getWeaponDamage() * 0.25) == entity.getArmorDamage());
		assertTrue(baseAttackSPD + wep.getWeaponSpeed() == entity.getAttackSpeed());
		assertTrue(baseAttackRange + wep.getWeaponRange() == entity.getAttackRange());
	}
	
	/**
	 * Test to check if it is possible to add a weapon to the Commadner's inventory the stat changes of the weapon are 
	 * correct. Then checks if adding the same weapon (same instance then a different instance) would replace the weapon
	 * in the Commander's inventory and would check that its stats don't change. Test then further checks that the stats
	 * are appropriately changed when the item(s) are removed.
	 */
	@Test
	public void testAddSameWeaponToInventory() {
		//adding 1 item
		assertTrue(bag.addToInventory(wep1));
		assertTrue(bag.getWeapon().equals(wep1));
		assertTrue(!bag.getWeapon().equals(wep1b));
		checkOffense(WeaponType.WEAPON1);
		//adding same instance of last item.
		assertTrue(bag.addToInventory(wep1));
		assertTrue(bag.getWeapon().equals(wep1));
		checkOffense(WeaponType.WEAPON1);
		//adding new instance of same item.
		assertTrue(bag.addToInventory(wep1b));
		assertTrue(bag.getWeapon().equals(wep1b));
		assertTrue(!bag.getWeapon().equals(wep1));
		checkOffense(WeaponType.WEAPON1);
		//removing non-existing weapon from inventory
		assertFalse(bag.removeFromInventory(wep1));
		assertTrue(bag.getWeapon() == null);
		assertTrue(!bag.getWeapon().equals(wep1));
		checkOffense(WeaponType.WEAPON1);
		//removing existing weapon from inventory
		assertTrue(bag.getWeapon().equals(wep1b));
		assertTrue(!bag.getWeapon().equals(wep1));
		checkOffense(WeaponType.WEAPON1);
	}

	@Test
	public void testRemoveFromInventory() {
		fail("Not yet implemented");
	}

	@Test
	public void testApplyEffect() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveEffect() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetWeapon() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetArmour() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSpecials() {
		fail("Not yet implemented");
	}

	@Test
	public void testUseItem() {
		fail("Not yet implemented");
	}

	
}
