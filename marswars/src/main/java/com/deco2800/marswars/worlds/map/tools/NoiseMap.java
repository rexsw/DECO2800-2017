package com.deco2800.marswars.worlds.map.tools;

import java.util.Random;

/**
 * NOT FUNCTIONAL YET!
 * Creates a map of (natural, perlin-style)random noise.
 * Very useful for placing natural looking patterns of tiles, i.e. patches of grass.
 * Very useful as a natural heightmap when terrain z height is introduced.
 */
public class NoiseMap {
    //2d array to hold noise
    private int[][] noiseMap;
    private int width;
    private int height;

    //range in which getnoise will return
    private int range;
    //how steeply the graph can possibly incline/decline
    private int steepness = 1;

    NoiseMap(int width, int height, int noiseLevels, int steepness){
        if (noiseLevels<2){ throw new IllegalArgumentException("Invalid levels of noise"); }
        if (width<2 || height < 2){ throw new IllegalArgumentException("Invalid dimensions"); }
        range=100;

        if (noiseLevels<100) { range = noiseLevels; }

        this.width = width;
        this.height = height;
        this.steepness = steepness;

        noiseMap = new int[width][height];
        //generate noise
        noiseX();
        noiseY();
    }

    /*
    random value between 0 and range at x,y
     */
    public int getNoiseAt(int x, int y){
        return noiseMap[x][y];
    }

    //fills row noise
    private void noiseX(){
        int[] noise = noise1D(width);
        for (int iy=0; iy<height; iy++){
            for (int ix=0; ix<width; ix++){
                noiseMap[ix][iy]=noise[ix];
            }
        }
    }

    //fills column noise
    private void noiseY(){
        int[] noise = noise1D(height);
        for (int ix=0; ix<width; ix++){
            for (int iy=0; iy<height; iy++){
                //simple linear interpolation
                noiseMap[ix][iy]=(noiseMap[ix][iy]+noise[iy])/2;
            }
        }
    }

    //generate a 1 dimensional noise map
    // !!! intersection of maps??
    private int[] noise1D(int length){
        Random r = new Random();
        int[] noise = new int[length];
        int steep;

        noise[0] = r.nextInt(range);

        for (int i=1; i<length; i++) {
            steep = r.nextInt(steepness*2+1)-steepness;
            noise[i] = noise[i-1]+steep;
            //keep noise in range
            if (noise[i]<0) noise[i] = 0;
            if (noise[i]>=range) noise[i] = range - 1;
        }

        return noise;
    }
}
