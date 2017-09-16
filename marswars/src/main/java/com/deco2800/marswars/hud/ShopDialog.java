package com.deco2800.marswars.hud;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.deco2800.marswars.entities.items.ArmourType;
import com.deco2800.marswars.entities.items.ItemType;
import com.deco2800.marswars.entities.items.SpecialType;
import com.deco2800.marswars.entities.items.Weapon;
import com.deco2800.marswars.entities.items.WeaponType;
import com.deco2800.marswars.entities.units.Commander;
import com.deco2800.marswars.managers.TextureManager;

public class ShopDialog extends Dialog{
	
    private TextureManager textureManager;
    private int iconSize = Gdx.graphics.getWidth() / 12;
    private List<ItemType> itemList;
    private Table heroTable;
    private Label status;
    private Commander selectedHero;
    
	public ShopDialog(String title, Skin skin, TextureManager manager) {
		super(title, skin);
		this.getTitleLabel().setAlignment(Align.center);
		this.textureManager = manager;	
		this.itemList = new ArrayList<>();
		this.getContentTable().setDebug(true);
		this.getContentTable().left();
		
		status = new Label("update me", skin);
		
		itemList.add(WeaponType.WEAPON1);
		itemList.add(WeaponType.WEAPON2);
		itemList.add(ArmourType.ARMOUR1);
		itemList.add(SpecialType.AOEHEAL);
		
		final Table scrollTable = new Table();
		scrollTable.top();
		scrollTable.debugAll();
		scrollTable.add(new Label("Item", skin)).width(iconSize).top().center();
		scrollTable.add(new Label("Description", skin)).width(iconSize).expandX().top().left();
		scrollTable.add(new Label("Cost", skin)).width(iconSize).top();
		scrollTable.row();
		
		for (ItemType item:itemList) {
			Texture texture = textureManager.getTexture(item.getTextureString());
			ImageButton button = generateItemButton(texture);
			
			button.addListener(new ClickListener() {  
	            public void clicked(InputEvent event, float x, float y){
	                status.setText(item.getName());
	                if (selectedHero != null) {
	                	if (item instanceof WeaponType) {
	                		Weapon weapon = new Weapon((WeaponType) item);
		                	selectedHero.addItemToInventory(weapon);
		                	status.setText("Bought " + weapon.getName() + " for " + selectedHero.toString());
	                	} else {
	                		status.setText("not a weapon");
	                	}
	                } else {
	                	status.setText("unsuccessful shopping!!!!!");
	                }
	                }
	        });
			
			scrollTable.add(button).width(iconSize).height(iconSize).top();
	        scrollTable.add(new Label(item.getDescription(), skin)).width(iconSize).top().left();
	        scrollTable.add(new Label(item.getCostString(), skin)).width(iconSize).top().left();
	        scrollTable.row();
		}
		
        final ScrollPane scroller = new ScrollPane(scrollTable);
        
//        Texture heroImage = textureManager.getTexture("hero_button");
//		Texture heroOffImage = textureManager.getTexture("hero_button_off");
//        ImageButton heroButton = generateHeroButton(heroImage, heroOffImage);

        heroTable = new Table();
        heroTable.top();
        heroTable.add(new Label("Commander", skin)).width(iconSize);
        heroTable.row();
//        table.add(heroButton).width(iconSize).height(iconSize).top();
        
        this.getContentTable().add(scroller).fill().expand().top();
        this.getContentTable().add(heroTable).width(iconSize).top();
        
        
        this.getContentTable().row();
        this.getContentTable().add(status).expandX().center().colspan(2);
        
        
        
	}
	
	private ImageButton generateItemButton(Texture image) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		return new ImageButton(imgDraw);
	}
	
	private ImageButton generateHeroButton(Texture image, Texture offImage) {
		TextureRegion imgRegion = new TextureRegion(image);
		TextureRegionDrawable imgDraw = new TextureRegionDrawable(imgRegion);
		TextureRegion offImgRegion = new TextureRegion(offImage);
		TextureRegionDrawable offImgDraw = new TextureRegionDrawable(offImgRegion);

		ImageButton button = new ImageButton(offImgDraw);
		button.getStyle().imageChecked = imgDraw;
        
		return button;
	}
	
	public void addHeroIcon(Commander hero) {
		Texture heroImage = textureManager.getTexture("hero_button");
		Texture heroOffImage = textureManager.getTexture("hero_button_off");
        ImageButton heroButton = generateHeroButton(heroImage, heroOffImage);
        heroTable.add(heroButton).width(iconSize).height(iconSize).top();
        
        heroButton.addListener(new ClickListener(){
        	public void clicked(InputEvent event, float x, float y){
                if(heroButton.isChecked()) {
                	heroButton.setChecked(true);
                	status.setText("set true");
                	System.out.println(hero.getArmorDamage());
                	selectedHero = hero;
                } else {
                	heroButton.setChecked(false);
                	status.setText("set false");
                	selectedHero = null;
                }
                }
        	});
	}
	
	@Override
    public float getPrefWidth() {
        return Gdx.graphics.getWidth() /2.5f;
    }

    @Override
    public float getPrefHeight() {
        return Gdx.graphics.getHeight() /1.8f;
    }
	
	@Override
	protected void result(Object object){
		
	}
}
