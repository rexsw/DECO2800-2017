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
        int[] costs = {2, 2, 2, 2};
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
    public void illegalArgumentTechnologyTest() {
        // not four resources, only one for this test
        technology = new Technology(new int[]{2000}, "Not enough resources",
                new ArrayList<>(), "Description");
        // the resource costs are negative
        technology = new Technology(new int[]{-1, -1, -1, -1}, "Bad resource" +
                " cost technology", new ArrayList<>(), "Description");
        // null name
        technology = new Technology(new int[]{1, 1, 1 ,1}, null,
                new ArrayList<>(), "Description");
        // null parents
        technology = new Technology(new int[]{1, 1, 1, 1}, "No parents :(",
                null, "Description");
        // null description
        technology = new Technology(new int[]{1 ,1 ,1, 1}, "Null description " +
                "tech", new ArrayList<>(), null);
    }
}
