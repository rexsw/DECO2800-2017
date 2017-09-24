package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class ArmourTest {
    @Test
    public void constructorTest () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertTrue(armour != null);
    }

    @Test
    public void getArmourValue () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertTrue(armour != null);
        Assert.assertEquals(15, armour.getArmourValue());
    }

    @Test
    public void getArmourHealth () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertEquals(30, armour.getArmourHealth());
    }

    @Test
    public void getArmourMoveSpeed () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertTrue(armour.getMoveSpeed() == 10.0);
    }
    @Test
    public void getArmourType () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertEquals("ARMOUR", armour.getItemType().toString());
    }

    @Test
    public void getArmourDescription () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        String test = "A1\n" +
                "Armour: 15\n" +
                "Health: 30\n" +
                "MovementSpeed: 10.0";
        Assert.assertEquals(test, armour.getDescription());
    }

    @Test
    public void getArmourTexture () {
        Armour armour = new Armour(ArmourType.ARMOUR1);
        Assert.assertEquals("defence_helmet", armour.getTexture().toString());
    }
}
