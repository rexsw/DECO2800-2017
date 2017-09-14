package com.deco2800.marswars.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.managers.TextureManager;
import com.deco2800.marswars.technology.Technology;

public class ShopDialog extends Dialog{
	int row_height = Gdx.graphics.getWidth() / 12;
    int col_width = Gdx.graphics.getWidth() / 12;
    private Table table;
    private TextureManager textureManager;
    private int iconSize = Gdx.graphics.getWidth() / 12;
    
    
	public ShopDialog(String title, Skin skin, TextureManager manager) {
		super(title, skin);
		this.textureManager = manager;
//		table = new Table();
//		//table.setPosition(0, this.getHeight());
//		table.setWidth(this.getWidth());
//		table.setHeight(this.getHeight());
//		this.add(table);
//		table.debugAll();
		
		this.getContentTable().debugAll();
		//Icon for player- 
		
		Texture image = textureManager.getTexture("power_gloves");
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		ImageButton button = new ImageButton(imgDraw);
		ImageButton button8 = new ImageButton(imgDraw);
		ImageButton button5 = new ImageButton(imgDraw);
		ImageButton button6 = new ImageButton(imgDraw);
		ImageButton button7 = new ImageButton(imgDraw);
		
		final Table scrollTable = new Table();
        scrollTable.add(button).width(iconSize).height(iconSize);
        scrollTable.row();
        scrollTable.add(button8).width(iconSize).height(iconSize);
        scrollTable.row();
        scrollTable.add(button6).width(iconSize).height(iconSize);
        scrollTable.row();
        scrollTable.add(button5).width(iconSize).height(iconSize);
        final ScrollPane scroller = new ScrollPane(scrollTable);
        this.getContentTable().add(scroller).fill().expand();
        
//		Texture image2 = textureManager.getTexture("power_gloves");
//		TextureRegion imgRegion2 = new TextureRegion(image2);
//		TextureRegionDrawable imgDraw2 = new TextureRegionDrawable(imgRegion2);
//		ImageButton button2 = new ImageButton(imgDraw2);
//		ImageButton button3 = new ImageButton(imgDraw2);
//		ImageButton button4 = new ImageButton(imgDraw2);
//		
//        this.getContentTable().add(button).width(iconSize).height(iconSize);
//        this.getContentTable().add(button5).width(iconSize).height(iconSize);
//        this.getContentTable().row();
//        Label text = new Label("text",skin);
//        this.getContentTable().add(text);
        
        
        
//        table.add(button6).width(iconSize).height(iconSize);
//        table.add(button7).width(iconSize).height(iconSize);
//        table.add(button8).width(iconSize).height(iconSize);
//        table.add(button2).width(iconSize).height(iconSize);
//		table.row();
//		table.add(button3).width(iconSize).height(iconSize);
//        table.add(button4).width(iconSize).height(iconSize);
//        table.row();
		
		
//        table.add(text);
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
