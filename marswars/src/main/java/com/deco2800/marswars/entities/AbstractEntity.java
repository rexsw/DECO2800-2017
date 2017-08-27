package com.deco2800.marswars.entities;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.util.Box3D;
import com.deco2800.marswars.worlds.AbstractWorld;

/**
 * A AbstractEntity is an item that can exist in both 3D and 2D worlds
 * AbstractEntities are rendered by Render2D and Render3D An item that does not
 * need to be rendered should not be a WorldEntity
 */
public abstract class AbstractEntity implements Renderable, Comparable<AbstractEntity> {

	private Box3D position;

	private float xRenderLength;

	private float yRenderLength;

	private boolean centered;

	private String texture = "error_box";

	protected boolean canWalkOver = false;

	/**
	 * Constructor for the abstract entity
	 * @param posX
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 */
	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength) {
		this(posX, posY, posZ, xLength, yLength, zLength, xLength, yLength, false);
	}

	/**
	 * Full blown constructor for the abstract entity
	 * @param
	 * @param posY
	 * @param posZ
	 * @param xLength
	 * @param yLength
	 * @param zLength
	 * @param xRenderLength
	 * @param yRenderLength
	 * @param centered
	 */
	public AbstractEntity(float posX, float posY, float posZ, float xLength, float yLength, float zLength,
						  float xRenderLength, float yRenderLength, boolean centered) {
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.centered = centered;

		if (centered) {
			posX += (1-xLength/2);
			posY += (1-yLength/2);
		}
		this.position = new Box3D(posX, posY, posZ, xLength, yLength, zLength);
	}

	/**
	 * Constructor for the abstract entity with Box3D position passed in
	 * @param position
	 * @param xRenderLength
	 * @param yRenderLength
	 * @param centered
	 */
	public AbstractEntity(Box3D position, float xRenderLength, float yRenderLength, boolean centered) {
		this.position = new Box3D(position);
		this.xRenderLength = xRenderLength;
		this.yRenderLength = yRenderLength;
		this.centered = centered;
	}

	/**
	 * Get the X position of this AbstractWorld Entity
	 * 
	 * @return The X position
	 */
	public float getPosX() {
		float x = position.getX();
		if (this.centered) {
			x -= (1 - this.position.getXLength() / 2);
		}
		return x;
	}

	/**
	 * Get the Y position of this AbstractWorld Entity
	 * 
	 * @return The Y position
	 */
	public float getPosY() {
		float y = position.getY();
		if (this.centered) {
			y -= (1 - this.position.getYLength() / 2);
		}
		return y;
	}

	/**
	 * Get the Z position of this AbstractWorld Entity
	 * 
	 * @return The Z position
	 */
	public float getPosZ() {
		return position.getZ();
	}

	/**
	 * Sets the current position of the abstract Entity
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition(float x, float y, float z) {
		if (this.centered) {
			y += (1 - this.position.getYLength() / 2);
			x += (1 - this.position.getXLength() / 2);
		}
		this.position.setX(x);
		this.position.setY(y);
		this.position.setZ(z);
	}

	/**
	 * Sets the Position X
	 * @param x
	 */
	public void setPosX(float x) {
		if (this.centered) {
			x += (1-this.position.getXLength() / 2);
		}
		this.position.setX(x);
	}

	/**
	 * Sets the position Y
	 * @param y
	 */
	public void setPosY(float y) {
		if (this.centered) {
			y += (1 - this.position.getYLength() / 2);
		}
		this.position.setY(y);
	}

	/**
	 * Sets the position Z
	 * @param z
	 */
	public void setPosZ(float z) {
		this.position.setZ(z);
	}

	/**
	 * Get the height value of this item. In 3D worlds this is the stack index. In
	 * 2D worlds this is the height of an object
	 * 
	 * @return height
	 */
	public float getZLength() {
		return position.getZLength();
	}

	/**
	 * Get the item's x direction length
	 * 
	 * @return xLength
	 */
	public float getXLength() {
		return position.getXLength();
	}

	/**
	 * Get the item's y direction length
	 * 
	 * @return xLength
	 */
	public float getYLength() {
		return position.getYLength();
	}

	/**
	 * Check if this abstract entity is colliding with another abstract entity
	 * 
	 * @param entity
	 * @return True if collides, False if not
	 */
	public boolean collidesWith(AbstractEntity entity) {
		return this.position.overlaps(entity.position);
	}

	/**
	 * Get the x Render length of the entity
	 * 
	 * @return xRenderLength
	 */
	@Override
	public float getXRenderLength() {
		return this.xRenderLength;
	}

	/**
	 * Get the y Render length of the entity
	 * 
	 * @return yRenderLength
	 */
	@Override
	public float getYRenderLength() {
		return this.yRenderLength;
	}

	/**
	 * Returns a Box3D representing the location.
	 * 
	 * @return Returns a Box3D representing the location.
	 */
	public Box3D getBox3D() {
		return new Box3D(position);
	}

	/**
	 * Gives the string for the texture of this entity. This does not mean the
	 * texture is currently registered.
	 * 
	 * @return texture string.
	 */
	public String getTexture() {
		return texture;
	}

	/**
	 * Sets the texture string for this entity. Check the texture is registered with
	 * the TextureRegister.
	 * 
	 * @param texture String texture id.
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}

	/**
	 * Allows sorting of WorldEntities for Isometric rendering
	 * 
	 * @param o the object to be compared to.
	 * @return -1 if less than, 0 if equal or 1 if more than.
	 */
	@Override
	public int compareTo(AbstractEntity o) {
		float cartX = this.position.getX();
		float cartY = this.getParent().getLength() - this.position.getY();

		float isoX = (cartX - cartY) / 2.0f;
		float isoY = (cartX + cartY) / 2.0f;

		float oCartX = o.getPosX();
		float oCartY = o.getParent().getLength() - o.getPosY();

		float oIsoX = (oCartX - oCartY) / 2.0f;
		float oIsoY = (oCartX + oCartY) / 2.0f;

		if (Math.abs(isoY - oIsoY) < 0.000001f) {
			if (isoX < oIsoX) {
				return 1;
			} else if (isoX > oIsoX) {
				return -1;
			} else {
				return 0;
			}
		} else if (isoY < oIsoY) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * Equals method
	 *
	 * @param o the object to compare to.
	 * @return returns whhether two objects are instances of AbstractEntity &
	 * 			equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		AbstractEntity that = (AbstractEntity) o;

		if (position != null ? !position.equals(that.position) : that.position != null)
			return false;
		return texture != null ? texture.equals(that.texture) : that.texture == null;
	}

	/**
	 * Hashcode method
	 *
	 * @return returns a hashcode for an instance of the AbstractEntity class
	 */
	@Override
	public int hashCode() {
		int result = position != null ? position.hashCode() : 0;
		result = 31 * result + (texture != null ? texture.hashCode() : 0);
		return result;
	}

	/**
	 * gets the parent world for this entity
	 * NOTE: This is useless now that we have GameManager
	 *
	 * @deprecated
	 * @return returns the world loaded on the game manager.
	 */
	@Deprecated
	public AbstractWorld getParent() {
		return GameManager.get().getWorld();
	}

	/**
	 * Returns the distance between two entities
	 *
	 * @param e the end point.
	 * @return Returns the distance between two entities
	 */
	public float distance(AbstractEntity e) {
		return this.getBox3D().distance(e.getBox3D());
	}

	/**
	 * Returns true if this entity can be walked over.
	 * Allows the renderer to render entitys ontop of eachother if required
	 *
	 * @return Returns true if this entity can be walked over.
	 */
	public boolean canWalOver() {
		return canWalkOver;
	}
}
