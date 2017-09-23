package com.deco2800.marswars;

import com.deco2800.marswars.entities.units.Carrier;
import com.deco2800.marswars.actions.ActionType;

import org.junit.Test;

/**
 * Code coverage and Junit tests for the Carrier class
 * 
 * @author jdtran21
 *
 */
public class CarrierTest {
	
	@Test
	public void carrier() {
		Carrier carry = new Carrier(0, 0, 0, 0);		
		carry.getPassengers();		
		carry.getStats();		
		ActionType nextAction = ActionType.MOVE;
		carry.setNextAction(nextAction);
	}
}