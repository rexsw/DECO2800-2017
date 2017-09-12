package com.deco2800.marswars.managers;


import java.util.*;


import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.technology.*;

public class TechnologyManager extends Manager{
    //each tech thingo has id, Cost(Rocks, Crystal, Water, Biomass), Name, parent(list)
    //private Map<Integer, Integer[], String, List<Integer>> techMap = ..
    // .. new HashMap<Integer, Integer[], String, List<Integer>>();
    // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>



    float spacAttack = 1.0f;
    float spacMove = 5.0f;

    public HashMap<String, int[]> unitAttributes = new HashMap<>();

    public Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();

    public TechnologyManager() {
        techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Upgrade Cost", new ArrayList<Technology>(),
                "A cheap technology"));
        techMap.put(2, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Attack", new ArrayList<Technology>(),
                "An expensive technology"));
        techMap.put(3, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Defense", new ArrayList<Technology>(),
                "An expensive technology"));


        unitAttributes.put("Soldier", new int[]{10, 500, 50, 250, 50, 8, 30});
        unitAttributes.put("Astronaut", new int[]{10, 500, 50, 250, 50, 8, 30});
        unitAttributes.put("Healer", new int[]{10, 500, -25, 200, 150, 10, 20});

        //These need to be implemented on the unit class end of things first, Using soldier as a testing unit.
//        unitAttributes.put("Bullet", new int[]{10, 500, 50, 250, 50, 8, 30});
//        unitAttributes.put("MissleEntity", new int[]{10, 500, 50, 250, 50, 8, 30});
//        unitAttributes.put("Tank", new int[]{10, 500, 50, 250, 50, 8, 30});


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
        for (int i = 0; i<unitAttributes.size();i++){
            //iterate through map and reduce all unit costs
        }
    }
    public void attackSoldierUpgrade(){
            unitAttributes.get("Soldier")[2] *= 1.5;
            //also need to set all existing solider to have this much
    }
    public void defenseUpgrade(){
        //placeholder for defense Upgrade functionality
    }

    public void buildingArmorUpgrade(){

    }

    public void buildingConstructionTimeUpgrade(){

    }


    /**
     * Provides a method to check that the requirements for researching a Technology exists, if they are
     * all satisfied then this function will adjust your resources to reflect the research costs
     * and return a message.
     * @param techMan
     * @param tech
     * @return String with message about whether or not the research was okay and why
     */
    public String checkPrereqs(TechnologyManager techMan, Technology tech, int techID){
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);

        if(!(getActive().contains(tech.getParents()))){
            return "You have not researched the required Technology for this upgrade";
        }
        if(!techMan.getActive().contains(tech)) {
            return "You have already researched this upgrade";
        }
        if (!(resourceManager.getRocks() > tech.getCost()[0])) {
            return "Insufficient Rocks";
        }
        if (!(resourceManager.getCrystal() > tech.getCost()[1])) {
            return "Insufficient Crystals";
        }
        if (!(resourceManager.getWater() > tech.getCost()[1])) {
            return "Insufficient Water levels";
        }
        if (!(resourceManager.getBiomass() > tech.getCost()[1])) {
            return "Insufficient Biomass";
        }
        return activateTech(techMan, tech, resourceManager, techID);
    }

    public String activateTech(TechnologyManager techMan, Technology tech, ResourceManager resourceManager,int techID){
        resourceManager.setRocks(resourceManager.getRocks() - tech.getCost()[0]);
        resourceManager.setCrystal(resourceManager.getCrystal() - tech.getCost()[1]);
        resourceManager.setWater(resourceManager.getWater() - tech.getCost()[2]);
        resourceManager.setBiomass(resourceManager.getBiomass() - tech.getCost()[3]);
        techMan.addActiveTech(tech);
        if(techID == 1){
            costUpgrade();
        }
        if(techID == 2){
            attackSoldierUpgrade();
        }
        if(techID == 3){
            defenseUpgrade();
        }
        return "Technology successfully researched";
    }
    public float getSpacAttack() {
        return spacAttack;
    }

    public void setSpacAttack(float spacAttack) {
        this.spacAttack = spacAttack;
    }

    public float getSpacMove() {
        return spacMove;
    }

    public void setSpacMove(float spacMove) {
        this.spacMove = spacMove;
    }
}