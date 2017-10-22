package com.deco2800.marswars.entities;

import org.junit.Assert;
import com.deco2800.marswars.entities.units.Corn;

import org.junit.Test;

/**
 * @author haoxuan on 23/10/17
 */

public class CornTest {

	@Test
	public void getStatTest() {
		Corn corn= new Corn(0, 0, 0, 1);
		Assert.assertTrue(corn.getStats() != null);
		Assert.assertEquals(50, corn.getStats().getHealth());
		Assert.assertEquals(50, corn.getStats().getMaxHealth());
		Assert.assertEquals("Ambient Animal", corn.getStats().getName());
	}

}
