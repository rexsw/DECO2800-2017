package com.deco2800.marswars;

import static org.junit.Assert.*;

import org.junit.Test;

import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.ResourceType;

public class GatheredResourceTest {
	@Test
	public void ConstructorTest() {
		GatheredResource gr = new GatheredResource(ResourceType.WATER, 10);
		
		assertEquals(gr.getAmount(), 10);
		assertEquals(gr.getType(), ResourceType.WATER);
	}
}
