package com.deco2800.marswars;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseEntityTest {	
		
	@Test
	public void ConstructorTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t.getPosX(), 0, 0.1);
		assertEquals(t.getPosY(), 1, 0.1);
		assertEquals(t.getPosZ(), 2, 0.1);
		assertEquals(t.getXLength(), 3, 0.1);
		assertEquals(t.getYLength(), 4, 0.1);
		assertEquals(t.getZLength(), 5, 0.1);
	}
	
	@Test
	public void FullBlownConstructorTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t = new BaseEntity(0, 1, 2, 3, 4, 5, 3, 4, true);
		assertEquals(t.getPosX(), 0, 0.1);
		assertEquals(t.getPosY(), 1, 0.1);
		assertEquals(t.getPosZ(), 2, 0.1);
		assertEquals(t.getXLength(), 3, 0.1);
		assertEquals(t.getYLength(), 4, 0.1);
		assertEquals(t.getZLength(), 5, 0.1);
	}
	
	@Test
	public void SetPositionsTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		t1.setPosition(0, 0, 0);
		assertEquals(t1.getPosX(), 0, 0.1);
		assertEquals(t1.getPosY(), 0, 0.1);
		assertEquals(t1.getPosZ(), 0, 0.1);
		
		t1.setPosX(3);
		assertEquals(t1.getPosX(), 3, 0.1);
		assertEquals(t1.getPosY(), 0, 0.1);
		assertEquals(t1.getPosZ(), 0, 0.1);
		
		t1.setPosY(2);
		assertEquals(t1.getPosX(), 3, 0.1);
		assertEquals(t1.getPosY(), 2, 0.1);
		assertEquals(t1.getPosZ(), 0, 0.1);
		
		t1.setPosZ(1);
		assertEquals(t1.getPosX(), 3, 0.1);
		assertEquals(t1.getPosY(), 2, 0.1);
		assertEquals(t1.getPosZ(), 1, 0.1);
	}
	
	@Test
	public void SetCostTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t1.getCost(), 0);
		t1.setCost(100);
		assertEquals(t1.getCost(), 100);
	}
	
	@Test
	public void ColidableTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t1.isCollidable(), true);
	}
	
	@Test
	public void SelectedTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t1.isSelected(), false);
		t1.makeSelected();
		assertEquals(t1.isSelected(), true);
		t1.deselect();
		assertEquals(t1.isSelected(), false);
	}
	
	@Test
	public void EntityTypeTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t1.getEntityType(), EntityType.NOT_SET);
		t1.setEntityType(EntityType.HERO);
		assertEquals(t1.getEntityType(), EntityType.HERO);
		t1.setEntityType(EntityType.NOT_SET);
		assertEquals(t1.getEntityType(), EntityType.NOT_SET);
	}
	
	/**
	 * Not sure how to test adding actions because not sure how to make an 
	 * instance of Class.
	 */
	@Test
	public void ActionsTest() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		BaseEntity t1 = new BaseEntity(0, 1, 2, 3, 4, 5);
		assertEquals(t1.getValidActions(), null);	
	}
}
