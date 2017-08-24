package com.deco2800.marswars;

import com.deco2800.marswars.technology.Technology;
import org.junit.Test;
import java.util.ArrayList;
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
        technology = new Technology(costs, name, new ArrayList<>());
        // test to see if the costs are the same
        assertArrayEquals(technology.getCost(), costs);
        // test to see if the name is the same
        assertEquals(technology.getName(), name);
    }

    /**
     * Tests to see if IllegalArgumentExceptions are thrown, as they should be
     */
    @Test (expected = IllegalArgumentException.class)
    public void illegalArgumentTechnologyTest() {
        // not four resources, only one for this test
        technology = new Technology(new int[]{2000}, "Not enough resources",
                new ArrayList<>());
        // the resource costs are negative
        technology = new Technology(new int[]{-1, -1, -1, -1}, "Bad resource" +
                " cost technology", new ArrayList<>());
        // null name
        technology = new Technology(new int[]{1, 1, 1 ,1}, null, new ArrayList<>());
        // null parents
        technology = new Technology(new int[]{1, 1, 1, 1}, "No parents :(", null);
    }
}
