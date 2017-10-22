package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.Technology;
import org.junit.Test;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author Bosco Bartilomo
 *
 * Tests to see if technologies work properly
 *
 * Hi Sam :)
 */
public class TechnologyManagerTest {

    private TechnologyManager technologyManager = new TechnologyManager();
    Technology tech1 = new Technology(new int[]{10, 0, 0}, "Armour 1", new ArrayList<>(),
            "1st level Armour tech");

    /**
     * Checks to see if technology constructors work properly
     */
    @Test
    public void technologyManagerConstructorTest() {
        //is the active empty
        assertTrue(technologyManager.getActive().size() == 0);
    }

    /**
     * Check to see if adding a technology activates it
     */
    @Test
    public void addTechnologyTest() {
        int[] costs = {0, 0, 0};
        String name = "Constructor test technology";
        List<Technology> parentList = new ArrayList<>();
        String description = "Test description";
        Technology technology = new Technology(costs, name, parentList, description);
        assertTrue(!technologyManager.getActive().contains(technology));
        technologyManager.addActiveTech(technology);
        //should have sufficient resources
        assertTrue(technologyManager.getActive().contains(technology));
    }

    @Test
    public void getTech() throws Exception {
        assertTrue(technologyManager.getTech(1).equals(tech1));
    }

    @Test
    public void getUnitAttribute() throws Exception {
    }

    @Test
    public void getAllTechs() throws Exception {
        assertTrue(!technologyManager.getAllTechs().equals(null));
    }

    @Test
    public void getActive() throws Exception {
    }

    @Test
    public void addActiveTech() throws Exception {
    }

    @Test
    public void attackUpgrade() throws Exception {
        technologyManager.attackUpgrade();
    }

    @Test
    public void armourUpgrade() throws Exception {
        technologyManager.armourUpgrade();
    }

    @Test
    public void speedUpgrade() throws Exception {
        technologyManager.speedUpgrade();
    }

    @Test
    public void healthUpgrade() throws Exception {
        technologyManager.healthUpgrade();
    }

    @Test
    public void nootropicsUpgrade() throws Exception {
    }

    @Test
    public void cowLevelUpgrade() throws Exception {
    }

    @Test
    public void steroidsUpgrade() throws Exception {
    }

    @Test
    public void vampirismUpgrade() throws Exception {
    }

    @Test
    public void unlockArmourLevelOne() throws Exception {
    }

    @Test
    public void unlockArmourLevelTwo() throws Exception {
    }

    @Test
    public void unlockArmourLevelThree() throws Exception {
    }

    @Test
    public void armourIsUnlocked() throws Exception {
    }

    @Test
    public void unlockWeaponLevelOne() throws Exception {
    }

    @Test
    public void unlockWeaponLevelTwo() throws Exception {
    }

    @Test
    public void unlockWeaponLevelThree() throws Exception {
    }

    @Test
    public void weaponIsUnlocked() throws Exception {
    }

    @Test
    public void unlockSpecial() throws Exception {
    }

    @Test
    public void specialIsUnlocked() throws Exception {
    }

    @Test
    public void unlockHeroFactory() throws Exception {
    }

    @Test
    public void checkPrereqs() throws Exception {
        ResourceManager rs = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
        rs.setRocks(0,1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(2), 2,
                1).equals("You have not researched the required technology for this upgrade"));

        assertTrue(technologyManager.checkPrereqs(technologyManager, tech1, 1,
                1).equals("Insufficient Rocks"));
        rs.setRocks(100,1);

        rs.setCrystal(0,1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(5), 5,
                1).equals("Insufficient Crystals"));
        rs.setCrystal(100,1);
        rs.setBiomass(0,1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(9), 9,
                1).equals("Insufficient Biomass"));
        rs.setBiomass(100,1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, tech1, 1,
                1).equals("Activating Technology!"));
        assertTrue(technologyManager.checkPrereqs(technologyManager, tech1, 1,
                1).equals("You have already researched this upgrade"));
    }

    @Test
    public void activateTech() throws Exception {
        ResourceManager rs = new ResourceManager();
        assertTrue(!technologyManager.getActive().contains(tech1));
        technologyManager.activateTech(technologyManager,technologyManager.getTech(1),rs,1,1);
        assertTrue(technologyManager.getActive().contains(tech1));
    }

    @Test
    public void getAvailableBuildings() throws Exception {
    }


}