package com.deco2800.marswars.entities;

import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;

import com.deco2800.marswars.actions.ActionType;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Code coverage and Junit tests for the Carrier class
 * 
 * @author jdtran21
 * @co-author treenhan
 *
 */
public class CarrierTest {

	/**
	 * initialize everything needed by the tests
	 */
	@Before
	public void init(){
		GameManager.get().setWorld(new BaseWorld("resources/mapAssets/tinyMars.tmx"));
	}


	@Test @Ignore
	public void carrier() {
		Carrier carry = new Carrier(0, 0, 0, 0);	
		Soldier soljaBoy = new Soldier(1, 1, 1, 1);
		carry.load(soljaBoy);
		carry.getPassengers();		
		carry.getStats();		
		ActionType nextAction = ActionType.MOVE;
		carry.setNextAction(nextAction);
	}

	@Test @Ignore
	public void onRightClickTest(){
		Carrier carry = new Carrier(0, 0, 0, 0);
		carry.onRightClick(1f,1f);
		carry.onRightClick(200f,200f);
	}

	@Test @Ignore
	public void unloadAction(){
		Carrier carry = new Carrier(0, 0, 0, 0);
		carry.unload();
		carry.unloadPassenger();
	}


}
