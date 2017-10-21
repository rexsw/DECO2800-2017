package com.deco2800.marswars.mainmenu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.hud.ChatBox;
import com.deco2800.marswars.net.LobbyMessageAction;

/**
 * A version of the chatbox used in the pre-game lobby. 
 * 
 * @author James McCall
 *
 */
public class LobbyChatBox extends ChatBox{

    public LobbyChatBox(Skin skin) {
        super(skin);
    }
    
    @Override
    public void sendMessage() {
        String message = this.messageTextField.getText();
        if (!"".equals(message)) {
            LobbyMessageAction action = new LobbyMessageAction(message);
            netManager.getNetworkClient().sendObject(action);
        }
        messageTextField.setText("");
    }

}
