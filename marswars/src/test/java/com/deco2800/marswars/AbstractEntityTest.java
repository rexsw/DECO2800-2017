package com.deco2800.marswars;


import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


class TestEntity extends AbstractEntity {

	public TestEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength, float xRenderLength, float yRenderLength, boolean centered) {
		super(new Box3D(posX, posY, posZ, xLength, yLength, zLength), xRenderLength, yRenderLength, centered);
	}

	public TestEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(new Box3D(posX, posY, posZ, xLength, yLength, zLength));
	}
	
	public TestEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		super(position, xRenderLength, yRenderLength, centered);
	}
}

public class AbstractEntityTest {
	@Test
	public void worldEntityConstructorTest() {
		AbstractEntity e = new TestEntity(0, 0, 0, 1, 1, 1);
		assertEquals(e.getPosX(), 0, 0.1);
		assertEquals(e.getPosY(), 0, 0.1);
		assertEquals(e.getPosZ(), 0, 0.1);
		assertEquals(e.getXLength(), 1, 0.1);

		AbstractEntity e2 = new TestEntity(10, 20, 30, 1, 1, 1);
		assertEquals(e2.getPosX(), 10, 0.1);
		assertEquals(e2.getPosY(), 20, 0.1);
		assertEquals(e2.getPosZ(), 30, 0.1);
		assertEquals(e2.getXLength(), 1, 0.1);

		AbstractEntity e3 = new TestEntity(10, 20, 30, 45, 1, 1);
		assertEquals(e3.getPosX(), 10, 0.1);
		assertEquals(e3.getPosY(), 20, 0.1);
		assertEquals(e3.getPosZ(), 30, 0.1);
		assertEquals(e3.getXLength(), 45, 0.1);
	}

	@Test
	public void textureTest() {
		AbstractEntity e = new TestEntity(0, 0, 0, 0, 1, 1);

		e.setTexture("blah");
		assertEquals(e.getTexture(), "blah");
	}

	@Test
	public void orderingTest() {
		GameManager.get().setWorld(new BaseWorld(200, 200));
		AbstractEntity e1 = new TestEntity(0, 100, 0, 0, 1, 1);
		AbstractEntity e2 = new TestEntity(100, 0, 0, 0, 1, 1);

		List<AbstractEntity> list = new ArrayList<AbstractEntity>();
		list.add(e1);
		list.add(e2);

		Collections.sort(list);
		assertEquals(list.get(1), e1);
		assertEquals(list.get(0), e2);

		list.removeAll(list);
		list.add(e2);
		list.add(e1);

		Collections.sort(list);
		assertEquals(list.get(1), e1);
		assertEquals(list.get(0), e2);
	}

	@Test
	public void collisionTest() {
		GameManager.get().setWorld(new BaseWorld(10, 10));

		AbstractEntity e1 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		AbstractEntity e2 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, false);
		assertFalse(e1.collidesWith(e2));

		AbstractEntity e3 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		AbstractEntity e4 = new TestEntity(1, 1, 0, 1, 1, 1);
		Assert.assertTrue(e3.collidesWith(e4));


		AbstractEntity e5 = new TestEntity(1.25f, 1.25f, 0, 0.4f, 0.4f, 1, 1, 1, false);
		AbstractEntity e6 = new TestEntity(1.55f, 1.55f, 0, 0.4f, 0.4f, 1, 1, 1, false);
		assertTrue(e5.collidesWith(e6));

		AbstractEntity e7 = new TestEntity(1.25f, 1.25f, 0, 0.4f, 0.4f, 1, 1, 1, false);
		AbstractEntity e8 = new TestEntity(1.66f, 1.66f, 0, 0.4f, 0.4f, 1, 1, 1, false);
		assertFalse(e7.collidesWith(e8));

		AbstractEntity e9 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		AbstractEntity e10 = new TestEntity(1.1f, 1.1f, 0, 0.1f, 0.1f, 1, 1, 1, true);
		assertTrue(e9.collidesWith(e10));

		AbstractEntity e11 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		AbstractEntity e12 = new TestEntity(1.2f, 1.2f, 0, 0.1f, 0.1f, 1, 1, 1, true);
		assertFalse(e11.collidesWith(e12));
	}
	
	@Test
	public void box3DConstructorTest() {
		Box3D position = new Box3D(0f, 1f, 2f, 1f, 1f, 1f);
		AbstractEntity e = new TestEntity(position, 1, 2, false);
		assertEquals(e.getPosX(), 0, 0.1);
		assertEquals(e.getPosY(), 1, 0.1);
		assertEquals(e.getPosZ(), 2, 0.1);
		assertEquals(e.getXLength(), 1, 0.1);
		assertEquals(e.getYLength(), 1, 0.1);
		assertEquals(e.getZLength(), 1, 0.1);
		assertEquals(e.getXRenderLength(), 1, 0.1);
		assertEquals(e.getYRenderLength(), 2, 0.1);	
		assertEquals(e.getBox3D(), position);
	}
	
	@Test
	public void positionTest() {
		AbstractEntity e1 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		AbstractEntity e2 = new TestEntity(1, 1, 0, 0.1f, 0.1f, 1, 1, 1, true);
		
		e1.setPosition(2, 4, 6);
		assertEquals(e1.getPosX(), 2, 0.1);
		assertEquals(e1.getPosY(), 4, 0.1);
		assertEquals(e1.getPosZ(), 6, 0.1);
		
		e2.setPosX(2);
		assertEquals(e2.getPosX(), 2, 0.1);
		e2.setPosY(4);
		assertEquals(e2.getPosY(), 4, 0.1);
		e2.setPosZ(6);
		assertEquals(e2.getPosZ(), 6, 0.1);
	}
	
	@Test @Ignore
	public void compareTest() {
		AbstractEntity e1 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e2 = new TestEntity(1, 1, 1, 1, 1, 1);
		
		assertEquals(e1.compareTo(e2), 0);
	}
	
	@Test
	public void equalsTest() {
		AbstractEntity e1 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e2 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e3 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e4 = new TestEntity(1, 1, 1, 1, 1, 1);
		
		e1.setTexture("small_water");
		e2.setTexture("large_water");
		e4.setTexture("small_water");
		assertFalse(e1.equals(null));
		assertFalse(e1.equals(e2));
		assertFalse(e1.equals(e3));
		assertTrue(e1.equals(e4));
		assertTrue(e1.hashCode() == e4.hashCode());	
	}
	
	@Test
	public void distanceTest() {
		AbstractEntity e1 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e2 = new TestEntity(1, 1, 1, 1, 1, 1);
		AbstractEntity e3 = new TestEntity(2, 2, 2, 1, 1, 1);
		
		assertEquals(e1.distance(e2), 0, 0.1);
		assertEquals(e3.distance(e2), Math.sqrt(3), 0.1);
	}
	
	@Test
	public void walkOverTest() {
		AbstractEntity e = new TestEntity(0, 0, 0, 1, 1, 1);
		assertEquals(e.canWalOver(), false);
	}
}
