package com.deco2800.marswars;

import static org.junit.Assert.*;

import org.junit.Test;

import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;

public class GatheredResourceTest {
	@Test
	public void constructorTest() {
		GatheredResource gr = new GatheredResource(ResourceType.BIOMASS, 10);
		
		assertEquals(gr.getAmount(), 10);
		assertEquals(gr.getType(), ResourceType.BIOMASS);
		
		GatheredResource gr2 = new GatheredResource(ResourceType.ROCK, -10);
		assertEquals(gr2.getAmount(), 0);
		assertEquals(gr2.getType(), ResourceType.ROCK);
	}
}
