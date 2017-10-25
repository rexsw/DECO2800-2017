package com.deco2800.marswars.entities.items;

import com.deco2800.marswars.entities.items.effects.HealthEffect;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		bomb = new Special(SpecialType.NUKE);
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
    	Assert.assertTrue(SpecialType.MISSILE == bomb.getEnum());
    }

    /**
     * test the get method for determining the last use of the item for both when the item is up to it's last use and 
     * when it isn't
     */
    @Test
    public void getUse () {
        Assert.assertEquals(false, bomb.useItem());
        Special aoeHeal = new Special(SpecialType.HEALTHSHOT);
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
        String testString = "Nuke\n" +
                "Damage: 700\n";
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
    	Assert.assertTrue(com.getHealth() == baseHP - 700);
    	Special heal = new Special(SpecialType.HEALTHBLESS);
    	heal.getEffect().get(0).applyEffect(com);
    	Assert.assertTrue(com.getHealth() == baseHP);
    }
    
    /**
     * Test some aspects of the SpecialType that is been left over by other test cases
     */
    @Test
    public void specialTypeTest() {
    	// check radius and duration
    	SpecialType special = SpecialType.NUKE;
    	Assert.assertEquals(0, special.getRadius());
    	Assert.assertEquals(0, special.getDuration());
    	
    	//check cost
    	int[] cost = special.getCost();
    	Assert.assertTrue(cost[0]==200 && cost[1] == 100 && cost[2] == 200);
    	
    	//check texture
    	Assert.assertEquals(special.getTextureString(), "nuke");
    	
    	// check cost string
    	String test = "Rock: 200\n" +
        		"Crystal: 100\n" +
                "Biomass: 200\n";
        Assert.assertEquals(test, special.getCostString());
        
        //check description
        test = "Name: Nuke\n" +
        		"Type: Special\n" +
                "Damage: 700\n" +
                "Target: ENEMY TEAM";
        Assert.assertEquals(test, special.getDescription());
    }
}
