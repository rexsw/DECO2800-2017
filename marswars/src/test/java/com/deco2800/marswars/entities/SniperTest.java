package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Sniper;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author haoxuan on 23/09/17
 *
 */

public class SniperTest {
	@Test
	public void constructorTest() {
		Sniper sniper = new Sniper(2, 1, 1, 1);
		Assert.assertTrue(sniper != null);
	}
	
	@Test
	public void getStatTest() {
		Sniper sniper = new Sniper(2, 1, 1, 1);
		Assert.assertTrue(sniper.getStats() != null);
		Assert.assertEquals(500, sniper.getStats().getHealth());
		Assert.assertEquals(500, sniper.getStats().getMaxHealth());
		Assert.assertEquals("Sniper", sniper.getStats().getName());
	}


}
