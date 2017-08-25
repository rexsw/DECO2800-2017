package com.deco2800.marswars;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

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
	public void RocksTest() {		
		rm.setRocks(100);
		assertEquals(rm.getRocks(), 100);
		rm.setRocks(80);
		assertEquals(rm.getRocks(), 80);
	}
	
	@Test
	public void BiomassTest() {
		rm.setBiomass(100);
		assertEquals(rm.getBiomass(), 100);
		rm.setBiomass(80);
		assertEquals(rm.getBiomass(), 80);
	}
	
	@Test
	public void WaterTest() {
		rm.setWater(100);
		assertEquals(rm.getWater(), 100);
		rm.setWater(80);
		assertEquals(rm.getWater(), 80);
	}
	
	@Test
	public void CrystalTest() {	
		rm.setCrystal(100);
		assertEquals(rm.getCrystal(), 100);
		rm.setCrystal(80);
		assertEquals(rm.getCrystal(), 80);
	}

	@AfterClass
	public static void finish() {
		game.exit();
	}
}
