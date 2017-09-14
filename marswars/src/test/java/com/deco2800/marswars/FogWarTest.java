package com.deco2800.marswars;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.deco2800.marswars.managers.FogManager;
import com.deco2800.marswars.managers.GameManager;

/**
 * test for the fog world
 *
 * @Author Treenhan, jdtran21
 * Created by Treenhan on 8/26/17.
 */
public class FogWarTest {
//    FogWorld world = new FogWorld();
//
//     @Test
//    public void checkArrayNotNull(){
//         assertNotNull(world.getFogMap());
//     }
	FogManager fogOfWar = (FogManager)(GameManager.get().getManager(FogManager.class));
	
	@Test (expected = IllegalArgumentException.class)
	public void invalidWidth() {
		FogManager.initialFog(-1, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void invalidLength() {
		FogManager.initialFog(1, -1);
	}
	
	@Test
	public void getFogs() {
		FogManager.initialFog(2, 2);
		assertThat("Unseen tile is not unseen", FogManager.getFog(1, 1), is(equalTo(0)));
		assertThat("Unseen tile is not unseen", FogManager.getBlackFog(1, 1), is(equalTo(0)));	
	}
	
	@Test
	public void toggleFog() {
		assertThat("ToggleFog is not true", FogManager.getToggleFog(), is(equalTo(true)));
		FogManager.toggleFog(false);
		assertThat("ToggleFog did not toggle", FogManager.getToggleFog(), is(equalTo(false)));
	}
	
	@Test
	public void sightRange() {
		FogManager.initialFog(10, 10);
		FogManager.sightRange(0, 0, 2, true);
		assertThat(FogManager.getFog(0, 0), is(equalTo(2)));
		assertThat(FogManager.getBlackFog(0, 0), is(equalTo(1)));
		FogManager.sightRange(0, 0, 2, false);
		assertThat(FogManager.getFog(0, 0), is(equalTo(0)));
		assertThat(FogManager.getBlackFog(0, 0), is(equalTo(1)));
		FogManager.sightRange(9, 9, 2, true);
		assertThat(FogManager.getFog(9, 9), is(equalTo(2)));
		assertThat(FogManager.getBlackFog(9, 9), is(equalTo(1)));
		FogManager.sightRange(9, 9, 2, false);
		assertThat(FogManager.getFog(9, 9), is(equalTo(0)));
		assertThat(FogManager.getBlackFog(9, 9), is(equalTo(1)));
	}
}
