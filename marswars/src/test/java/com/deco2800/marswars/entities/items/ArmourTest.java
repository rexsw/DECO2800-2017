package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class ArmourTest {
    @Test
    public void constructorTest () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertTrue(armour != null);
    }

    @Test
    public void getArmourValue () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertTrue(armour != null);
        Assert.assertEquals(1000, armour.getArmourValue());
    }

    @Test
    public void getArmourHealth () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertEquals(500, armour.getArmourHealth());
    }

    @Test
    public void getArmourMoveSpeed () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertTrue(Math.abs(armour.getMoveSpeed() - 0.05f)< 0.00001f);
    }
    @Test
    public void getArmourType () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertEquals("ARMOUR", armour.getItemType().toString());
    }

    @Test
    public void getArmourDescription () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        String test = "Combat Helmet\n" +
                "Armour: 1000\n" +
                "Health: 500\n" +
                "MovementSpeed: 0.05";
        Assert.assertEquals(test, armour.getDescription());
    }

    @Test
    public void getArmourTexture () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertEquals("helmet_1", armour.getTexture().toString());
    }
}
