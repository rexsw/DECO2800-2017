package com.deco2800.marswars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Tests for BaseEntity class.
 * 
 * NOTE: 
 * There are also tests for the ActionSetter class at the very bottom because it was convenient to put them in
 * this class (BaseEntities already set up here).
 * 
 * @author Michael
 *
 */
public class BaseEntityTest {
	private BaseEntity t;
	private BaseEntity t3;
	private BaseEntity t1;
	private BaseEntity t2;

	@Before
	public void initialise() {
		GameManager.get().setWorld(new BaseWorld(20, 20));
		t = new BaseEntity(3, 2, 2, 3, 4, 5);
		t3 = new BaseEntity(7, 8, 2, 1, 1, 1);
		t1 = new BaseEntity(new Box3D(0, 1, 2, 3, 4, 5), 1, 1, true);
		t2 = new BaseEntity(new Box3D(0f, 1f, 2f, 3f, 4f, 5f), 1, 1, false);
	}

	@Test
	public void constructorTest() {
		assertEquals(t.getPosX(), 3, 0.1);
		assertEquals(t.getPosY(), 2, 0.1);
		assertEquals(t.getPosZ(), 2, 0.1);
		assertEquals(t.getXLength(), 3, 0.1);
		assertEquals(t.getYLength(), 4, 0.1);
		assertEquals(t.getZLength(), 5, 0.1);
	}

	@Test
	public void constructorsTest() {
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
	public void fullBlownConstructorTest() {
		assertEquals(t1.getPosX(), 0, 0.1);
		assertEquals(t1.getPosY(), 1, 0.1);
		assertEquals(t1.getPosZ(), 2, 0.1);
		assertEquals(t1.getXLength(), 3, 0.1);
		assertEquals(t1.getYLength(), 4, 0.1);
		assertEquals(t1.getZLength(), 5, 0.1);
	}

	@Test
	public void setPositionsTest() {
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
	public void setCostTest() {
		assertEquals(t.getCost(), 0);
		t.setCost(100);
		assertEquals(t.getCost(), 100);
	}

	@Test
	public void colidableTest() {
		assertEquals(t.isCollidable(), true);
	}

	@Test
	public void selectedTest() {
		assertEquals(t.isSelected(), false);
		t.makeSelected();
		assertEquals(t.isSelected(), true);
		t.deselect();
		assertEquals(t.isSelected(), false);
	}

	@Test
	public void entityTypeTest() {
		assertEquals(t.getEntityType(), EntityType.NOT_SET);
		t.setEntityType(EntityType.HERO);
		assertEquals(t.getEntityType(), EntityType.HERO);
		t.setEntityType(EntityType.NOT_SET);
		assertEquals(t.getEntityType(), EntityType.NOT_SET);
	}

	@Test
	public void actionsTest() {
		assertEquals(t.getValidActions(), null);
	}

	@Test
	public void addActionTest() {
		ArrayList<ActionType> expected = new ArrayList<>();
		expected.add(ActionType.GATHER);
		assertTrue(t.addNewAction(ActionType.GATHER));
		assertEquals(t.getValidActions(), expected);

		assertFalse(t.addNewAction(ActionType.GATHER));
		assertEquals(t.getValidActions(), expected);

		t.addNewAction(ActionType.MOVE);
		expected.add(ActionType.MOVE);
		assertEquals(t.getValidActions(), expected);
	}

	@Test
	public void removeActionTest() {
		t.addNewAction(ActionType.GATHER);
		t.addNewAction(ActionType.MOVE);
		assertTrue(t.removeActions(ActionType.MOVE));

		assertFalse(t.removeActions(ActionType.MOVE));

		ArrayList<ActionType> expected = new ArrayList<>();
		expected.add(ActionType.GATHER);
	}

	@Test
	public void helpTextTest() {
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
		String aispacman = "This is an AI spacman";
		String techtree = "You have clicked on the base";

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
		
		t.setEntityType(EntityType.AISPACMAN);
		assertEquals(t.getHelpText().getText().toString(), aispacman);
		
		t.setEntityType(EntityType.TECHTREE);
		assertEquals(t.getHelpText().getText().toString(), techtree);
		
	}

	@Test
	/*
	 * The method currently doesn't make any sense, this test method will need update when the actual function get
	 * changed
	 */
	public void buttonTest() {
		assertEquals(t.getButton(), null);
		t.buttonWasPressed();
	}

	/**
	 * Testing some functionality of ActionSetter class here (i.e. setting gather action and setting move action) Was
	 * done here rather than another class because this class had already had BaseEntities set up so it was more
	 * convenient.
	 */

	@Test
	public void setGatherAction() {
		t.addNewAction(ActionType.GATHER);
		t3.addNewAction(ActionType.GATHER);
		// test trying to gather an entity that is not a resource
		assertEquals(ActionSetter.setAction(t, 7, 8, ActionType.GATHER), false);
		// test trying to gather nothing
		assertEquals(ActionSetter.setAction(t3, 19, 19, ActionType.GATHER), false);
		// testing if it can make an entity gather a resource.
		Resource rock = new Resource(18, 18, 1, 1, 1, ResourceType.ROCK);
		System.out.println(rock.isCollidable());
		assertEquals(ActionSetter.setAction(t3, 18, 18, ActionType.GATHER), true);
	}

	@Test
	public void setMoveAction() {
		t3.addNewAction(ActionType.MOVE);
		// moving on top of another entity that cannot be moved on top on
		assertEquals(ActionSetter.setAction(t3, 7, 8, ActionType.MOVE), true);
		// moving to somewhere without anything.
		assertEquals(ActionSetter.setAction(t3, 15, 15, ActionType.MOVE), true);
	}

	@Test
	public void getActionNameTest() {
		assertEquals(ActionSetter.getActionName(ActionType.MOVE), "Move");
		assertEquals(ActionSetter.getActionName(ActionType.GATHER), "Gather");
		assertEquals(ActionSetter.getActionName(ActionType.DAMAGE), "Attack");
		assertEquals(ActionSetter.getActionName(ActionType.GENERATE), "Create");
	}
	
}
