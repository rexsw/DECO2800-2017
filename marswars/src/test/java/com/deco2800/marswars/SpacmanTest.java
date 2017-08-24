package com.deco2800.marswars;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.GatheredResource;
import com.deco2800.marswars.entities.LineOfSight;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.entities.ResourceType;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class SpacmanTest {
private static HeadlessApplication game;
	
	@Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void ConstructorTest() {
		Spacman man = new Spacman(1,1,1);
		assertEquals(man.getHealth(), 100);
		assertEquals(man.getOwner(), null);
		assertEquals(man.checkBackpack(), false);
		assertFalse(man.isWorking());
		assertEquals(man.getLineOfSight(), new LineOfSight(1, 1, 1, 1, 1));
	}
	
	@Test
	public void SetPositionTest() {
		Spacman man = new Spacman(1,1,1);
		man.setPosition(0, 0, 0);
		
		man.setPosX(3);
		man.setPosY(4);
		man.setPosZ(5);
	}
	
	@Test
	public void BackPackTest() {
		Spacman man = new Spacman(1,1,1);
		man.addGatheredResource(new GatheredResource(ResourceType.ROCK, 10));
		
		assertEquals(man.checkBackpack(), true);
		
		GatheredResource removed = man.removeGatheredResource();
		assertEquals(removed.getType(), ResourceType.ROCK);
		assertEquals(removed.getAmount(), 10);
		assertEquals(man.checkBackpack(), false);
	}
	
	@Test
	public void HealthTest() {
		Spacman man = new Spacman(1,1,1);
		GameManager.get().setWorld(new BaseWorld(10,10));
		GameManager.get().getWorld().addEntity(man);
		
		man.setHealth(5);	
		assertEquals(man.getHealth(), 5);
		
		man.setHealth(-1);
		assertFalse(GameManager.get().getWorld().getEntities().contains(man));
	}
	
	@Test
	public void OwnerTest() {
		Spacman man = new Spacman(1,1,1);
		Spacman man2 = new Spacman(1,1,1);
		Spacman man3 = new Spacman(1,1,1);
		Resource mockResource = Mockito.mock(Resource.class);
		Manager mockManager = Mockito.mock(Manager.class);
		Manager mockManager2 = Mockito.mock(Manager.class);
		man.setOwner(mockManager);
		man2.setOwner(mockManager2);
		man3.setOwner(mockManager);
		
		assertEquals(man.getOwner(), mockManager);
		assertFalse(man.sameOwner(man2));
		assertTrue(man.sameOwner(man3));
		assertFalse(man.sameOwner(mockResource));
	}
	
	@Test
	public void ActionTest() {
		Spacman man = new Spacman(1,1,1);
		DecoAction action = Mockito.mock(DecoAction.class);
		man.setAction(action);
		
		assertTrue(man.isWorking());
	}
	
	@Test
	public void OnClickTest() {
		Spacman man = new Spacman(1,1,1);
		MouseHandler mockHandler = Mockito.mock(MouseHandler.class);
		PlayerManager mockManager = Mockito.mock(PlayerManager.class);
		man.setOwner(mockManager);
		man.onClick(mockHandler);
		
		assertEquals(man.getTexture(), "spacman_blue");
		
	}
}
