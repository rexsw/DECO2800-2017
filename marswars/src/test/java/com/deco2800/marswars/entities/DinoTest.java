package com.deco2800.marswars.entities;

import org.junit.Assert;
import com.deco2800.marswars.entities.units.Dino;

import org.junit.Test;

/**
 * @author haoxuan on 23/10/17
 */

public class DinoTest {

	@Test
	public void getStatTest() {
		Dino dino= new Dino(1, 0, 0, 0);
		Assert.assertTrue(dino.getStats() != null);
		Assert.assertEquals(300, dino.getStats().getHealth());
		Assert.assertEquals(300, dino.getStats().getMaxHealth());
		Assert.assertEquals("Ambient Animal", dino.getStats().getName());
	}
}
