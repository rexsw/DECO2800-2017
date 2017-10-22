package com.deco2800.marswars.renderers;

/**
 * An object that can be rendered on the screen.
 * Renderables should have a texture that they can return
 * when asked using the onRender function.
 *
 * Textures should be size suitable to the game.
 */
public interface Renderable {

    /**
     * Renderables must impliment the onRender function.
     * This function allows the current rendering system to request a texture
     * from the object being rendered.
     *
     * Returning null will render an error image in this items place.
     * @return The texture to be rendered onto the screen
     */
    String getTexture();

    /**
     * Gets the X position
     * @return the x position
     */
    float getPosX();

    /**
     * Gets the Y position
     * @return the y position
     */
    float getPosY();

    /**
     * Gets the Z position
     * @return the z position
     */
    float getPosZ();

    /**
     * gets the x offset for rendering
     * @return the x offset
     */
    float getXoff();

    /**
     * gets the y offset for rendering
     * @return te y offset
     */
    float getYoff();
    /**
     * Gets the X render length of the entity
     * @return the x render length of the entity
     */
    float getXRenderLength();

    /**
     * Gets the Y render length of the entity
     * @return the y render length of the entity
     */
    float getYRenderLength();
}
