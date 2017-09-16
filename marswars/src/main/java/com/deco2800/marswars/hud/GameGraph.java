package com.deco2800.marswars.hud;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Draws the game stat graph.
 * @author Naziah Siddique
 *
 */
public class GameGraph{
	private float graphSize = 400; 
	
	private Skin skin;
	private ShapeRenderer renderer;
	
	public GameGraph(){
		int[] Yvalues = {1, 2, 3, 4, 5, 6};
		renderer = new ShapeRenderer(); 
	}
	
	public void render(){
		renderer.begin(ShapeType.Filled);
		//fix up the position
		renderer.rect(0, 0, graphSize, graphSize);
		renderer.end();
	}
	
	public void resize(){
		;
	}
	
	public void dispose(){
		renderer.dispose();
	}
}