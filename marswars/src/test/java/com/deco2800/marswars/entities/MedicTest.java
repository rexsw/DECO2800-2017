package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Medic;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author haoxuan on 23/09/17
 *
 */

public class MedicTest {
    @Test
    public void constructorTest() {
        Medic medic = new Medic(1, 0, 0 , 1);
        Assert.assertTrue(medic != null);
    }
    
    /**
     * Test if the medic can identify different owners
     */
    @Test 
    public void testSameOwner() {
    	Medic medic = new Medic(1, 0, 0, 1);
    	Medic medic1 = new Medic(1, 0, 0, 1);
    	Medic medic2 = new Medic(1, 0, 0, 2);
    	Assert.assertTrue(medic.setTargetType(medic1));
    	Assert.assertFalse(medic.setTargetType(medic2));
    }

}
