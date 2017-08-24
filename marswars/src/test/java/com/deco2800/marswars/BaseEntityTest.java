package com.deco2800.marswars;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BaseEntityTest {	
	private BaseEntity t;
	
	@Before
	public void initialise() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		t = new BaseEntity(0, 1, 2, 3, 4, 5);
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
	
	/**
	 * Not sure how to test adding actions because not sure how to make an 
	 * instance of Class.
	 */
	@Test
	public void ActionsTest() {
		assertEquals(t.getValidActions(), null);	
	}
}
