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
public class MissileEntity extends BaseEntity implements HasDamage, HasOwner,
        HasAction {

    private int armorDamage; // armorDamage of the entity
    private int damage; // the damage of the entity
    private float speed;
    private Manager owner = null; // the owner of the player
    private Optional<DecoAction> currentAction = Optional.empty();
    private AttackableEntity target; //Missile should only be created once target is confirmed viable target

    private static final Logger LOGGER = LoggerFactory.getLogger(MissileEntity.class);

    public MissileEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                         AttackableEntity target, int damage, int armorDamage) {
        super(posX, posY, posZ, xLength, yLength, zLength);
        this.modifyCollisionMap(true);
        this.target = target;
        this.damage = damage;
        this.armorDamage = armorDamage;
    }

    public MissileEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
                         float xRenderLength, float yRenderLength, boolean centered, AttackableEntity target,
                         int damage, int armorDamage) {
        super(posX, posY, posZ, xLength, yLength, zLength, xRenderLength, yRenderLength, centered);
        this.target = target;
        this.damage = damage;
        this.armorDamage = armorDamage;
    }

    @SuppressWarnings("deprecation")
    public MissileEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered, AttackableEntity target,
                         int damage, int armorDamage) {
        super(position, xRenderLength, yRenderLength, centered);
        this.target = target;
        this.damage = damage;
        this.armorDamage = armorDamage;
        // TODO Auto-generated constructor stub
    }

    /**
     * Return the damage of the entity
     * @return the damage of the entity
     */
    @Override
    public int getDamageDeal() {
        return damage;
    }

    /**
     * Set the damage of the entity
     * @param the damage of the entity
     */
    @Override
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Set the armor damage of the entity
     * @param the new armor damage of the entity
     */
    @Override
    public void setArmorDamage(int armorDamage) {
        this.armorDamage = armorDamage;
    }

    /**
     * Return the armor damage of the entity
     * @return the armor damage of the entity
     */
    @Override
    public int getArmorDamage() {
        return armorDamage;
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

    /**
     * Set the owner of the entity
     * @param the owner of the entity
     */
    @Override
    public void setOwner(Manager owner) {
        this.owner = owner;
    }

    /**
     * Return the owner of the entity
     * @return the owner of the entity
     */
    @Override
    public Manager getOwner() {
        return this.owner;
    }

    /**
     * Check if an entity shares the same owner
     * @param an entity
     * @return true if the parameter shares the same owner, false otherwise
     */
    @Override
    public boolean sameOwner(AbstractEntity entity) {
        boolean isInstance = entity instanceof HasOwner;
        return isInstance && this.owner == ((HasOwner) entity).getOwner();
    }

    /**
     * Check if the entity currently has an action
     * @return true if there is an ongoing action
     */
    @Override
    public boolean isWorking() { return currentAction.isPresent(); }

    /**
     * Set a new action for an entity
     * @param an action for the entity to take
     */
    @Override
    public void setAction(DecoAction action) {
        currentAction = Optional.of(action);
    }

    /**
     * Return the target of the missile
     * @return the target for the missile
     */
    public AttackableEntity getTarget() { return target; }

    /**
     * Set a new target for the missile to attack
     * @param an new target for the missile
     */
    public void setTarget(AttackableEntity target) { this.target = target; }
    
    public float getSpeed() { return speed; }
    
    public void setSpeed(float speed) { this.speed = speed; }

}
