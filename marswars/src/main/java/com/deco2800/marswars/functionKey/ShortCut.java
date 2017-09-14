package com.deco2800.marswars.functionKey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class ShortCut {
	private Set<Integer> inputKeys;

	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	private int switcher = 0;
	private int cSwitcher = 0;
	private int delCount = 0;
	private int bCount = 0;
	private int cameraPointer = 0;
	
	public ShortCut(OrthographicCamera camera, Set<Integer> set) {
		inputKeys = new HashSet<Integer>();
	}
	
	public void addKey(int key) {
		inputKeys.add(key);
	}
	
	public void removeKey(int key) {
		inputKeys.remove(key);
	}
	
	public void storeCameraPosition(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.C)){
			if(cSwitcher == 0){
				ArrayList<Float> xyPosition = new ArrayList<Float>();
				xyPosition.add(camera.position.x);
				xyPosition.add(camera.position.y);
				cameraPosition.add(xyPosition);
				cSwitcher++;
			}
		}else{
			cSwitcher = 0;
		}
	}
	
	public void nextCameraPosition(OrthographicCamera camera) {
		if(inputKeys.contains(Input.Keys.N) && !cameraPosition.isEmpty()){
			ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
			if(switcher == 0){
				float x= camera.position.x - nextPosition.get(0);
				float y = camera.position.y - nextPosition.get(1);
				x *= -1;
				y *= -1;
				if(camera.position.x > nextPosition.get(0) || (camera.position.x <= nextPosition.get(0)&& camera.position.x >0)){
					camera.translate(x, 0);
				}
				if(camera.position.y > nextPosition.get(1) || 
						(camera.position.y <= nextPosition.get(1))){
					camera.translate(0, y);
				}
				switcher++;
				cameraPointer++;
				cameraPointer = cameraPointer % cameraPosition.size();
			}	
		}else{
			switcher = 0;
		}
	}
	
	public void backCameraPosition(OrthographicCamera camera) {
		if(inputKeys.contains(Input.Keys.B) && !cameraPosition.isEmpty()){
			if(bCount == 0){
				cameraPointer--;
				if (cameraPointer < 0) {
					cameraPointer = cameraPosition.size()-1;
				}
				cameraPointer = cameraPointer % cameraPosition.size();
				ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
				float x= camera.position.x - nextPosition.get(0);
				float y = camera.position.y - nextPosition.get(1);
				x *= -1;
				y *= -1;
				if(camera.position.x > nextPosition.get(0) || (camera.position.x <= nextPosition.get(0)&& camera.position.x >0)){
					camera.translate(x, 0);
				}
				if(camera.position.y > nextPosition.get(1) || 
						(camera.position.y <= nextPosition.get(1))){
					camera.translate(0, y);
				}
				bCount++;
				
			}	
		}else{
			bCount = 0;
		}
	}
	
	public void deleteStoredCameraPosition() {
		if(inputKeys.contains(Input.Keys.Z) && cameraPosition.size() > 0) {
			if(delCount == 0) {
				//cameraPointer = (cameraPointer - 1) % (cameraPosition.size() - 1);
				cameraPosition.remove(0);
				delCount++;
			}
		} else {
			delCount = 0;
		}
	}
	
	public void pause() {
		
		
	}
}
