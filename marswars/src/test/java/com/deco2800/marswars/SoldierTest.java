package com.deco2800.marswars;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Soldier;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Code coverage and Junit tests for the Soldier class
 * 
 * @author jdtran21
 *
 */
public class SoldierTest {
	Soldier soldier;
	Soldier soldier2;
	Soldier enemySoldier;
	List<AttackableEntity> enemy;
	
	/**
	 * Initializes the soldiers to be tested
	 */
	@Before
	public void createSoldier() {
		soldier = new Soldier(0, 0, 0, 0);
		soldier.setOwner(0);
		soldier.setPosX(1);
		soldier.setPosY(1);
		soldier.setPosZ(1);				
		soldier2 = new Soldier(0, 0, 0, 0);
		soldier2.setOwner(0);
		soldier2.setPosition(1, 1, 1);		
		enemySoldier = new Soldier(1, 2, 3, 4);
		enemySoldier.setOwner(1);
		enemySoldier.setPosX(1);
		enemySoldier.setPosY(1);
		enemySoldier.setPosZ(1);	
		enemy = new ArrayList<AttackableEntity>();
		enemy.add(enemySoldier);
	}
	
	/**
	 * Covers the combat features of the Soldier class
	 */
	@Test
	public void soldierCombat() {
		soldier.attack(soldier2);
		soldier.attackDefensively(soldier2);
		assertThat("You cannot attack a friendly unit", soldier.setTargetType(soldier2), is(equalTo(false)));
		soldier.attack(enemySoldier);
		soldier.getStances(enemy);
		soldier.defensiveBehaviour(enemy);
		soldier.skirmishingBehaviour(enemy);
		soldier.timidBehaviour(enemy);
	}
	
	/**
	 * Covers the other features of the soldier Soldier
	 */
	@Test
	public void soldierMisc() {
		soldier.faceTowards(0, 0);
		soldier.faceTowards(0, 5);
		soldier.faceTowards(5, 0);
		soldier.faceTowards(5, 5);		
		soldier.resetTexture();
		assertThat("solider is not soldier", soldier.toString(), is(equalTo("Soldier")));
		//assertThat("Wrong DefaultName", soldier.getDefaultTexture(), is(equalTo(null)));
		soldier.getStats();
		//assertThat("Wrong MissileName", soldier.getMissileTexture(), is(equalTo(null)));
		soldier.checkOwnerChange();
		soldier.regeneration();
		ActionType nextAction = ActionType.MOVE;
		soldier.setNextAction(nextAction);
		Optional<DecoAction> decoAction = null;
		soldier.setCurrentAction(decoAction);
	}
}
