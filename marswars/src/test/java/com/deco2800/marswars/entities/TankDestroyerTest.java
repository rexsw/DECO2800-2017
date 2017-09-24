package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.TankDestroyer;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author xhy6006 on 23/09/17
 *
 */


public class TankDestroyerTest {
	@Test
	public void constructorTest() {
		TankDestroyer tankdestroyer = new TankDestroyer(1, 1, 1, 1);
		Assert.assertTrue(tankdestroyer != null);
	}
	
	@Test
	public void getStatTest() {
		TankDestroyer tankdestroyer = new TankDestroyer(1, 1, 1, 1);
		Assert.assertTrue(tankdestroyer.getStats() != null);
		Assert.assertEquals(1000, tankdestroyer.getStats().getHealth());
		Assert.assertEquals(1000, tankdestroyer.getStats().getMaxHealth());
		Assert.assertEquals("TankDestroyer", tankdestroyer.getStats().getName());
	}

}
