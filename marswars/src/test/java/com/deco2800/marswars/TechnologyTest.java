package com.deco2800.marswars;

import com.deco2800.marswars.technology.Technology;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author Alec Bassingthwaighte (alecbass)
 *
 * Tests to see if technologies work properly
 *
 * Hi Sam :)
 */
public class TechnologyTest {
    private Technology technology;

    /**
     * Checks to see if technology constructors work properly
     */
    @Test
    public void technologyConstructorTest() {
        int[] costs = {2, 2, 2};
        String name = "Constructor test technology";
        List<Technology> parentList = new ArrayList<>();
        String description = "Test description";
        technology = new Technology(costs, name, parentList, description);
        // test to see if the costs are the same
        assertArrayEquals(technology.getCost(), costs);
        // test to see if the name is the same
        assertEquals(technology.getName(), name);
        // test to see if the parents are the same
        assertEquals(technology.getParents(), parentList);
        // test to see if the descriptions are the same
        assertEquals(technology.getDescription(), description);
    }

    /**
     * Tests to see if IllegalArgumentExceptions are thrown, as they should be
     */
    @Test (expected = IllegalArgumentException.class)
    public void badResourceArrayLengthTechnologyTest() {
        // not four resources, only one for this test
        technology = new Technology(new int[]{2000}, "Not enough resources",
                new ArrayList<>(), "Description");
    }

    /** Negative resource cost tests */
    @Test (expected = IllegalArgumentException.class)
    public void negativeTechnologyResourceCostTest() {
        // the resource costs are negative
        technology = new Technology(new int[]{-1, -1, -1}, "Bad resource" +
                " cost technology", new ArrayList<>(), "Description");
    }

    /** Tests against a null technology name */
    @Test (expected = IllegalArgumentException.class)
    public void nullTechnologyNameTest() {
        // null name
        technology = new Technology(new int[]{1, 1, 1}, null,
                new ArrayList<>(), "Description");
    }

    /** Tests against a null parent technology list */
    @Test (expected = IllegalArgumentException.class)
    public void nullTechnologyParentsTest() {
        // null parents
        technology = new Technology(new int[]{1, 1, 1}, "No parents :(",
                null, "Description");
    }

    /** Tests against a null technology description */
    @Test (expected = IllegalArgumentException.class)
    public void nullTechonologyDescriptionTest() {
        // null name
        technology = new Technology(new int[]{1, 1, 1}, "Null description",
                new ArrayList<>(), null);
    }

    /** Tests the toString() method of a techonology */
    @Test
    public void techonologyToStringTest() {
        List<Technology> parentTechs = new ArrayList<>();
        Technology parentTech = new Technology(new int[]{10, 10, 10}, "Parent tech", new ArrayList<>(), "Parent technology");
        parentTechs.add(parentTech);
        technology = new Technology(new int[]{1, 1, 1}, "toString() tech", parentTechs, "Tests toString()");
        String comparedString = "Technology: toString() tech" + '\n' + "Cost: 1, 1, 1" + '\n' + "Parent techs: " + parentTech.getName() + ", " + '\n' + "Description: Tests toString()";
        System.out.println(technology.toString());
        assertEquals(technology.toString(), comparedString);
    }

    /** Tests the equals() method of techonlogies */
    @Test
    public void technologyEqualsTest() {
        Technology technology = new Technology(new int[]{10, 10, 10}, "Equals technology", new ArrayList<>(), "Equals technology");
        assertEquals(technology, technology);
        Technology nullTechnology = null;
        // null
        assertNotEquals(technology, nullTechnology);
        // different class
        assertNotEquals(technology, "This is a string, not a technology");
        List<Technology> otherTechnologyParents = new ArrayList<>();
        otherTechnologyParents.add(technology);
        Technology otherTechnology = new Technology(new int[]{5, 5, 5}, "Other techonology", otherTechnologyParents, "Other technology");
        assertNotEquals(technology, otherTechnology);
    }
}
