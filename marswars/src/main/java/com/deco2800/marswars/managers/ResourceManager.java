package com.deco2800.marswars.managers;


import java.util.HashMap;
import java.util.Map;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager{
private static final String CLOSED = "closed.wav";
	private Map<Integer, Integer> rocks = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> crystal = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> water = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> biomass = new HashMap<Integer, Integer>();
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
			sound.playSound(CLOSED);
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
			sound.playSound(CLOSED);
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
			sound.playSound(CLOSED);
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
			sound.playSound(CLOSED);
		}
		this.biomass.put(team, biomass);
		}
		else {
			this.biomass.put(team, biomass);
		}
	}


	
	/* This class does not use the setAction(DecoAction)
	 * functions from HasOwner
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
	
}