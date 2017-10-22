package com.deco2800.marswars.entities.units;

import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasAction;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.Optional;

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
    protected String missileTexture;
    protected String upleftMissileTexture;
    protected String uprightMissileTexture;
    protected String downleftMissileTexture;
    protected String downrightMissileTexture;
    private int areaDamage;
    private AttackableEntity ownerEntity;
   
    public MissileEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                         AttackableEntity target, int damage, int armorDamage, String missileTexture, int areaDamage, int owner, AttackableEntity ownerEntity) {
        super(posX, posY, posZ, xLength, yLength, zLength);
        this.setAllMissileTextture();
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
    
    public void setOwnerEntity(AttackableEntity ownerEntity) {
    	this.ownerEntity = ownerEntity;
    }
    
    public AttackableEntity getOwnerEntity() {
    	return  ownerEntity;
    }
    
    public void faceTowards(float x, float y) {
		if(this.getPosX()>=x && this.getPosY()>=y) {
			this.setMissileTexture(downleftMissileTexture);
		}
		else if(this.getPosX()>=x && this.getPosY()<y) {
			this.setMissileTexture(downrightMissileTexture);
		}
		else if(this.getPosX()<x && this.getPosY()>=y) {
			this.setMissileTexture(upleftMissileTexture);
		}
		else if(this.getPosX()<x && this.getPosY()<y) {
			this.setMissileTexture(uprightMissileTexture);
		}
		else {
			this.setMissileTexture(missileTexture);
		}
	}
    
    public void setAllMissileTextture() {
		TextureManager tm = (TextureManager) GameManager.get().getManager(TextureManager.class);
		try {
					
			this.upleftMissileTexture =tm.loadUnitSprite(this, "upleft") ;
			this.uprightMissileTexture =tm.loadUnitSprite(this, "upright") ;
			this.downleftMissileTexture =tm.loadUnitSprite(this, "downleft") ;
			this.downrightMissileTexture =tm.loadUnitSprite(this, "downright") ;
			this.missileTexture = tm.loadUnitSprite(this, "missile");
			
		}
		catch(NullPointerException n){
			return;
		}
	}

}
