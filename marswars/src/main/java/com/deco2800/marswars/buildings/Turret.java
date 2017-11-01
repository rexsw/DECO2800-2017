package com.deco2800.marswars.buildings;

import com.badlogic.gdx.audio.Sound;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.SoundManager;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * Created by grumpygandalf on 25/9/17.
 *
 * A turret that can be used for base defense
 */

public class Turret extends BuildingEntity {
	
	int totalLoaded = 0;
	/**
	 * Constructor for the turret.
	 * @param world The world that will hold the turret.
	 * @param posX its x position on the world.
	 * @param posY its y position on the world.
	 * @param posZ its z position on the world.
	 */
	public Turret(AbstractWorld world, float posX, float posY, float posZ, int owner) {
		super(posX, posY, posZ, BuildingType.TURRET, owner);
		this.defaultMissileName = "missileturret";
		setStance(2);
		if (this.isAi()) {
			this.setDamage(90);
			canAttack = true;
		}
	}
	
    /**
     * Increases damage * 2 of turret
     */
	public void powerUpTurret(){
		this.setDamage(this.getDamageDeal()*2);
	}
	
    /**
     * Adds loaded target into list of loaded units
     * 
     * @param target
     * @return true if able to load the target, false otherwise
     */
    public boolean loadAstronaut(Astronaut target) {
	try {
	    SoundManager sound = (SoundManager) GameManager.get()
			.getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound("carrier-loading-sound.mp3");
		sound.playSound(loadedSound);
	} catch (NullPointerException e) {
	}
	if (totalLoaded< 5) {
		target.setHealth(-1);
		totalLoaded++;
		this.setDamage(45 * totalLoaded);
		canAttack = true;
		return true;
	    }
	return false;
    }
    
    /**   
    * Unloads astronauts from turret
    * 
    * @param target
    * @return true if able to unload astronauts false otherwise
    */
   public boolean unloadAstronauts() {
	try {
	    SoundManager sound = (SoundManager) GameManager.get()
			.getManager(SoundManager.class);
		Sound loadedSound = sound.loadSound("carrier-loading-sound.mp3");
		sound.playSound(loadedSound);
	} catch (NullPointerException e) {
	}
	if (totalLoaded >= 1) {
		Astronaut astro = new Astronaut(this.getPosX(), this.getPosY(), 0, this.getOwner());
		GameManager.get().getWorld().addEntity(astro);
		totalLoaded--;
		this.setDamage(45 * totalLoaded);
		if (totalLoaded == 0) {
			canAttack = false;
		}
		return true;
	}
	return false;
   }
}
