package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * A class to be an ambient animals to populate the world
 * 
 * @author Michelle Mo
 *
 */
public class AmbientAnimal extends Soldier{
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	private AmbientState state;
	private int maxTravelTime;
	private int traveledTime;
	private int waitingTime;
	private String name;
	
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
		setDefaultAttributes();
		
	}
	
	
	/**
	 * 
	 */
	public void setDefaultAttributes() {
		TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
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
		Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);
		/* Ensure new position is on the map */
		if (p.getX() < 0 || p.getY() < 0 
				|| p.getX() >= world.getWidth() 
				|| p.getY() >= world.getLength()) {
			return;
		}
		/* Check that the new position is free
		with the exception of Water entities */
		List<BaseEntity> tileEntities =
				world.getEntities((int)p.getX(), (int)p.getY());

		if (this.moveAway(tileEntities)) {
			// No good
			return;
		}
		LOGGER.info("Aniaml is on a tile with another entity, move out of the way");
		/* Finally move to that position using a move action */
		currentAction = Optional.of(new MoveAction((int)p.getX(), (int)p.getY(), this));
		this.setAction(new MoveAction((int)p.getX(), (int)p.getY(), this));
	
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
			this.selectedTextureName = null;
			this.defaultTextureName =tm.loadUnitSprite(this, "default") ;
			this.upleftTextureName =tm.loadUnitSprite(this, "upleft") ;
			this.uprightTextureName =tm.loadUnitSprite(this, "upright") ;
			this.downleftTextureName =tm.loadUnitSprite(this, "downleft") ;
			this.downrightTextureName =tm.loadUnitSprite(this, "downright") ;
			this.defaultMissileName = null;
			this.movementSound = "endturn.wav";
		}
		catch(NullPointerException n){
			LOGGER.error("setAlltexture has error");
			return;
		}
	}
	

}
