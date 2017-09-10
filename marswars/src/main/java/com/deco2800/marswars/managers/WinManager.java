package com.deco2800.marswars.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinManager extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(WinManager.class);
	private GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
	private int teams;
	private String winner;
	
	@Override
	public void onTick(long i) {
		teams = black.teamsAlive();
		if(teams == 1) {
			winner = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(black.getAlive());
			HandleWinner(winner);
		}
	}
	
	private void HandleWinner(String winner) {
		LOGGER.error("winner team " + winner);
		System.exit(0);
		//kill game and through a winner messager
	}

}
