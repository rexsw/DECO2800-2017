package com.deco2800.marswars;

import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.PlayerManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerAiInteractionTest {
	
	@Test
	public void AiPlayerTeams() {
		AiManager test = new AiManager();
		PlayerManager player = new PlayerManager();
		assertEquals(player.getTeam(), 0);
		test.setTeam(1);
		assertEquals(test.getTeam(), 1);
	}
	
	@Test
	public void AiPlayerDifferentTeams() {
		AiManager test = new AiManager();
		PlayerManager player = new PlayerManager();
		test.setTeam(1);
		assertEquals(test.sameTeam(player), false);
		assertEquals(player.sameTeam(test), false);
	}
	
	@Test
	public void AiPlayerSameTeam() {
		AiManager test = new AiManager();
		PlayerManager player = new PlayerManager();
		test.setTeam(1);		
		assertEquals(test.sameTeam(player), false);
		player.setTeam(1);
		assertEquals(test.sameTeam(player), true);
		assertEquals(player.sameTeam(test), true);
	}
}
