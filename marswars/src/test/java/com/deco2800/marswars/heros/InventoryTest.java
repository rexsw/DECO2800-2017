package com.deco2800.marswars.heros;

import com.deco2800.marswars.entities.Inventory;
import com.deco2800.marswars.entities.items.*;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InventoryTest {
	//Necessary instances to be made/defined.
	private static BaseWorld baseWorld;
	private static Commander entity; //Commander to test with and check stats.
	private static Inventory bag; //Instance of Inventory class to test with
	// making all the items to test with.
	Weapon wep1 = new Weapon(WeaponType.HANDGUN, 1);
	Weapon wep1b = new Weapon(WeaponType.HANDGUN, 1);
	Weapon wep2 = new Weapon(WeaponType.RIFLE, 1);
	Armour arm1 = new Armour(ArmourType.HELMET, 1);
	Armour arm11 = new Armour(ArmourType.HELMET, 1);
	Armour arm2 = new Armour(ArmourType.GOGGLE, 1);
	Special heal1 = new Special(SpecialType.HEALTHSHOT);
	Special heal2 = new Special(SpecialType.HEALTHSTATION);
	Special boomHeal = new Special(SpecialType.HEALTHBLESS);
	Special bomb = new Special(SpecialType.MISSILE);
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
	private static float baseSPD;
	
	/**
	 * private helper method to revert the Commander's stats to what there were initially
	 */
	private void refresh() {
		entity.setMaxArmor(baseMaxArmour);
		entity.setArmor(baseArm);
		entity.setHealth(baseHP);
		entity.setMaxHealth(baseMaxHP);
		entity.setSpeed(baseSPD);
		entity.setDamage(baseDMG);
		entity.setAttackRange(baseAttackRange);
		entity.setArmorDamage(baseArmDMG);
		entity.setAttackSpeed(baseAttackSPD);
	}
	
	/**
	 * Private helper method to check the changes in the commander's offensive stats with the proved expected offensive 
	 * stats.
	 * @param dmg  expected damage stat
	 * @param armdmg  expected armour damage stat
	 * @param atkspd  expected attack speed stat
	 * @param atkrng  expected attack rang stat
	 */
	private void checkOffense(int dmg, int armdmg, int atkspd, int atkrng) {
		assertTrue(dmg == entity.getDamageDeal());
		assertTrue(armdmg == entity.getArmorDamage());
		assertTrue(atkspd == entity.getAttackSpeed());
		assertTrue(atkrng == entity.getAttackRange());
	}
	
	
	/**
	 * Private helper method to check that the Commander has the provided defense stats.
	 * @param arm  the expected current armour.
	 * @param maxArm  the expected max armour stat
	 * @param hp  the expected health stat
	 * @param maxHP  the expected max health stat
	 * @param spd  the expected speed stat
	 */
	private void checkDefence(int arm, int maxArm, int hp, int maxHP, float spd) {
		assertTrue(arm == entity.getArmor());
		assertTrue(maxArm == entity.getMaxArmor());
		assertTrue(hp == entity.getHealth());
		assertTrue(maxHP == entity.getMaxHealth());
		assertTrue(spd == entity.getSpeed());
	}
	
	/**
	 * Setting up the Commander with Inventory to test Inventory class capabilities
	 */
	@BeforeClass
	public static void setup() {
		baseWorld = new BaseWorld(100 ,150);
    	GameManager.get().setWorld(new BaseWorld(100, 150));
        // get the starting base stats of the Commander
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
        baseSPD = entity.getSpeed();
	}
	
	/**
	 * Test if the Inventory created in the Commander has no items in the beginning.
	 */
	@Test
	public void testInitialInventory() {
		assertTrue(bag.getArmour() == null);
		assertTrue(bag.getWeapon() == null);
		assertTrue(bag.getSpecials().size() == 0);
		assertFalse(bag.removeFromInventory(wep1));
		assertFalse(bag.removeFromInventory(arm1));
	}
	
	/**
	 * tests the overriden equals method so that Weapon and Armour classes are compared by teir respective enumerate 
	 * values used to create them.
	 */
	@Test
	public void testEquality() {
		assertTrue(wep1.equals(wep1b));
		assertTrue(arm1.equals(arm11));
		assertFalse(wep1.equals(wep2));
		assertFalse(arm1.equals(arm2));
		assertFalse(wep1.equals(new Integer(0)));
		assertFalse(arm1.equals(new Integer(12)));
	}

	/**
	 * Test to check if it is possible to add a weapon to the Commadner's inventory the stat changes of the weapon are 
	 * correct. Then checks if adding different weapons to an Inventory already with a weapon replaces the weapon
	 * in the Commander's inventory and would check that its stats change accordingly. Test then further checks that the
	 * stats are appropriately changed when the item(s) are removed. Checks equality between different instances of the
	 * same weapon class defined by the same WeaponType enumerate value.
	 */
	@Test
	public void testAddDiffWeaponToInventory() {
		//adding 1 item
		assertTrue(bag.addToInventory(wep1));
		assertTrue(bag.getWeapon().equals(wep1));
		checkOffense(baseDMG + wep1.getWeaponDamage(), (int) (baseArmDMG  + wep1.getWeaponDamage() * 0.25), 
				baseAttackSPD + wep1.getWeaponSpeed(), baseAttackRange + wep1.getWeaponRange());
		//adding same instance of last item.
		assertTrue(bag.addToInventory(wep1));
		checkOffense(baseDMG + WeaponType.HANDGUN.getWeaponDamage(), 
				(int) (baseArmDMG  + WeaponType.HANDGUN.getWeaponDamage() * 0.25), 
				baseAttackSPD + WeaponType.HANDGUN.getWeaponSpeed(), 
				baseAttackRange + WeaponType.HANDGUN.getWeaponRange());
		//adding new different of same item.
		assertTrue(bag.addToInventory(wep2));
		checkOffense(baseDMG + WeaponType.RIFLE.getWeaponDamage(), 
				(int) (baseArmDMG  + WeaponType.RIFLE.getWeaponDamage() * 0.25), 
				baseAttackSPD + WeaponType.RIFLE.getWeaponSpeed(), 
				baseAttackRange + WeaponType.RIFLE.getWeaponRange());
		//remove item from bag so other tests start off with empty bag also test removal independence from parameter
		assertFalse(bag.removeFromInventory(wep1));
		assertTrue(bag.removeFromInventory(wep2));
		assertTrue(bag.getWeapon() == null);
	}

	
	
	/**
	 * Test to check armour items get replaced if there already is an armour item in the inventory. Also checks if the 
	 * stats are applied properly for items when the Commander is at max health and max armour.
	 */
	public void testAddDiffArmourToInventory() {
		//adding 1 item
		assertTrue(bag.addToInventory(arm1));
		assertTrue(bag.getArmour().equals(arm1));
		assertTrue(!bag.getArmour().equals(arm2));
		checkDefence(baseArm + arm1.getArmourValue(), baseMaxArmour + arm1.getArmourValue(), 
				baseHP + arm1.getArmourHealth(), baseMaxHP + arm1.getArmourHealth(), baseSPD + arm1.getMoveSpeed());
		//adding same instance of last item.
		assertTrue(bag.addToInventory(arm2));
		assertTrue(bag.getArmour().equals(arm2));
		checkDefence(baseArm + arm1.getArmourValue(), baseMaxArmour + arm1.getArmourValue(), 
				baseHP + arm1.getArmourHealth(), baseMaxHP + arm1.getArmourHealth(), baseSPD + arm1.getMoveSpeed());
		//remove item from bag so other tests start off with empty bag also test removal independence from parameter
		assertTrue(bag.removeFromInventory(wep1));
		assertTrue(bag.getWeapon() == null);
	}
	
	
	/**
	 * Test defensive stats when the Commander's stats have been lowered such that removing the item would remove more
	 * of the stat that they currently have. Should be treated with the default values provided.
	 */
	@Test
	public void testReducedDefenceStatRemoveArmour() {
		assertTrue(bag.addToInventory(arm2));
		entity.setMaxArmor(10);
		entity.setArmor(10);
		entity.setHealth(10);
		entity.setMaxHealth(10);
		entity.setSpeed(0.001f);
		assertTrue(bag.removeFromInventory(arm2));
		checkDefence(1, 1, 1, 1, 0.01f);
		refresh();
	}
	
	/**
	 * Test offensive stats when the Commander's stats have been lowered such that removing the item would remove more
	 * of the stat that they currently have. Should be treated with the default values provided.
	 */
	@Test
	public void testReducedOffenceStatRemoveArmour() {
		assertTrue(bag.addToInventory(wep2));
		entity.setDamage(10);
		entity.setArmorDamage(10);
		entity.setAttackRange(2);
		entity.setAttackSpeed(4);
		assertTrue(bag.removeFromInventory(wep2));
		checkOffense(1, 1, 1, 1);
		refresh();
	}
	
	/**
	 * Tests that the maximum capacity for Special items is indeed 4 and that the 5th item that is attempted to be added
	 * did in fact not get added. Also tests typical case of removing items from the inventory.
	 */
	@Test
	public void testAddMaxSpecialItems() {
		assertTrue(bag.addToInventory(heal1));
		assertTrue(bag.addToInventory(heal2));
		assertTrue(bag.addToInventory(boomHeal));
		assertTrue(bag.addToInventory(bomb));
		assertFalse(bag.addToInventory(nuke));
		List<Special> specs = bag.getSpecials();
		assertFalse(specs.contains(nuke));
		assertTrue(bag.removeFromInventory(heal2));
		assertTrue(bag.removeFromInventory(heal1));
		assertTrue(bag.removeFromInventory(boomHeal));
		assertTrue(bag.removeFromInventory(bomb));
	}
	
	/**
	 * Testing if adding a Special item created from the same SpecialType enumerate value would be added into another
	 * slot and whether if they can be removed separately.
	 */
	public void testAddSameSpecialItems() {
		Special heal1b = new Special(SpecialType.HEALTHSHOT);
		assertTrue(bag.addToInventory(heal2));
		assertTrue(bag.addToInventory(heal1));
		assertTrue(bag.addToInventory(heal1b));
		assertTrue(bag.removeFromInventory(heal1b));
		assertTrue(bag.removeFromInventory(heal2));
		assertTrue(bag.removeFromInventory(heal1));	
	}
}
