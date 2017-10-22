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
	public void loadandunloadTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 0);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	    carrier.unloadIndividual();
	    carrier.unload();
	}
	
	@Test
	public void loadTestDifferentTeam() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 2);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	}
	@Test
	public void unloadTest() {
	    
	}
        
	@Test
	public void loadPassengerTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 2, 0);
	    Soldier soldier2 = new Soldier(1, 2, 1, 0);
	    Soldier soldier3 = new Soldier(1, 1, 3, 0);
	    Soldier soldier4 = new Soldier(1, 3, 1, 0);
	    Soldier soldier5 = new Soldier(1, 3, 1, 0);
	    carrier.loadPassengers(soldier1);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
	    carrier.loadPassengers(soldier2);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
	    carrier.loadPassengers(soldier3);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == soldier3);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
	    carrier.loadPassengers(soldier4);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == soldier3);
            Assert.assertTrue(carrier.getPassengers()[3] == soldier4);
            Assert.assertFalse(carrier.loadPassengers(soldier5));
	}
	
	@Test
	public void unloadAllTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 2, 0);
	    Soldier soldier2 = new Soldier(1, 2, 1, 0);
	    Soldier soldier3 = new Soldier(1, 1, 3, 0);
	    Soldier soldier4 = new Soldier(1, 3, 1, 0);
	    carrier.loadPassengers(soldier1);
	    carrier.loadPassengers(soldier2);
	    carrier.loadPassengers(soldier3);
	    carrier.loadPassengers(soldier4);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == soldier3);
            Assert.assertTrue(carrier.getPassengers()[3] == soldier4);
            carrier.unloadPassenger();
            Assert.assertTrue(carrier.getPassengers()[0] == null);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
	}
	
	@Test
	public void unloadAllEmptyTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Assert.assertFalse(carrier.unloadPassenger());
	}
	
	@Test 
	public void unloadSingleEmptyTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Assert.assertFalse(carrier.unloadPassengerIndividual());
	}
	
	@Test
	public void unloadSingleTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 2, 0);
	    Soldier soldier2 = new Soldier(1, 2, 1, 0);
	    Soldier soldier3 = new Soldier(1, 1, 3, 0);
	    Soldier soldier4 = new Soldier(1, 3, 1, 0);
	    carrier.loadPassengers(soldier1);
	    carrier.loadPassengers(soldier2);
	    carrier.loadPassengers(soldier3);
	    carrier.loadPassengers(soldier4);
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == soldier3);
            Assert.assertTrue(carrier.getPassengers()[3] == soldier4);
            carrier.unloadPassengerIndividual();
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == soldier3);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
            carrier.unloadPassengerIndividual();
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == soldier2);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
            carrier.unloadPassengerIndividual();
            Assert.assertTrue(carrier.getPassengers()[0] == soldier1);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
            carrier.unloadPassengerIndividual();
            Assert.assertTrue(carrier.getPassengers()[0] == null);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
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
	    Carrier carrier2 = new Carrier(0, 1, 1, 1);
	    carrier.setNextAction(ActionType.LOAD);
	    Assert.assertFalse(carrier2.getCurrentAction().isPresent());
	    Carrier carrier3 = new Carrier(0, 1, 2, 2);
	    carrier.setNextAction(ActionType.UNLOAD);
	    Assert.assertFalse(carrier3.getCurrentAction().isPresent());
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
