package com.deco2800.marswars;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.util.Box3D;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

class TestOnwerEntity extends AbstractEntity implements HasOwner {


	public TestOnwerEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(new Box3D(posX, posY, posZ, xLength, yLength, zLength));
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
		a.setOwner(-1);
		assertEquals(a.getOwner(), -1);
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
