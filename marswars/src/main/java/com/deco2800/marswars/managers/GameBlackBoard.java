package com.deco2800.marswars.managers;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A class to track various things in the game and to keep a history of them
 * 
 * @author Scott Whittington
 *
 */
public class GameBlackBoard extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameBlackBoard.class);
	private List<Integer> teams;
	private Map<Integer,Map<Field, List<Integer>>> values = new HashMap<Integer,Map<Field, List<Integer>>>();
	private ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	private int alive;
	private int timer;
	private int index = 0;
	private boolean enableCheck = false;
	
	/**
	 * acceptable fields for use in the blackboard used for type safety 
	 */
	public enum Field {
		BIOMASS, CRYSTAL, ROCKS, UNITS, UNITS_LOST, COMBAT_UNITS, BUILDINGS, TECHNOLOGY,
		ASTRONAUTS,
	}
	

	@Override
	public void onTick(long i) {
		if (!enableCheck) {
			return;
		}
		//adds to the history of each field every few ticks
		timer++;
		if(timer % 10 == 0) {
			for(int teamid: teams) {
				values.get(teamid).get(Field.BIOMASS).add(rm.getRocks(teamid));
				values.get(teamid).get(Field.CRYSTAL).add(rm.getCrystal(teamid));
				values.get(teamid).get(Field.ROCKS).add(rm.getRocks(teamid));
				values.get(teamid).get(Field.UNITS).add(this.count(teamid, Field.UNITS));
				values.get(teamid).get(Field.UNITS_LOST).add(this.count(teamid, Field.UNITS_LOST));
				values.get(teamid).get(Field.COMBAT_UNITS).add(this.count(teamid, Field.COMBAT_UNITS));
				values.get(teamid).get(Field.ASTRONAUTS).add(this.count(teamid, Field.ASTRONAUTS));
				values.get(teamid).get(Field.BUILDINGS).add(this.count(teamid, Field.BUILDINGS));
				//currentlly not counting techology so this is for graph testing on ui
				values.get(teamid).get(Field.TECHNOLOGY).add(ThreadLocalRandom.current().nextInt(1, 50));
				
			}
			index++;
		}
	}
	
	/**
	 * sets the BlackBoard at the start of a game
	 * 
	 * @ensure called after the game world has been set up and is only called once
	 */
	public void set() {
		values = new HashMap<Integer,Map<Field, List<Integer>>>();
		teams = new ArrayList<Integer>();
		index = 0;
		rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		int teamid;
		for(BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner) {
				teamid = ((HasOwner) e).getOwner();
				if(values.containsKey(teamid)) {
					updateunit(e);
				}
				else if(teamid != 0) {
					EnumMap<Field, List<Integer>> teammap = new EnumMap<Field, List<Integer>>(Field.class);
					setMaps(teammap, teamid);
					updateunit(e);
				}
			}
		}
		enableCheck = true;
	}
	
	/**
	 * a helper methoad to set a Map up for use in the BlackBaord
	 * 
	 * @param setmap map the map to be set up
	 * @param teamid int the teamid to map to
	 */
	private void setMaps(EnumMap<Field, List<Integer>> setmap, int teamid) {
		ArrayList<Integer> base = new ArrayList<Integer>();
		base.add(0);
		setmap.put(Field.BIOMASS, new ArrayList<Integer>(base));
		setmap.put(Field.CRYSTAL,  new ArrayList<Integer>(base));
		setmap.put(Field.ROCKS,  new ArrayList<Integer>(base));
		setmap.put(Field.UNITS,  new ArrayList<Integer>(base));
		setmap.put(Field.UNITS_LOST,  new ArrayList<Integer>(base));
		setmap.put(Field.COMBAT_UNITS,  new ArrayList<Integer>(base));
		setmap.put(Field.ASTRONAUTS,  new ArrayList<Integer>(base));
		setmap.put(Field.BUILDINGS,  new ArrayList<Integer>(base));
		setmap.put(Field.TECHNOLOGY,  new ArrayList<Integer>(base));
		values.put(teamid, setmap);
		teams.add(teamid);
	}
	
	/**
	 * a test if the mappings have been set up correctly
	 * 
	 * @return true iff the map has been set correctly 
	 */
	public boolean isSet() {
		return values != null;
	}
	
	/**
	 * gets the current index of the history
	 * 
	 * @return the index in list of where the blackboard is up to in the history
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Updates the information about a teams units when an enity is added
	 * 
	 * @param enity BaseEntity the entity to be updated added to the world
	 */
	public void updateunit(BaseEntity enity) {
		int teamid = enity.getOwner();
		if(!values.containsKey(teamid)) {
			return;
		}
		int count = values.get(teamid).get(Field.UNITS).get(index);
		LOGGER.info("unit count " + count + " teamid " + teamid);
		count++;
		values.get(teamid).get(Field.UNITS).set(index, count);
		if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get(Field.BUILDINGS).get(index);
			count++;
			values.get(teamid).get(Field.BUILDINGS).set(index, count);
			return;
		}
		else if(enity instanceof Astronaut) {
			count = values.get(teamid).get(Field.ASTRONAUTS).get(index);
			count++;
			values.get(teamid).get(Field.ASTRONAUTS).set(index, count);
		}
		else if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get(Field.COMBAT_UNITS).get(index);
			count++;
			values.get(teamid).get(Field.COMBAT_UNITS).set(index, count);
		}
		
	}
	
	/**
	 * Updates the information about a teams units when an enity is killed
	 * 
	 * @param enity BaseEntity the entity that has been killed
	 */
	public void updateDead(BaseEntity enity) {
		int teamid = enity.getOwner();
		if(!values.containsKey(teamid)) {
			return;
		}
		int count = values.get(teamid).get(Field.UNITS).get(index);
		int dead = values.get(teamid).get(Field.UNITS_LOST).get(index);
		count--;
		dead++;
		values.get(teamid).get(Field.UNITS).set(index, count);
		values.get(teamid).get(Field.UNITS_LOST).set(index, dead);
		if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get(Field.BUILDINGS).get(index);
			count--;
			values.get(teamid).get(Field.BUILDINGS).set(index, count);
			return;
		}
		else if(enity instanceof Astronaut) {
			count = values.get(teamid).get(Field.ASTRONAUTS).get(index);
			count--;
			values.get(teamid).get(Field.ASTRONAUTS).set(index, count);
		}
		else if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get(Field.COMBAT_UNITS).get(index);
			count--;
			values.get(teamid).get(Field.COMBAT_UNITS).set(index, count);
		}
	}
	
	/**
	 * checks how many teams are alive, also sets alive to a teamid that is alive
	 * 
	 * @return count int the number of teams still alive
	 */
	public int teamsAlive() {
		int count = 0;
		if(values!=null) {
			for (int t : values.keySet()) {
				if (values.get(t).get(Field.UNITS).get(index) != 0) {
					alive = t;
					count++;

				}
			}
		}
		return count;
	}
	
	/**
	 * Returns a team that is alive, no rules for which one
	 * 
	 * @return the teamid of a team that is alive
	 * @ensure teamsAlive is called before this
	 */
	public int getAlive() {
		return alive;
	}
	
	/**
	 * returns the histoy of of a set of points i.e resources
	 * 
	 * @ensure history is a valid field i.e Biomass, Units
	 * @param teamid int the teamid for the history
	 * @param history String the type of history to return i.e biomass
	 * @return float[] an array of the history of this field 
	 */
	public float[] getHistory(int teamid, Field history){
		//note this methoad is werid because it's used to graph the data in a lidbgx
		//polyline which needs the data in a werid way 
		float[] returnv = new float[(index+2)*2];
		returnv[0] = 0;
		returnv[1] = 0;
		for(int i = 0; i < index; i+=2) {
			returnv[i + 2] = this.values.get(teamid).get(history).get(i);
			returnv[i + 3] = this.values.get(teamid).get(history).get(i+1);
		}
		return returnv;
	}
	
	/**
	 * gives a count of a current field
	 * 
	 * @ensure field is a valid field i.e Biomass, Units
	 * @param teamid int the teamid of the team being counted
	 * @param field string the field being counted
	 * @return int the count of this field
	 */
	public int count(int teamid, Field field) {
		try {
			return values.get(teamid).get(field).get(index);
		}
		catch(NullPointerException e) {
			return -1;
		}
	}
	
	public int highCount(Field field) {
		int ret = -1;
		for(int i: teams) {
			ret = Math.max(ret, count(i, field));
		}
		return ret;
	}
	
	/**
	 * Sets the control variable to allow checking
	 * * @param set
	 */
	public void setCheck(boolean set) {
		this.enableCheck = set;
	}

	/**
	 * Clears the board entirely back to nulls/0's. Should only be used when resetting a game.
	 */
	public void clear() {
		teams = null;
		values = null;
		rm = null;
		alive = 0;
		timer = 0;
		index = 0;
		enableCheck  = false;
	}

}
