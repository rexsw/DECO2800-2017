package com.deco2800.marswars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.BaseWorld;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseEntityTest {	
	private BaseEntity t;
	private BaseEntity t1;
	private BaseEntity t2;
	
	@Before
	public void initialise() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		t = new BaseEntity(0, 1, 2, 3, 4, 5);
		t1 = new BaseEntity(0, 1, 2, 3, 4, 5, 1, 1, true);
		
		Box3D position = new Box3D(0f, 1f, 2f, 3f, 4f, 5f);
		t2 = new BaseEntity(position, 1, 1, false);
	}
	
	@Test
	public void ConstructorTest() {
		assertEquals(t.getPosX(), 0, 0.1);
		assertEquals(t.getPosY(), 1, 0.1);
		assertEquals(t.getPosZ(), 2, 0.1);
		assertEquals(t.getXLength(), 3, 0.1);
		assertEquals(t.getYLength(), 4, 0.1);
		assertEquals(t.getZLength(), 5, 0.1);
	}
	
	@Test
	public void ConstructorsTest() {
		assertEquals(t1.getPosX(), 0, 0.1);
		assertEquals(t1.getPosY(), 1, 0.1);
		assertEquals(t1.getPosZ(), 2, 0.1);
		assertEquals(t1.getXLength(), 3, 0.1);
		assertEquals(t1.getYLength(), 4, 0.1);
		assertEquals(t1.getZLength(), 5, 0.1);
		
		assertEquals(t2.getPosX(), 0, 0.1);
		assertEquals(t2.getPosY(), 1, 0.1);
		assertEquals(t2.getPosZ(), 2, 0.1);
		assertEquals(t2.getXLength(), 3, 0.1);
		assertEquals(t2.getYLength(), 4, 0.1);
		assertEquals(t2.getZLength(), 5, 0.1);
	}
	
	@Test
	public void FullBlownConstructorTest() {
		assertEquals(t.getPosX(), 0, 0.1);
		assertEquals(t.getPosY(), 1, 0.1);
		assertEquals(t.getPosZ(), 2, 0.1);
		assertEquals(t.getXLength(), 3, 0.1);
		assertEquals(t.getYLength(), 4, 0.1);
		assertEquals(t.getZLength(), 5, 0.1);
	}
	
	@Test
	public void SetPositionsTest() {
		t.setPosition(0, 0, 0);
		assertEquals(t.getPosX(), 0, 0.1);
		assertEquals(t.getPosY(), 0, 0.1);
		assertEquals(t.getPosZ(), 0, 0.1);
		
		t.setPosX(3);
		assertEquals(t.getPosX(), 3, 0.1);
		assertEquals(t.getPosY(), 0, 0.1);
		assertEquals(t.getPosZ(), 0, 0.1);
		
		t.setPosY(2);
		assertEquals(t.getPosX(), 3, 0.1);
		assertEquals(t.getPosY(), 2, 0.1);
		assertEquals(t.getPosZ(), 0, 0.1);
		
		t.setPosZ(1);
		assertEquals(t.getPosX(), 3, 0.1);
		assertEquals(t.getPosY(), 2, 0.1);
		assertEquals(t.getPosZ(), 1, 0.1);
	}
	
	@Test
	public void SetCostTest() {
		assertEquals(t.getCost(), 0);
		t.setCost(100);
		assertEquals(t.getCost(), 100);
	}
	
	@Test
	public void ColidableTest() {
		assertEquals(t.isCollidable(), true);
	}
	
	@Test
	public void SelectedTest() {
		assertEquals(t.isSelected(), false);
		t.makeSelected();
		assertEquals(t.isSelected(), true);
		t.deselect();
		assertEquals(t.isSelected(), false);
	}
	
	@Test
	public void EntityTypeTest() {
		assertEquals(t.getEntityType(), EntityType.NOT_SET);
		t.setEntityType(EntityType.HERO);
		assertEquals(t.getEntityType(), EntityType.HERO);
		t.setEntityType(EntityType.NOT_SET);
		assertEquals(t.getEntityType(), EntityType.NOT_SET);
	}
	
	@Test
	public void ActionsTest() {
		assertEquals(t.getValidActions(), null);	
		
		t.initActions();
		assertEquals(t.getValidActions(), new ArrayList<Class>());		 
	}
	
	@Test
	public void AddActionTest() {
		ArrayList<Class> expected = new ArrayList<>();
		expected.add(GatherAction.class);
		
		t.initActions();
		assertTrue(t.addNewAction(GatherAction.class));
		assertEquals(t.getValidActions(), expected);
		
		assertFalse(t.addNewAction(GatherAction.class));
		assertEquals(t.getValidActions(), expected);
		
		t.addNewAction(MoveAction.class);
		expected.add(MoveAction.class);
		assertEquals(t.getValidActions(), expected);
	}
	
	@Test
	public void RemoveActionTest() {
		t.initActions();
		t.addNewAction(GatherAction.class);
		t.addNewAction(MoveAction.class);
		assertTrue(t.removeActions(MoveAction.class));
		
		assertFalse(t.removeActions(MoveAction.class));
		
		ArrayList<Class> expected = new ArrayList<>();
		expected.add(GatherAction.class);	
	}
	
	@Test
	public void HelpTextTest() {
		MarsWars mockWar = Mockito.mock(MarsWars.class);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(mockWar, conf);
		Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
		String notSet = "This entity has not had its type set";
		String building = "This is a building";
		String unit = "This is a unit";
		String hero = "This is a hero";
		String resource = "This is a resource";
		
		t.setEntityType(EntityType.NOT_SET);
		assertEquals(t.getHelpText().getText().toString(), notSet);
		
		t.setEntityType(EntityType.BUILDING);
		assertEquals(t.getHelpText().getText().toString(), building);
		
		t.setEntityType(EntityType.UNIT);
		assertEquals(t.getHelpText().getText().toString(), unit);
		
		t.setEntityType(EntityType.HERO);
		assertEquals(t.getHelpText().getText().toString(), hero);
		
		t.setEntityType(EntityType.RESOURCE);
		assertEquals(t.getHelpText().getText().toString(), resource);
	}

	
	@Test
	/*
	 * The method currently doesn't make any sense, this test method will need update
	 * when the actual function get changed
	 */
	public void ButtonTest() { 
		assertEquals(t.getButton(), null);
		t.buttonWasPressed();
	}
}
