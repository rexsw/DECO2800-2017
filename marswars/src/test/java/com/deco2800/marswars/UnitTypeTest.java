package com.deco2800.marswars;

import com.deco2800.marswars.entities.units.UnitTypes;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;


public class UnitTypeTest {
    @Test
    public void checkUnitTypes() {
        assertNotNull(UnitTypes.ASTRONAUT);
        assertNotNull(UnitTypes.TANK);
        assertNotNull(UnitTypes.SPACMAN);
        assertNotNull(UnitTypes.SOLDIER);
        assertNotNull(UnitTypes.COMMANDER);
    }

}
