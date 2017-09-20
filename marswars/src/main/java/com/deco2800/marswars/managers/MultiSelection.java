package com.deco2800.marswars.managers;

import com.badlogic.gdx.math.Vector3;
import com.deco2800.marswars.InitiateGame.InputProcessor;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.worlds.AbstractWorld;

import java.util.List;

/**this class will contain info for multi selection
 * Created by Treenhan on 9/19/17.
 * this class holds the information about multiselection
 */
public class MultiSelection extends Manager {
    //private static AbstractWorld world = GameManager.get().getWorld();

    /**
     * this array contain the status of each tile
     * if it is 1 = it is selected
     * if it is 0 = it is not selected
     */
    private static Array2D<Integer> selectedTiles;

    /**
     * get the status of a selected tile
     * @param x
     * @param y
     * @return
     */
    public static Integer getSelectedTiles(int x, int y){
        return selectedTiles.get(x,y);
    }

    /**
     * reset the whole array to 0 after selecting
     */
    public static void resetSelectedTiles() {
        AbstractWorld world = GameManager.get().getWorld();
        int width=world.getWidth();
        int length=world.getLength();
        selectedTiles = new Array2D<Integer>(width, length);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                selectedTiles.set(i, j, 0);
            }
        }
    }

    /**
     * update the selecting area based on the ending points
     * @param endX
     * @param endY
     */
    public static void updateSelectedTiles(int endX,int endY){

        //this check the boundaries
        AbstractWorld world = GameManager.get().getWorld();
        if (endX < 0){
            endX=0;
        }else if( endX > world.getWidth()){
            endX=world.getWidth();
        }

        if (endY < 0){
            endY=0;
        }else if( endY > world.getLength()){
            endY=world.getLength();
        }

        //assign the ending points
        tiles[2]=endX;
        tiles[3]=endY;

        //loop through to get the selected area
        if (tiles[0] >= tiles[2]) {
            if (tiles[1] >= tiles[3]) {
                for (int i = tiles[2]; i <= tiles[0]; i++) {
                    for (int j = tiles[3]; j <= tiles[1]; j++) {
                        selectedTiles.set(i,j,1);
                    }
                }
            } else {
                for (int i = tiles[2]; i <= tiles[0]; i++) {
                    for (int j = tiles[1]; j <= tiles[3]; j++) {
                        selectedTiles.set(i,j,1);
                    }
                }
            }
        } else {
            if (tiles[1] >= tiles[3]) {
                for (int i = tiles[0]; i <= tiles[2]; i++) {
                    for (int j = tiles[3]; j <= tiles[1]; j++) {
                        selectedTiles.set(i,j,1);
                    }
                }
            } else {
                for (int i = tiles[0]; i <= tiles[2]; i++) {
                    for (int j = tiles[1]; j <= tiles[3]; j++) {
                        selectedTiles.set(i,j,1);
                    }
                }
            }
        }
    }

    /**
     * this array contains the starting and ending point of the selection
     * tiles[0]=startX
     * tiles[1]=startY
     * tiles[2]=endX
     * tiles[3]=endY
     */
    private static int[] tiles = new int[4];

    /**
     * this function will add the starting tile
     * @param x
     * @param y
     */
    public void addStartTile(float x, float y){
        float tileWidth = (float) GameManager.get().getWorld().getMap().getProperties().get("tilewidth", Integer.class);
        float tileHeight = (float) GameManager.get().getWorld().getMap().getProperties().get("tileheight", Integer.class);
        float projX;
        float projY;

        projX = x/tileWidth;
        projY = -(y - tileHeight / 2f) / tileHeight + projX;
        projX -= projY - projX;


        //adding the starting tile
        tiles[0]=(int)projX;
        tiles[1]=(int)projY;
    }

    /**
     * this function will add the ending tile
     * @param x
     * @param y
     */
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

        //add the ending tile
        tiles[2]=(int)projX;
        tiles[3]=(int)projY;
    }

    /**
     * this function will loop through the selected area and call left click to each tile
     */
    public void clickAllTiles() {

        //loop through the area and call the mouse handler at every position
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

    //this function will call the mouse handler at a specific position
    public void callMouseHandler(int x, int y){
        MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
        mouseHandler.handleMouseClick(x, y, 0,true);

    }


}
