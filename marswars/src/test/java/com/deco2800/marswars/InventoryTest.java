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
	BaseWorld baseWorld;
	ColourManager cm = (ColourManager) GameManager.get().getManager(ColourManager.class);
	Commander entity; //Commander to test with and chack stats.
	Inventory bag; //Instance of Inventory class to test with
	// making all the items to test with.
	Weapon wep1 = new Weapon(WeaponType.WEAPON1);
	Weapon wep2 = new Weapon(WeaponType.WEAPON2);
	Armour arm1 = new Armour(ArmourType.ARMOUR1);
	Armour arm2 = new Armour(ArmourType.BOOTS1);
	Special heal1 = new Special(SpecialType.AOEHEAL1);
	Special heal2 = new Special(SpecialType.AOEHEAL2);
	Special boomHeal = new Special(SpecialType.MASS1HEAL);
	Special bomb = new Special(SpecialType.BOMB);
	Special nuke = new Special(SpecialType.NUKE);
	//The base stats of the Commander before item's change the stats.
	int baseArm;
	int baseHP;
	int baseMaxHP;
	int baseMaxArmour;
	int baseAttackRange;
	int baseArmDMG;
	int baseDMG;
	int baseAttackSPD;
	
	/**
	 * Setting up the Commander with Inventory to test Inventory class capabilities
	 */
	@BeforeClass
	public void setup() {
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

	
	@Test
	public void testAddToInventory() {
		bag.addToInventory(wep1);
		assertTrue(bag.getWeapon().equals(wep1));
		
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
