package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 * Modified by Zeid on 22/10/17
 */
public class ArmourTest {

    Armour helmet1;
    Armour helmet2;
    Armour helmet3;
    Armour goggle1;
    Armour goggle2;
    Armour goggle3;

    @Before
    public void setUp() {
        helmet1 = new Armour(ArmourType.HELMET, 1);
        helmet2 = new Armour(ArmourType.HELMET, 2);
        helmet3 = new Armour(ArmourType.HELMET, 3);
        goggle1 = new Armour(ArmourType.GOGGLE, 1);
        goggle2 = new Armour(ArmourType.GOGGLE, 2);
        goggle3 = new Armour(ArmourType.GOGGLE, 3);
    }


    @Test
    public void constructorTest () {new Armour(ArmourType.HELMET, 1);
        Assert.assertTrue(helmet1 != null);
        Assert.assertTrue(helmet2 != null);
        Assert.assertTrue(helmet3 != null);
        Assert.assertTrue(goggle1 != null);
        Assert.assertTrue(goggle2 != null);
        Assert.assertTrue(goggle3 != null);

    }

    @Test
    public void getArmourValue () {
        Assert.assertTrue(helmet1 != null);
        Assert.assertEquals(1000, helmet1.getArmourValue());
        Assert.assertTrue(helmet2 != null);
        Assert.assertEquals(1200, helmet2.getArmourValue());
        Assert.assertTrue(helmet3 != null);
        Assert.assertEquals(1400, helmet3.getArmourValue());
        Assert.assertTrue(goggle1 != null);
        Assert.assertEquals(3000, goggle1.getArmourValue());
        Assert.assertTrue(goggle2 != null);
        Assert.assertEquals(3600, goggle2.getArmourValue());
        Assert.assertTrue(goggle3 != null);
        Assert.assertEquals(4200, goggle3.getArmourValue());
    }

    @Test
    public void getArmourHealth () {
        Assert.assertEquals(500, helmet1.getArmourHealth());
        Assert.assertEquals(600, helmet2.getArmourHealth());
        Assert.assertEquals(700, helmet3.getArmourHealth());
        Assert.assertEquals(1500, goggle1.getArmourHealth());
        Assert.assertEquals(1800, goggle2.getArmourHealth());
        Assert.assertEquals(2100, goggle3.getArmourHealth());

    }

    @Test
    public void getArmourMoveSpeed () {
        Assert.assertTrue(Math.abs(helmet1.getMoveSpeed() - 0.05f)< 0.00001f);
        Assert.assertTrue(Math.abs(helmet2.getMoveSpeed() - 0.06f)< 0.00001f);
        Assert.assertTrue(Math.abs(helmet3.getMoveSpeed() - 0.07f)< 0.00001f);
        Assert.assertTrue(Math.abs(goggle1.getMoveSpeed() - 0.1f)< 0.00001f);
        Assert.assertTrue(Math.abs(goggle2.getMoveSpeed() - 0.12f)< 0.00001f);
        Assert.assertTrue(Math.abs(goggle3.getMoveSpeed() - 0.14f)< 0.00001f);
    }

    @Test
    public void getArmourType () {
        Assert.assertEquals("ARMOUR", helmet1.getItemType().toString());
        Assert.assertEquals("ARMOUR", helmet2.getItemType().toString());
        Assert.assertEquals("ARMOUR", helmet3.getItemType().toString());
        Assert.assertEquals("ARMOUR", goggle1.getItemType().toString());
        Assert.assertEquals("ARMOUR", goggle2.getItemType().toString());
        Assert.assertEquals("ARMOUR", goggle3.getItemType().toString());
    }

    @Test
    public void getName(){
        Assert.assertEquals("Combat Helmet", helmet1.getName());
        Assert.assertEquals("Combat Helmet", helmet2.getName());
        Assert.assertEquals("Combat Helmet", helmet3.getName());
        Assert.assertEquals("Tactical Goggle", goggle1.getName());
        Assert.assertEquals("Tactical Goggle", goggle2.getName());
        Assert.assertEquals("Tactical Goggle", goggle3.getName());
    }

    @Test
    public void getArmourDescription () {
        // description for base item only, as ShopDialog.getItemStats() handles different levels
        String test = "Combat Helmet\n" +
                "Armour: 1000\n" +
                "Health: 500\n" +
                "MovementSpeed: 0.05";
        Assert.assertEquals(test, helmet1.getDescription());
        String test2 = "Tactical Goggle\n" +
                "Armour: 3000\n" +
                "Health: 1500\n" +
                "MovementSpeed: 0.1";
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
        // for base items only, as ShopDialog handles displaying different item levels
        Assert.assertEquals("helmet_1", helmet1.getTexture().toString());
        Assert.assertEquals("goggle_1", goggle1.getTexture().toString());

    }

}
