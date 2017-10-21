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
        LOGGER.debug("Create a hero factory");
    }


//
//    /**
//     * Get the help text label of this hero factory
//     * @return Label that used to displayed in the HUD
//     */
//    @Override
//    public Label getHelpText() {
//        return new Label("You have clicked on a hero factory.", new Skin(Gdx
//                .files
//                .internal("uiskin.json")));
//    }
//

//    /**
//     * Create the 'Create Hero' button object
//     * handle relative click event as well
//     * @return Button that used to create hero
//     */
//    @Override
//    public Button getButton() {
//        Button button = new TextButton("Create Hero", new Skin(Gdx.files
//                .internal
//                ("uiskin.json")));
//        button.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                buttonWasPressed();
//            }
//        });
//        return button;
//    }
//
//    /**
//     * Handler for button pressed action
//     * Builds hero unit if button was pressed. Note that there are a maximum of
//     * three hero units in game
//     *
//     */
//    public void buttonWasPressed() {
//        ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
//        // need to add some way to check global number of heros here
//        if (resourceManager.getBiomass(this.owner) > 30) {
//            resourceManager.setBiomass(resourceManager.getBiomass(this.owner)
//                            - 30,
//                    this.owner);
//           /* currentAction = Optional.of(new GenerateAction(new HeroSpacman
//                    (world, this.getPosX() - 1, this.getPosY() - 1, 0)));
//            // increment golbal hero counter here
//             */ // NEED TO FIX THIS TO MAKE IT WORK
//        }
//    }

}
