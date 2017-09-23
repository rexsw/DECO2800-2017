package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class WeaponTest {
    @Test
    public void ConstructorTest(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertTrue(weapon != null);
    }
    @Test
    public void getWeaponDamage(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals(10, weapon.getWeaponDamage());
    }
    @Test
    public void getWeaponRange(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals(3, weapon.getWeaponRange());
    }
    @Test
    public void getWeaponSpeed(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals(90, weapon.getWeaponSpeed());
    }
    @Test
    public void getItemType(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals("WEAPON", weapon.getItemType().toString());
    }
    @Test
    public void getName(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals("W1", weapon.getName());
    }
    @Test
    public void getDescription(){
        String test = "W1\n" +
                "Damage: 10\n" +
                "Speed: 90\n" +
                "Range: 3";
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals(test, weapon.getDescription());
    }
    @Test
    public void getTexture(){
        Weapon weapon = new Weapon(WeaponType.WEAPON1);
        Assert.assertEquals("power_gloves", weapon.getTexture().toString());
    }
}
