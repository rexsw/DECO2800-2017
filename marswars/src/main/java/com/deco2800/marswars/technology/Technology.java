package com.deco2800.marswars.technology;

import java.util.Arrays;
import java.util.List;

public class Technology {
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list) and a description
	private int id = 0;
	private int[] cost;
	private String name;
	private List<Technology> parents;
	private String description;

	public Technology(int[] cost, String name, List<Technology> parents, String description) {
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
		} else if (description == null) {
			throw new IllegalArgumentException("Technology description " +
					"must not be null");
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
		this.parents = parents;
		this.description = description;

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

	public String getDescription() {
		return description;
	}
	/**
	 *com.deco2800.marswars.technology upgrade which does Spacman.changeCost(5)
	 *
	 */


	/* Generic Java Object methods, feel free to change toString() and stuff
	if you like */

	/** Returns a string representation of a technlogy
	 *
	 * @Author Alec Bassingthwaighte (alecbass)
	 * @return a string representation of this technlogy
	 */
	@Override
	public String toString() {
		String str = "Technology: " + name + '\n' + "Cost: " + cost[0]
				+ ", " + cost[1] + ", " + cost[2] + ", " + cost[3] + '\n' + "Parent techs: ";
		for (Technology tech : parents) {
			// add in each parent
			str += tech.getName() + ", ";
		}
		str += '\n' + "Description: " + this.description;
		return str;
	}

	/** IntelliJ auto-generated */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Technology that = (Technology) o;

		if (!Arrays.equals(cost, that.cost)) {
			return false;
		}
		if (!name.equals(that.name)) {
			return false;
		}
		return parents.equals(that.parents);
	}

	/** IntelliJ auto-generated because I'm bad at hashcodes
	 * @return the hashcode
	 */
	@Override
	public int hashCode() {
		int result = id;
		result = 31 * result + Arrays.hashCode(cost);
		result = 31 * result + name.hashCode();
		result = 31 * result + parents.hashCode();
		return result;
	}
}


