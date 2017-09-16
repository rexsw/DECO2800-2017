package com.deco2800.marswars.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.TerrainElements.ResourceType;
import com.deco2800.marswars.entities.buildings.BuildingEntity;
import com.deco2800.marswars.entities.units.AttackableEntity;

public class GameBlackBoard extends Manager implements TickableManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameBlackBoard.class);
	private List<Integer> teams = new ArrayList<Integer>();
	private Map<Integer,Map<String, List<Integer>>> values = new HashMap<Integer,Map<String, List<Integer>>>();
	private ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	private int alive;
	private int timer;
	private int index = 0;
	

	@Override
	public void onTick(long i) {
		timer++;
		if(timer % 10 == 0) {
			for(int teamid: teams) {
				values.get(teamid).get("Biomass").add(rm.getBiomass(teamid));
				values.get(teamid).get("Crystal").add(rm.getCrystal(teamid));
				values.get(teamid).get("Rocks").add(rm.getRocks(teamid));
				values.get(teamid).get("Rocks").add(rm.getWater(teamid));
				values.get(teamid).get("Units").add(this.count(teamid, "Units"));
				values.get(teamid).get("Units Lost").add(this.count(teamid, "Units Lost"));
				values.get(teamid).get("Combat Units").add(this.count(teamid, "Combat Units"));
				values.get(teamid).get("Buildings").add(this.count(teamid, "Buildings"));
			}
			index++;
			
		}
	}
	
	public void set() {
		values = new HashMap<Integer,Map<String, List<Integer>>>();
		int teamid;
		for(BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof HasOwner) {
				teamid = ((HasOwner) e).getOwner();
				if(values.containsKey(teamid)) {
					updateunit(teamid, e);
				}
				else {
					HashMap<String, List<Integer>> teammap = new HashMap<String, List<Integer>>();
					Set(teammap, teamid);
					updateunit(teamid, e);
				}
			}
		}
	}
	
	private void Set(HashMap<String, List<Integer>> setmap, int teamid) {
		ArrayList<Integer> base = new ArrayList<Integer>();
		base.add(0);
		setmap.put("Biomass", base);
		setmap.put("Crystal", base);
		setmap.put("Rocks", base);
		setmap.put("Water", base);
		setmap.put("Units", base);
		setmap.put("Units Lost", base);
		setmap.put("Combat Units", base);
		setmap.put("Buildings", base);
		values.put(teamid, setmap);
		teams.add(teamid);
	}
	
	public void updateunit(int teamid, BaseEntity enity) {
		int count = values.get(teamid).get("Units").get(index);
		count++;
		values.get(teamid).get("Units").set(index, count);
		if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get("Combat Units").get(index);
			count++;
			values.get(teamid).get("Combat Units").set(index, count);
		}
		else if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get("Buildings").get(index);
			count++;
			values.get(teamid).get("Buildings").set(index, count);
		}
	}
	
	public void updateDead(int teamid, BaseEntity enity) {
		int count = values.get(teamid).get("Units").get(index);
		int dead = values.get(teamid).get("Units Lost").get(index);
		count--;
		dead++;
		values.get(teamid).get("Units").set(index, count);
		values.get(teamid).get("Units Lost").set(index, dead);
		if(enity instanceof AttackableEntity) {
			count = values.get(teamid).get("Combat Units").get(index);
			count--;
			values.get(teamid).get("Combat Units").set(index, count);
		}
		else if(enity instanceof BuildingEntity) {
			count = values.get(teamid).get("Buildings").get(index);
			count--;
			values.get(teamid).get("Buildings").set(index, count);
		}
	}
	

	public int teamsAlive() {
		int count = 0;
		for(int t: values.keySet()) {
			if(values.get(t).get("Units").get(index) != 0) {
				alive = t;
				count++;
			}
		}
		return count;
	}
	
	public int getAlive() {
		return alive;
	}
	
	public float[] getHistory(int teamid, String history){
		float[] returnv = new float[index+1];
		for(int i = 0; i < index+1; i++) {
			returnv[i] = this.values.get(teamid).get(history).get(i);
		}
		return returnv;
	}
	
	public int count(int teamid, String field) {
		return values.get(teamid).get(field).get(index);
	}
	

}
