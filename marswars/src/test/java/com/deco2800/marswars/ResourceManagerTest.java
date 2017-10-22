package com.deco2800.marswars;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class ResourceManagerTest {
	private static ResourceManager rm;
	private static HeadlessApplication game;
	
	@Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

	@BeforeClass
	public static void setup() {
		MarsWars mockWar = Mockito.mock(MarsWars.class);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		game = new HeadlessApplication(mockWar, conf);
		rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	}

	@Test
	public void rocksTest() {		
		rm.setRocks(100, 1);
		assertEquals(rm.getRocks(1), 100);
		rm.setRocks(80, 1);
		assertEquals(rm.getRocks(1), 80);
	}
	
	@Test
	public void biomassTest() {
		rm.setBiomass(100, 1);
		assertEquals(rm.getBiomass(1), 100);
		rm.setBiomass(80, 1);
		assertEquals(rm.getBiomass(1), 80);
	}
	
	@Test
	public void crystalTest() {	
		rm.setCrystal(100, 1);
		assertEquals(rm.getCrystal(1), 100);
		rm.setCrystal(80, 1);
		assertEquals(rm.getCrystal(1), 80);
	}

	@AfterClass
	public static void finish() {
		game.exit();
	}
}
