package com.deco2800.marswars.entities.items;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.deco2800.marswars.entities.items.effects.HealthEffect;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

/**
 * Class to test the Special items class general methods. Also tests the effects of  the BOMB item. 
 * Only testing on Commander because the applyEffect methods so far are only enabled for Commander for testing purposes.
 * When the limitation is taken out, this test would still work because AttackableEntity encapsulates Commander.
 * Created by Nick on 22/9/17.
 */
public class SpecialTest {
	private static BaseWorld baseWorld;
	private Commander com; //Tank to test HealthEffect.
	private int baseHP; //store the starting health of the commander each time.
	Special bomb;
	
	/**
	 * Set up the base world.
	 */
	@BeforeClass
	public static void setup() {
		baseWorld = new BaseWorld(1000 ,1500);
    	GameManager.get().setWorld(new BaseWorld(1000, 1500));
	}
	
	/**
	 * Make new bomb items and commanders for each test. 
	 */
	@Before
	public void prepare() {
		bomb = new Special(SpecialType.BOMB);
		com = new Commander(0, 0, 0, 1);
		baseHP = com.getHealth();
	}
	
	/**
	 * Test the constructor
	 */
    @Test
    public void constructorTest () {
        Assert.assertTrue(bomb != null);
    }
    
    public void enumTest() {
    	Assert.assertTrue(SpecialType.BOMB == bomb.getEnum());
    }

    /**
     * test the get method for determining the last use of the item for both when the item is up to it's last use and 
     * when it isn't
     */
    @Test
    public void getUse () {
        Assert.assertEquals(false, bomb.useItem());
        Special aoeHeal = new Special(SpecialType.REGEN_SHOT);
        Assert.assertEquals(true, aoeHeal.useItem());
    }
    

    /**
     * test the get method for the Item type enumerate value of the item (not the one to make it).
     */
    @Test
    public void getItemType () {
        Assert.assertEquals("SPECIAL", bomb.getItemType().name());
    }
    
    /**
     * test the get method for the description of the bomb
     */
    @Test
    public void getDescription () {
        String testString = "Bomb\n" +
                "Damage: 400\n";
        Assert.assertEquals(testString, bomb.getDescription());
    }
    
    /**
     * Tests whether HealthEffect can change the Commander's health when used. Also checks the typical damage case and 
     * the over heal (heal more than max HP) case.
     */
    @Test
    public void useEffect() {
    	//should normally run useItem method, but for testing it is not needed here.
    	HealthEffect bombEff = (HealthEffect) bomb.getEffect().get(0);
    	bombEff.applyEffect(com);
    	
    	Assert.assertTrue(com.getHealth() == baseHP - 400);
    	Special heal = new Special(SpecialType.MASS1HEAL);
    	heal.getEffect().get(0).applyEffect(com);
    	Assert.assertTrue(com.getHealth() == baseHP);
    }
}
