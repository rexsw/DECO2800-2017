package com.deco2800.marswars.technology;

import com.deco2800.marswars.entities.Spacman;

import java.util.List;

public class Technology {
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	private int id;
	private int[] cost;
	private String name;
	private List<Technology> parents;
	public Technology(int[] cost, String name, List<Technology> parents) {
		if (cost.length != 4) {
			// bad resource cost length
			throw new IllegalArgumentException("Resource costs must not be " +
					"an array of 4 integers");
		} else if (name == null) {
			// bad technology name
			throw new IllegalArgumentException("Technology names cannot be " +
					"null");
		} else if (parents == null) {
			throw new IllegalArgumentException("The list of parent " +
					"technologies must not be null");
		}
		for (int thisCost: cost) {
			// check each resource cost
			if (thisCost < 0) {
				// negative cost - bad!!!!!!!!!!!
				throw new IllegalArgumentException("A technology resource " +
						"cost must not be negative");
			}
		}
		this.cost = cost;
		this.name = name;
		this. parents = parents;
		
	}
	
	public int[] getCost(){
		return cost;
	}
	
	public String getName(){
		return name;
	}
	
	public List<Technology> getParents(){
		return parents;
	}

    /**
     *com.deco2800.marswars.technology upgrade which does Spacman.changeCost(5)
     *
     */

    public void costUpgrade(){
        System.out.println("oewnfoiwenfoienfoiemfoiewfoierfj HELLO");
        Spacman.changeCost(5);
    }
	
}


