package com.deco2800.marswars;

import com.badlogic.gdx.Input;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.functionkeys.ShortCut;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 
 * @author 
 * @co-author haoxuan
 *
 */

public class ShortCutTest {
	
	private ShortCut shortCut;
	
	@Before
	public void setup() {
		GameManager.get().setWorld(new BaseWorld(10,10));
		GameManager.get().setMiniMap(null);
		shortCut = new ShortCut();
	}

	@Test
	public void testShortCutAdd() {
		shortCut.addKey(Input.Keys.A);
	}
	
	@Test
	public void testShortCutRemove() {
		shortCut.addKey(Input.Keys.A);
		shortCut.removeKey(Input.Keys.A);
	}
	
	@Test
	public void testAddExtraSpacman() {
		shortCut.addKey(Input.Keys.G);
		shortCut.addExtraSpacMan();
		shortCut.removeKey(Input.Keys.G);
		Astronaut man = new Astronaut(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(man));
	}
	
	@Test
	public void testAddAiSpacMan() {
		shortCut.addKey(Input.Keys.G);
		shortCut.addKey(Input.Keys.CONTROL_LEFT);
		shortCut.addExtraAiSpacMan();
		shortCut.removeKey(Input.Keys.G);
		shortCut.removeKey(Input.Keys.CONTROL_LEFT);
		Astronaut man = new Astronaut(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(man));
		assertEquals(0, ((Astronaut) GameManager.get().getWorld().getEntities().get(0)).getOwner());
	}
	
	@Test
	public void testAddExtraTank() {
		shortCut.addKey(Input.Keys.K);
		shortCut.addExtraTank();
		shortCut.removeKey(Input.Keys.K);
		Tank t = new Tank(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(t));
		assertEquals(-1, ((Tank) GameManager.get().getWorld().getEntities().get(0)).getOwner());
	}
	
	@Test
	public void testAddExtraSoldier() {
		shortCut.addKey(Input.Keys.J);
		shortCut.addExtraSoldier();
		shortCut.removeKey(Input.Keys.J);
		Soldier s = new Soldier(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(s));
		assertEquals(-1, ((Soldier) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}
	
	@Test
	public void testAddExtraSniper() {
		shortCut.addKey(Input.Keys.I);
		shortCut.addExtraSniper();
		shortCut.removeKey(Input.Keys.I);
		Sniper sn = new Sniper(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(sn));
		assertEquals(-1, ((Sniper) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}
	
	@Test
	public void testAddExtraHacker() {
		shortCut.addKey(Input.Keys.L);
		shortCut.addExtraHacker();
		shortCut.removeKey(Input.Keys.L);
		Hacker h = new Hacker(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(h));
		assertEquals(-1, ((Hacker) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}
	
	@Test @Ignore
	public void testAddExtraMedic() {
		shortCut.addKey(Input.Keys.P);
		shortCut.addExtraSoldier();
		shortCut.removeKey(Input.Keys.P);
		Medic m = new Medic(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(m));
		assertEquals(-1, ((Medic) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}
	
	@Test @Ignore
	public void testAddExtraCarrier() {
		shortCut.addKey(Input.Keys.R);
		shortCut.addExtraSoldier();
		shortCut.removeKey(Input.Keys.R);
		Carrier c = new Carrier(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(c));
		assertEquals(-1, ((Carrier) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}

	@Test @Ignore
	public void testAddExtraTankDestroyer() {
		shortCut.addKey(Input.Keys.O);
		shortCut.addExtraSoldier();
		shortCut.removeKey(Input.Keys.O);
		TankDestroyer td = new TankDestroyer(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/2,0,-1);
		assertEquals(true, GameManager.get().getWorld().getEntities().contains(td));
		assertEquals(-1, ((TankDestroyer) GameManager.get().getWorld().getEntities().get(0)).getOwner());		
	}
}
