package com.deco2800.marswars.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * A class the handles winning the game
 * 
 * @author Scott Whittington
 *
 */
public class WinManager extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WinManager.class);
	private GameBlackBoard black = (GameBlackBoard) GameManager.get()
			.getManager(GameBlackBoard.class);
	private int teams;
	private String winner;
	private Dialog winnermsn = null;

	@Override
	public void onTick(long i) {
		teams = black.teamsAlive();
		if (teams == 1) {
			//combat win for a team
			LOGGER.info("tick winner " + teams);
			winner = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(black.getAlive());
			winnermsn = new HandleWinner("Game Over", GameManager.get().getSkin(), winner, "Military");
		}
		teams = ((ResourceManager) GameManager.get().getManager(ResourceManager.class)).CappedTeam();
		if(teams != 0) {
			//economic win for a team
			winner = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(teams);
			winnermsn = new HandleWinner("Game Over", GameManager.get().getSkin(), winner, "Economic");
		}
	}
	
	/**
	 * for testing is a winner has been choose 
	 * 
	 * @return true iif a winner has been picked
	 */
	public boolean isWinner() {
		return winnermsn != null;
	}
	
	/**
	 * A dialog class for acting on the win 
	 * 
	 * @author Scott Whittingon
	 *
	 */
	private class HandleWinner extends Dialog {

		private TimeManager timeManager = (TimeManager) GameManager.get().getManager(TimeManager.class);

		public HandleWinner(String title, Skin skin, String winner, String wintype) {
			super(title, skin);
			{
				text(winner + " team wins via " + wintype + " victory");
				button("Ok", 1);
				button("View Game Stats", 2);
				timeManager.pause();
			}
		}

		@Override
		protected void result(final Object object) {
			if (object == (Object) 1) {
				System.exit(0);
			} else {
				// later add viewing of game stats here
				System.exit(0);
			}
		}
	}

}
