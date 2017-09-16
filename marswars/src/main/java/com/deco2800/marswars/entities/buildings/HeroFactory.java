package com.deco2800.marswars.entities.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.actions.ActionType;
import com.deco2800.marswars.actions.DecoAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.managers.ResourceManager;
import com.deco2800.marswars.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class HeroFactory extends BuildingEntity implements Clickable, Tickable,
        HasProgress, HasOwner, HasAction {

    /* A single action for this building */
    Optional<DecoAction> currentAction = Optional.empty();

    private static final Logger LOGGER = LoggerFactory.getLogger(HeroFactory.class);

    private int owner;
    private AbstractWorld world;
    boolean selected = false;

    /**
     * Constructor for the hero factory.
     * @param world The world that will hold the hero factory.
     * @param posX its x position on the world.
     * @param posY its y position on the world.
     * @param posZ its z position on the world.
     */
    public HeroFactory(AbstractWorld world, float posX, float posY, float posZ) {
        super(posX, posY, posZ, BuildingType.BUNKER);
        this.setTexture("bunker");  // temporary texture
        this.setEntityType(EntityType.BUILDING);
        this.world = world;
        this.setCost(200);
        this.setSpeed(1.5f);
        this.addNewAction(ActionType.GENERATE);
        this.addNewAction(ActionType.CREATEITEM);
        world.deSelectAll();
    }

    /**
     * Give an action to the hero factory
     * @param action
     */
    public void giveAction(DecoAction action) {
        if (!currentAction.isPresent()) {
            currentAction = Optional.of(action);
        }
    }

    /**
     * On click handler
     */
    @Override
    public void onClick(MouseHandler handler) {
        if(!this.isAi()) {
            if (!selected) {
                selected = true;
                LOGGER.error("clicked on hero factory");
            }
        } else {
            LOGGER.error("clicked on AI hero factory");

        }
    }

    /**
     * On right click handler
     */
    @Override
    public void onRightClick(float x, float y) {
    }

    /**
     * On Tick handler
     * @param i time since last tick
     */
    @Override
    public void onTick(int i) {

        if (currentAction.isPresent()) {
            currentAction.get().doAction();

            if (currentAction.get().completed()) {
                currentAction = Optional.empty();
            }
        }
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
     * Deselect the hero factory
     */
    @Override
    public void deselect() {
        selected = false;
    }

    /**
     * Get the help text label of this hero factory
     * @return Label
     */
    @Override
    public Label getHelpText() {
        return new Label("You have clicked on a hero factory.", new Skin(Gdx
                .files
                .internal("uiskin.json")));
    }

    /**
     * Get the progress of current action
     * @return int
     */
    @Override
    public int getProgress() {
        if (currentAction.isPresent()) {
            return currentAction.get().actionProgress();
        }
        return 0;
    }

    /**
     * Check if there is an action currently assigned to the base
     * @return boolean
     */
    @Override
    public boolean showProgress() {
        return currentAction.isPresent();
    }
    /**
     * Set the owner of this hero factory
     * @param owner
     */
    @Override
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Get the owner of this hero factory
     * @return owner
     */
    @Override
    public int getOwner() {
        return this.owner;
    }

    /**
     * Check if the AbstractEntity passed in and this entity has the same owner
     * @return boolean
     */
    @Override
    public boolean sameOwner(AbstractEntity entity) {
        return entity instanceof  HasOwner &&
                this.owner == ((HasOwner) entity).getOwner();
    }

    /**
     * This method is a duplication of the showProgress method, consider delete one of them
     * sadly two different interfaces
     * @return boolean
     */
    public boolean isWorking() {
        return currentAction.isPresent();
    }

    /**
     * Set the action of this hero factory
     * @param action
     */
    public void setAction(DecoAction action) {
        currentAction = Optional.of(action);
    }

    /**
     * Returns the current action (used in WeatherManager)
     * @return
     */
    public Optional<DecoAction> getAction() {
        return currentAction;
    }

    @Override
    public boolean isAi() {
        return owner >= 0;
    }


    /**
     * Create the 'Create Hero' button object
     * @return Button
     */
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
     * three hero units in
     * game
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

    /**
     * Returns the current action of the entity
     * @return current action
     */
    @Override
    public Optional<DecoAction> getCurrentAction() {
        return currentAction;
    }

}
