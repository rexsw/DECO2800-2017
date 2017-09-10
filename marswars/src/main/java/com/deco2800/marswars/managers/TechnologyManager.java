package com.deco2800.marswars.managers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;


import com.deco2800.marswars.technology.*;

public class TechnologyManager extends Manager{
    public Map<Integer, Technology> techMap = new HashMap<Integer, Technology>();
    private Set<Technology> activeTech = new HashSet<Technology>();
    private Technology heroFactory;
    private ArrayList<Technology> armourL1Parents;
    private Technology armourLevelOne;
    private ArrayList<Technology> armourL2Parents;
    private Technology armourLevelTwo;
    private ArrayList<Technology> armourL3Parents;
    private Technology armourLevelThree;
    private ArrayList<Technology> weaponL1Parents;
    private Technology weaponLevelOne;
    private ArrayList<Technology> weaponL2Parents;
    private Technology weaponLevelTwo;
    private ArrayList<Technology> weaponL3Parents;
    private Technology weaponLevelThree;
    private ArrayList<Technology> specialParents;
    private Technology special;

    public TechnologyManager() {
        setUpHeroTechs();
        setUpTechMap();
    }

    public Technology getTech(int id){
        return techMap.get(id);
    }
    /**
     provides a function to generate a List<String> representation of all the available technologies
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

    public void unlockHeroFactory() {

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
            unlockHeroFactory();
        }
        if(techID == 2){
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