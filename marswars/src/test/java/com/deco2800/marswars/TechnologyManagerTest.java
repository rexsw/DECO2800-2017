package com.deco2800.marswars;

import com.deco2800.marswars.managers.TechnologyManager;
import com.deco2800.marswars.technology.Technology;
import org.junit.Test;

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
        technologyManager.checkPrereqs(technologyManager, technologyManager.getTech(1), 1, -1);
    }

}