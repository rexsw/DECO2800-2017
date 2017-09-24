package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Medic;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author xhy6006 on 23/09/17
 *
 */

public class MedicTest {
    @Test
    public void constructorTest() {
        Medic medic = new Medic(1, 0, 0 , 1);
        Assert.assertTrue(medic != null);
    }

}
