package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.NetManager;
import com.deco2800.marswars.net.ReadyAction;

public class ReadyButton extends TextButton {
    // Status, is the player ready
    boolean status;
    
    // The Net Manager so you can communicate with the server
    NetManager netManager = (NetManager) GameManager.get().getManager(NetManager.class);
    // Ready Up string
    private static final String READY_UP = "Ready Up";
    // Cancel ready string
    private static final String CANCEL_READY = "Cancel Ready";
    
    public ReadyButton(Skin skin) {
        super(READY_UP, skin);
        this.status = false;
        setupHandler();
    }

    /**
     * Setups the handler for when the button is clicked so that the text changes and the change of state is
     * communicated to the server.
     */
    private void setupHandler() {
        this.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                status = !status;
                setButtonString();
                ReadyAction ready = new ReadyAction(status);
                netManager.getNetworkClient().sendObject(ready);
            }
        });
    }
    
    /**
     * Set the appropriate message to display on the button given its current state.
     */
    private void setButtonString() {
        String result;
        if (status) {
            result = CANCEL_READY;
        } else {
            result = READY_UP;
        }
        this.setText(result);
    }
}
