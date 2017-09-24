package com.deco2800.marswars.mainMenu;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.hud.ChatBox;
import com.deco2800.marswars.net.LobbyMessageAction;

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
