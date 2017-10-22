package com.deco2800.marswars;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.Technology;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        assertTrue(technologyManager.getActive().isEmpty());
        technologyManager.addActiveTech(tech1);
        assertTrue(technologyManager.getActive().contains(tech1));
    }

    @Test
    public void armourUnlocks() throws Exception {
        technologyManager.setUpUnlockStates();

        //Test Armors
        assertFalse(technologyManager.armourIsUnlocked(1));
        assertFalse(technologyManager.armourIsUnlocked(2));
        assertFalse(technologyManager.armourIsUnlocked(3));
        assertFalse(technologyManager.armourIsUnlocked(100));
        technologyManager.unlockArmourLevelOne();
        technologyManager.unlockArmourLevelTwo();
        technologyManager.unlockArmourLevelThree();
        assertTrue(technologyManager.armourIsUnlocked(1));
        assertTrue(technologyManager.armourIsUnlocked(2));
        assertTrue(technologyManager.armourIsUnlocked(3));


        //Test Weapons
        assertFalse(technologyManager.weaponIsUnlocked(1));
        assertFalse(technologyManager.weaponIsUnlocked(2));
        assertFalse(technologyManager.weaponIsUnlocked(3));
        assertFalse(technologyManager.weaponIsUnlocked(100));
        technologyManager.unlockWeaponLevelOne();
        technologyManager.unlockWeaponLevelTwo();
        technologyManager.unlockWeaponLevelThree();
        assertTrue(technologyManager.weaponIsUnlocked(1));
        assertTrue(technologyManager.weaponIsUnlocked(2));
        assertTrue(technologyManager.weaponIsUnlocked(3));

        //Test Special
        assertFalse(technologyManager.specialIsUnlocked());
        technologyManager.unlockSpecial();
        assertTrue(technologyManager.specialIsUnlocked());
    }

    @Test
    public void unlockHeroFactory() throws Exception {
    }

    @Test @Ignore
    public void checkPrereqs() throws Exception {
        ResourceManager rs = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
        rs.setRocks(0,-1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(2), 2,
                -1).equals("You have not researched the required technology for this upgrade"));

        assertTrue(technologyManager.checkPrereqs(technologyManager, tech1, 1,
                -1).equals("Insufficient Rocks"));
        rs.setRocks(100,-1);

        rs.setCrystal(0,-1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(5), 5,
                -1).equals("Insufficient Crystals"));
        rs.setCrystal(100,-1);
        rs.setBiomass(0,-1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(9), 9,
                -1).equals("Insufficient Biomass"));
        rs.setBiomass(100,-1);
        assertTrue(technologyManager.checkPrereqs(technologyManager, tech1, 1,
                -1).equals("Activating Technology!"));
    }

    @Test
    public void activateTech() throws Exception {
        ResourceManager rs = new ResourceManager();
        assertTrue(!technologyManager.getActive().contains(tech1));
        technologyManager.activateTech(technologyManager,technologyManager.getTech(1),rs,1,1);
        assertTrue(technologyManager.getActive().contains(tech1));
    }


}