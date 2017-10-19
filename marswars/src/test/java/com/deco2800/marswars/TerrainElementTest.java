package com.deco2800.marswars;

import com.deco2800.marswars.entities.TerrainElements.TerrainElementTypes;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class TerrainElementTest {
        @Test
        public void checkTerrain() {
            assertNotNull(TerrainElementTypes.CAVE);
            assertNotNull(TerrainElementTypes.LAKE);
            assertNotNull(TerrainElementTypes.POND);
            assertNotNull(TerrainElementTypes.QUICKSAND);

        }

    }
