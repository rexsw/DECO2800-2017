package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.entities.buildings.Base;
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

/**
 * Created by Scott Whittington on 22/08
 * control the current ai, every tick the ai looks at it's units and 
 * ensures they're doing something useful
 * warning spicy i hope you like meat balls 
 */
public class AiManager extends AbstractPlayerManager implements TickableManager {
	private List<Integer> teamid = new LinkedList<Integer>();
	private int numAIPlayers;
	private static final Logger LOGGER = LoggerFactory.getLogger(AiManager.class);
	private Map<Integer, Integer> alive = new HashMap<Integer, Integer>();
	private State state = State.DEFAULT;
	private long timeAtStateChange;
	
	public static enum State {
		DEFAULT, AGGRESSIVE, DEFENSIVE
	}

	@Override
	public void onTick(long l) {
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
					switch (state) {
					case AGGRESSIVE:
						useEnemy(x);
						break;
					case DEFAULT:
						soldierDefend(x);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * generate new spacman when a base has more than 30 rocks
	 */
	private void generateSpacman(Base x) {
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if(!x.isWorking() && rm.getRocks(x.getOwner()) > 30) {
			//sets the ai base to make more spacman if possible
			LOGGER.info("ai - set base to make spacman");
			rm.setRocks(rm.getRocks(x.getOwner()) - 30, x.getOwner());
			Astronaut r = new Astronaut(x.getPosX(), x.getPosY(), 0, x.getOwner());
			x.setAction(new GenerateAction(r));
		}
	}
			
	private void useEnemy(Soldier x) {
		//lets the ai target player spacman with it's enemyspacmen
		if(x.isWorking()) {
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
		if(soldier.isWorking()) {
			return;
		}
		for( BaseEntity base : GameManager.get().getWorld().getEntities()) {
			if(base instanceof Base && soldier.sameOwner(base)) {
				LOGGER.info("ai - setting unit to move to base" + base.toString());
				Base y = (Base) base;
				// Move soldier to base (Not currently working, so will just not set any actions)
				soldier.setAction(new MoveAction(base.getPosX(), base.getPosY(),
						(AbstractEntity)soldier));
				return;
			}
		}
	}

	private void useSpacman(Astronaut x) {
		if(!(x.isWorking())) {
			//allow spacmans to collect the closest resources
			//LOGGER.info("ticking on " + x.toString() + ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(x.getOwner()));
			Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
			x.setAction(new GatherAction(x, (Resource) resource.get()));
			//LOGGER.info(resource.get().getTexture() + "");
			LOGGER.info("ai - set spacman to grather");
		}
	}
			
			
	/**
	 * if the ai has no more spacman under it's control, removes it's units
	 * and sets it to "dead" so it won't tick anymore 
	 */
	public boolean isKill(int key) {
		//in this case "dead" is an Ai with no spacman
		if(((GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class)).count(key, "Units") == 0) {
		LOGGER.info("ai - is kill");
		alive.put(key, 0);
		return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * @return true iff Ai is alive else false 
	 */
	public boolean alive(int key) {
		return alive.get(key)==1;
	}
	
	public void addTeam(int id) {
		teamid.add(id);
		alive.put(id, 1);
	}
	
	public List<Integer> getAiTeam(){
		return teamid;
	}
	
	/**
	 * Sets the current state of the AI Manager
	 * @param newState
	 */
	public void setState(State newState) {
		state = newState;
		timeAtStateChange = TimeManager.getInGameTime();
	}
	
	/**
	 * Get the current state of the AI Manager
	 * @return
	 */
	public State getState() {
		return state;
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
		return TimeManager.getInGameTime() - timeAtStateChange;
	}
}
