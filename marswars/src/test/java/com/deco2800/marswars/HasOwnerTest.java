package com.deco2800.marswars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.managers.PlayerManager;
import com.deco2800.marswars.worlds.BaseWorld;

class TestOnwerEntity extends AbstractEntity implements HasOwner {


	public TestOnwerEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, xLength, yLength, zLength);
	}
	
	private int owner;
	public Optional<DecoAction> currentAction = Optional.empty();

	@Override
	public void setOwner(int owner) {
		this.owner = owner;
	}

	@Override
	public int getOwner() {
		return this.owner;
	}

	@Override
	public boolean sameOwner(AbstractEntity entity) {
		if(entity instanceof HasOwner) {
			return this.owner == ((HasOwner) entity).getOwner();
		} else {
			return false;
		}
	}
	
	public boolean isWorking() {
		if(currentAction.isPresent()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setAction(DecoAction action) {
		currentAction = Optional.of(action);
	}

	@Override
	public boolean isAi() {
		return owner >= 0;
	}
}

public class HasOwnerTest {
	@Test
	public void TestEntityTest() {
		TestOnwerEntity a = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		TestOnwerEntity b = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		b.setOwner(-1);
		assertEquals(a.getOwner(), null);
		assertEquals(a.isWorking(), false);
	}

	@Test
	public void SameOnwerTest() {
		TestOnwerEntity a = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		TestOnwerEntity b = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		b.setOwner(-1);
		assertEquals(a.sameOwner(b), false);
		a.setOwner(-1);
		assertEquals(a.sameOwner(b), true);
	}
	
	@Test
	public void DecoActionTest() {
		TestOnwerEntity a = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		DecoAction b = new MoveAction(0,0,a);
		assertEquals(a.isWorking(), false);
		a.setAction(b);
		assertEquals(a.isWorking(), true);
	}
	
	@Test
	public void isAiTest() {
		TestOnwerEntity a = new TestOnwerEntity(0, 0, 0, 0, 0, 0);
		a.setOwner(1);
		assertEquals(a.isAi(), true);
	}

	
}
