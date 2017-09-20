package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.units.AmbientAnimal;
import com.deco2800.marswars.entities.units.AmbientAnimal.AmbientState;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.util.WorldUtil;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.deco2800.marswars.managers.TimeManager;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.lang.Math;

/**
 * Created by Scott Whittington on 22/08
 * control the current ai, every tick the ai looks at it's units and 
 * ensures they're doing something useful
 * warning spicy i hope you like meat balls 
 */
public class AiManager extends AbstractPlayerManager implements TickableManager {
	private List<Integer> teamid = new LinkedList<Integer>();
	private static final Logger LOGGER = LoggerFactory.getLogger(AiManager.class);
	private List<State> state = new LinkedList<State>();
	private long timeAtStateChange;
	private TimeManager tm = (TimeManager) GameManager.get().getManager(TimeManager.class);
	private Random rand = new Random();
	private int tickNumber = 0;
	
	public static enum State {
		DEFAULT, AGGRESSIVE, DEFENSIVE
	}

	@Override
	public void onTick(long l) {
		//Increase the current tick count
		tickNumber++;
		//Check for state change every 10 ticks
		if (tickLock(10,5)) {
			decideChangeState();
		}
		for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner && ((HasOwner) e).isAi()) {
				if(e instanceof Astronaut) {
					Astronaut x = (Astronaut)e;
					useSpacman(x);
				} else if(e instanceof Base) {
					Base x = (Base)e;
					generateSpacman(x);
				} else if(e instanceof Soldier) {
					Soldier x = (Soldier)e;
					//Action depends on current state
					switch (getState(e.getOwner())) {
					case DEFAULT:
						soldierDefend(x);
						break;
					case AGGRESSIVE:
						soldierAttack(x);
						break;
					case DEFENSIVE:
						break;
					}
				} else if(e instanceof AmbientAnimal){
					ambientController((AmbientAnimal)e);
				}
			}
		}
	}
	
	
	/**
	 * A controller which control the ambient animal's actions
	 * @param ambient
	 */
	public void ambientController(AmbientAnimal ambient){
		AmbientState as = ambient.getState();
		switch(as){
		case DEFAULT:
			break;
		case TRAVEL:
			break;
		case ATTACKBACK:
			break;
		}
	}
	
	public void ambientTravel(AmbientAnimal ambient){
		if (ambient.getTravelTime() < ambient.getMaxTravelTime()) {
			//add moveAction() here
			ambient.setTravelTime(ambient.getTravelTime() + 1);
		} else {
			ambient.setTravelTime(0);
			ambient.setState(AmbientState.DEFAULT);
		}
	}
	
	/**
	 * generate new spacman when a base has more than 30 rocks
	 */
	private void generateSpacman(Base x) {
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if(!x.showProgress() && rm.getRocks(x.getOwner()) > 30) {
			//sets the ai base to make more spacman if possible
			LOGGER.info("ai - set base to make spacman");
			rm.setRocks(rm.getRocks(x.getOwner()) - 30, x.getOwner());
			Astronaut r = new Astronaut(x.getPosX(), x.getPosY(), 0, x.getOwner());
			GameManager.get().getWorld().addEntity(r);
			x.setAction(new GenerateAction(r));
		}
	}
	
	/**
	 * Orders a soldier to attack an enemy
	 * @param x
	 */
	private void soldierAttack(Soldier x) {
		//lets the ai target player spacman with it's enemyspacmen
		if(x.showProgress()) {
			return;
		}
		for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
			if(r instanceof AttackableEntity && !x.sameOwner(r)) {
				//LOGGER.error("ai - setting unit to attack " + r.toString());
				AttackableEntity y = (AttackableEntity) r;
				x.attack(y);
				return;
			}
		}
}
		
	/**
	 * Tasks all soldiers to move back to base
	 * @param x
	 */
	private void soldierDefend(Soldier soldier) {
		//lets the ai target player spacman with it's enemyspacmen
		if(soldier.showProgress()) {
			return;
		}
		for( BaseEntity base : GameManager.get().getWorld().getEntities()) {
			if(base instanceof Base && soldier.sameOwner(base)) {

				Base y = (Base) base;
				// Move soldier to base (Not currently working, so will just not set any actions)
				soldier.setAction(new MoveAction(base.getPosX(), base.getPosY(),
						(AttackableEntity)soldier));
				return;
			}
		}
	}
	
	/**
	 * Orders an Astronaut to gather resources
	 * @param x
	 */
	private void useSpacman(Astronaut x) {
		if(!(x.showProgress())) {
			//allow spacmans to collect the closest resources
			//LOGGER.info("ticking on " + x.toString() + ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(x.getOwner()));
			Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
			x.setAction(new GatherAction(x, (Resource) resource.get()));
			//LOGGER.info(resource.get().getTexture() + "");
			LOGGER.info("ai - set spacman to gather");
		}
	}
	
	/**
	 * Adds an Ai team to the AiManager
	 * @param id
	 */
	public void addTeam(Integer id) {
		teamid.add(id);
		state.add(State.DEFAULT);
	}
	
	/**
	 * A list of integers of Ai team ids
	 * @return List<Integer> 
	 */
	public List<Integer> getAiTeam(){
		return teamid;
	}
	
	/**
	 * Removes a team from the list of Ai players
	 * @param id int the team's id to kill 
	 */
	public void kill(Integer id) {
		if(teamid.contains(id)) {
			state.remove(getStateIndex(id));
			teamid.remove(id);
		}
	}
	
	/**
	 * Sets a restriction boolean to only run every nth tick
	 * Returns true if on the specified tick number
	 * @param modulus
	 * @param offset
	 * @return
	 */
	private boolean tickLock(int modulus, int offset){
		if (((this.tickNumber + offset) % modulus) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the specified team's strength (number of units + buildings)
	 * @param teamID
	 * @return
	 */
	private int teamStrengthCount(Integer teamID) {
		GameBlackBoard blackboard = (GameBlackBoard) GameManager.get().
				getManager(GameBlackBoard.class);
		// test if GameBlackBoard exists
		if (blackboard != null) {
			return blackboard.count(teamID, GameBlackBoard.Field.UNITS)
					+ blackboard.count(teamID, GameBlackBoard.Field.BUILDINGS);
		}
		return -1;
	}
	
	/**
	 * Returns the highest team's strength (number of units + buildings)
	 * Used to consider hostility
	 * @return
	 */
	private int highestStrengthCount() {
		int strength = 0;
		//Assumed 1 player
		strength = teamStrengthCount(-1);
		for (int i = 1; i <= teamid.size(); i++) {
			strength = Math.max(teamStrengthCount(i) , strength);
		}
		return strength;
	}
	
	/**
	 * Determines Ai team state depending on their strength compared to the leader's
	 * @param teamID
	 * @return
	 */
	private State hostility(Integer teamID) {
		double ratio = 0.0;
		if (teamStrengthCount(teamID) > 0) {
			// Compares team strength to global strength
			ratio = highestStrengthCount() / teamStrengthCount(teamID);
		}
		if (ratio >= 0.80) {
			// If strong, then attack
			return State.AGGRESSIVE;
		} else if (ratio >= 0.50) {
			// If moderate, then default stance
			return State.DEFAULT;
		} else {
			// If weak, then defend
			return State.DEFENSIVE;
		}
	}
	
	/**
	 * Decides whether or not to change state
	 * Will check every (in-game) hour
	 */
	public void decideChangeState() {
		for (Integer teamID = 1; teamID <= teamid.size(); teamID++) {
			if (getTimeSinceStateChange() > 60*60) {
				setState(teamID, hostility(teamID));
				LOGGER.info("AI [" + teamID + "] State updated to " + state);
			}
		}
	}
	
	/**
	 * Gets the state index in its Linked List that corresponds to the TeamID
	 * @param teamID
	 * @return
	 */
	public int getStateIndex(Integer teamID) {
		int position = teamid.indexOf(teamID);
		if (position == -1) {
			LOGGER.error("Invalid Team Id");
		}
		return position;
	}
	
	/**
	 * Sets the current state of the AI Manager
	 * @param newState
	 */
	public void setState(Integer teamID, State newState) {
		state.set(getStateIndex(teamID), newState);
		timeAtStateChange = tm.getGameSeconds();	//  note that timeAtStateChange is
														// one variable for all states
	}
	
	/**
	 * Get the current state of the team id of the AI Manager
	 * @return
	 */
	public State getState(Integer teamID) {
		return state.get(getStateIndex(teamID));
	}

	/**
	 * Get the time at which the AI Manager last changed state
	 * @return
	 */
	public long getTimeAtStateChange() {
		return timeAtStateChange;
	}
	
	/**
	 * Get the time difference since the AI Manager last changed state
	 * @return
	 */
	public long getTimeSinceStateChange() {
		return tm.getGameSeconds() - timeAtStateChange;
	}
}
