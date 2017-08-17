package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import technology.Technology;

public class TechnologyManager extends Manager{
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	//private Map<Integer, Integer[], String, List<Integer>> techMap = new HashMap<Integer, Integer[], String, List<Integer>>();
	private Map<Integer, Technology> techMap= new HashMap<Integer, Technology>();
	private Map<Integer, Technology> activeTech = new HashMap<Integer, Technology>();
	
	public TechnologyManager() {
		techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Test Technology", new ArrayList<Technology>()));
	}
	
	public Technology getTech(int id){
		return techMap.get(1);
	}
	
	public Technology getActive(int id){
		return activeTech.get(1);
	}
}
