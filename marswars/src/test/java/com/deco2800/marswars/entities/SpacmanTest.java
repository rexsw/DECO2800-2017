package com.deco2800.marswars.entities;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.BaseWorld;

public class SpacmanTest {
	
	@Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Test
	public void constructorTest() {
		Spacman man = new Spacman(1,1,1);
		assertEquals(man.getHealth(), 100);
		assertEquals(man.getOwner(), null);
		assertEquals(man.checkBackpack(), false);
		assertFalse(man.isWorking());
	}
	
	@Test
	public void setPositionTest() {
		Spacman man = new Spacman(1,1,1);
		man.setPosition(0, 0, 0);
		
		man.setPosX(3);
		man.setPosY(4);
		man.setPosZ(5);
	}
	
	@Test
	public void backPackTest() {
		Spacman man = new Spacman(1,1,1);
		man.addGatheredResource(new GatheredResource(ResourceType.ROCK, 10));
		
		assertEquals(man.checkBackpack(), true);
		
		GatheredResource removed = man.removeGatheredResource();
		assertEquals(removed.getType(), ResourceType.ROCK);
		assertEquals(removed.getAmount(), 10);
		assertEquals(man.checkBackpack(), false);
	}
	
	@Test
	public void healthTest() {
		Spacman man = new Spacman(1,1,1);
		GameManager.get().setWorld(new BaseWorld(10,10));
		GameManager.get().getWorld().addEntity(man);
		
		man.setHealth(5);	
		assertEquals(man.getHealth(), 5);
		
		man.setHealth(-1);
		assertFalse(GameManager.get().getWorld().getEntities().contains(man));
	}
	
	@Test
	public void ownerTest() {
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
	public void actionTest() {
		Spacman man = new Spacman(1,1,1);
		DecoAction action = Mockito.mock(DecoAction.class);
		man.setAction(action);
		
		assertTrue(man.isWorking());
	}
	
	@Test
	public void onClickTest() {
		Spacman man = new Spacman(1,1,1);
		PlayerManager mockManager = Mockito.mock(PlayerManager.class);
		man.setOwner(mockManager);
		MouseHandler mockHandler = Mockito.mock(MouseHandler.class);
		//man.onClick(mockHandler); Can't run this line mockito doesn't like Gamemanager trying to get tile dimensions
		assertEquals(man.getTexture(), "spacman_green");
	}
}
