package com.deco2800.marswars.managers;


import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.technology.Technology;

import java.util.*;

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
    /*
     * item system integration into techtree not implemented yet, still in progress. So lines from here to
     * "private Technology special;" are placeholder at the moment.
     */
    // hero factory tech
    private ArrayList<Technology> heroFactoryParents = new ArrayList<Technology>();
    private Technology heroFactory;

    // armour level techs
    private Technology armourLevelOne;
    private ArrayList<Technology> armourL1Parents = new ArrayList<Technology>();
    private Technology armourLevelTwo;
    private ArrayList<Technology> armourL2Parents = new ArrayList<Technology>();
    private Technology armourLevelThree;
    private ArrayList<Technology> armourL3Parents = new ArrayList<Technology>();

    // special techs
    private ArrayList<Technology> specialParents = new ArrayList<Technology>();
    private Technology special;

    // weapon level techs
    private Technology weaponLevelOne;
    private ArrayList<Technology> weaponL1Parents = new ArrayList<Technology>();
    private Technology weaponLevelTwo;
    private ArrayList<Technology> weaponL2Parents = new ArrayList<Technology>();
    private Technology weaponLevelThree;
    private ArrayList<Technology> weaponL3Parents = new ArrayList<Technology>();


    private ArrayList<BuildingType> buildingsAvailable;

    private ArrayList<Technology> armourTech2Parents = new ArrayList<Technology>();
    private ArrayList<Technology> armourTech3Parents = new ArrayList<Technology>();
    private ArrayList<Technology> armourTech4Parents = new ArrayList<Technology>();
    private ArrayList<Technology> attackTech2Parents = new ArrayList<Technology>();
    private ArrayList<Technology> attackTech3Parents = new ArrayList<Technology>();
    private ArrayList<Technology> attackTech4Parents = new ArrayList<Technology>();
    private ArrayList<Technology> speedTech2Parents = new ArrayList<Technology>();
    private ArrayList<Technology> speedTech3Parents = new ArrayList<Technology>();
    private ArrayList<Technology> speedTech4Parents = new ArrayList<Technology>();
    private ArrayList<Technology> healthTech2Parents = new ArrayList<Technology>();
    private ArrayList<Technology> healthTech3Parents = new ArrayList<Technology>();
    private ArrayList<Technology> healthTech4Parents = new ArrayList<Technology>();
    private ArrayList<Technology> cowTechParents = new ArrayList<Technology>();
    private ArrayList<Technology> steroidsParents = new ArrayList<Technology>();
    private ArrayList<Technology> vampireParents = new ArrayList<Technology>();



    public TechnologyManager() {
        setUpArmourTechs();
        setUpDamageTechs();
        setUpSpeedTechs();
        setUpHealthTechs();
        setUpSpecialTechs();
        setUpHeroFactoryTech();
        setUpArmourItemLevelTechs();
        setUpSpecialItemsTech();
        setUpWeaponLevelTechs();
        setUnitAttributes();
    }

    public void setUpArmourTechs() {
        //Armour tech set up
        techMap.put(1, new Technology(new int[]{10, 0, 0, 0}, "Armour 1", new ArrayList<>(),
                "A cheap technology"));
        this.armourTech2Parents = new ArrayList<>();
        armourTech2Parents.add(techMap.get(1));
        techMap.put(2, new Technology(new int[]{10, 10, 0, 0}, "Armour 2", armourTech2Parents,
                "An expensive technology"));
        this.armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(3, new Technology(new int[]{10, 10, 10, 0}, "Armour 3", armourTech3Parents,
                "An expensive technology"));
        this.armourTech4Parents = new ArrayList<>();
        armourTech4Parents.add(techMap.get(3));
        techMap.put(4, new Technology(new int[]{10, 10, 10, 10}, "Armour 4", armourTech4Parents,
                "An expensive technology"));
    }
    public void setUpDamageTechs() {
        //Damage tech set up
        techMap.put(5, new Technology(new int[]{0, 20, 0, 0}, "Damage 1", new ArrayList<>(),
                "A cheap technology"));
        this.attackTech2Parents = new ArrayList<>();
        attackTech2Parents.add(techMap.get(5));
        techMap.put(6, new Technology(new int[]{5, 25, 0, 0}, "Damage 2", attackTech2Parents,
                "An expensive technology"));
        this.attackTech3Parents = new ArrayList<>();
        attackTech3Parents.add(techMap.get(6));
        techMap.put(7, new Technology(new int[]{10, 30, 5, 0}, "Damage 3", attackTech3Parents,
                "An expensive technology"));
        this.attackTech4Parents = new ArrayList<>();
        attackTech4Parents.add(techMap.get(7));
        techMap.put(8, new Technology(new int[]{30, 30, 10, 5}, "Damage 4", attackTech4Parents,
                "An expensive technology"));
    }
    public void setUpSpeedTechs() {
        //Speed tech Set up
        techMap.put(9, new Technology(new int[]{0, 0, 5, 5}, "Speed 1", new ArrayList<Technology>(),
                "A cheap technology"));
        this.speedTech2Parents = new ArrayList<>();
        speedTech2Parents.add(techMap.get(9));
        techMap.put(10, new Technology(new int[]{10, 10, 10, 10}, "Speed 2", speedTech2Parents,
                "An expensive technology"));
        this.speedTech3Parents = new ArrayList<>();
        speedTech3Parents.add(techMap.get(10));
        techMap.put(11, new Technology(new int[]{15, 15, 15, 15}, "Speed 3", speedTech3Parents,
                "An expensive technology"));
        this.speedTech4Parents = new ArrayList<>();
        speedTech4Parents.add(techMap.get(11));
        techMap.put(12, new Technology(new int[]{20, 20, 20, 25}, "Speed 4", speedTech4Parents,
                "An expensive technology"));
    }
    public void setUpHealthTechs() {
        //Health Tech Set up
        techMap.put(13, new Technology(new int[]{0, 0, 0, 20}, "Health 1", new ArrayList<Technology>(),
                "A cheap technology"));
        this.healthTech2Parents = new ArrayList<>();
        healthTech2Parents.add(techMap.get(13));
        techMap.put(14, new Technology(new int[]{0, 0, 0, 35}, "Health 2", healthTech2Parents,
                "An expensive technology"));
        this.healthTech3Parents = new ArrayList<>();
        healthTech3Parents.add(techMap.get(14));
        techMap.put(15, new Technology(new int[]{0, 0, 20, 40}, "Health 3", healthTech3Parents,
                "An expensive technology"));
        this.healthTech4Parents = new ArrayList<>();
        healthTech4Parents.add(techMap.get(15));
        techMap.put(16, new Technology(new int[]{30, 15, 10, 50}, "Health 4", healthTech4Parents,
                "An expensive technology"));
    }
    public void setUpSpecialTechs() {
        //Special tech Set up
        this.steroidsParents = new ArrayList<>();
        steroidsParents.add(techMap.get(4));
        steroidsParents.add(techMap.get(8));
        steroidsParents.add(techMap.get(16));
        techMap.put(18, new Technology(new int[]{30, 30, 30, 30}, "Steroids", steroidsParents,
                "An expensive technology"));
        this.armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(19, new Technology(new int[]{100, 100, 100, 100}, "Cow Level", new ArrayList<Technology>(),
                "An expensive technology"));
        this.armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(20, new Technology(new int[]{99999, 9999, 999, 0}, "Vampirism", new ArrayList<Technology>(),
                "An expensive technology"));
    }

    public void setUpHeroFactoryTech() {
        heroFactory = new Technology(new int[]{0, 0, 20, 20}, "Hero " +
                "Factory", heroFactoryParents,
                "Unlocks the ability to build factories to manufacture " +
                        "hero units.");
        techMap.put(21, heroFactory);
    }

    public void setUpArmourItemLevelTechs() {
        // Armour item level upgrades setup
        armourL1Parents.add(techMap.get(21));
        // armour temporarily costs water and biomass for testing
        armourLevelOne = new Technology(new int[]{0, 0, 10, 10}, "Armour " +
                "Level 1", armourL1Parents,
                "Unlocks the ability to build Level One Armour for Hero " +
                        "units.");
        techMap.put(22, armourLevelOne);

        armourL2Parents.add(techMap.get(22));
        armourLevelTwo = new Technology(new int[]{0, 0, 20, 20}, "Armour " +
                "Level 2",
                armourL2Parents,
                "Unlocks the ability to build Level Two Armour for Hero " +
                        "units.");
        techMap.put(23, armourLevelTwo);

        armourL3Parents.add(techMap.get(23));
        armourLevelThree = new Technology(new int[]{0, 0, 40, 40}, "Armour " +
                "Level 3",
                armourL3Parents,
                "Unlocks the ability to build Level Three Armour for Hero " +
                        "units.");
        techMap.put(24, armourLevelThree);
    }
    public void setUpSpecialItemsTech() {
        // Special item unlock tech setup
        specialParents = new ArrayList<Technology>();
        specialParents.add(heroFactory);
        special = new Technology(new int[]{20, 20, 20, 20}, "Special " +
                "Items Unlock",
                specialParents,
                "Unlocks the ability to build Special items for Hero units");
        techMap.put(25, special);
    }

    public void setUpWeaponLevelTechs() {
        // Weapon item level upgrades setup
        weaponL1Parents.add(techMap.get(25));
        weaponLevelOne = new Technology(new int[]{20, 20, 0, 0}, "Weapon " +
                "Level" +
                " 1",
                weaponL1Parents,
                "Unlocks the ability to build Level One Weapons for Hero " +
                        "units.");
        techMap.put(26, weaponLevelOne);

        weaponL2Parents.add(techMap.get(26));
        weaponLevelTwo = new Technology(new int[]{30, 30, 0, 0}, "Weapon " +
                "Level 2",
                weaponL2Parents,
                "Unlocks the ability to build Level Two Weapons for Hero " +
                        "units.");
        techMap.put(27, weaponLevelTwo);

        weaponL3Parents.add(techMap.get(27));
        weaponLevelThree = new Technology(new int[]{40, 40, 0, 0}, "Weapon " +
                "Level 3",
                weaponL3Parents,
                "Unlocks the ability to build Level Three Weapons for Hero " +
                        "units.");
        techMap.put(28, weaponLevelThree);
    }

    public void setUnitAttributes() {
        // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
        unitAttributes.put("TankDestroyer", new int[]{10, 500, 200, 200, 100, 12, 10});
        unitAttributes.put("Soldier", new int[]{10, 1000, 100, 250, 50, 8, 30});
        unitAttributes.put("Tank", new int[]{10, 1000, 100, 500, 150, 10, 20});
        unitAttributes.put("Astronaut", new int[]{10, 500, 50, 250, 50, 8, 30});
        unitAttributes.put("Medic", new int[]{10, 250, -25, 200, 150, 10, 20});
        unitAttributes.put("Sniper", new int[]{10, 500, 100, 200, 100, 16, 20});
        unitAttributes.put("Commander", new int[]{10, 1000, 100, 500, 250, 10, 40});
        unitAttributes.put("Ambient", new int[]{10,1000*1000,100,50,0,30});

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
    public void attackUpgrade(){
        unitAttributes.get("Soldier")[2] *= 2;
        unitAttributes.get("Soldier")[4] *= 2;

        }
    public void armourUpgrade(){
        unitAttributes.get("Soldier")[3] *= 2;
    }
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER. IN PROGRESS.
     */
    public void unlockHeroFactory() {
        System.out.println("\n Hero Factory unlocked \n");
    }

    public void speedUpgrade() {
        unitAttributes.get("Soldier")[6] *= 2;
        unitAttributes.get("Soldier")[5] *= 1.4f;
    }
    public void healthUpgrade() {
        unitAttributes.get("Soldier")[1] *= 2;

    }
    public void cowLevelUpgrade() {

    }
    public void steroidsUpgrade() {
        unitAttributes.get("Soldier")[1] *= .5;
        unitAttributes.get("Soldier")[2] *= 3;
        unitAttributes.get("Soldier")[3] *= 3;
        unitAttributes.get("Soldier")[4] *= 3;
        unitAttributes.get("Soldier")[5] *= 3;
        unitAttributes.get("Soldier")[6] *= 3;
    }
    public void vampirismUpgrade() {
        unitAttributes.get("Soldier")[1] *= 9999;
        unitAttributes.get("Soldier")[2] *= 9999;
        unitAttributes.get("Soldier")[3] *= 9999;
        unitAttributes.get("Soldier")[4] *= 9999;
        unitAttributes.get("Soldier")[5] *= 9999;
        unitAttributes.get("Soldier")[6] *= 9999;
    }


    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockArmourLevelOne() {

    }
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockArmourLevelTwo() {

    }
    
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockArmourLevelThree() {

    }

    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockWeaponLevelOne() {

    }
    
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockWeaponLevelTwo() {

    }
    
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
    public void unlockWeaponLevelThree() {

    }

    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER
     */
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
        resourceManager.setBiomass(80, teamid);
        resourceManager.setWater(80, teamid);
//        System.out.println("prereqs are: " + tech.getParents());
//        System.out.println("active tech is: " + getActive());

        for (Technology techX : tech.getParents()) {
            if (!(getActive().contains(techX))) {
                return "You have not researched the required technology for this upgrade";
            }
        }
        if(techMan.getActive().contains(tech)) {
            return "You have already researched this upgrade";
        }
//        for (int i = 0; i < 10000; i++) {
//            System.out.println("ROCKS NEEDED: " + tech.getCost()[0]);
//            System.out.println("CRYSTAL NEEDED: " + tech.getCost()[1]);
//            System.out.println("WATER NEEDED: " + tech.getCost()[2]);
//            System.out.println("BIOMASS NEEDED: " + tech.getCost()[3]);
//
//            System.out.println("ROCK STOCKPILE: " + resourceManager.getRocks(teamid));
//            System.out.println("CRYSTAL STOCKPILE: " + resourceManager.getCrystal(teamid));
//            System.out.println("WATER STOCKPILE: " + resourceManager.getWater(teamid));
//            System.out.println("BIOMASS STOCKPILE: " + resourceManager.getBiomass(teamid));
//
//        }
        if (tech.getCost()[0] > resourceManager.getRocks(teamid)) {
            return "Insufficient Rocks";
        }
        if (tech.getCost()[1] > resourceManager.getCrystal(teamid)) {
            return "Insufficient Crystals";
        }
        if (tech.getCost()[2] > resourceManager.getWater(teamid)) {
            return "Insufficient Water levels";
        }
        if (tech.getCost()[3] > resourceManager.getBiomass(teamid)) {
            return "Insufficient Biomass";
        }
        return "Activating Technology!";
 //       return activateTech(techMan, tech, resourceManager, techID, teamid);
    }

    public String activateTech(TechnologyManager techMan, Technology tech, ResourceManager resourceManager,int techID, int teamid){
        resourceManager.setRocks(resourceManager.getRocks(teamid) - tech.getCost()[0], teamid);
        resourceManager.setCrystal(resourceManager.getCrystal(teamid) - tech.getCost()[1], teamid);
        resourceManager.setWater(resourceManager.getWater(teamid) - tech.getCost()[2], teamid);
        resourceManager.setBiomass(resourceManager.getBiomass(teamid) - tech.getCost()[3], teamid);
        techMan.addActiveTech(tech);

        if(techID == 1 || techID == 2 || techID == 3 || techID == 4){
            armourUpgrade();
        }
        if(techID == 5 || techID == 6 || techID == 7 || techID == 8){
            attackUpgrade();
        }
        if(techID == 9 || techID == 10 || techID == 11 || techID == 12){
            speedUpgrade();
        }
        if(techID == 13 || techID == 14 || techID == 15 || techID == 16){
            healthUpgrade();
        }
        if(techID == 17){
            // needs something here, used to be HF tech, now moved to 21
        }
        if(techID == 18){
            steroidsUpgrade();
        }
        if(techID == 19){
            cowLevelUpgrade();
        }
        if(techID == 20){
            vampirismUpgrade();
        }
        if(techID == 21){
            unlockHeroFactory();
        }
        if(techID == 22){
            unlockArmourLevelOne();
        }
        if(techID == 23){
            unlockArmourLevelTwo();
        }
        if(techID == 24){
            unlockArmourLevelThree();
        }
        if(techID == 25){
            unlockSpecial();
        }
        if(techID == 26){
            unlockWeaponLevelOne();
        }
        if(techID == 27){
            unlockWeaponLevelTwo();
        }
        if(techID == 28){
            unlockWeaponLevelThree();
        }
        System.out.println("######################################");
        return "Technology successfully researched";
    }

    /**
     * Gets the buildings available for specified team 
     * [IMPORTANT NOTE] I can't see a way to check tech for each team based on team ID yet
     */
    public ArrayList<BuildingType> getAvailableBuildings() {
    	buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
    		            BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS));
    	// ADD HEROFACTORY to buildingsAvailable if the tech is unlocked (NOT IMPLEMENTED)
    	return buildingsAvailable;
    }

    public Set<Technology> getAllTech() {
        Set<Technology> techSet = new HashSet<Technology>();
        for (int i = 1; i<9;i++){
            techSet.add(this.getTech(i));
        }
        return techSet;
    }
    
    public int getID(Technology tech){
        if(tech == this.getTech(1)){
            return 1;
        } else
        if(tech == this.getTech(2)){
            return 2;
        } else
        if(tech == this.getTech(3)){
            return 3;
        }
        if(tech == this.getTech(4)){
            return 4;
        }
        if(tech == this.getTech(5)){
            return 5;
        }
        if(tech == this.getTech(6)){
            return 6;
        }
        if(tech == this.getTech(7)){
            return 7;
        }
        if(tech == this.getTech(8)){
            return 8;
        }

         return 0;
    }
}