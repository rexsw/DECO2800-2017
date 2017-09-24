package com.deco2800.marswars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 
 * This class creates a hero factory building that used to create hero characters
 * It may also act as a shop for hero in the later stage
 * NOTE: currently it does not affect the game
 *
 */
public class HeroFactory extends BuildingEntity {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(HeroFactory.class);

    private int owner;
    private AbstractWorld world;
    private boolean selected = false;

    /**
     * Constructor for the hero factory.
     * @param world The world that will hold the hero factory.
     * @param posX its x position on the world.
     * @param posY its y position on the world.
     * @param posZ its z position on the world.
     */
    public HeroFactory(AbstractWorld world, float posX, float posY, float
            posZ, int owner) {
        super(posX, posY, posZ, BuildingType.HEROFACTORY, owner);
        this.setTexture("bunker");  // temporary texture
        this.setEntityType(EntityType.BUILDING);
        this.world = world;
        this.setCost(200);
        this.setSpeed(1.5f);
        this.addNewAction(ActionType.GENERATE);
        world.deSelectAll();
        LOGGER.debug("Create a hero factory");
    }

    /**
     * Check is the hero factory has been selected
     * @return true if is selected, false otherwise
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * Deselect the hero factory, change the selected state
     */
    @Override
    public void deselect() {
        selected = false;
    }

   /**
    * Select the hero factory
    */
    public void select() {
        selected = true;
    }

    /**
     * Get the help text label of this hero factory
     * @return Label that used to displayed in the HUD
     */
    @Override
    public Label getHelpText() {
        return new Label("You have clicked on a hero factory.", new Skin(Gdx
                .files
                .internal("uiskin.json")));
    }

    /**
     * Set the owner of this hero factory
     * @param owner to be set to
     */
    @Override
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Get the owner of this hero factory
     * @return owner of this factory
     */
    @Override
    public int getOwner() {
        return this.owner;
    }

    /**
     * Check if the AbstractEntity passed in and this entity has the same owner
     * @return true if these two entity has the same owner, false otherwise
     */
    @Override
    public boolean sameOwner(AbstractEntity entity) {
        return entity instanceof  HasOwner &&
                this.owner == ((HasOwner) entity).getOwner();
    }

    /**
     * This method is a duplication of the showProgress method, consider delete one of them
     * sadly two different interfaces
     * @return true if this factory currently has an action, false otherwise
     */
    public boolean isWorking() {
        return currentAction.isPresent();
    }

    /**
     * Set the action of this hero factory
     * @param action to be set to this factory
     */
    public void setAction(DecoAction action) {
        currentAction = Optional.of(action);
    }

    /**
     * Create the 'Create Hero' button object
     * handle relative click event as well
     * @return Button that used to create hero
     */
    @Override
    public Button getButton() {
        Button button = new TextButton("Create Hero", new Skin(Gdx.files
                .internal
                ("uiskin.json")));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonWasPressed();
            }
        });
        return button;
    }

    /**
     * Handler for button pressed action
     * Builds hero unit if button was pressed. Note that there are a maximum of
     * three hero units in game
     * 
     */
    public void buttonWasPressed() {
        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
        // need to add some way to check global number of heros here
        if (resourceManager.getBiomass(this.owner) > 30) {
            resourceManager.setBiomass(resourceManager.getBiomass(this.owner)
                            - 30,
                    this.owner);
           /* currentAction = Optional.of(new GenerateAction(new HeroSpacman
                    (world, this.getPosX() - 1, this.getPosY() - 1, 0)));
            // increment golbal hero counter here
             */ // NEED TO FIX THIS TO MAKE IT WORK
        }
    }
}
