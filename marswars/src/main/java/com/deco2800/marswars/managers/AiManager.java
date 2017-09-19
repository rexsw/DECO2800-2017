package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.TerrainElements.Resource;
import com.deco2800.marswars.buildings.Base;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(AiManager.class);
	private State state = State.DEFAULT;
	private long timeAtStateChange;
	private TimeManager tm = (TimeManager) GameManager.get().getManager(TimeManager.class);
	
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
		if(!x.showProgress() && rm.getRocks(x.getOwner()) > 30) {
			//sets the ai base to make more spacman if possible
			LOGGER.info("ai - set base to make spacman");
			rm.setRocks(rm.getRocks(x.getOwner()) - 30, x.getOwner());
			Astronaut r = new Astronaut(x.getPosX(), x.getPosY(), 0, x.getOwner());
			GameManager.get().getWorld().addEntity(r);
			x.setAction(new GenerateAction(r));
		}
	}
			
	private void useEnemy(Soldier x) {
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
	if(!(x.showProgress())) {
		//allow spacmans to collect the closest resources
		//LOGGER.info("ticking on " + x.toString() + ((ColourManager) GameManager.get().getManager(ColourManager.class)).getColour(x.getOwner()));
		Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
		x.setAction(new GatherAction(x, (Resource) resource.get()));
		//LOGGER.info(resource.get().getTexture() + "");
		LOGGER.error("ai - set spacman to grather");
	}
}

	public void addTeam(Integer id) {
		teamid.add(id);
	}
	/**
	 * @return List<Integer> a list of integers of ai team ids
	 */
	public List<Integer> getAiTeam(){
		return teamid;
	}
	
	/**
	 * 
	 * @param id int the team's id to kill and remove from the list of ai players
	 */
	public void kill(Integer id) {
		if(teamid.contains(id)) {
			teamid.remove(id);
		}
	}
	
	/**
	 * Sets the current state of the AI Manager
	 * @param newState
	 */
	public void setState(State newState) {
		state = newState;
		timeAtStateChange = tm.getGameSeconds();
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
		return tm.getGameSeconds() - timeAtStateChange;
	}
}
