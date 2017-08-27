package com.deco2800.marswars;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.deco2800.marswars.managers.AiManagerTest;
import com.deco2800.marswars.managers.PlayerManager;

public class PlayerAiInteractionTest {
	
	@Test
	public void AiPlayerTeams() {
		AiManagerTest test = new AiManagerTest();
		PlayerManager player = new PlayerManager();
		assertEquals(player.getTeam(), 0);
		test.setTeam(1);
		assertEquals(test.getTeam(), 1);
	}
	
	@Test
	public void AiPlayerDifferentTeams() {
		AiManagerTest test = new AiManagerTest();
		PlayerManager player = new PlayerManager();
		test.setTeam(1);
		assertEquals(test.sameTeam(player), false);
		assertEquals(player.sameTeam(test), false);
	}
	
	@Test
	public void AiPlayerSameTeam() {
		AiManagerTest test = new AiManagerTest();
		PlayerManager player = new PlayerManager();
		test.setTeam(1);		
		assertEquals(test.sameTeam(player), false);
		player.setTeam(1);
		assertEquals(test.sameTeam(player), true);
		assertEquals(player.sameTeam(test), true);
	}
}
