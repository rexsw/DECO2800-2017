package com.deco2800.marswars;

import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.hud.MiniMap;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttackableEntityTest {
	AttackableEntity test;
	AttackableEntity constructor2Test;
	AttackableEntity constructor3Test;
	AttackableEntity enemy;
	Box3D position;
	
	@Before
	public void initialise() {
		GameManager.get().setWorld(new BaseWorld(50, 50));
		test = new AttackableEntity(1, 1, 1, 1, 1, 1);
		test.setOwner(-1);
		enemy = new AttackableEntity(10, 10, 10, 1, 1, 1);
		enemy.setOwner(1);
		constructor2Test = new AttackableEntity(new Box3D(5, 5, 5, 2, 2, 2), 2, 2, true);
		constructor2Test.setOwner(0);
		position = new Box3D(15, 15, 15, 2, 3, 1);
		constructor3Test = new AttackableEntity(position, 2, 3, true);
		constructor3Test.setOwner(1);
	}
	
	@Test
	public void testConstructors() {
		assertEquals(test.getPosX(), 1, 0.1);
		assertEquals(test.getPosY(), 1, 0.1);
		assertEquals(test.getPosZ(), 1, 0.1);
		assertEquals(test.getXLength(), 1, 0.1);
		assertEquals(test.getYLength(), 1, 0.1);
		assertEquals(test.getZLength(), 1, 0.1);
		assertEquals(test.getAttackRange(), 0);
		assertEquals(test.getArmor(), 0);
		assertEquals(test.getMaxArmor(), 0);
		assertEquals(test.getAttackSpeed(), 0);
		assertEquals(test.showProgress(), false);
		assertEquals(test.getOwner(), -1);
		assertEquals(test.getDamageDeal(), 0);
		assertEquals(test.getMaxHealth(), 0);
		assertEquals(test.getHealth(), 0);
		assertEquals(test.getCurrentAction(), Optional.empty());
		assertEquals(test.showProgress(), false);
		assertEquals(test.getArmorDamage(), 0);
		
		assertEquals(constructor2Test.getPosX(), 5, 0.1);
		assertEquals(constructor2Test.getPosY(), 5, 0.1);
		assertEquals(constructor2Test.getPosZ(), 5, 0.1);
		assertEquals(constructor2Test.getXLength(), 2, 0.1);
		assertEquals(constructor2Test.getYLength(), 2, 0.1);
		assertEquals(constructor2Test.getZLength(), 2, 0.1);
		assertEquals(constructor2Test.getAttackRange(), 0);
		assertEquals(constructor2Test.getArmor(), 0);
		assertEquals(constructor2Test.getMaxArmor(), 0);
		assertEquals(constructor2Test.getAttackSpeed(), 0);
		assertEquals(constructor2Test.showProgress(), false);
		assertEquals(constructor2Test.getOwner(), 0);
		assertEquals(constructor2Test.getDamageDeal(), 0);
		assertEquals(constructor2Test.getMaxHealth(), 0);
		assertEquals(constructor2Test.getHealth(), 0);
		assertEquals(constructor2Test.getCurrentAction(), Optional.empty());
		assertEquals(constructor2Test.showProgress(), false);
		assertEquals(constructor2Test.getArmorDamage(), 0);
		
		assertEquals(constructor3Test.getPosX(), 15, 0.1);
		assertEquals(constructor3Test.getPosY(), 15, 0.1);
		assertEquals(constructor3Test.getPosZ(), 15, 0.1);
		assertEquals(constructor3Test.getXLength(), 2, 0.1);
		assertEquals(constructor3Test.getYLength(), 3, 0.1);
		assertEquals(constructor3Test.getZLength(), 1, 0.1);
		assertEquals(constructor3Test.getAttackRange(), 0);
		assertEquals(constructor3Test.getArmor(), 0);
		assertEquals(constructor3Test.getMaxArmor(), 0);
		assertEquals(constructor3Test.getAttackSpeed(), 0);
		assertEquals(constructor3Test.showProgress(), false);
		assertEquals(constructor3Test.getOwner(), 1);
		assertEquals(constructor3Test.getDamageDeal(), 0);
		assertEquals(constructor3Test.getMaxHealth(), 0);
		assertEquals(constructor3Test.getHealth(), 0);
		assertEquals(constructor3Test.getCurrentAction(), Optional.empty());
		assertEquals(constructor3Test.showProgress(), false);
		assertEquals(constructor3Test.getArmorDamage(), 0);
	}


	@Test
	public void testSetAttackRange() {
		test.setAttackRange(20);
		assertEquals(test.getAttackRange(), 20);
	}

	@Test
	public void testSetArmor() {
		test.setMaxArmor(100);
		test.setArmor(100);
		assertEquals(test.getArmor(), 100);
		test.setArmor(-100);
		assertEquals(test.getArmor(), 0);
	}

	@Test
	public void testSetMaxArmor() {
		test.setMaxArmor(100);
		assertEquals(test.getMaxArmor(), 100);
		test.setMaxArmor(0);
		assertEquals(test.getMaxArmor(), 0);
	}

	@Test
	public void testSetDamage() {
		test.setDamage(100);
		assertEquals(test.getDamageDeal(), 100);
		test.setDamage(0);
		assertEquals(test.getDamageDeal(), 0);
	}

	@Test
	public void testSetMaxHealth() {
		test.setMaxHealth(100);
		assertEquals(test.getMaxHealth(), 100);
		test.setMaxHealth(0);
		assertEquals(test.getMaxHealth(), 0);
	}

	@Ignore
	@Test
	public void testSetHealth() {
		test.setMaxHealth(100);
		test.setHealth(100);
		
		assertEquals(test.getHealth(), 100);
		GameManager.get().setMiniMap(new MiniMap());
		GameManager.get().getWorld().addEntity(test);
		List<BaseEntity> alive = GameManager.get().getWorld().getEntities();
		assertEquals(true, alive.contains(test));
		test.setHealth(0);
		assertEquals(test.getHealth(), 0);
		alive = GameManager.get().getWorld().getEntities(); //check that test gets removed when hp drops to 0 or below.
		assertEquals(false, alive.contains(test));
	}

	@Test
	public void testSetAction() {
		AttackAction act = new AttackAction(test, enemy);
		test.setAction(act);
		assertEquals(test.getCurrentAction().get(), act);
		assertEquals(test.showProgress(), true);
		test.setEmptyAction();
		assertEquals(test.getCurrentAction(), Optional.empty());
		assertEquals(test.showProgress(), false);
	}

	@Test public void testAttackAction(){
		AttackAction act = new AttackAction(test, enemy);
		assertTrue(act.actionProgress()==0);
		assertFalse(act.completed());
	}

	@Test
	public void testSetOwner() {
		test.setOwner(-1);
		assertEquals(test.getOwner(), -1);
		enemy.setOwner(-1);
		assertEquals(test.sameOwner(enemy), true);
		assertEquals(test.sameOwner(constructor2Test), false);
	}

	
	@Test
	public void testSetArmorDamage() {
		test.setArmorDamage(100);
		assertEquals(test.getArmorDamage(), 100);
		test.setArmorDamage(0);
		assertEquals(test.getArmorDamage(), 0);
	}

	@Test
	public void testSetAttackSpeed() {
		test.setAttackSpeed(20);
		assertEquals(test.getAttackSpeed(), 20);
	}
	
	@Test
	public void testSetLoyalty() {
		Soldier soldier = new Soldier(1, 0, 0, 1);
		Carrier carrier = new Carrier(1, 0, 0, 1);
		int maxLoyalty = carrier.getMaxLoyalty();
		carrier.setLoyalty(maxLoyalty + 1);
		Assert.assertTrue(carrier.getLoyalty() == maxLoyalty);
	}

}
