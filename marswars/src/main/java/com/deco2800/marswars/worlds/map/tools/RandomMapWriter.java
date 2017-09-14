package com.deco2800.marswars.worlds.map.tools;

import com.deco2800.marswars.util.Point;

import java.lang.Math.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.math.MathUtils.floor;

/**
 * <p>Class for generating a new TMX map file to be loaded. For the purpose of randon tile placement without changing
 * the tilemap at runtime or using entity tiles.</p>
 *
 * <p>Saves the generated map as "tmap.tmx" - or whatever mapName is</p>
 *
 * <p>Use the parameters like noiseMap to integrate with other features, using the same noiseMap to
 * place resources or units will result in them being placed on the same tile types</p>
 *
 * @author Matthew Jordan
 */
public class RandomMapWriter {
    //width and height of the map
    private int width, height;
    /*contains height-order tiles for placement with simplex noise
        placed in order from 0 to length-1. Water tiles or other tiles that represent low terrain would be at the bottom
        of the list.
     */
    private List orderTiles;
    //List that affects the ratio of one tile piece to another, in same order as orderTiles
    private NoiseMap noiseMap;
    //output file
    public final static String FILENAME = "resources/mapAssets/tmap.tmx";
    //some hard coded constants
    private final int tileHeight = 32;
    private final int tileWidth = 55;
    //add tiles
    private Map<pointInt, String> tileOverride = new HashMap<pointInt, String>();

    /**
     * create a new Random Map Writer
     * @param width width of the map to be generated
     * @param height height of the map to be generated
     * @param orderTiles integer list of of tile ID's,
     * @param noiseMap the noiseMap to use
     */
    public RandomMapWriter(int width, int height, List<Integer> orderTiles, NoiseMap noiseMap) {
        this.width = width;
        this.height = height;
        this.orderTiles = orderTiles;
        this.noiseMap = noiseMap;
    }

    public void addTile(int x, int y, int tile){
        tileOverride.put(new pointInt(x,y), tile+"");
    }

    /**
     * write it outtt! (to the file)
     */
    public void writeMap() throws IOException{
        PrintWriter writer = new PrintWriter(new File(FILENAME));

        //fill the header info
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<map version=\"1.0\" tiledversion=\"1.0.2\" orientation=\"isometric\" " +
                "renderorder=\"right-down\" width=\""+width+"\" height=\""+height+"\" tilewidth=\""+tileWidth+"\" " +
                "tileheight=\""+tileHeight+"\" nextobjectid=\"1\">\n" +
                " <tileset firstgid=\"1\" source=\"tileset1.tsx\"/>\n" +
                " <layer name=\"Tile Layer 1\" width=\"100\" height=\"100\">\n" +
                "  <data encoding=\"csv\">");

        //fill from noisemap
        double noise=0;
        int ix,iy;
        for(iy=0; iy<height; iy++){
            for(ix=0; ix<width; ix++){
                //check if this cell is to be overridden
                String x = tileOverride.get(new pointInt(ix,iy));
                if ( x!=null ){
                    writer.write(x);
                }
                else {
                    noise = noiseMap.getNoiseAt(ix, iy);
                    int index = (int) Math.floor((noise + 1) / 2 * orderTiles.size());
                    writer.write(orderTiles.get(index).toString());
                }
                writer.write(",");
            }
        }

        //write XML tail info
        writer.write("</data>\n</layer>\n</map>");
        //flush the buffer to the file
        writer.flush();
        //done!
        writer.close();
    }


    /**
     * private class to store an int corresponding to a point
     */
    private class pointInt{
        private int x;
        private int y;

        /**
         * create a new point
         * @param x x pos
         * @param y y pos
         */
        pointInt(int x, int y){
            this.x=x;
            this.y=y;
        }

        /**
         * returns the x position
         * @return x
         */
        public int getX(){
            return x;
        }

        /**
         * returns the y position
         * @return y
         */
        public int getY(){
            return y;
        }

        /**
         * returns the hashcode
         * @return hashcode
         */
        @Override
        public int hashCode() {
            return x;
        }

        /**
         * checks if this is at the same point as o
         * @param o
         * @return t/f, whether they share the same pos
         */
        @Override
        public boolean equals(Object o) {
            return ((pointInt) o).getX()==x && ((pointInt) o).getY()==y;
        }
    }
}
