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

        //Damage tech set up
        techMap.put(5, new Technology(new int[]{0, 20, 0, 0}, "Damage 1", new ArrayList<>(),
                "An expensive technology"));
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


        //Speed tech Set up
        techMap.put(9, new Technology(new int[]{0, 0, 5, 5}, "Speed 1", new ArrayList<Technology>(),
                "An expensive technology"));
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


        //Health Tech Set up
        techMap.put(13, new Technology(new int[]{0, 0, 0, 20}, "Health 1", new ArrayList<Technology>(),
                "An expensive technology"));
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


        //Special tech Set up
        techMap.put(17, new Technology(new int[]{10, 10, 0, 15}, "Hero Factory", new ArrayList<Technology>(),
                "An expensive technology"));
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


 // unitAttribute format; <"Name of Unit", [Cost, MaxHealth, Damage, Armor, ArmorDamage, AttackRange, AttackSpeed]>
        unitAttributes.put("TankDestroyer", new int[]{10, 500, 200, 200, 100, 12, 10});
        unitAttributes.put("Soldier", new int[]{10, 500, 50, 250, 50, 8, 30});
        unitAttributes.put("Tank", new int[]{10, 2500, 75, 500, 150, 10, 20});
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
    public void attackUpgrade(){
        unitAttributes.get("Soldier")[2] *= 2;
        unitAttributes.get("Soldier")[4] *= 2;
        }
    public void armourUpgrade(){
        unitAttributes.get("Soldier")[3] *= 2;
    }
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
        if (!(resourceManager.getWater(teamid) > tech.getCost()[2])) {
            return "Insufficient Water levels";
        }
        if (!(resourceManager.getBiomass(teamid) > tech.getCost()[3])) {
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
            unlockHeroFactory();
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
        System.out.println("######################################");
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
//        techMap.put(17, heroFactory);
//        techMap.put(2, armourLevelOne);
//        techMap.put(3, armourLevelTwo);
//        techMap.put(4, armourLevelThree);
//        techMap.put(5, weaponLevelOne);
//        techMap.put(6, weaponLevelTwo);
//        techMap.put(7, weaponLevelThree);
//        techMap.put(8, special);
    }
}