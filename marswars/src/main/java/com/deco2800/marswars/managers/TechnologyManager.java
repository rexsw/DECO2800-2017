package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import technology.technology;

public class TechnologyManager extends Manager{
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	//private Map<Integer, Integer[], String, List<Integer>> techMap = new HashMap<Integer, Integer[], String, List<Integer>>();
	private Map<Integer, technology> techMap= new HashMap<Integer, technology>();
	private Map<Integer, technology> activeTech = new HashMap<Integer, technology>();
	
	public TechnologyManager() {
		techMap.put(1, new technology(new int[]{10, 0, 0, 0}, "Test Technology", new ArrayList<technology>()));
	}
	
	public technology getTech(int id){
		return techMap.get(1);
	}
	
	public technology getActive(int id){
		return activeTech.get(1);
	}
}
