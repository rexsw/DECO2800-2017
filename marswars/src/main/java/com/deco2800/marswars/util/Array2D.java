package com.deco2800.marswars.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 2D array implementation.
 *
 * @param <T>
 * @author Anonymousthing
 */
public class Array2D<T> {
    private int width;
    private int length;

    private Object[] arr;

    private static final Logger LOGGER = LoggerFactory.getLogger(Array2D.class);

    //NEVER DELETE THIS, THIS IS USED BY GAME LOADING FUNCTION
    Array2D(){};

    /**
     * Constructs a 2D array of the given dimensions
     *
     * @param width  the width of the array
     * @param height the height of the array
     */
    public Array2D(int width, int height) {
        this.width = width;
        this.length = height;
        arr = new Object[width * height];
    }

    /**
     * Gets the object at the given coordinate in the array
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the object
     */
    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        if (x >= width || y >= length || x < 0 || y < 0) {
            LOGGER.error("X: " + x + " Y: " + y);
            throw new IndexOutOfBoundsException();
        }
        // This cast will always be safe as set() only takes values of type T
        return (T) arr[y * width + x];
    }

    /**
     * Sets the object at the given coordinate in the array
     *
     * @param x   the x coordinate
     * @param y   the y coordinate
     * @param val the object
     */
    public void set(int x, int y, T val) {
        if (x >= width || y >= length || x < 0 || y < 0) {
            throw new IndexOutOfBoundsException();
        }
        arr[y * width + x] = val;
    }

    /**
     * Gets the width of the array
     *
     * @return array width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the array
     *
     * @return array height
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the array in the form of a list
     *
     * @return list form of array
     */
    @SuppressWarnings("unchecked")
	public List<T> getList() {
        ArrayList<T> list = new ArrayList<>();
        for (int i = 0; i < width * length; i++) {
            if (arr[i] != null) {
                list.add((T) arr[i]);
            }
        }
        return list;
    }
}