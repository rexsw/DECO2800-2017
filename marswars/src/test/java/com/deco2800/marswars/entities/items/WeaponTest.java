package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class WeaponTest {
    @Test
    public void ConstructorTest(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertTrue(weapon != null);
    }
    @Test
    public void getWeaponDamage(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals(100, weapon.getWeaponDamage());
    }
    @Test
    public void getWeaponRange(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals(5, weapon.getWeaponRange());
    }
    @Test
    public void getWeaponSpeed(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals(20, weapon.getWeaponSpeed());
    }
    @Test
    public void getItemType(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals("WEAPON", weapon.getItemType().toString());
    }
    @Test
    public void getName(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals("Hand Gun", weapon.getName());
    }
    @Test
    public void getDescription(){
        String test = "Hand Gun\n" +
                "Damage: 100\n" +
                "Speed: 20\n" +
                "Range: 5";
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals(test, weapon.getDescription());
    }
    @Test
    public void getTexture(){
        Weapon weapon = new Weapon(WeaponType.HANDGUN, 1);
        Assert.assertEquals("gun_1", weapon.getTexture().toString());
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
