package com.deco2800.marswars;

import com.deco2800.marswars.util.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseWorldUtilTest {

    private Point point1;
    private Point point2;
    private Point point3;
    @Before
    public void setup(){
        point1 = new Point(2.34f, 3.123f);
        point2 = new Point(2.0f, 2.0f);
        point3 = new Point(2.01f, 2.0f);
    }

    @Test
    public void testPointToString(){
        Assert.assertEquals("Output string not equal",
                "(2.340000, 3.123000)", point1.toString());
    }

    @Test
    public void testPointDistanceTo(){
        System.out.println(point2.distanceTo(point3));
        Assert.assertEquals(0.01f, point2.distanceTo(point3),0.001);
    }

}
