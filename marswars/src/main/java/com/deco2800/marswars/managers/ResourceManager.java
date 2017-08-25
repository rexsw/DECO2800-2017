package com.deco2800.marswars.managers;

import java.util.Optional;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.HasOwner;

/**
 * Resource Manager
 *
 */
public class ResourceManager extends Manager implements HasOwner{
private static final String CLOSED = "closed.wav";
	private int rocks = 0;
	private int crystal = 0;
	private int water = 0;
	private int biomass = 0;
	
	// Each ResourceManager belongs to a Owner, i.e. each owner will have their own
	//  ResourceManager
	private Manager owner = null;

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
	
	/**
	 * Sets the ResourceManager's owner
	 * @param owner
	 */
	public void setOwner(Manager owner) {
		this.owner = owner;
	}

	/**
	 * Gets the ResourceManager's owner
	 * @return
	 */
	public Manager getOwner() {
		return this.owner;
	}

	/**
	 * 
	 * @param entity
	 * @return
	 */
	public boolean sameOwner(AbstractEntity entity) {
		if(entity instanceof HasOwner) {
			return this.owner == ((HasOwner) entity).getOwner();
		} else {
			return false;
		}
	}

	@Override
	public boolean isWorking() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setAction(DecoAction action) {
		// TODO Auto-generated method stub
		
	}
	
	/* This class does not use the isWorking() or setAction(DecoAction)
	 * functions from HasOwner
	 */
	
}