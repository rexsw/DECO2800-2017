package com.deco2800.marswars.entities;

import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.entities.units.Soldier;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

//import com.deco2800.marswars.managers.GameManager;
//import com.deco2800.marswars.worlds.BaseWorld;
//import org.junit.Before;

/**
 * Code coverage and Junit tests for the Carrier class
 * 
 * @author jdtran21
 * @co-author treenhan hwkhoo
 *
 */
public class CarrierTest {

	/**
	 * initialize everything needed by the tests
	 */
//	@Before
//	public void init(){
//		GameManager.get().setWorld(new BaseWorld("resources/mapAssets/tinyMars.tmx"));
//	}

    	@Test
	public void constructorTest() {
	    Carrier carrier = new Carrier(1, 0, 0 , 1);
	    Assert.assertTrue(carrier != null);
	    Assert.assertTrue(carrier.getLoadStatus() == 2);
	}
	
        @Test
        public void getStatTest() {
            Carrier carrier = new Carrier(0, 0, 0, 1);
            Assert.assertTrue(carrier.getStats() != null);
            Assert.assertEquals(1000, carrier.getStats().getHealth());
            Assert.assertEquals(1000, carrier.getStats().getMaxHealth());
            Assert.assertEquals("Carrier", carrier.getStats().getName());
        }
        
        @Test
        public void emptyCarrier() {
            Carrier carrier = new Carrier(0, 0, 0, 1);
            Assert.assertTrue(carrier.getPassengers()[0] == null);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
        }
        
	@Test
	public void loadTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 0);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	}
        
	@Test
	public void loadTestDifferentTeam() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 2);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	}
	
	@Test
	public void setActionTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 2);
	    carrier.setNextAction(ActionType.MOVE);
	    Assert.assertFalse(carrier.getCurrentAction().isPresent());
	    carrier.load(soldier1);
	    carrier.setNextAction(ActionType.LOAD);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	    carrier.setNextAction(ActionType.UNLOAD);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
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
