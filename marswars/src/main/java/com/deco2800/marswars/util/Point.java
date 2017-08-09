package com.deco2800.marswars.util;

/**
 * Created by Declan on 30/07/2017.
 */
public class Point {
	private float x, y;

	/**
	 * Constructor for the point class
	 * @param x
	 * @param y
	 */
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the X coordiante
	 * @return
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the Y coordinate
	 * @return
	 */
	public float getY() {
		return y;
	}

	/**
	 * Equals method
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (Float.compare(point.x, x) != 0) return false;
		return Float.compare(point.y, y) == 0;
	}

	/**
	 * hashcode method
	 * @return
	 */
	@Override
	public int hashCode() {
		int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
		result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
		return result;
	}

	/**
	 * Returns a string representaiton of the point
	 * @return
	 */
	@Override
	public String toString() {
		return String.format("(%f, %f)", x, y);
	}

	/**
	 * Calcualtes the distance between two points
	 * @param other
	 * @return
	 */
	public float distanceTo(Point other) {
		float dx = other.x - x;
		float dy = other.y - y;
		return (float)Math.sqrt(dx * dx + dy * dy);
	}
}