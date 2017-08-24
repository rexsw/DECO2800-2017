package com.deco2800.marswars.worlds.map.tools;

import java.util.Random;
//import com.deco2800.marswars.worlds.map.tools.OpenSimplexNoise;

/**
 * NOT FUNCTIONAL YET!
 * Creates a map of simplex noise.
 * Very useful for placing natural looking patterns of tiles, i.e. patches of grass.
 * Very useful as a natural heightmap when terrain z height is introduced.
 */
public class NoiseMap {
    //2d array to hold noise
    private double featureSize = 32;
    private int width;
    private int height;
    private OpenSimplexNoise noise;
    //seed
    Random r = new Random();

    /**
     * Constructor for the noise map
     * @param width X axis size
     * @param height Y axis size
     */
    public NoiseMap(int width, int height){
        this.width = width;
        this.height = height;
        noise = new OpenSimplexNoise(r.nextInt());
    }

    /**
     * Constructor for the noise map
     * @param width X axis size
     * @param height Y axis size
     * @param featureSize parameter that effects size of
     */
    public NoiseMap(int width, int height, int featureSize){
        this.width = width;
        this.height = height;
        this.featureSize = featureSize;
        noise = new OpenSimplexNoise(r.nextInt());
    }

    /**
     * gets the noise (0<=noise<1) at x,y
     * @param x x position
     * @param y y position
     * @return noise double between 0 and 1
     */
    public double getNoiseAt(int x, int y){
        if (x>=width||x<0||y>=height||y<0){
            throw new IllegalArgumentException("x & y must be in range of the dimensions");
        }
        return noise.eval(x/featureSize, y/featureSize);
    }
}
