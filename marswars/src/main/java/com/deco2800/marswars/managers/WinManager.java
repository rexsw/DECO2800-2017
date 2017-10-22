package com.deco2800.marswars.managers;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.hud.GameStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private Dialog winnermsn;
	private boolean gamewin = false;
	private boolean econwin = true;
	private boolean milwin = true;
	private WINS winCondition = null;
	
	public static enum WINS {
		ECON, MIL, BOTH
	}

	@Override
	public void onTick(long i) {
		//on tick checks in someone has won
		teams = black.teamsAlive();
		if (teams == 1 && milwin) {
			//combat win for a team
			LOGGER.info("tick winner " + teams);
			gamewin = true;
			if(GameManager.get().getSkin() != null) {
				winner = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(black.getAlive());
				winnermsn = new HandleWinner("Game Over", GameManager.get().getSkin(), winner, "Military");
				winnermsn.show(GameManager.get().getStage());
			}
		}
		teams = ((ResourceManager) GameManager.get().getManager(ResourceManager.class)).CappedTeam();
		if(teams != 0 && econwin) {
			//economic win for a team
			gamewin = true;
			if(GameManager.get().getSkin() != null) {
				winner = ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(teams);
				winnermsn = new HandleWinner("Game Over", GameManager.get().getSkin(), winner, "Economic");
				winnermsn.show(GameManager.get().getStage());
			}
		}
	}
	
	
	/**
	 * for testing is a winner has been choose 
	 * 
	 * @return true iif a winner has been picked
	 */
	public boolean isWinner() {
		return gamewin;
	}
	
	public void setwinconditions(WINS condtion) {
		this.winCondition = condtion;
		switch(condtion){
			case ECON:
				econwin = true;
				milwin = false;
			case MIL:
				econwin = false;
				milwin = true;
			case BOTH:
				econwin = true;
				milwin = true;
		}
	}

	/**
	 * return win condition for gamesave
	 * @return
	 */
	public WINS getWinCondition(){
		return winCondition;
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
				GameManager.get().resetGame();
			} else {
				GameStats stats = new GameStats(GameManager.get().getStage(), GameManager.get().getSkin(),GameManager.get().getGui(),(TextureManager) GameManager.get().getManager(TextureManager.class));
				stats.showStats();
			}
		}
	}

}
