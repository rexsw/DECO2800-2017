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
    
    /**
     * test the get method for the description of the helmet
     */
    @Test
    public void getArmourTypeDescription () {
        ArmourType armour = ArmourType.HELMET;
        String test = "Name: Combat Helmet\n" +
        		"Type: Armour\n" +
                "Armour: 1000\n" +
                "MaxHealth: 500\n" +
                "Move Speed: 0.05";
        Assert.assertEquals(test, armour.getDescription());
    }
    
    /**
     * test the get method for the cost of the helmet
     */
    @Test
    public void getArmourTypeCost () {
        ArmourType armour = ArmourType.HELMET;
        int[] cost = armour.getCost();
        Assert.assertTrue(cost[0] == 80 && cost[1] == 30 && cost[2] == 100);
    }
    
    /**
     * test the get method string method for get the description of the helmet
     */
    @Test
    public void getArmourTypeCostDescription () {
        ArmourType armour = ArmourType.HELMET;
        String test = "Rock: 80\n" +
        		"Crystal: 30\n" +
                "Biomass: 100\n";
        Assert.assertEquals(test, armour.getCostString());
    }

    @Test
    public void getArmourTexture () {
        Armour armour = new Armour(ArmourType.HELMET, 1);
        Assert.assertEquals("helmet_1", armour.getTexture().toString());
    }

}
