package com.deco2800.marsinvasion;

import com.deco2800.marsinvasion.actions.GenerateAction;
import com.deco2800.moos.entities.Tree;
import com.deco2800.moos.worlds.AbstractWorld;
import com.deco2800.moos.worlds.ExampleWorld;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GenerateActionTest {
	@Test
	public void TestActionCompleted() {
		AbstractWorld w = mock(ExampleWorld.class);
		Tree t = new Tree(w, 0,0,0);
		GenerateAction a = new GenerateAction(t, w);

		assertEquals(a.actionProgress(), 0);

		for(int i = 0; i < 100; i++) {
			a.doAction();
		}

		// Shouldn't have been called by now
		verify(w, times(0)).addEntity(t);
		assertEquals(a.completed(), false);

		// This should trigger the action
		a.doAction();
		verify(w, times(1)).addEntity(t);
		assertEquals(a.completed(), true);
		assertEquals(a.actionProgress(), 100);
	}
}