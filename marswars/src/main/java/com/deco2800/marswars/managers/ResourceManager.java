package com.deco2800.marswars.managers;


import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.buildings.Bunker;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.Soldier;

import java.util.HashMap;
import java.util.Map;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager implements TickableManager{
private static final String CLOSED = "closed.wav";
	private Map<Integer, Integer> rocks = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> crystal = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> water = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> biomass = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> population = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> maxPopulation = new HashMap<Integer, Integer>();
	/**
	 * Gets the number of rocks
	 * @return
	 */
	public int getRocks(int team) {
		if(this.rocks.containsKey(team)) {
		return rocks.get(team);
		}
		return -1;
	}

	/**
	 * Sets the number of rocks
	 * @param rocks
	 */
	public void setRocks(int rocks, int team) {
		if(this.rocks.containsKey(team)) {
		if (this.rocks.get(team) < rocks) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.rocks.put(team, rocks);
		}
		else {
			this.rocks.put(team, rocks);
		}
	}

	/**
	 * Gets the current crystal
	 * @return
	 */
	public int getCrystal(int team) {
		if(this.crystal.containsKey(team)) {
		return crystal.get(team);
		}
		return -1;
	}

	/**
	 * Sets the current crystal
	 * @param crystal
	 */
	public void setCrystal(int crystal, int team) {
		if(this.crystal.containsKey(team)) {
		if (this.crystal.get(team) < crystal) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.crystal.put(team, crystal);
	}
		else {
			this.crystal.put(team, crystal);
		}
	}
	

	/**
	 * Gets the current water
	 * @return
	 */
	public int getWater(int team) {
		if(this.water.containsKey(team)) {
		return water.get(team);
		}
		return -1;
	}

	/**
	 * Sets the current water
	 * @param water
	 */
	public void setWater(int water, int team) {
		if(this.water.containsKey(team)) {
		if (this.water.get(team) < water) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.water.put(team, water);
	}
	else {
		this.water.put(team, water);
	}
	}
	
	/**
	 * Gets the current biomass
	 * @return
	 */
	public int getBiomass(int team) {
		if(this.biomass.containsKey(team)) {
		return biomass.get(team);
		}
		return -1;
	}

	/**
	 * Sets the current biomass
	 * @param biomass
	 * @param team
	 */
	public void setBiomass(int biomass, int team) {
		if(this.biomass.containsKey(team)) {
		if (this.biomass.get(team) < biomass) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.biomass.put(team, biomass);
		}
		else {
			this.biomass.put(team, biomass);
		}
	}


	
	/**
	 * test if a team as reached the resource win condition
	 * 
	 * @return int a non zero teamid if there a winer else 0 if no winer
	 */
	public int CappedTeam() {
		for(int teamid:rocks.keySet()) {
			if(rocks.get(teamid) > 400 && biomass.get(teamid) > 400 &&
					crystal.get(teamid) > 400 && water.get(teamid) > 400) {
				return teamid;
			}
		}
		return 0;
	}
	
	/**
	 * Gets the population
	 * @param team
	 * @return
	 */
	public int getPopulation(int team) {
		if(this.population.containsKey(team)) {
		return population.get(team);
		}
		return -1;
	}

	/**
	 * Sets the current population
	 * @param population
	 * @param team
	 */
	public void setPopulation(int population, int team) {
		if(this.population.containsKey(team)) {
		if (this.population.get(team) < population) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.population.put(team, population);
		}
		else {
			this.population.put(team, population);
		}
	}
	
	/**
	 * Gets the population limit
	 * @param team
	 * @return gets the max population
	 */
	public int getMaxPopulation(int team) {
		if(this.maxPopulation.containsKey(team)) {
		return maxPopulation.get(team);
		}
		return -1;
	}

	/**
	 * Sets the current population limit
	 * @param population
	 * @param team
	 */
	public void setMaxPopulation(int population, int team) {
		if(this.maxPopulation.containsKey(team)) {
		if (this.maxPopulation.get(team) < population) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			Sound loadedSound = sound.loadSound(CLOSED);
			sound.playSound(loadedSound);
		}
		this.maxPopulation.put(team, population);
		}
		else {
			this.maxPopulation.put(team, population);
		}
	}

	/**
	 * This will update number of units belonging to each team
	 * this should use the balckboard it'd be more effective i'll swtich it over later
	 * @param i
	 */
	@Override
	public void onTick(long i) {
		for (int key : maxPopulation.keySet()) {
			int popCount = 0;
			int maxPopCount = 10;
			for (BaseEntity b : GameManager.get().getWorld().getEntities()) {
				if (b instanceof Soldier && b.getOwner() == key) {
					popCount ++;
				}
			}
			for (BaseEntity b : GameManager.get().getWorld().getEntities()) {
				if (b instanceof Bunker && b.getOwner() == key) {
					Bunker bunker = (Bunker)b;
					if(bunker.getBuilt()) {
						maxPopCount += 5;
					}
				}
			}
			setPopulation(popCount, key);
			setMaxPopulation(maxPopCount, key);
		}
	}
	
}