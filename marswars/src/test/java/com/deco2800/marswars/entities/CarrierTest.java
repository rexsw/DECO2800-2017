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
 * Unable to test onRightClick and onTick but these are the same as
 * that in the Soldier superclass
 * 
 * @author hwkhoo
 * @co-author treenhan jdtran21
 *
 */
public class CarrierTest {

    
    	/**
    	 *  Checks that carrier unit is correctly initialised
    	 */
    	@Test
	public void constructorTest() {
	    Carrier carrier = new Carrier(1, 0, 0 , 1);
	    Assert.assertTrue(carrier != null);
	    Assert.assertTrue(carrier.getLoadStatus() == 2);
	}
	
    	/**
    	 * Checks that carrier unit has correct stats
    	 */
        @Test
        public void getStatTest() {
            Carrier carrier = new Carrier(0, 0, 0, 1);
            Assert.assertTrue(carrier.getStats() != null);
            Assert.assertEquals(1000, carrier.getStats().getHealth());
            Assert.assertEquals(1000, carrier.getStats().getMaxHealth());
            Assert.assertEquals("Carrier", carrier.getStats().getName());
        }
        
        /**
         * Checks that carrier units start empty
         */
        @Test
        public void emptyCarrier() {
            Carrier carrier = new Carrier(0, 0, 0, 1);
            Assert.assertTrue(carrier.getPassengers()[0] == null);
            Assert.assertTrue(carrier.getPassengers()[1] == null);
            Assert.assertTrue(carrier.getPassengers()[2] == null);
            Assert.assertTrue(carrier.getPassengers()[3] == null);
        }
        
        /**
         * Checks that carrier unit assigns action on load
         */
	@Test
	public void loadandunloadTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 0);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	    carrier.unloadIndividual();
	    carrier.unload();
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	}
	
	/**
	 * Checks that carrier unit assigns MOVE action on loading different team unit
	 */
	@Test
	public void loadTestDifferentTeam() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Soldier soldier1 = new Soldier(1, 1, 1, 2);
	    carrier.load(soldier1);
	    Assert.assertTrue(carrier.getCurrentAction().isPresent());
	}
        
	/**
	 * Checks that carrier is able to load units
	 */
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
	
	/**
	 * Checks that carrier is able to unload all units
	 */
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
	
	/**
	 * Checks carrier unloads empty carrier correctly
	 */
	@Test
	public void unloadAllEmptyTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Assert.assertFalse(carrier.unloadPassenger());
	}
	
	/**
	 * Checks carrier unloads empty carrier correctly
	 */
	@Test 
	public void unloadSingleEmptyTest() {
	    Carrier carrier = new Carrier(0, 0, 0 , 0);
	    Assert.assertFalse(carrier.unloadPassengerIndividual());
	}
	
	/**
	 * Checks carrier unloads single units correctly
	 */
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
            Assert.assertFalse(carrier.unloadPassengerIndividual());
	}
	
	/**
	 * Checks carrier sets action correctly
	 */
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


}
