package com.deco2800.marswars;

import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.hud.EntityPortrait;
import com.deco2800.marswars.worlds.BaseWorld;
import org.junit.Assert;
import org.junit.Test;
/**
 * Created by Hayden on 23/10/2017.
 * Cannot make other tests because I cant get skins to load in a test
 */
public class EntityPortraitTest {

    @Test
    public void nullCreationTest(){
        Base b = new Base(new BaseWorld(10,10),0,0,0,1);
        EntityPortrait e2 = b.getPortrait();
        Assert.assertFalse(e2 != null);
    }
}
