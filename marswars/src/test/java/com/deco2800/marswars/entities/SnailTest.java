package com.deco2800.marswars.entities;

import org.junit.Assert;
import com.deco2800.marswars.entities.units.Snail;

import org.junit.Test;

/**
 * @author haoxuan on 23/10/17
 */

public class SnailTest {


	@Test
	public void getStatTest() {
		Snail snail= new Snail(1, 0, 0, 1);
		Assert.assertTrue(snail.getStats() != null);
		Assert.assertEquals(100, snail.getStats().getHealth());
		Assert.assertEquals(100, snail.getStats().getMaxHealth());
		Assert.assertEquals("Ambient Animal", snail.getStats().getName());
	}

}
