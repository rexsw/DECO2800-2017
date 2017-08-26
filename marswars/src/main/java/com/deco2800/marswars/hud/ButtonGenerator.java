package com.deco2800.marswars.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.actions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate the hud buttons for an action
 * Created by Hayden on 26/08/2017.
 */

public class ButtonGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionType.class);
    private Skin skin;

    public ButtonGenerator(Skin skin) {
        this.skin = skin;
    }


    public Button getButton(ActionType c, BaseEntity selectedEntity) {
        Button newButton = new TextButton(ActionSetter.getActionName(c), skin);
        newButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LOGGER.info("Action button pressed");
                selectedEntity.setNextAction(c);
            }
        });
    return newButton;
    }

}
