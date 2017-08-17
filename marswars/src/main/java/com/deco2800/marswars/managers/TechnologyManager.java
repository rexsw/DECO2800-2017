package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


import technology.technology;

public class TechnologyManager extends Manager{
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	//private Map<Integer, Integer[], String, List<Integer>> techMap = new HashMap<Integer, Integer[], String, List<Integer>>();
	private Set<technology> techSet= new HashSet<technology>();
	
	public TechnologyManager() {
		techSet.add(new technology(1, new int[]{10, 0, 0, 0}, "Test Technology", new ArrayList<technology>()));
	}
}
