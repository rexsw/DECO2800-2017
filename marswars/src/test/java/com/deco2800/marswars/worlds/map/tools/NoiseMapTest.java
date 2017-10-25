package com.deco2800.marswars.worlds.map.tools;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NoiseMapTest {
    NoiseMap noise;

    @Before
    public void setup(){
        noise = new NoiseMap(100, 102);
    }

    @Test
    public void constructorTest(){
        assertTrue(noise.getWidth()==100);
        assertTrue(noise.getHeight()==102);
    }

    @Test
    public void getNoiseAtTest() throws Exception {
        System.out.println("/n noise: "+noise.getNoiseAt(1,1));
        assertTrue(noise.getNoiseAt(1,1)<=1 && noise.getNoiseAt(1,1)>=-1);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getNoiseAtExceptionTest(){
        noise.getNoiseAt(-1,-1);
    }

    @Test (expected = IndexOutOfBoundsException.class)
    public void getNoiseAtExceptionTest2(){
        noise.getNoiseAt(200,200);
    }

}