package com.deco2800.marswars.entities.units;

import java.util.Optional;

import com.deco2800.marswars.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.managers.Manager;
import com.deco2800.marswars.util.Box3D;

/**
 * @author Vinson Yeung on 25/8/17
 *
 */
public class MissileEntity extends BaseEntity implements HasAction{

    private int armorDamage; // armorDamage of the entity
    private int damage; // the damage of the entity
    private float speed;
    private Optional<DecoAction> currentAction = Optional.empty();
    private AttackableEntity target; //Missile should only be created once target is confirmed viable target
    private String missileTexture;
    private int areaDamage;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MissileEntity.class);

    public MissileEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                         AttackableEntity target, int damage, int armorDamage, String missileTexture, int areaDamage, int owner) {
        super(posX, posY, posZ, xLength, yLength, zLength);
        this.modifyCollisionMap(true);
    }

    /**
     * Return the damage of the entity
     * @return the damage of the entity
     */
    public int getDamageDeal() {
        return damage;
    }
    
    /**
     * Set the damage
     */
    public void setDamage(int damage) {
    	this.damage = damage;
    }

    /**
     * Return the armor damage of the entity
     * @return the armor damage of the entity
     */
    public int getArmorDamage() {
        return armorDamage;
    }
    
    /**
     * Set armor damage
     */
    public void setArmorDamage(int armorDamage) {
    	this.armorDamage = armorDamage;
    }

    @Override
    public Optional<DecoAction> getCurrentAction() {
        return currentAction;
    }

    /**
     * Removes any current action and set action to empty
     */
    public void setEmptyAction() {
        currentAction = Optional.empty();
    }
    
    public String getMissileTexture() {
    	return missileTexture;
    }
    
    public void setMissileTexture(String missileTexture) {
    	this.missileTexture = missileTexture;
    }
    
    public int getareaDamage() {
    	return areaDamage;
    }
    
    public void setareaDamage(int area) {
    	this.areaDamage = area;
    }

    /**
     * Set a new action for an entity
     * @param action for the entity to take
     */
    @Override
    public void setAction(DecoAction action) {
        currentAction = Optional.of(action);
    }

    /**
     * Return the target of the missile
     * @return the target for the missile
     */
    public AttackableEntity getTarget() { 
    	return target; 
    }

    /**
     * Set a new target for the missile to attack
     * @param target for the missile
     */
    public void setTarget(AttackableEntity target) { 
    	this.target = target; 
    }
    
    public float getSpeed() { 
    	return speed; 
    }
    
    public void setSpeed(float speed) { 
    	this.speed = speed; 
    }
    

}
