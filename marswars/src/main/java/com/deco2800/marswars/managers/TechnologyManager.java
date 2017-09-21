package com.deco2800.marswars.managers;


import java.util.*;


import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.technology.*;

public class TechnologyManager extends Manager{
    //each tech thingo has id, Cost(Rocks, Crystal, Water, Biomass), Name, parent(list)
    //private Map<Integer, Integer[], String, List<Integer>> techMap = ..
    // .. new HashMap<Integer, Integer[], String, List<Integer>>();



    float spacAttack = 1.0f;
    float spacMove = 5.0f;

    // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
    public HashMap<String, int[]> unitAttributes = new HashMap<>();
    public Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();
    private Technology heroFactory;
    private ArrayList<Technology> armourL1Parents = new ArrayList<Technology>();
    private Technology armourLevelOne;
    private ArrayList<Technology> armourL2Parents = new ArrayList<Technology>();
    private Technology armourLevelTwo;
    private ArrayList<Technology> armourL3Parents = new ArrayList<Technology>();
    private Technology armourLevelThree;
    private ArrayList<Technology> weaponL1Parents = new ArrayList<Technology>();
    private Technology weaponLevelOne;
    private ArrayList<Technology> weaponL2Parents = new ArrayList<Technology>();
    private Technology weaponLevelTwo;
    private ArrayList<Technology> weaponL3Parents = new ArrayList<Technology>();
    private Technology weaponLevelThree;
    private ArrayList<Technology> specialParents = new ArrayList<Technology>();
    private Technology special;

    public TechnologyManager() {
        techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Upgrade Cost", new ArrayList<Technology>(),
                "A cheap technology"));
        techMap.put(2, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Attack", new ArrayList<Technology>(),
                "An expensive technology"));
        techMap.put(3, new Technology(new int[]{30, 0, 0, 0}, "Upgrade Defense", new ArrayList<Technology>(),
                "An expensive technology"));

 // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
        unitAttributes.put("TankDestroyer", new int[]{10, 500, 200, 200, 100, 12, 10});
        unitAttributes.put("Soldier", new int[]{10, 1000, 100, 250, 50, 8, 30});
        unitAttributes.put("Tank", new int[]{10, 1000, 100, 500, 150, 10, 20});
        unitAttributes.put("Astronaut", new int[]{10, 500, 50, 250, 50, 8, 30});
        unitAttributes.put("Medic", new int[]{10, 250, -25, 200, 150, 10, 20});
        unitAttributes.put("Sniper", new int[]{10, 500, 100, 200, 100, 16, 20});
        unitAttributes.put("Commander", new int[]{10, 1000, 100, 500, 250, 10, 40});

        //These need to be implemented on the unit class end of things first, Using soldier as a testing unit.
//        unitAttributes.put("Bullet", new int[]{10, 500, 50, 250, 50, 8, 30});
//        unitAttributes.put("MissleEntity", new int[]{10, 500, 50, 250, 50, 8, 30});
//        unitAttributes.put("Tank", new int[]{10, 500, 50, 250, 50, 8, 30});


       setUpHeroTechs();
        setUpTechMap();
    }

    public Technology getTech(int id){
        return techMap.get(id);
    }
    /**
     provides a function to generate a List<String> representation of all the available technologies
     */
    public int getUnitAttribute(String name, int attribute){
        return 	unitAttributes.get(name)[attribute];
    }

    public ArrayList<Technology> getAllTechs() {
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
    public void unlockHeroFactory() {
        System.out.println("\n Hero Factory unloicked \n");
    }
    public void unlockArmourLevelOne() {

    }
    public void unlockArmourLevelTwo() {

    }
    public void unlockArmourLevelThree() {

    }
    public void unlockWeaponLevelOne() {

    }
    public void unlockWeaponLevelTwo() {

    }
    public void unlockWeaponLevelThree() {

    }
    public void unlockSpecial() {

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
     * @param teamid - the team researching this tech
     * @return String with message about whether or not the research was okay and why
     */
    public String checkPrereqs(TechnologyManager techMan, Technology tech, int techID, int teamid){
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);

        for (Technology techX : tech.getParents()) {
            if (!(getActive().contains(techX))) {
                return "You have not researched the required Technology for this upgrade";
            }
        }
        if(techMan.getActive().contains(tech)) {
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
            //unlockHeroFactory();
            attackSoldierUpgrade();
        }
        if(techID == 2){
            attackSoldierUpgrade();
            unlockArmourLevelOne();
        }
        if(techID == 3){
            unlockArmourLevelTwo();
        }
        if(techID == 4){
            unlockArmourLevelThree();
        }
        if(techID == 5){
            unlockWeaponLevelOne();
        }
        if(techID == 6){
            unlockWeaponLevelTwo();
        }
        if(techID == 7){
            unlockWeaponLevelThree();
        }
        if(techID == 8){
            unlockSpecial();
        }
        return "Technology successfully researched";
    }
    private void setUpHeroTechs() {
        heroFactory = new Technology(new int[]{0, 0, 20, 20}, "Hero " +
                "Factory", new ArrayList<Technology>(), "Unlocks the ability" +
                " to build factories to manufacture hero units.");

        this.armourL1Parents = new ArrayList<Technology>();
        armourL1Parents.add(heroFactory);
        armourLevelOne = new Technology(new int[]{20, 20, 0, 0}, "Armour " +
                "Level One",armourL1Parents, "Unlocks the " +
                "ability to build Level One Armour for Hero units.");

        this.armourL2Parents = new ArrayList<Technology>();
        armourL2Parents.add(armourLevelOne);
        armourLevelTwo = new Technology(new int[]{40, 40, 0, 0}, "Armour " +
                "Level Two", armourL2Parents, "Unlocks the " +
                "ability to build Level Two Armour for Hero units.");

        this.armourL3Parents = new ArrayList<Technology>();
        armourL3Parents.add(armourLevelTwo);
        armourLevelThree = new Technology(new int[]{60, 60, 0, 0}, "Armour " +
                "Level Three", armourL3Parents, "Unlocks the " +
                "ability to build Level Three Armour for Hero units.");

        this.weaponL1Parents = new ArrayList<Technology>();
        weaponL1Parents.add(heroFactory);
        weaponLevelOne = new Technology(new int[]{20, 20, 0, 0}, "Weapons " +
                "Level One", weaponL1Parents, "Unlocks the " +
                "ability to build Level One Weapons for Hero units.");

        this.weaponL2Parents = new ArrayList<Technology>();
        weaponL2Parents.add(weaponLevelOne);
        weaponLevelTwo = new Technology(new int[]{40, 40, 0, 0}, "Weapon " +
                "Level Two", weaponL2Parents, "Unlocks the " +
                "ability to build Level Two Weapons for Hero units.");

        this.weaponL3Parents = new ArrayList<Technology>();
        weaponL3Parents.add(weaponLevelTwo);
        weaponLevelThree = new Technology(new int[]{60, 60, 0, 0}, "Weapons " +
                "Level Three", weaponL3Parents, "Unlocks the " +
                "ability to build Level Three Weapons for Hero units.");

        this.specialParents = new ArrayList<Technology>();
        specialParents.add(heroFactory);
        special = new Technology(new int[]{20, 20, 20, 20}, "Armour " +
                "Level One", specialParents, "Unlocks the " +
                "ability to build Special items for Hero units..");
    }

    private void setUpTechMap() {
        techMap.put(1, heroFactory);
        techMap.put(2, armourLevelOne);
        techMap.put(3, armourLevelTwo);
        techMap.put(4, armourLevelThree);
        techMap.put(5, weaponLevelOne);
        techMap.put(6, weaponLevelTwo);
        techMap.put(7, weaponLevelThree);
        techMap.put(8, special);
    }
}