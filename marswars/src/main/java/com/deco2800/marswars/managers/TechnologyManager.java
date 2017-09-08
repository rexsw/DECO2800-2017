package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


import com.deco2800.marswars.technology.*;

public class TechnologyManager extends Manager{
    //each tech thingo has id, Cost(Rocks, Crystal, Water, Biomass), Name, parent(list)
    //private Map<Integer, Integer[], String, List<Integer>> techMap = ..
    // .. new HashMap<Integer, Integer[], String, List<Integer>>();
    public Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();

    public TechnologyManager() {
        techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Upgrade Cost", new ArrayList<Technology>(),
                "A cheap technology"));
        techMap.put(2, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Attack", new ArrayList<Technology>(),
                "An expensive technology"));
        techMap.put(3, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Defense", new ArrayList<Technology>(),
                "An expensive technology"));
    }

    public Technology getTech(int id){
        return techMap.get(id);
    }
    /**
     provides a function to generate a List<String> representation of all the available technologies
     */

    public ArrayList<Technology> listForm() {
        ArrayList<Technology> techList = new ArrayList<>();
        for (int j = 0; j < techMap.size(); j++) {
            techList.add(getTech(j));
        }
        return techList;
    }

    public Set<Technology> getActive(){ return activeTech; }

    public void addActiveTech(Technology tech) {activeTech.add(tech); }

    public void costUpgrade(){
        //placeholder for cost Upgrade functionality
    }
    public void attackUpgrade(){
        //placeholder for attack Upgrade functionality

    }
    public void defenseUpgrade(){
        //placeholder for defense Upgrade functionality
    }


    /**
     * Provides a method to check that the requirements for researching a Technology exists, if they are
     * all satisfied then this function will adjust your resources to reflect the research costs
     * and return a message.
     * @param techMan
     * @param tech
     * @param teamid - the team researching this tech
     * @return String with message about whether or not the research was okay and why
     */
    public String checkPrereqs(TechnologyManager techMan, Technology tech, int techID, int teamid){
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);

        if(!(getActive().contains(tech.getParents()))){
            return "You have not researched the required Technology for this upgrade";
        }
        if(!techMan.getActive().contains(tech)) {
            return "You have already researched this upgrade";
        }
        if (!(resourceManager.getRocks(teamid) > tech.getCost()[0])) {
            return "Insufficient Rocks";
        }
        if (!(resourceManager.getCrystal(teamid) > tech.getCost()[1])) {
            return "Insufficient Crystals";
        }
        if (!(resourceManager.getWater(teamid) > tech.getCost()[1])) {
            return "Insufficient Water levels";
        }
        if (!(resourceManager.getBiomass(teamid) > tech.getCost()[1])) {
            return "Insufficient Biomass";
        }
        return activateTech(techMan, tech, resourceManager, techID, teamid);
    }

    public String activateTech(TechnologyManager techMan, Technology tech, ResourceManager resourceManager,int techID, int teamid){
        resourceManager.setRocks(resourceManager.getRocks(teamid) - tech.getCost()[0], teamid);
        resourceManager.setCrystal(resourceManager.getCrystal(teamid) - tech.getCost()[1], teamid);
        resourceManager.setWater(resourceManager.getWater(teamid) - tech.getCost()[2], teamid);
        resourceManager.setBiomass(resourceManager.getBiomass(teamid) - tech.getCost()[3], teamid);
        techMan.addActiveTech(tech);
        if(techID == 1){
            costUpgrade();
        }
        if(techID == 2){
            attackUpgrade();
        }
        if(techID == 3){
            defenseUpgrade();
        }
        return "Technology successfully researched";
    }
}