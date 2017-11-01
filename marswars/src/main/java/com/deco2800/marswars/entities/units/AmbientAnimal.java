package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * A class to be an ambient animals to populate the world
 * 
 * @author Michelle Mo
 *
 */
public class AmbientAnimal extends Soldier {
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	private AmbientState state;
	private int maxTravelTime;
	private int traveledTime;
	private int waitingTime;
	private ResourceType drop = ResourceType.BIOMASS;
	
	public enum AmbientState {
		DEFAULT, TRAVEL, ATTACKBACK
	}
	
	
	public AmbientAnimal(float posX, float posY, float posZ) {
		super(posX, posY, posZ, 0);
		maxTravelTime = 5;
		traveledTime = 0;
		state = AmbientState.DEFAULT;
		this.setOwner(0);
		this.setEntityType(EntityType.UNIT);
		this.name = "Ambient";
		
	}
	
	@Override
	/**
	 * set attributs
	 */
	public void setAttributes() {
		this.setMaxHealth(300);
		this.setHealth(300);
		this.setDamage(20);
		this.setArmor(0);
		this.setMaxArmor(0);
		this.setArmorDamage(20);
		this.setAttackRange(1);
		this.setAttackSpeed(1);
		this.setSpeed(0.01f);
	}

	/**
	 * attack the unit who attacked it
	 */
	public void attack() {
		List<BaseEntity> entityList = GameManager.get().getWorld().getEntities();
		List<AttackableEntity> enemy = new ArrayList<AttackableEntity>();
		//For each entity in the world
		for (BaseEntity e: entityList) {
			//If an attackable entity
			if (e instanceof AttackableEntity) {
				//Not owned by the same player
				AttackableEntity attackable = (AttackableEntity) e;
				if (!this.sameOwner(attackable)) {
					//Within attacking distance
					float diffX = attackable.getPosX() - this.getPosX();
					float diffY = attackable.getPosY() - this.getPosY();
					if (Math.abs(diffX) + Math.abs(diffY) <= this.getAttackRange()) {
						enemy.add((AttackableEntity) e);
					}
				}
			}
		}
		skirmishingBehaviour(enemy);
	}
	
	@Override
	public void setHealth(int health) {
		super.setHealth(health);
		if (health <= 0 ) {
			GameManager.get().getWorld().addEntity(new Resource(this.getPosX(), this.getPosY(), 0, 1f, 1f, getDrop()));
		}
	}
	
	/**
	 * moves the aniaml 
	 */
	public void move(){
		BaseWorld world = GameManager.get().getWorld();
		/* We are stuck on a tile with another entity
		 * therefore randomize a close by position and see if its a good
		 * place to move to
		 */
		Random r = new Random();
		/* Ensure new position is on the map */
		/* Check that the new position is free
		with the exception of Water entities */
		/* Finally move to that position using a move action */
		this.setAction(new MoveAction(r.nextInt((int) this.getPosX() + 10) % world.getWidth(),
				r.nextInt((int) this.getPosY() + 10)% world.getLength(), this));
	}
	
	
	/**
	 * gets the animals current state
	 * 
	 * @return the animals current ai state
	 */
	public AmbientState getState(){
		return state;
	}
	
	/**
	 * gets the max travel time for this unit
	 * 
	 * @return the animals max travel time 
	 */
	public int getMaxTravelTime(){
		return maxTravelTime;
	}
	
	/**
	 * returns how long the animal has been travlling 
	 * 
	 * @return the current time the animal has been travelling
	 */
	public int getTravelTime(){
		return traveledTime;
	}
	
	/**
	 * gets how long the animals waiting time is
	 * 
	 * @return the total time the animal should wait
	 */
	public int getWaitingTime(){
		return waitingTime;
	}
	
	/**
	 * sets the animals control state
	 * 
	 * @param newState AmbientState the state to set the animal to
	 */
	public void setState(AmbientState newState){
		state = newState;
	}
	
	/**
	 * sets how long the animal should wait
	 * 
	 * @param time int the new amount of time an animal should wait
	 */
	public void setWaitingTime(int time){
		waitingTime = time;
	}
	
	/**
	 * set how long the animal should travel for
	 * 
	 * @param time int the new amount of time an animal should travel
	 */
	public void setTravelTime(int time){
		this.traveledTime = time;
	}

	@Override
	public String toString(){
		return "Ambient Animal";
	}
	
	@Override
	public Optional<DecoAction> getCurrentAction() {
		return currentAction;
	}
	
	/**
	 * gets the animals stats 
	 */
	public EntityStats getStats() {
		return new EntityStats("Ambient Animal", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	
	@Override
	public void setAllTextture() {
		TextureManager tm = (TextureManager) GameManager.get().getManager(TextureManager.class);
		try {
			this.selectedTextureName = tm.loadUnitSprite(this, "default") ;
			this.defaultTextureName = tm.loadUnitSprite(this, "default") ;
			this.upleftTextureName = tm.loadUnitSprite(this, "upleft") ;
			this.uprightTextureName = tm.loadUnitSprite(this, "upright") ;
			this.downleftTextureName = tm.loadUnitSprite(this, "downleft") ;
			this.downrightTextureName = tm.loadUnitSprite(this, "downright") ;
			this.defaultMissileName = tm.loadUnitSprite(this, "missile");
			this.movementSound = "endturn.wav";
		}
		catch(NullPointerException n){
			LOGGER.error("setAlltexture has error");
			return;
		}
	}


	public ResourceType getDrop() {
		return drop;
	}


	public void setDrop(ResourceType drop) {
		this.drop = drop;
	}
	

}
