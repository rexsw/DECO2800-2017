package com.deco2800.marswars.managers;


import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.technology.Technology;

import java.util.*;

public class TechnologyManager extends Manager{

    //UNIT STAT IDs
    private static final int MAX_HEALTH = 1;
    private static final int DAMAGE = 2;
    private static final int ARMOUR = 3;
    private static final int ARMOUR_DAMAGE = 4;
    private static final int ATTACK_RANGE = 5;
    private static final int ATTACK_SPEED = 6;

    private HashMap<String, int[]> unitAttributes = new HashMap<>();
    private Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();
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
    private boolean heroFactoryUnlocked;
    private boolean specialUnlocked;
    private boolean armourL1Unlocked;
    private boolean armourL2Unlocked;
    private boolean armourL3Unlocked;
    private boolean weaponL1Unlocked;
    private boolean weaponL2Unlocked;
    private boolean weaponL3Unlocked;

    private String soldierString = "Soldier";
    
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
        heroFactoryUnlocked = false;
        specialUnlocked = false;
        armourL1Unlocked = false;
        armourL2Unlocked = false;
        armourL3Unlocked = false;
        weaponL1Unlocked = false;
        weaponL2Unlocked = false;
        weaponL3Unlocked = false;
    }

    /**
     * Creates and organises the general Unit Armour technologies
     */
    public void setUpArmourTechs() {
        //Armour tech set up
        techMap.put(1, new Technology(new int[]{10, 0, 0}, "Armour 1", new ArrayList<>(),
                "1st level Armour tech"));
        ArrayList<Technology> armourTech2Parents = new ArrayList<>();
        armourTech2Parents.add(techMap.get(1));
        techMap.put(2, new Technology(new int[]{10, 0, 0}, "Armour 2", armourTech2Parents,
                "2nd level armour tech (metapod used harden)"));
        ArrayList<Technology> armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(3, new Technology(new int[]{10, 0, 0}, "Armour 3", armourTech3Parents,
                "3rd level Armour tech (really getting hard)"));
        ArrayList<Technology> armourTech4Parents = new ArrayList<>();
        armourTech4Parents.add(techMap.get(3));
        techMap.put(4, new Technology(new int[]{10, 0, 0}, "Armour 4", armourTech4Parents,
                "4th level Armour tech"));
    }

    /**
     * creates and organises the general units Damage technologies
     */
    public void setUpDamageTechs() {
        //Damage tech set up
        techMap.put(5, new Technology(new int[]{0, 10, 0}, "Damage 1", new ArrayList<>(),
                "1st level Damage tech, increased both types of damage"));
        ArrayList<Technology> attackTech2Parents = new ArrayList<>();
        attackTech2Parents.add(techMap.get(5));
        techMap.put(6, new Technology(new int[]{0, 10, 0}, "Damage 2", attackTech2Parents,
                "2nd level Damage tech, increased both types of damage"));
        ArrayList<Technology> attackTech3Parents = new ArrayList<>();
        attackTech3Parents.add(techMap.get(6));
        techMap.put(7, new Technology(new int[]{0, 10, 0}, "Damage 3", attackTech3Parents,
                "3rd level Damage tech, increased both types of damage"));
        ArrayList<Technology> attackTech4Parents = new ArrayList<>();
        attackTech4Parents.add(techMap.get(7));
        techMap.put(8, new Technology(new int[]{0, 10, 0}, "Damage 4", attackTech4Parents,
                "4th level Damage tech, increased both types of damage"));
    }

    /**
     * Creates and organises the general unit speed technologies
     */
    public void setUpSpeedTechs() {
        //Speed tech Set up
        techMap.put(9, new Technology(new int[]{0, 0, 10}, "Speed 1", new ArrayList<>(),
                "1st level Speed tech, increases movement and attack speed"));
        ArrayList<Technology> speedTech2Parents = new ArrayList<>();
        speedTech2Parents.add(techMap.get(9));
        techMap.put(10, new Technology(new int[]{0, 0, 10}, "Speed 2", speedTech2Parents,
                "2nd level Speed tech, increases movement and attack speed"));
        ArrayList<Technology> speedTech3Parents = new ArrayList<>();
        speedTech3Parents.add(techMap.get(10));
        techMap.put(11, new Technology(new int[]{0, 0, 10}, "Speed 3", speedTech3Parents,
                "3rd level Speed tech, increases movement and attack speed"));
        ArrayList<Technology> speedTech4Parents = new ArrayList<>();
        speedTech4Parents.add(techMap.get(11));
        techMap.put(12, new Technology(new int[]{0, 0, 10}, "Speed 4", speedTech4Parents,
                "4th level Speed tech, increases movement and attack speed"));
    }

    /**
     * Creates and organises the general unit health technologies
     */
    public void setUpHealthTechs() {
        //Health Tech Set up
        techMap.put(13, new Technology(new int[]{5, 5, 5}, "Health 1", new ArrayList<>(),
                "1st level Health tech"));
        ArrayList<Technology> healthTech2Parents = new ArrayList<>();
        healthTech2Parents.add(techMap.get(13));
        techMap.put(14, new Technology(new int[]{5, 5, 5}, "Health 2", healthTech2Parents,
                "2nd level Health tech"));
        ArrayList<Technology> healthTech3Parents = new ArrayList<>();
        healthTech3Parents.add(techMap.get(14));
        techMap.put(15, new Technology(new int[]{5, 5, 5}, "Health 3", healthTech3Parents,
                "3rd level Health tech"));
        ArrayList<Technology> healthTech4Parents = new ArrayList<>();
        healthTech4Parents.add(techMap.get(15));
        techMap.put(16, new Technology(new int[]{5, 5, 5}, "Health 4", healthTech4Parents,
                "4th level Health tech"));
    }

    /**
     * Creates and organises the general units special technologies
     */
    public void setUpSpecialTechs() {
        techMap.put(17, new Technology(new int[]{10, 10, 20}, "Nootropics", new ArrayList<>(),
                "Feed your brain"));

        ArrayList<Technology> steroidsParents = new ArrayList<>();
        steroidsParents.add(techMap.get(4));
        steroidsParents.add(techMap.get(8));
        steroidsParents.add(techMap.get(16));
        techMap.put(18, new Technology(new int[]{100, 100, 100}, "Steroids", steroidsParents,
                "Increases Everything"));

        ArrayList<Technology> armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(19, new Technology(new int[]{9999999, 9999999,9999999}, "Cow Level", new ArrayList<Technology>(),
                "There is no secret Cow Level"));

        armourTech3Parents = new ArrayList<>();
        armourTech3Parents.add(techMap.get(2));
        techMap.put(20, new Technology(new int[]{300, 300, 300}, "Vampirism", new ArrayList<Technology>(),
                "Why did I make this?"));
    }

    //Below here are hero upgrades, these are for the hero shop and not for the general units.

    /** Sets up the hero factory technology, including costs and parents.
     *
     * */
    public void setUpHeroFactoryTech() {
        heroFactory = new Technology(new int[]{0, 50, 0}, "Hero " +
                "Factory", heroFactoryParents,
                "Unlocks the ability to build factories to manufacture " +
                        "hero units.");
        techMap.put(21, heroFactory);
    }

    /** Sets up weapon item level technologies, including costs and parents.
     *  Note that each preceding weapon item tech must be unlocked before
     *  the next and that the Hero Factory must be unlocked before the level
     *  1 weapon can be unlocked.
     */
    public void setUpWeaponLevelTechs() {
        // Weapon item level upgrades setup
        weaponL1Parents.add(techMap.get(21));
        weaponLevelOne = new Technology(new int[]{40, 40, 0}, "Weapon " +
                "Level" +
                " 1",
                weaponL1Parents,
                "Unlocks the ability to build Level One Weapons for Hero " +
                        "units.");
        techMap.put(22, weaponLevelOne);

        weaponL2Parents.add(techMap.get(22));
        weaponLevelTwo = new Technology(new int[]{70, 70, 0}, "Weapon " +
                "Level 2",
                weaponL2Parents,
                "Unlocks the ability to build Level Two Weapons for Hero " +
                        "units.");
        techMap.put(23, weaponLevelTwo);

        weaponL3Parents.add(techMap.get(23));
        weaponLevelThree = new Technology(new int[]{100, 100, 0}, "Weapon " +
                "Level 3",
                weaponL3Parents,
                "Unlocks the ability to build Level Three Weapons for Hero " +
                        "units.");
        techMap.put(24, weaponLevelThree);
    }

    /** Sets up special item technology, including costs and parents.
     *
     */
    public void setUpSpecialItemsTech() {
        // Special item unlock tech setup
        specialParents.add(heroFactory);
        special = new Technology(new int[]{20, 20, 20}, "Special " +
                "Items Unlock",
                specialParents,
                "Unlocks the ability to build Special items for Hero units");
        techMap.put(25, special);
    }

    /** Sets up armour item level technologies, including costs and parents.
     *  Note that each preceding armour item tech must be unlocked before
     *  the next and that Special items must be unlocked before the level
     *  1 armour can be unlocked.
     */
    public void setUpArmourItemLevelTechs() {
        // Armour item level upgrades setup
        armourL1Parents.add(techMap.get(25));
        armourLevelOne = new Technology(new int[]{20, 30, 30}, "Armour " +
                "Level 1", armourL1Parents,
                "Unlocks the ability to build Level One Armour for Hero " +
                        "units.");
        techMap.put(26, armourLevelOne);

        armourL2Parents.add(techMap.get(26));
        armourLevelTwo = new Technology(new int[]{20, 60, 60}, "Armour " +
                "Level 2",
                armourL2Parents,
                "Unlocks the ability to build Level Two Armour for Hero " +
                        "units.");
        techMap.put(27, armourLevelTwo);

        armourL3Parents.add(techMap.get(27));
        armourLevelThree = new Technology(new int[]{40, 80, 80}, "Armour " +
                "Level 3",
                armourL3Parents,
                "Unlocks the ability to build Level Three Armour for Hero " +
                        "units.");
        techMap.put(28, armourLevelThree);
    }



    /** Sets up the stats for all the general units
     */
    public void setUnitAttributes() {

        // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
        unitAttributes.put("TankDestroyer", new int[]{10, 800, 50, 500, 400, 10, 20});
        unitAttributes.put(soldierString, new int[]{10, 400, 40, 350, 75, 8, 20});
        unitAttributes.put("Tank", new int[]{10, 2500, 100, 1500, 150, 10, 20});
        unitAttributes.put("Astronaut", new int[]{10, 250, 20, 200, 10, 5, 10});
        unitAttributes.put("Medic", new int[]{10, 250, -50, 400, 0, 8, 10});
        unitAttributes.put("Sniper", new int[]{10, 500, 80, 200, 100, 16, 20});
        unitAttributes.put("Commander", new int[]{10, 1000, 100, 500, 250, 10, 40});
        unitAttributes.put("Ambient", new int[]{10,1000*1000,100,50,0,30});
        unitAttributes.put("Hacker", new int[]{10, 750, 50, 400, 0, 8, 20}); // loyalty damage
        unitAttributes.put("Carrier", new int[] {10, 1000, 0, 500, 0, 0, 0});
        unitAttributes.put("Spatman", new int[] {10, 300, 2, 200, 0, 8, 5}); // attackspeed damage
        unitAttributes.put("Spacman", new int[] {10, 300, 0, 200, 0, 8, 5});
    }
    /** Returns the Technology with the specified ID
     */
    public Technology getTech(int id){
        return techMap.get(id);
    }

    /** Provides a function to generate a List<String> representation of all the available technologies
     */
    public int getUnitAttribute(String name, int attribute){
        return 	unitAttributes.get(name)[attribute];
    }

    /**
     * Returns an array of all the Technologies
     * @return
     */
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
        unitAttributes.get(soldierString)[DAMAGE] *= 2;
        unitAttributes.get(soldierString)[ARMOUR_DAMAGE] *= 2;
        }

    public void armourUpgrade(){

        unitAttributes.get(soldierString)[ARMOUR] *= 2;
    }

    public void speedUpgrade() {
        unitAttributes.get(soldierString)[ATTACK_SPEED] *= 2;
        unitAttributes.get(soldierString)[ATTACK_RANGE] *= 2;
    }
    public void healthUpgrade() {

        unitAttributes.get(soldierString)[MAX_HEALTH] *= 2;


    }

    public void nootropicsUpgrade(){
        unitAttributes.get(soldierString)[MAX_HEALTH] *= .2;
        unitAttributes.get(soldierString)[ATTACK_RANGE] *= 1.5;
        unitAttributes.get(soldierString)[ATTACK_SPEED] *= 1.5;
        unitAttributes.get(soldierString)[DAMAGE] *= 1.5;
        unitAttributes.get(soldierString)[ARMOUR] *= 1.5;
        unitAttributes.get(soldierString)[ARMOUR_DAMAGE] *= 1.5;

    }
    public void cowLevelUpgrade() {
    }

    public void steroidsUpgrade() {
        unitAttributes.get(soldierString)[MAX_HEALTH] *= .5;
        unitAttributes.get(soldierString)[ATTACK_RANGE] *= 3;
        unitAttributes.get(soldierString)[ATTACK_SPEED] *= 3;
        unitAttributes.get(soldierString)[DAMAGE] *= 3;
        unitAttributes.get(soldierString)[ARMOUR] *= 3;
        unitAttributes.get(soldierString)[ARMOUR_DAMAGE] *= 3;
    }
    public void vampirismUpgrade() {
        unitAttributes.get(soldierString)[MAX_HEALTH] *= 9999;
        unitAttributes.get(soldierString)[DAMAGE] *= 9999;
        unitAttributes.get(soldierString)[ATTACK_SPEED] *= 9999;
        unitAttributes.get(soldierString)[ATTACK_RANGE] *= 9999;
        unitAttributes.get(soldierString)[ARMOUR_DAMAGE] *= 9999;
        unitAttributes.get(soldierString)[ARMOUR] *= 9999;
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

    /**
     * Unlocks the Hero Factory building.
     */
    public void unlockHeroFactory() {
        heroFactoryUnlocked = true;
    }

    /**
     * Returns boolean representing whether Hero Factory is unlocked.
     */
    public boolean heroFactoryIsUnlocked() {
        return heroFactoryUnlocked;
    }


    public void buildingArmorUpgrade(){

    }
 
    public void buildingConstructionTimeUpgrade(){

    }

    /**
     * Provides a method to check that the requirements for researching a Technology exists, if they are
     * all satisfied then this function will adjust your resources to reflect the research costs
     * and return a message.
     * @param techMan The Associated technologyManager Class used
     * @param tech The Tech attempting to activate
     * @param teamid - the team researching this tech
     * @return String with message about whether or not the research was okay and why
     */
    public String checkPrereqs(TechnologyManager techMan, Technology tech, int techID, int teamid){
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);

        for (Technology techX : tech.getParents()) {
            if (!(getActive().contains(techX))) {
                return "You have not researched the required technology for this upgrade";
            }
        }
        if(techMan.getActive().contains(tech)) {
            return "You have already researched this upgrade";
        }
        if (tech.getCost()[0] > resourceManager.getRocks(-1)) {
            return "Insufficient Rocks";
        }
        if (tech.getCost()[1] > resourceManager.getCrystal(-1)) {
            return "Insufficient Crystals";
        }
        if (tech.getCost()[2] > resourceManager.getBiomass(-1)) {
            return "Insufficient Biomass";
        }
        return "Activating Technology!";
    }

    /**
     * Checks the ID of the requested technology and executes the functionality of it
     * and reduces resource levels but the cost.
     * @param techMan The Associated technologyManager
     * @param tech The Requested Technology
     * @param resourceManager The ResourceManager being used
     * @param techID The ID tag of the technology
     * @param teamid the ID of the team attempting to upgrade their tech
     */
    public void activateTech(TechnologyManager techMan, Technology tech, ResourceManager resourceManager, int techID, int teamid){
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
            nootropicsUpgrade();
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
    }

    /**
     * Sets up the dependencies of the items' levels (research)
     */
    private void setUpHeroTechs() {
        heroFactory = new Technology(new int[]{0, 0, 20}, "Hero " +
                "Factory", new ArrayList<Technology>(), "Unlocks the ability" +
                " to build factories to manufacture hero units.");

        ArrayList<Technology> armourL1Parents = new ArrayList<Technology>();
        armourL1Parents.add(heroFactory);
        armourLevelOne = new Technology(new int[]{20, 20, 0}, "Armour " +
                "Level One", armourL1Parents, "Unlocks the " +
                "ability to build Level One Armour for Hero units.");

        this.armourL2Parents = new ArrayList<Technology>();
        armourL2Parents.add(armourLevelOne);
        armourLevelTwo = new Technology(new int[]{40, 40, 0}, "Armour " +
                "Level Two", armourL2Parents, "Unlocks the " +
                "ability to build Level Two Armour for Hero units.");

        this.armourL3Parents = new ArrayList<Technology>();
        armourL3Parents.add(armourLevelTwo);
        armourLevelThree = new Technology(new int[]{60, 60, 0}, "Armour " +
                "Level Three", armourL3Parents, "Unlocks the " +
                "ability to build Level Three Armour for Hero units.");

        this.weaponL1Parents = new ArrayList<Technology>();
        weaponL1Parents.add(heroFactory);
        weaponLevelOne = new Technology(new int[]{20, 20, 0}, "Weapons " +
                "Level One", weaponL1Parents, "Unlocks the " +
                "ability to build Level One Weapons for Hero units.");

        this.weaponL2Parents = new ArrayList<Technology>();
        weaponL2Parents.add(weaponLevelOne);
        weaponLevelTwo = new Technology(new int[]{40, 40, 0}, "Weapon " +
                "Level Two", weaponL2Parents, "Unlocks the " +
                "ability to build Level Two Weapons for Hero units.");

        this.weaponL3Parents = new ArrayList<Technology>();
        weaponL3Parents.add(weaponLevelTwo);
        weaponLevelThree = new Technology(new int[]{60, 60, 0}, "Weapons " +
                "Level Three", weaponL3Parents, "Unlocks the " +
                "ability to build Level Three Weapons for Hero units.");

        this.specialParents = new ArrayList<Technology>();
        specialParents.add(heroFactory);
        special = new Technology(new int[]{20, 20, 20}, "Armour " +
                "Level One", specialParents, "Unlocks the " +
                "ability to build Special items for Hero units..");
    }

        /**
         * Gets the buildings available for specified team
         *
         */
    public ArrayList<BuildingType> getAvailableBuildings() {
        ArrayList<BuildingType> buildingsAvailable;
        if (heroFactoryIsUnlocked()) {
            buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
            		BuildingType.WALL, BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS, BuildingType.HEROFACTORY, BuildingType.SPACEX));
        } else {
            buildingsAvailable = new ArrayList<BuildingType>(Arrays.asList(
            		BuildingType.WALL, BuildingType.BASE, BuildingType.BUNKER, BuildingType.TURRET, BuildingType.BARRACKS, BuildingType.SPACEX));
        }
    	return buildingsAvailable;
    }


}