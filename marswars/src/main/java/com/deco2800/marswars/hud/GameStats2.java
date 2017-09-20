package com.deco2800.marswars.hud;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;

public class GameStats2 implements ApplicationListener{

	private Stage stage;
	private Skin skin;
	private Table table;
	private String[] StatName = new String[] {"Biomass","Crystal",  "Rocks",  "Water", "Units",  "Units Lost",  "Combat Units", "Buildings"};
	private String StatSelect = StatName[0];
	private GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);

	private ShapeRenderer sr;
	private OrthographicCamera camera;
	

	private float[] getPoints() {
		return black.getHistory(-1, StatSelect);
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
		sr.dispose();
	}

	@Override
	public void create() {
		this.stage = new Stage(new ScreenViewport());
		this.skin = new Skin(Gdx.files.internal("uiskin.json"));
		this.camera = new OrthographicCamera(1920, 1080);
		this.sr = new ShapeRenderer();
		this.sr.setProjectionMatrix(this.camera.combined);
	}

	@Override
	public void render() {
		float[] test = new float[100];
		float[] testx = new float[100];
		for(int i =0; i < 100; i++) {
			test[i] = (float) (i * 2.5);
			testx[i] = (float) (i);
		}
		
		
		ShapeRenderer sr= new ShapeRenderer();
		sr.setProjectionMatrix(GameManager.get().getCamera().combined);
		sr.begin(ShapeType.Line);
		sr.rect(10, 10, 50, 50);
		sr.polyline(test);
		sr.rect(0, 0, 160, 160);
	}
	
}
