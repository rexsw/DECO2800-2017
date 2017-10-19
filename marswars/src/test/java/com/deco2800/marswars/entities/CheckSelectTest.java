package com.deco2800.marswars.entities;

import com.deco2800.marswars.buildings.BuildingType;
import com.deco2800.marswars.buildings.CheckSelect;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Nick on 19/10/17.
 */
public class CheckSelectTest {
    @Test
    public void checkConstructor () {
        CheckSelect check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BASE);
        Assert.assertNotNull(check);
    }

    @Test
    public void checkNotLimitedBuildingConstructor () {
        CheckSelect check = new CheckSelect(0, 0, 0, 0, 0, 0, true);
        Assert.assertNotNull(check);
    }

    @Test
    public void checkGreen () {
        CheckSelect check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BASE);
        Assert.assertNotNull(check);
        check.setGreen();
        Assert.assertEquals("greenSelect2", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.TURRET);
        Assert.assertNotNull(check);
        check.setGreen();
        Assert.assertEquals("greenSelect4", check.getTexture());


        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BARRACKS);
        Assert.assertNotNull(check);
        check.setGreen();
        Assert.assertEquals("greenSelect3", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BUNKER);
        Assert.assertNotNull(check);
        check.setGreen();
        Assert.assertEquals("greenSelect1", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.HEROFACTORY);
        Assert.assertNotNull(check);
        check.setGreen();
        Assert.assertEquals("greenSelect6", check.getTexture());
    }

    @Test
    public void checkRed () {
        CheckSelect check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BASE);
        Assert.assertNotNull(check);
        check.setRed();
        Assert.assertEquals("redSelect2", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.TURRET);
        Assert.assertNotNull(check);
        check.setRed();
        Assert.assertEquals("redSelect4", check.getTexture());


        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BARRACKS);
        Assert.assertNotNull(check);
        check.setRed();
        Assert.assertEquals("redSelect3", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.BUNKER);
        Assert.assertNotNull(check);
        check.setRed();
        Assert.assertEquals("redSelect1", check.getTexture());

        check = new CheckSelect(0, 0, 0, 0, 0, 0, BuildingType.HEROFACTORY);
        Assert.assertNotNull(check);
        check.setRed();
        Assert.assertEquals("redSelect6", check.getTexture());
    }
}
