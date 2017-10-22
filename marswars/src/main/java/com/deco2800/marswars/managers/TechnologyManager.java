package com.deco2800.marswars.managers;


import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.technology.Technology;

import java.util.*;
import com.deco2800.marswars.hud.ShopDialog;

public class TechnologyManager extends Manager{
    //each tech has id, Cost(Rocks, Crystal, Biomass), Name, parent(list)
    // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
    private HashMap<String, int[]> unitAttributes = new HashMap<>();
    private Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();
    /*
     * item system integration into techtree not implemented yet, still in progress. So lines from here to
     * "private Technology special;" are placeholder at the moment.
     */
    // hero factory tech
    private ArrayList<Technology> heroFactoryParents = new ArrayList<Technology>();
    private Technology heroFactory;
    //armour level techs
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

    // booleans to store unlock states
    private boolean specialUnlocked;
    private boolean armourL1Unlocked;
    private boolean armourL2Unlocked;
    private boolean armourL3Unlocked;
    private boolean weaponL1Unlocked;
    private boolean weaponL2Unlocked;
    private boolean weaponL3Unlocked;


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
    private ArrayList<Technology> vampireParents = new ArrayList<Technology>();


    public TechnologyManager() {
        setUpUnlockStates();

        setUpArmourTechs();
        setUpDamageTechs();
        setUpSpeedTechs();
        setUpHealthTechs();
        setUpSpecialTechs();
        setUpHeroFactoryTech();
        setUpWeaponLevelTechs();
        setUpSpecialItemsTech();
        setUpArmourItemLevelTechs();
        setUnitAttributes();
    }

    public void setUpUnlockStates() {
        specialUnlocked = false;
        armourL1Unlocked = false;
        armourL2Unlocked = false;
        armourL3Unlocked = false;
        weaponL1Unlocked = false;
        weaponL2Unlocked = false;
        weaponL3Unlocked = false;
    }

    public void setUpArmourTechs() {
        //Armour tech set up
        techMap.put(1, new Technology(new int[]{10, 10, 10}, "Armour 1", new ArrayList<>(),
                "A cheap technology"));
        ArrayList<Technology> armourTech2Parents = new ArrayList<>();
        armourTech2Parents.add(techMap.get(1));
        techMap.put(2, new Technology(new int[]{20, 20, 20}, "Armour 2", armourTech2Parents,
                "An expensive technology"));
        ArrayList<Technology> armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(3, new Technology(new int[]{30, 30, 30}, "Armour 3", armourTech3Parents,
                "An expensive technology"));
        ArrayList<Technology> armourTech4Parents = new ArrayList<>();
        armourTech4Parents.add(techMap.get(3));
        techMap.put(4, new Technology(new int[]{40, 40, 40}, "Armour 4", armourTech4Parents,
                "An expensive technology"));
    }
    public void setUpDamageTechs() {
        //Damage tech set up
        techMap.put(5, new Technology(new int[]{15, 15, 0}, "Damage 1", new ArrayList<>(),
                "An expensive technology"));
        ArrayList<Technology> attackTech2Parents = new ArrayList<>();
        attackTech2Parents.add(techMap.get(5));
        techMap.put(6, new Technology(new int[]{30, 30, 0}, "Damage 2", attackTech2Parents,
                "An expensive technology"));
        ArrayList<Technology> attackTech3Parents = new ArrayList<>();
        attackTech3Parents.add(techMap.get(6));
        techMap.put(7, new Technology(new int[]{45, 45, 0}, "Damage 3", attackTech3Parents,
                "An expensive technology"));
        ArrayList<Technology> attackTech4Parents = new ArrayList<>();
        attackTech4Parents.add(techMap.get(7));
        techMap.put(8, new Technology(new int[]{60, 60, 0}, "Damage 4", attackTech4Parents,
                "An expensive technology"));
    }
    public void setUpSpeedTechs() {
        //Speed tech Set up
        techMap.put(9, new Technology(new int[]{0, 10, 10}, "Speed 1", new ArrayList<>(),
                "An expensive technology"));
        ArrayList<Technology> speedTech2Parents = new ArrayList<>();
        speedTech2Parents.add(techMap.get(9));
        techMap.put(10, new Technology(new int[]{0, 20, 20}, "Speed 2", speedTech2Parents,
                "An expensive technology"));
        ArrayList<Technology> speedTech3Parents = new ArrayList<>();
        speedTech3Parents.add(techMap.get(10));
        techMap.put(11, new Technology(new int[]{0, 30, 30}, "Speed 3", speedTech3Parents,
                "An expensive technology"));
        ArrayList<Technology> speedTech4Parents = new ArrayList<>();
        speedTech4Parents.add(techMap.get(11));
        techMap.put(12, new Technology(new int[]{0, 40, 40}, "Speed 4", speedTech4Parents,
                "An expensive technology"));
    }
    public void setUpHealthTechs() {
        //Health Tech Set up
        techMap.put(13, new Technology(new int[]{0, 0, 20}, "Health 1", new ArrayList<>(),
                "An expensive technology"));
        ArrayList<Technology> healthTech2Parents = new ArrayList<>();
        healthTech2Parents.add(techMap.get(13));
        techMap.put(14, new Technology(new int[]{0, 0, 40}, "Health 2", healthTech2Parents,
                "An expensive technology"));
        ArrayList<Technology> healthTech3Parents = new ArrayList<>();
        healthTech3Parents.add(techMap.get(14));
        techMap.put(15, new Technology(new int[]{0, 0, 60}, "Health 3", healthTech3Parents,
                "An expensive technology"));
        ArrayList<Technology> healthTech4Parents = new ArrayList<>();
        healthTech4Parents.add(techMap.get(15));
        techMap.put(16, new Technology(new int[]{0, 0, 80}, "Health 4", healthTech4Parents,
                "An expensive technology"));
    }

    public void setUpSpecialTechs() {
        //Special tech Set up
        techMap.put(17, new Technology(new int[]{10, 10, 20}, "Nootropics", new ArrayList<>(),
                "A cheap technology"));
        ArrayList<Technology> steroidsParents = new ArrayList<>();
        steroidsParents.add(techMap.get(4));
        steroidsParents.add(techMap.get(8));
        steroidsParents.add(techMap.get(16));
        techMap.put(18, new Technology(new int[]{0, 20, 30}, "Steroids", steroidsParents,
                "An expensive technology"));

        armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(19, new Technology(new int[]{0, 30, 60}, "Cow Level", new ArrayList<Technology>(),
                "An expensive technology"));

        armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(20, new Technology(new int[]{0, 40, 80}, "Vampirism", new ArrayList<Technology>(),
                "An expensive technology"));
    }

    public void setUpHeroFactoryTech() {
        heroFactory = new Technology(new int[]{0, 0, 0}, "Hero " +
                "Factory", heroFactoryParents,
                "Unlocks the ability to build factories to manufacture " +
                        "hero units.");
        techMap.put(21, heroFactory);
    }

    public void setUpWeaponLevelTechs() {
        // Weapon item level upgrades setup
        weaponL1Parents.add(techMap.get(21));
        weaponLevelOne = new Technology(new int[]{0, 0, 0}, "Weapon " +
                "Level" +
                " 1",
                weaponL1Parents,
                "Unlocks the ability to build Level One Weapons for Hero " +
                        "units.");
        techMap.put(22, weaponLevelOne);

        weaponL2Parents.add(techMap.get(22));
        weaponLevelTwo = new Technology(new int[]{0, 0, 0}, "Weapon " +
                "Level 2",
                weaponL2Parents,
                "Unlocks the ability to build Level Two Weapons for Hero " +
                        "units.");
        techMap.put(23, weaponLevelTwo);

        weaponL3Parents.add(techMap.get(23));
        weaponLevelThree = new Technology(new int[]{0, 0, 0}, "Weapon " +
                "Level 3",
                weaponL3Parents,
                "Unlocks the ability to build Level Three Weapons for Hero " +
                        "units.");
        techMap.put(24, weaponLevelThree);
    }


    public void setUpSpecialItemsTech() {
        // Special item unlock tech setup
        //specialParents = new ArrayList<Technology>();
        specialParents.add(heroFactory);
        special = new Technology(new int[]{0, 0, 0}, "Special " +
                "Items Unlock",
                specialParents,
                "Unlocks the ability to build Special items for Hero units");
        techMap.put(25, special);
    }

    public void setUpArmourItemLevelTechs() {
        // Armour item level upgrades setup
        armourL1Parents.add(techMap.get(25));
        armourLevelOne = new Technology(new int[]{0, 0, 0}, "Armour " +
                "Level 1", armourL1Parents,
                "Unlocks the ability to build Level One Armour for Hero " +
                        "units.");
        techMap.put(26, armourLevelOne);

        armourL2Parents.add(techMap.get(26));
        armourLevelTwo = new Technology(new int[]{0, 0, 0}, "Armour " +
                "Level 2",
                armourL2Parents,
                "Unlocks the ability to build Level Two Armour for Hero " +
                        "units.");
        techMap.put(27, armourLevelTwo);

        armourL3Parents.add(techMap.get(27));
        armourLevelThree = new Technology(new int[]{0, 0, 0}, "Armour " +
                "Level 3",
                armourL3Parents,
                "Unlocks the ability to build Level Three Armour for Hero " +
                        "units.");
        techMap.put(28, armourLevelThree);
    }

    public void setUnitAttributes() {
        // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
        unitAttributes.put("TankDestroyer", new int[]{10, 500, 200, 200, 100, 12, 10});
        unitAttributes.put("Soldier", new int[]{10, 750, 100, 500, 100, 8, 20});
        unitAttributes.put("Tank", new int[]{10, 2500, 100, 1500, 150, 10, 20});
        unitAttributes.put("Astronaut", new int[]{10, 250, 20, 200, 10, 5, 10});
        unitAttributes.put("Medic", new int[]{10, 250, -50, 400, 0, 8, 10});
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

    public void attackUpgrade(){
        unitAttributes.get("Soldier")[2] *= 2;
        unitAttributes.get("Soldier")[4] *= 2;

        }
    public void armourUpgrade(){

        unitAttributes.get("Soldier")[3] *= 2;
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
     * Unlocks Level 1 Armour for Commander units.
     */
    public void unlockArmourLevelOne() {
        armourL1Unlocked = true;
    }
    /**
     * Unlocks Level 2 Armour for Commander units.
     */
    public void unlockArmourLevelTwo() {
        armourL2Unlocked = true;
    }
    
    /**
     * Unlocks Level 3 Armour for Commander units.
     */
    public void unlockArmourLevelThree() {
        armourL3Unlocked = true;
    }

    /**
     * Returns boolean representing whether armour of a particular level has been unlocked.
     */
    public boolean armourIsUnlocked(int level) {
        switch(level) {
            case 1:
                return armourL1Unlocked;
            case 2:
                return armourL2Unlocked;
            case 3:
                return armourL3Unlocked;
            default:
                return false;
        }
    }

    /**
     * Unlocks Level 1 Weapons for Commander units.
     */
    public void unlockWeaponLevelOne() {
        weaponL1Unlocked = true;



    }
    
    /**
     * Unlocks Level 2 Weapons for Commander units.
     */
    public void unlockWeaponLevelTwo() {
        weaponL2Unlocked = true;
    }
    
    /**
     * Unlocks Level 3 Weapons for Commander units.
     */
    public void unlockWeaponLevelThree() {
        weaponL3Unlocked = true;
    }

    /**
     * Returns boolean representing whether armour of a particular level has been unlocked.
     */
    public boolean weaponIsUnlocked(int level) {
        switch(level) {
            case 1:
                return weaponL1Unlocked;
            case 2:
                return weaponL2Unlocked;
            case 3:
                return weaponL3Unlocked;
            default:
                return false;
        }
    }

    /**
     * Unlocks special items for Commander units.
     */
    public void unlockSpecial() {
        specialUnlocked = true;
    }

    /**
     * Returns boolean representing whether special items are unlocked.
     */
    public boolean specialIsUnlocked() {
        return specialUnlocked;
    }


    public void buildingArmorUpgrade(){

    }
 
    public void buildingConstructionTimeUpgrade(){

    }
    /**
     * NOT IMPLEMENTED YET. PLACEHOLDER. IN PROGRESS.
     */
    public void unlockHeroFactory() {
        System.out.println("\n Hero Factory unlocked \n");
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
//        resourceManager.setWater(80, teamid);
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
        if (tech.getCost()[0] > resourceManager.getRocks(teamid)) {
            return "Insufficient Rocks";
        }
        if (tech.getCost()[1] > resourceManager.getCrystal(teamid)) {
            return "Insufficient Crystals";
        }
        if (tech.getCost()[2] > resourceManager.getBiomass(teamid)) {
            return "Insufficient Biomass";
        }
        return "Activating Technology!";
 //       return activateTech(techMan, tech, resourceManager, techID, teamid);
    }

    public String activateTech(TechnologyManager techMan, Technology tech, ResourceManager resourceManager, int techID, int teamid){
        resourceManager.setRocks(resourceManager.getRocks(teamid) - tech.getCost()[0], teamid);
        resourceManager.setCrystal(resourceManager.getCrystal(teamid) - tech.getCost()[1], teamid);
        resourceManager.setBiomass(resourceManager.getBiomass(teamid) - tech.getCost()[2], teamid);
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
            unlockWeaponLevelOne();
        }
        if(techID == 23){
            unlockWeaponLevelTwo();
        }
        if(techID == 24){
            unlockWeaponLevelThree();
        }
        if(techID == 25){
            unlockSpecial();
        }
        if(techID == 26){
            unlockArmourLevelOne();
        }
        if(techID == 27){
            unlockArmourLevelTwo();
        }
        if(techID == 28){
            unlockArmourLevelThree();
        }

        return "Technology successfully researched";
    }
    

    /**
     * Gets the buildings available for specified team 
     * [IMPORTANT NOTE] I can't see a way to check tech for each team based on team ID yet
     */
    public ArrayList<BuildingType> getAvailableBuildings() {
        ArrayList<BuildingType> buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
                BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS, BuildingType.HEROFACTORY));
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

}