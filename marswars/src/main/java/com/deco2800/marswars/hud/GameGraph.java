package com.deco2800.marswars.hud;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.deco2800.marswars.managers.AiManager;
import com.deco2800.marswars.managers.GameBlackBoard;
import com.deco2800.marswars.managers.GameBlackBoard.Field;
import com.deco2800.marswars.managers.GameManager;

/**
 * Draws the game stat graph.
 * @author Naziah Siddique and Scott Whittington
 *
 */
public class GameGraph{
	private static float GRAPHSIZE = 400; 
	
	private ShapeRenderer renderer;
	private Field Graphtype;
	private float[] vertices;
	
	public GameGraph(Field graphtype){
		renderer = new ShapeRenderer();
		Graphtype = graphtype;
	}
	
	/**
	 * Renders the game graph
	 */
	public void render(){
		//renderer.setProjectionMatrix((new OrthographicCamera(1920, 1080)).combined);
		renderer.begin(ShapeType.Line);
		//fix up the position
		if(Graphtype == null) {
		}
		else {
			for(int i: ((AiManager) GameManager.get().getManager(AiManager.class)).getAiTeam()) {
				vertices = ((GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class)).getHistory(i, Graphtype);
				//renderer.setColor(((ColourManager) GameManager.get().getManager(ColourManager.class)).getLibColour(i));
				renderer.polyline(vertices);
			}
			vertices = ((GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class)).getHistory(-1, Graphtype);
			//renderer.setColor(((ColourManager) GameManager.get().getManager(ColourManager.class)).getLibColour(-1));
			renderer.polyline(vertices);
		}
		renderer.end();
	}
	
	/**
	 * Not yet implelemented- should resize the graph
	 */
	public void resize(){
		;
	}
	
	public void dispose(){
		renderer.dispose();
	}
}