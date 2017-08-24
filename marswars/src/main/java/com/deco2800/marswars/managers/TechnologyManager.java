package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


import technology.Technology;

public class TechnologyManager extends Manager{
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	//private Map<Integer, Integer[], String, List<Integer>> techMap = new HashMap<Integer, Integer[], String, List<Integer>>();
	private Map<Integer, Technology> techMap= new HashMap<Integer, Technology>();
	private Set<Technology> activeTech = new HashSet<Technology>();
	
	public TechnologyManager() {
		techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Test Technology", new ArrayList<Technology>()));
		techMap.put(2, new Technology(new int[]{30, 0, 0, 0}, "Expensive Upgrade", new ArrayList<Technology>()));
	}
	
	public Technology getTech(int id){
		return techMap.get(id);
	}
	
	public Set<Technology> getActive(){ return activeTech; }

	public void addActiveTech(Technology tech) {activeTech.add(tech); }
}
