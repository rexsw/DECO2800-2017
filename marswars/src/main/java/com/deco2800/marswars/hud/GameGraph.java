package com.deco2800.marswars.hud;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameManager;

/**
 * Draws the game stat graph.
 * @author Naziah Siddique and Scott Whittington
 *
 */
public class GameGraph{
	private float graphSize = 400; 
	
	private Skin skin;
	private ShapeRenderer renderer;
	private String Graphtype;
	private float[] vertices;
	
	public GameGraph(String graphtype){
		renderer = new ShapeRenderer();
		Graphtype = graphtype;
	}
	
	public void render(){
		//renderer.setProjectionMatrix((new OrthographicCamera(1920, 1080)).combined);
		renderer.begin(ShapeType.Line);
		//fix up the position
		if(Graphtype == "Fake") {
		}
		else {
			for(int i: ((AiManager) GameManager.get().getManager(AiManager.class)).getAiTeam()) {
				vertices = ((GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class)).getHistory(i, Graphtype);
				renderer.polyline(vertices);
			}
			vertices = ((GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class)).getHistory(-1, Graphtype);
			renderer.polyline(vertices);
		}
		renderer.end();
	}
	
	public void resize(){
		;
	}
	
	public void dispose(){
		renderer.dispose();
	}
}