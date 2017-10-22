package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 * Modified by Zeid on 22/10/17
 */
public class WeaponTest {
    Weapon handgun1;
    Weapon handgun2;
    Weapon handgun3;
    Weapon rifle1;
    Weapon rifle2;
    Weapon rifle3;

    @Before
    public void setUp() {
        handgun1 = new Weapon(WeaponType.HANDGUN, 1);
        handgun2 = new Weapon(WeaponType.HANDGUN, 2);
        handgun3 = new Weapon(WeaponType.HANDGUN, 3);
        rifle1 = new Weapon(WeaponType.RIFLE, 1);
        rifle2 = new Weapon(WeaponType.RIFLE, 2);
        rifle3 = new Weapon(WeaponType.RIFLE, 3);

    }

    @Test
    public void ConstructorTest(){
        Assert.assertTrue(handgun1 != null);
        Assert.assertTrue(handgun2 != null);
        Assert.assertTrue(handgun3 != null);
        Assert.assertTrue(rifle1 != null);
        Assert.assertTrue(rifle2 != null);
        Assert.assertTrue(rifle3 != null);

    }
    @Test
    public void getWeaponDamage(){
        Assert.assertEquals(100, handgun1.getWeaponDamage());
        Assert.assertEquals(120, handgun2.getWeaponDamage());
        Assert.assertEquals(140, handgun3.getWeaponDamage());
        Assert.assertEquals(300, rifle1.getWeaponDamage());
        Assert.assertEquals(360, rifle2.getWeaponDamage());
        Assert.assertEquals(420, rifle3.getWeaponDamage());
    }

    @Test
    public void getWeaponRange(){
        Assert.assertEquals(5, handgun1.getWeaponRange());
        Assert.assertEquals(6, handgun2.getWeaponRange());
        Assert.assertEquals(7, handgun3.getWeaponRange());
        Assert.assertEquals(10, rifle1.getWeaponRange());
        Assert.assertEquals(12, rifle2.getWeaponRange());
        Assert.assertEquals(14, rifle3.getWeaponRange());
    }

    @Test
    public void getWeaponSpeed(){
        Assert.assertEquals(20, handgun1.getWeaponSpeed());
        Assert.assertEquals(24, handgun2.getWeaponSpeed());
        Assert.assertEquals(28, handgun3.getWeaponSpeed());
        Assert.assertEquals(50, rifle1.getWeaponSpeed());
        Assert.assertEquals(60, rifle2.getWeaponSpeed());
        Assert.assertEquals(70, rifle3.getWeaponSpeed());
    }

    @Test
    public void getItemType(){
        Assert.assertEquals("WEAPON", handgun1.getItemType().toString());
        Assert.assertEquals("WEAPON", handgun2.getItemType().toString());
        Assert.assertEquals("WEAPON", handgun3.getItemType().toString());
        Assert.assertEquals("WEAPON", rifle1.getItemType().toString());
        Assert.assertEquals("WEAPON", rifle2.getItemType().toString());
        Assert.assertEquals("WEAPON", rifle3.getItemType().toString());
    }

    @Test
    public void getName(){
        Assert.assertEquals("Hand Gun", handgun1.getName());
        Assert.assertEquals("Hand Gun", handgun2.getName());
        Assert.assertEquals("Hand Gun", handgun3.getName());
        Assert.assertEquals("Rifle", rifle1.getName());
        Assert.assertEquals("Rifle", rifle2.getName());
        Assert.assertEquals("Rifle", rifle3.getName());
    }

    @Test
    public void getDescription(){
        // description for base item only, as ShopDialog.getItemStats() handles different levels
        String test = "Hand Gun\n" +
                "Damage: 100\n" +
                "Speed: 20\n" +
                "Range: 5";
        Assert.assertEquals(test, handgun1.getDescription());
        String test2 = "Rifle\n" +
                "Damage: 300\n" +
                "Speed: 50\n" +
                "Range: 10";
        Assert.assertEquals(test2, rifle1.getDescription());
    }

    @Test
    public void getTexture(){
        // for base items only, as ShopDialog handles displaying different item levels
        Assert.assertEquals("gun_1", handgun1.getTexture().toString());
        Assert.assertEquals("rifle_1", rifle1.getTexture().toString());

    }
    
    /**
     * Test some aspects of the WeaponType that is been left over by other test cases
     */
    @Test
    public void weaponTypeTest() {
    	WeaponType weapon = WeaponType.RIFLE;
    	// test cost
    	int[] cost = weapon.getCost();
    	Assert.assertTrue(cost[0] == 400 && cost[1] == 300 && cost[2] == 300);
    	
    	//test cost string
    	String test = "Rock: 400\n" +
        		"Crystal: 300\n" +
                "Biomass: 300\n";
        Assert.assertEquals(test, weapon.getCostString());
        
        //test get description
        test = "Name: Rifle\n" +
        		"Type: Weapon\n" +
                "Damage: 300\n" +
        		"Speed: 50\n" +
                "Range: 10";
        Assert.assertEquals(test, weapon.getDescription());
    }
}
