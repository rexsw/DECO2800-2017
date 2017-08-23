package com.deco2800.marswars.hud;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;


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
 *
 */
public class ChatBox extends Table {
    // This is the text input field for entering messages
    private TextField messageTextField;
    // This is the send button for messages
    private TextButton sendButton;
    // This is the scroll pane where the messages appear in a scrollable format.
    private ScrollPane chatPane;
    // This displays the chat messages in an ordered manner
    private List<String> chatMessages;
    
    /**
     * Creates a new instance of a ChatBox.
     * 
     * @param skin The UI skin to be applied to all elements of the ChatBox.
     */
    public ChatBox(Skin skin) {
        // Create the elements of chat box
        messageTextField = new TextField("", skin) ;
        sendButton = new TextButton("Send", skin);
        chatMessages = new List<String>(skin);
        chatPane = new ScrollPane(chatMessages, skin);
        
        // Set up properties of the elements then set layout
        setUpInputElements();
        setUpChatMessages();        
        setUpLayout();
        
    }
    
    /**
     * Helper method that sets up the messageTextField and sendButton with 
     * appropriate initial properties.
     * 
     */
    private void setUpInputElements() {
        sendButton.pad(5);
    }

    /**
     * Helper method that sets up the chatPane and chatMessages with 
     * appropriate initial properties.
     * 
     */
    private void setUpChatMessages() {
        // Entry message
        String test[]= {"Welcome to SpacWars chat, home to spatman memes."};
        
        chatMessages.setItems(test);
    }
    
    /**
     * Helper method that sets up the layout of all the elements within the 
     * table.
     * 
     */
    private void setUpLayout(){
        // Add all elements to the vertical group
        this.add(chatPane).expand().fill().colspan(2).maxHeight(100).minHeight(100);
        this.row();     
        this.add(messageTextField).expandX().fillX();
        this.add(sendButton);
        
        this.setWidth(200);
    }    
    
}