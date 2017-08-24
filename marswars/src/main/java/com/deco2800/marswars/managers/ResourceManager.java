package com.deco2800.marswars.managers;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager {

	private static final String CLOSED = "closed.wav";
	private int rocks = 0;
	private int crystal = 0;
	private int water = 0;
	private int biomass = 0;

	/**
	 * Gets the number of rocks
	 * @return
	 */
	public int getRocks() {
		return rocks;
	}

	/**
	 * Sets the number of rocks
	 * @param rocks
	 */
	public void setRocks(int rocks) {
		if (this.rocks < rocks) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound(CLOSED);
		}
		this.rocks = rocks;
	}

	/**
	 * Gets the current crystal
	 * @return
	 */
	public int getCrystal() {
		return crystal;
	}

	/**
	 * Sets the current crystal
	 * @param crystal
	 */
	public void setCrystal(int crystal) {
		if (this.crystal < crystal) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound(CLOSED);
		}
		this.crystal = crystal;
	}

	/**
	 * Gets the current water
	 * @return
	 */
	public int getWater() {
		return water;
	}

	/**
	 * Sets the current water
	 * @param water
	 */
	public void setWater(int water) {
		if (this.water < water) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound(CLOSED);
		}
		this.water = water;
	}
	
	/**
	 * Gets the current biomass
	 * @return
	 */
	public int getBiomass() {
		return biomass;
	}

	/**
	 * Sets the current biomass
	 * @param water
	 */
	public void setBiomass(int biomass) {
		if (this.biomass < biomass) {
			SoundManager sound = (SoundManager) GameManager.get().getManager(SoundManager.class);
			sound.playSound(CLOSED);
		}
		this.biomass = biomass;
	}
}