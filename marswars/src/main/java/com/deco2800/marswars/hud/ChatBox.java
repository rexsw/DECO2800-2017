package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;


/**
 * A class representing a chat box in the game. This class provides the general
 * UI layout for the chat box as well as access the both the message displayed 
 * in the chat box and messages typed into the text field.
 * 
 * UI Layout:
 * +---------------------------+
 * |    Messages Displayed     |
 * |       (Scrollable)        |
 * +--------------------+------+
 * | Enter new Message  | Send |
 * +--------------------+------+
 * 
 * As this class extends Table all normal Table operations can be performed on
 * a ChatBox.
 * 
 * @author James McCall
 */
public class ChatBox extends Table {
    
    // Size variables for the chat Pane to keep it at a fixed size
    private final static float CHAT_WIDTH = 300;
    private final static float CHAT_HEIGHT = 150;
    
    // This is the text input field for entering messages
    private TextField messageTextField;
    // This is the send button for messages
    private TextButton sendButton;
    // This is the allows messages to be scrolled 
    private ScrollPane chatPane;
    // This displays the chat messages in an ordered manner
    private Table chatMessages;
    // The skin used to style the table
    private Skin skin;
    
    /**
     * Creates a new instance of a ChatBox.
     * 
     * @param skin The UI skin to be applied to all elements of the ChatBox.
     */
    public ChatBox(Skin skin) {
        this.skin = skin;
        // Create the elements of chat box
        messageTextField = new TextField("", this.skin) ;
        sendButton = new TextButton("Send", this.skin);
        chatMessages = new Table(this.skin);
        chatPane = new ScrollPane(chatMessages, this.skin);
        
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
            sendMessage();
        }
        
        chatPane.act(delta);
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
        chatMessages.row();
        chatMessages.add(message).growX().bottom().left();
        
        // Update Chat Pane, so it scrolls to bottom
        chatPane.layout();
        chatPane.setScrollY(chatPane.getMaxY());
        chatPane.updateVisualScroll();
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
    private void sendMessage() {
        String message = messageTextField.getText();
        if (!message.equals("")) {
            // Currently not implemented correctly, adds to chat box instead of sending to server.
            addNewMessage(message);
        }
        messageTextField.setText("");
    }
    
    /**
     * Clears the chat message box so it is not displaying any messages.
     * 
     */
    public void clearMessages() {
        chatMessages.clearChildren();
    }
    
    /**
     * Helper method that sets up the messageTextField and sendButton with 
     * appropriate initial properties. Also setups the handler for the send 
     * button so when it is clicked the message from the text field is sent.
     */
    private void setUpInputElements() {
        sendButton.pad(5);
        
        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendMessage();
            }
        });
    }
    
    /**
     * Helper method that sets up the chatPane and chatMessages with 
     * appropriate initial properties. 
     */
    private void setUpChatMessages() {
        chatMessages.bottom();
        
    }
    
    /**
     * Helper method that sets up the layout of all the elements within the 
     * table.
     */
    private void setUpLayout(){
        this.add(chatPane).colspan(2).maxSize(CHAT_WIDTH, CHAT_HEIGHT).minSize(CHAT_WIDTH, CHAT_HEIGHT);
        this.row();     
        this.add(messageTextField).expandX().fillX();
        this.add(sendButton);
        
        this.setWidth(200);
    }    
    
}