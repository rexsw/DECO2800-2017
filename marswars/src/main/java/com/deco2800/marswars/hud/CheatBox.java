package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.TextureManager;

import java.util.List;


/**
 * A class representing a box used to enter cheat codes
 *
 */
public class CheatBox extends Table {

    // Size variables for the chat Pane to keep it at a fixed size
    private static final float CHAT_WIDTH = 300;
    private static final float CHAT_HEIGHT = 150;

    // This is the text input field for entering messages
    private TextField messageTextField;
    // This is the send button for messages
    private ImageButton sendButton;
    // This is the allows messages to be scrolled
    private ScrollPane chatPane;
    // This displays the chat messages in an ordered manner
    private Table chatMessages;
    // The skin used to style the table
    private Skin skin;
    private CodeInterpreter code;
    private TextureManager textureManager;

    /**
     * Creates a new instance of a ChatBox.
     *
     * @param skin The UI skin to be applied to all elements of the ChatBox.
     */
    public CheatBox(Skin skin, TextureManager textureManager) {
        this.skin = skin;
        // Create the elements of chat box
        this.code = new CodeInterpreter();
        this.setTextureManager(textureManager);
        this.messageTextField = new TextField("", this.skin) ; //$NON-NLS-1$

        //add dispActions button + image for it
        Texture arrowImage = textureManager.getTexture("arrow_button"); //$NON-NLS-1$
        TextureRegion arrowRegion = new TextureRegion(arrowImage);
        TextureRegionDrawable arrowRegionDraw = new TextureRegionDrawable(arrowRegion);
        this.sendButton = new ImageButton(arrowRegionDraw);

        this.chatMessages = new Table(this.skin);
        this.chatPane = new ScrollPane(this.chatMessages, this.skin);

        // Set up properties of the elements then set layout
        setUpInputElements();
        setUpChatMessages();
        setUpLayout();
    }

    /**
     * Sends the message in the chat text field when the enter key is pressed.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            readCheatCode();
        }

        this.chatPane.act(delta);
    }

    /**
     * Adds the new message to the bottom of the chat panel.
     *
     * @param newMessage The message to be added.
     */
    public void addNewMessage(String newMessage) {
        // Create Message label
        Label message = new Label(newMessage, this.skin);
        message.setWrap(true);
        message.setAlignment(Align.left);

        // Add message to table
        this.chatMessages.row();
        this.chatMessages.add(message).growX().bottom().left();

        // Update Chat Pane, so it scrolls to bottom
        this.chatPane.layout();
        this.chatPane.setScrollY(this.chatPane.getMaxY());
        this.chatPane.updateVisualScroll();
    }

    /**
     * Gets the string currently in the messageTextField and sends the message
     * to the server if the string is valid. Resets messageTextField to be
     * empty.
     *
     * A valid string is not empty.
     *
     * Note: This method is currently implemented to just add message to
     * chat box for testing purposes.Does not send to server.
     */
    private void readCheatCode() {
        String message = this.messageTextField.getText();
        if (!"".equals(message)) {

            code.executeCode(message);
        }
        this.messageTextField.setText("");
    }


    /**
     * Clears the chat message box so it is not displaying any messages.
     *
     */
    public void clearMessages() {
        this.chatMessages.clearChildren();
    }

    /**
     * Helper method that sets up the messageTextField and sendButton with
     * appropriate initial properties. Also setups the handler for the send
     * button so when it is clicked the message from the text field is sent.
     */
    private void setUpInputElements() {
        this.sendButton.pad(5);

        this.sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                readCheatCode();
            }
        });
    }

    /**
     * Helper method that sets up the chatPane and chatMessages with
     * appropriate initial properties.
     */
    private void setUpChatMessages() {
        this.chatMessages.bottom();

    }

    /**
     * Helper method that sets up the layout of all the elements within the
     * table.
     */
    private void setUpLayout(){
        this.add(this.chatPane).colspan(2).maxSize(CHAT_WIDTH, CHAT_HEIGHT).minSize(CHAT_WIDTH, CHAT_HEIGHT);
        this.row();
        this.add(this.messageTextField).expandX().fillX();
        this.add(this.sendButton).pad(5).height(30).width(30);

        this.setWidth(200);
    }

    public TextureManager getTextureManager() {
        return this.textureManager;
    }

    public void setTextureManager(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

}