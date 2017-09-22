package com.deco2800.marswars.entities.units;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityStats;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.util.Point;
import com.deco2800.marswars.worlds.BaseWorld;



public class AmbientAnimal extends AttackableEntity{
	protected static final Logger LOGGER = LoggerFactory.getLogger(AttackableEntity.class);
	private AmbientState state;
	private int maxTravelTime;
	private int traveledTime;
	private int waitingTime;
	private String name;
	
	public static enum AmbientState {
		DEFAULT, TRAVEL, ATTACKBACK
	}
	
	public AmbientAnimal(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		super(posX, posY, posZ, 1, 1, 1);
		maxTravelTime = 5;
		traveledTime = 0;
		state = AmbientState.DEFAULT;
		this.setOwner(0);
		this.setEntityType(EntityType.UNIT);
		this.name = "Ambient";
		setDefaultAttributes();
		
	}
	
	public void setDefaultAttributes() {
		TechnologyManager t = (TechnologyManager) GameManager.get().getManager(TechnologyManager.class);
		this.setMaxHealth(t.getUnitAttribute(this.name, 1));
		this.setHealth(t.getUnitAttribute(this.name, 1));
		this.setDamage(t.getUnitAttribute(this.name, 2));
		this.setArmor(t.getUnitAttribute(this.name, 3));
		this.setMaxArmor(t.getUnitAttribute(this.name, 3));
		this.setArmorDamage(t.getUnitAttribute(this.name, 4));
		this.setAttackRange(t.getUnitAttribute(this.name, 5));
		this.setAttackSpeed(t.getUnitAttribute(this.name, 6));
		
		this.setSpeed(0.01f);
	}

	/**
	 * attack the unit who attacked it
	 */
	public void attack() {
		
	}
	
	public void move(){
		BaseWorld world = GameManager.get().getWorld();
		/* We are stuck on a tile with another entity
		 * therefore randomize a close by position and see if its a good
		 * place to move to
		 */
		Random r = new Random();
		Point p = new Point(this.getPosX() + r.nextInt(2) - 1, this.getPosY() + r.nextInt(2) - 1);
		/* Ensure new position is on the map */
		if (p.getX() < 0 || p.getY() < 0 || p.getX() >= world.getWidth() || p.getY() >= world.getLength()) {
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
	
	}
	
	public void setState(AmbientState newState){
		state = newState;
	}
	
	public AmbientState getState(){
		return state;
	}
	
	public int getMaxTravelTime(){
		return maxTravelTime;
	}
	public int getTravelTime(){
		return traveledTime;
	}
	
	public int getWaitingTime(){
		return waitingTime;
	}
	
	public void setWaitingTime(int time){
		waitingTime = time;
	}
	
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
	
	public EntityStats getStats() {
		return new EntityStats("Ambient Animal", this.getHealth(),this.getMaxHealth(), null, this.getCurrentAction(), this);
	}
	

}
