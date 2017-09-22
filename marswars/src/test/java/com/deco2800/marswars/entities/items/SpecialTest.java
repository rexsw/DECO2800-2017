package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 22/9/17.
 */
public class SpecialTest {
    @Test
    public void constructorTest () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertTrue(bomb != null);
    }
    @Test
    public void getDuration () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals(0, bomb.getDuration());
    }
    @Test
    public void getTexture () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals("heal_needle", bomb.getTexture());
    }
    @Test
    public void getRadius () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals(5, bomb.getRadius());
    }

    @Test
    public void getUse () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals(false, bomb.useItem());
    }
    @Test
    public void getName () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals("Bomb", bomb.getName());
    }

    @Test
    public void getItemType () {
        Special bomb = new Special(SpecialType.BOMB);
        Assert.assertEquals("SPECIAL", bomb.getItemType().name());
    }
    @Test
    public void getDescription () {
        Special bomb = new Special(SpecialType.BOMB);
        //System.out.println(bomb.getDescription());
        String testString = "Bomb\n" +
                "Damage: 100\n";
        Assert.assertEquals(testString, bomb.getDescription());
    }
}
