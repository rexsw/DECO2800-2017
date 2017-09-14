package com.deco2800.marswars.functionKeys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;


/**
 * This Class is used to deal with some of the keyboard inputs as
 * command
 * 
 * @author Matthew Lee
 */
public class ShortCut {
	
	
	private Set<Integer> inputKeys = new HashSet<>();
	private static int bCounter = 0;
	private final int speed = 10;
	
	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	private int switcher = 0;
	private int cSwitcher = 0;
	private int cameraPointer = 0;
	
	private int pauseCount = 0;
	private int pSwitch = 0;
	
	public void process(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.UP) || inputKeys.contains(Input.Keys.W)) {
			camera.translate(0, 1 * speed * camera.zoom, 0);
		}
		if (inputKeys.contains(Input.Keys.DOWN) || inputKeys.contains(Input.Keys.S)) {
			camera.translate(0, -1 * speed * camera.zoom, 0);
		}
		if (inputKeys.contains(Input.Keys.LEFT) || inputKeys.contains(Input.Keys.A)) {
			camera.translate(-1 * speed * camera.zoom, 0, 0);
		}
		if (inputKeys.contains(Input.Keys.RIGHT) || inputKeys.contains(Input.Keys.D)) {
			camera.translate(1 * speed * camera.zoom, 0, 0);
		}
		if ((inputKeys.contains(Input.Keys.EQUALS)) && (camera.zoom > 0.5)) {
			camera.zoom /= 1.05;
		}
		if ((inputKeys.contains(Input.Keys.MINUS)) && (camera.zoom < 10)) {
			camera.zoom *= 1.05;
		}
		storeCameraPosition(camera);
		cameraGoToTheStoredPosition(camera);
		cameraBackToLastPosition(camera);
		pauseGame();
		quitGame();
	}
	
	public void addKey(int key) {
		inputKeys.add(key);
	}
	
	public void removeKey(int key) {
		inputKeys.remove(key);
	}
	
	public void storeCameraPosition(OrthographicCamera camera) {
		if ((inputKeys.contains(Input.Keys.C))){
			if(cSwitcher == 0){
				ArrayList<Float> XYPosition = new ArrayList<Float>();
				XYPosition.add(camera.position.x);
				XYPosition.add(camera.position.y);
				cameraPosition.add(XYPosition);
				cSwitcher++;
			}
		}else{
			cSwitcher = 0;
		}
	}
	
	public void cameraGoToTheStoredPosition(OrthographicCamera camera) {
		if((inputKeys.contains(Input.Keys.N))){
			if(!cameraPosition.isEmpty()){
				ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
				if(switcher == 0){
					float X= camera.position.x - nextPosition.get(0);
					float Y = camera.position.y - nextPosition.get(1);
					X *= -1;
					Y *= -1;
					if(camera.position.x > nextPosition.get(0) || (camera.position.x <= nextPosition.get(0)&& camera.position.x >0)){
						camera.translate(X, 0);
					}
					if(camera.position.y > nextPosition.get(1) || 
							(camera.position.y <= nextPosition.get(1))){
						camera.translate(0, Y);
					}
					switcher++;
					cameraPointer++;
					cameraPointer = cameraPointer % cameraPosition.size();
				}	
			}
		}else{
			switcher = 0;
		}
	}
	
	public void cameraBackToLastPosition(OrthographicCamera camera) {
		if((inputKeys.contains(Input.Keys.B))){
			if(!cameraPosition.isEmpty()){
				if(bCounter == 0){
					cameraPointer--;
					if (cameraPointer < 0) {
						cameraPointer = cameraPosition.size() - 1;
					}
					cameraPointer = cameraPointer % cameraPosition.size();
					ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
					float X= camera.position.x - nextPosition.get(0);
					float Y = camera.position.y - nextPosition.get(1);
					X *= -1;
					Y *= -1;
					if(camera.position.x > nextPosition.get(0) || (camera.position.x <= nextPosition.get(0)&& camera.position.x >0)){
						camera.translate(X, 0);
					}
					if(camera.position.y > nextPosition.get(1) || 
							(camera.position.y <= nextPosition.get(1))){
						camera.translate(0, Y);
					}
					bCounter++;
				}	
			}
		}else{
			bCounter = 0;
		}
	}
	
	public void pauseGame() {
		if(inputKeys.contains(Input.Keys.P)) {
			while(pauseCount == 0) {
				if(inputKeys.contains(Input.Keys.P) && pSwitch > 0) {
					pauseCount++;
				} 
				if(!inputKeys.contains(Input.Keys.P)){
					pSwitch++;
				}
			}
		}
	}
	
	public void moveToOneOfTheEntity() {
		
	}
	
	public void storeEntityPosition() {
		
		
	}
	
	public void entityOnClick() {
		
		
	}
	
	public void addExtraSpacMan() {
		
		
	}
	
	public void addResource() {
		
		
	}
	
	public void quitGame() {
		if(inputKeys.contains(Input.Keys.ESCAPE)) {
			System.exit(0);
		}
		
	}
}
