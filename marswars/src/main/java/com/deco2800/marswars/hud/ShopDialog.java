package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.TextureManager;

public class ShopDialog extends Dialog{
	
	private Skin skin;
    private Table table;
    private TextureManager textureManager;
    private int iconSize = Gdx.graphics.getWidth() / 12;
    
    
    private static final String reallyLongString = "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
            + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n"
            + "This\nIs\nA\nReally\nLong\nString\nThat\nHas\nLots\nOf\nLines\nAnd\nRepeats.\n";

    
	public ShopDialog(String title, Skin skin, TextureManager manager) {
		super(title, skin);
		this.skin = skin;
		this.getTitleLabel().setAlignment(Align.center);
		this.textureManager = manager;	
		this.getContentTable().setDebug(true);;
		this.getContentTable().left();

		Texture gloveImage = textureManager.getTexture("power_gloves");
		Texture needleImage = textureManager.getTexture("heal_needle");
		Texture helmetImage = textureManager.getTexture("defence_helmet");
		Texture heroImage = textureManager.getTexture("hero_button");
		Texture heroOffImage = textureManager.getTexture("hero_button_off");
		ImageButton gloveButton = generateItemButton(gloveImage);
		ImageButton needleButton = generateItemButton(needleImage);
		ImageButton helmetButton = generateItemButton(helmetImage);
		ImageButton button6 = generateItemButton(gloveImage);
		
		
		final Label text3 = new Label(reallyLongString, skin);
        text3.setAlignment(Align.center);
        text3.setWrap(true);
        
		final Table scrollTable = new Table();
		scrollTable.debugAll();
		scrollTable.add(new Label("Item", skin)).width(iconSize);
		scrollTable.add(new Label("Description", skin)).width(iconSize).expandX();
		scrollTable.add(new Label("Cost", skin)).width(iconSize);
		scrollTable.row();
        scrollTable.add(gloveButton).width(iconSize).height(iconSize).top();
        scrollTable.add(text3).width(iconSize);
        scrollTable.row();
        scrollTable.add(needleButton).width(iconSize).height(iconSize);
        scrollTable.row();
        scrollTable.add(button6).width(iconSize).height(iconSize);
        scrollTable.row();
        scrollTable.add(helmetButton).width(iconSize).height(iconSize);
        final ScrollPane scroller = new ScrollPane(scrollTable);
        
        ImageButton heroButton = generateHeroButton(heroImage, heroOffImage);

        Table table = new Table();
        table.add(new Label("Commander", skin)).width(iconSize);
        table.row();
        table.add(heroButton).width(iconSize).height(iconSize).top();
        this.getContentTable().add(scroller).fill().expand().align(Align.center);
        this.getContentTable().add(table).width(iconSize).top();
        
        Label status = new Label("update me", skin);
        this.getContentTable().row();
        this.getContentTable().add(status).expandX().center().colspan(2);
        
        helmetButton.addListener(new ClickListener() {  
            public void clicked(InputEvent event, float x, float y){
                status.setText("new!");
                }
        });
        
        heroButton.addListener(new ClickListener(){
//        	  @Override
//        	  public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//        	    status.setText("Press a Button");
//        	  }
//        	  @Override
//        	  public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//        	    status.setText("Pressed Image Button");
//        	    return true;
//        	  }
        	public void clicked(InputEvent event, float x, float y){
                if(heroButton.isChecked()) {
                	System.out.println("checked");
                	heroButton.setChecked(true);
                	System.out.print(heroButton.isChecked());
                } else {
                	System.out.println("Not checked");
                	heroButton.setChecked(false);
                }
                }
        	});
        
	}
	
	public ImageButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		return new ImageButton(imgDraw);
	}
	
	public ImageButton generateHeroButton(Texture image, Texture offImage) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		TextureRegion offImgRegion = new TextureRegion(offImage);
		TextureRegionDrawable offImgDraw = new TextureRegionDrawable(offImgRegion);

		ImageButton button = new ImageButton(imgDraw);
		button.getStyle().imageChecked = offImgDraw;
//		button.getStyle().imageUp = imgDraw;
//		button.getStyle().imageDown = offImgDraw;
        
		return button;
	}
	
	@Override
    public float getPrefWidth() {
        return Gdx.graphics.getWidth() /3;
    }

    @Override
    public float getPrefHeight() {
        return Gdx.graphics.getHeight() /2;
    }
	
	@Override
	protected void result(Object object){
		
	}
}
