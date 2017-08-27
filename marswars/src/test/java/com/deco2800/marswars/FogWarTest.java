package com.deco2800.marswars;

import com.deco2800.marswars.entities.FogOfWarLayer;
import com.deco2800.marswars.worlds.FogWorld;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * test for the fog world
 *
 * @Author Treenhan
 * Created by Treenhan on 8/26/17.
 */
public class FogWarTest {
    FogWorld world = new FogWorld();

     @Test
    public void checkArrayNotNull(){
         assertNotNull(world.getFogMap());
     }
}
