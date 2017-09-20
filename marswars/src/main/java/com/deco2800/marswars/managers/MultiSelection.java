package com.deco2800.marswars.managers;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.InitiateGame.InputProcessor;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.List;

/**
 * Created by Treenhan on 9/19/17.
 * this class holds the information about multiselection
 */
public class MultiSelection {
    AbstractWorld world = GameManager.get().getWorld();


    private int[] tiles = new int[4];

    public void addStartTile(float x, float y){
        float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
        float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
        float projX;
        float projY;

        projX = x/tileWidth;
        projY = -(y - tileHeight / 2f) / tileHeight + projX;
        projX -= projY - projX;

//        if (projX < 0 || projX > world.getWidth() || projY < 0 || projY > world.getLength()) {
//            return;//TODO put the boundary in
//        }

        tiles[0]=(int)projX;
        tiles[1]=(int)projY;
    }

    public void addEndTile(float x, float y){
        float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
        float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
        float projX;
        float projY;

        projX = x/tileWidth;
        projY = -(y - tileHeight / 2f) / tileHeight + projX;
        projX -= projY - projX;

//        if (projX < 0 || projX > world.getWidth() || projY < 0 || projY > world.getLength()) {
//            return;//TODO put the boundary in
//        }

        tiles[2]=(int)projX;
        tiles[3]=(int)projY;
    }

    public int getStartX(){
        return tiles[0];
    }

    public int getStartY(){
        return tiles[1];
    }

    public int getEndX(){
        return tiles[2];
    }

    public int getEndY(){
        return tiles[3];
    }

    public void clickAllTiles() {
        if (tiles[0] >= tiles[2]) {
            if (tiles[1] >= tiles[3]) {
                for (int i = tiles[2]; i <= tiles[0]; i++) {
                    for (int j = tiles[3]; j <= tiles[1]; j++) {
                        callMouseHandler(i, j);
                    }
                }
            } else {
                for (int i = tiles[2]; i <= tiles[0]; i++) {
                    for (int j = tiles[1]; j <= tiles[3]; j++) {
                        callMouseHandler(i, j);
                    }
                }
            }
        } else {
            if (tiles[1] >= tiles[3]) {
                for (int i = tiles[0]; i <= tiles[2]; i++) {
                    for (int j = tiles[3]; j <= tiles[1]; j++) {
                        callMouseHandler(i, j);
                    }
                }
            } else {
                for (int i = tiles[0]; i <= tiles[2]; i++) {
                    for (int j = tiles[1]; j <= tiles[3]; j++) {
                        callMouseHandler(i, j);
                    }
                }
            }
        }
    }

    public void callMouseHandler(int x, int y){
        MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
        mouseHandler.handleMouseClick(x, y, 0,true);

    }


}
