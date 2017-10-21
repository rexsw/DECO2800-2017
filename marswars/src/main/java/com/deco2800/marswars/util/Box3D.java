package com.deco2800.marswars.util;

/**
 * Representation of a box in 3d space, defined by a corner point in XYZ and
 * extends in x (xLength), y (yLength), and z (zLength).
 * 
 * @author leggy
 *
 */
public class Box3D {

	private float x;
	private float y;
	private float z;

	private float xLength;
	private float yLength;
	private float zLength;

	//NEVER DELETE THIS
	Box3D(){}

	/**
	 * Constructs a new Box3D with the given corner point and dimensions.
	 * 
	 * @param x
	 *            The corner point x-coordinate.
	 * @param y
	 *            The corner point y-coordinate.
	 * @param z
	 *            The corner point z-coordinate.
	 * @param xLength
	 *            The xLength (in x).
	 * @param yLength
	 *            The yLength (in y).
	 * @param zLength
	 *            The zLength (in z).
	 */
	public Box3D(float x, float y, float z, float xLength, float yLength, float zLength) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xLength = xLength;
		this.yLength = yLength;
		this.zLength = zLength;
	}

	/**
	 * Constructs a new Box3D based on the given box.
	 * 
	 * @param box
	 *            The box to copy.
	 */
	public Box3D(Box3D box) {
		this.x = box.x;
		this.y = box.y;
		this.z = box.z;
		this.xLength = box.xLength;
		this.yLength = box.yLength;
		this.zLength = box.zLength;
	}

	/**
	 * Returns the x coordinate.
	 * 
	 * @return Returns the x coordinate.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Sets the x coordinate.
	 * 
	 * @param x
	 *            The new x coordinate.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate.
	 * 
	 * @return Returns the y coordinate.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the y coordinate.
	 * 
	 * @param y
	 *            The new y coordinate.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the z coordinate.
	 * 
	 * @return Returns the z coordinate.
	 */
	public float getZ() {
		return z;
	}

	/**
	 * Sets the z coordinate.
	 * 
	 * @param z
	 *            The new z coordinate.
	 */
	public void setZ(float z) {
		this.z = z;
	}

	/**
	 * Returns the xLength (in x).
	 * 
	 * @return Returns the xLength.
	 */
	public float getXLength() {
		return xLength;
	}

	/**
	 * Returns the yLength (in y).
	 * 
	 * @return Returns the yLength.
	 */
	public float getYLength() {
		return yLength;
	}

	/**
	 * Returns the zLength (in z).
	 * 
	 * @return Returns the zLength.
	 */
	public float getZLength() {
		return zLength;
	}


	/**
	 *
	 * @param box the box that will be compared.
	 * @return whether the two boxes overlap.
	 */
	public boolean overlaps(Box3D box) {
		/*
		 * Checking non-collision on all 6 directions.
		 */

		// x smaller
		if (x + xLength < box.x) {
			return false;
		}

		// x larger
		if (x > box.x + box.xLength) {
			return false;
		}

		// y smaller
		if (y + yLength < box.y) {
			return false;
		}

		// y larger
		if (y > box.y + box.yLength) {
			return false;
		}

		// z smaller
		if (z + zLength < box.z) {
			return false;
		}

		// z larger
		return  z <= box.z + box.zLength;
	}

	/**
	 * Calculates the distance between two box3ds.
	 *
	 * @param o the box that marks the second point.
	 * @return the distance between the 2 points.
	 */
	public float distance(Box3D o) {
		return (float)(Math.sqrt(Math.pow(o.x - this.x, 2) + Math.pow(o.y - this.y, 2) + Math.pow(o.z - this.z, 2)));
	}
	
	@Override
	/**
	 * Check if two Box3d Objects are the same
	 * @param o
	 * @return
	 */
	public boolean equals(Object object) {
		final double epsilon = 0.00001;
		if (!(object instanceof Box3D)) {
			return false;
		}
		Box3D other = (Box3D) object;
		if (Math.abs(this.x - other.x) > epsilon && Math.abs(this.y - other.y) > epsilon && Math.abs(this.z - other.z) > epsilon) {
			return false;
		}
			return Math.abs(this.x - other.x) < epsilon && Math.abs(this.y - other.y) < epsilon && Math.abs(this.z - other.z) < epsilon;
		}

	/**
	 * HashCode method
	 */
	@Override
	public int hashCode() {
		int result = 31;
		result = result * 37 + ((Float)this.x).hashCode();
		result = result * 37 + ((Float)this.y).hashCode();
		result = result * 37 + ((Float)this.z).hashCode();
		result = result * 37 + ((Float)this.xLength).hashCode();
		result = result * 37 + ((Float)this.yLength).hashCode();
		result = result * 37 + ((Float)this.zLength).hashCode();
		return result;
	}
}
