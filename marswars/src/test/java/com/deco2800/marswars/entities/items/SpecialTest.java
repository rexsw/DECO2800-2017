package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * Class to test the Special items class general methods. Also tests the effects of  the BOMB item. 
 * Created by Nick on 22/9/17.
 */
public class SpecialTest {
	private static BaseWorld baseWorld;
	private Commander com; //Tank to test HealthEffect.
	private int baseHP;
	Special bomb;
	
	@BeforeClass
	public static void setup() {
		baseWorld = new BaseWorld(1000 ,1500);
    	GameManager.get().setWorld(new BaseWorld(1000, 1500));
	}
	
	@Before
	public void prepare() {
		bomb = new Special(SpecialType.BOMB);
		com = new Commander(0, 0, 0, 1);
		baseHP = com.getHealth();
	}
	
    @Test
    public void constructorTest () {
        Assert.assertTrue(bomb != null);
    }
    @Test
    public void getDuration () {
        Assert.assertEquals(0, bomb.getDuration());
    }
    @Test
    public void getTexture () {
        Assert.assertEquals("boot", bomb.getTexture());
    }
    @Test
    public void getRadius () {
        Assert.assertEquals(5, bomb.getRadius());
    }

    @Test
    public void getUse () {
        Assert.assertEquals(false, bomb.useItem());
    }
    @Test
    public void getName () {
        Assert.assertEquals("Bomb", bomb.getName());
    }

    @Test
    public void getItemType () {
        Assert.assertEquals("SPECIAL", bomb.getItemType().name());
    }
    @Test
    public void getDescription () {
        //System.out.println(bomb.getDescription());
        String testString = "Bomb\n" +
                "Damage: 100\n";
        Assert.assertEquals(testString, bomb.getDescription());
    }
    
    /**
     * Tests whether HealthEffect can change the Commander's health when used. Also checks the typical damage case and 
     * the over heal (heal more than max HP) case.
     */
    @Test
    public void useEffect() {
    	//should normally run useItem method, but for testing it is not needed here.
    	bomb.getEffect().get(0).applyEffect(com);
    	Assert.assertTrue(com.getHealth() == baseHP - 100);
    	Special heal = new Special(SpecialType.MASS1HEAL);
    	heal.getEffect().get(0).applyEffect(com);
    	Assert.assertTrue(com.getHealth() == baseHP);
    }
}
