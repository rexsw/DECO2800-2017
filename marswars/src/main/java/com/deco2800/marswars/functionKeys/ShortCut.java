package com.deco2800.marswars.functionKeys;

import java.util.ArrayList;

import java.util.HashSet;

import java.util.Scanner;

import java.util.List;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.marswars.entities.BaseEntity;

import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.Selectable;
import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.entities.units.Tank;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.renderers.Renderable;

import com.deco2800.marswars.entities.Spacman;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.util.Array2D;
import com.deco2800.marswars.worlds.BaseWorld;


/**
 * This Class is used to deal with some of the keyboard inputs as
 * command of the game and it might have some special functions.
 * 
 * @author Matthew Lee
 */
public class ShortCut {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortCut.class);
	

	private Set<Integer> inputKeys = new HashSet<>();
	
	private final int speed = 10;
	
	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	
	private ArrayList<BaseEntity> baseEntityList = new ArrayList<BaseEntity>();
	
	private int cameraPointer = 0;
	
	private int pauseCount = 0;
	private boolean n = false;
	private boolean c = false;
	private boolean b =  false;
	private boolean spac = false;
	private boolean aiSpac = false;
	private boolean tank = false;
	private boolean soldier = false;
	private boolean spacmanSelect = false;
	private boolean moveToEntity = false;
	private boolean delete = false;
	
	public void process(OrthographicCamera camera) {
		moveCamera(camera);
		storeCameraPosition(camera);
		cameraGoToTheStoredPosition(camera);
		cameraBackToLastPosition(camera);
		cameraDeleteLastPosition();
		entityOnClick();
		//moveToOneOfTheEntity(camera);
		addExtraSpacMan();
		addExtraAiSpacMan();
		addExtraTank();
		addExtraSoldier();
		//moveCameraToBase(camera);
		quitGame();
	}
	
	public void moveCamera(OrthographicCamera camera) {
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
	}
	
	public void addKey(int key) {
		inputKeys.add(key);
	}
	
	public void removeKey(int key) {
		inputKeys.remove(key);
	}
	
	public void storeCameraPosition(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.C) && !inputKeys.contains(Input.Keys.CONTROL_LEFT)){
			if(c == false){
				ArrayList<Float> XYPosition = new ArrayList<Float>();
				XYPosition.add(camera.position.x);
				XYPosition.add(camera.position.y);
				cameraPosition.add(XYPosition);
				c = true;
				LOGGER.error("cameraX " + Float.toString(camera.position.x) + " cameraY " + Float.toString(camera.position.y));
			}
		}else{
			c = false;
		}
	}
	
	public void cameraGoToTheStoredPosition(OrthographicCamera camera) {
		if((inputKeys.contains(Input.Keys.N))){
			if(!cameraPosition.isEmpty()){
				ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
				if(n == false){
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
					n =true;
					cameraPointer++;
					cameraPointer = cameraPointer % cameraPosition.size();
				}	
			}
		}else{
			n = false;
		}
	}
	
	public void cameraBackToLastPosition(OrthographicCamera camera) {
		if((inputKeys.contains(Input.Keys.B))){
			if(!cameraPosition.isEmpty()){
				if(b == false){
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
					b = true;
				}	
			}
		}else{
			b = false;
		}
	}
	
	public void cameraDeleteLastPosition() {
		if((inputKeys.contains(Input.Keys.C)) && inputKeys.contains(Input.Keys.CONTROL_LEFT)){
			if(!cameraPosition.isEmpty()){
				if(delete == false){
					delete = true;
					cameraPosition.remove(cameraPointer);
					cameraPointer--;
					if (cameraPointer < 0 && cameraPosition.size() > 0) {
						cameraPointer = cameraPosition.size() - 1;
						cameraPointer = cameraPointer % cameraPosition.size();
					} else if (cameraPointer < 0) {
						cameraPointer = 0;
					}
				}	
			}
		}else{
			delete = false;
		}
	}
	
	public void pauseGame() {
		if(inputKeys.contains(Input.Keys.P)) {
			Scanner sc = new Scanner(System.in);
			while(pauseCount == 0) {
				if (sc.next().charAt(0) == 'p') {
					pauseCount++;
				}
			}
		}
	}
	
	public void moveToOneOfTheEntity(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.Z) && baseEntityList.size() > 0) {
			if (moveToEntity == false) {
				int length = GameManager.get().getWorld().getLength();
				int width = GameManager.get().getWorld().getWidth();
				float X= ((camera.position.x / 32) - (baseEntityList.get(0).getPosX()) * width / length) ;
				float Y = camera.position.y - baseEntityList.get(0).getPosY();
				X *= -1;
				Y *= -1;
				camera.translate(X, Y, 0);
				moveToEntity = true;
			}
		} else {
			moveToEntity = false;
		}
	}

	/**
	 * this method is to move the camera to the home base by clicking the backspace button
	 *has not differentiated player's and ai's home base yet
	 * @param g the gamemanager.
	 * @param camera the orthographicCamera.
	 */
	public void moveCameraToBase(OrthographicCamera camera) {
		Array2D<List<BaseEntity>> a = GameManager.get().getWorld().getCollisionMap();
		BaseWorld  w = GameManager.get().getWorld();
		List<BaseEntity> l = w.getEntities();
		float xNow = camera.position.x;
		float yNow = camera.position.y;
		float baseX;
		float baseY;
		if (inputKeys.contains(Input.Keys.BACKSPACE)) {
				for(BaseEntity i: l)
				{
					if(i instanceof  Base)
					{
						baseX = i.getPosX();
						baseY = i.getPosY();
						System.out.println(baseX+"   "+baseY);
						System.out.println(xNow+"   "+yNow);
						inputKeys.remove(Input.Keys.Z);

						camera.position.x = baseX*58;
						camera.position.y = baseY*18;
						camera.update();
						return;


					}

				}
		}
	}

	public void moveToOneOfTheEntity() {
		

	}
	
	public void storeEntityPosition() {
		
		
	}
	
	public void entityOnClick() {
		if (inputKeys.contains(Input.Keys.V)){
			if (spacmanSelect == false) {
				for (Renderable e : GameManager.get().getWorld().getEntities()) {
					if ((e instanceof BaseEntity) && ((BaseEntity) e).isSelected()) {
						baseEntityList.add((BaseEntity) e);
						LOGGER.error("stored a BaseEntity");
					}
				}
				spacmanSelect = true;
			}
		} else {
			spacmanSelect = false;
			for (Renderable e : GameManager.get().getWorld().getEntities()) {
				if ((e instanceof BaseEntity) && !((BaseEntity) e).isSelected() && baseEntityList.contains((BaseEntity) e)) {
					baseEntityList.remove((BaseEntity) e);
					LOGGER.error("remove a BaseEntity from the list");
				}
			}
		}
	}
	
	public void addExtraSpacMan() {
		if (inputKeys.contains(Input.Keys.G) && !inputKeys.contains(Input.Keys.CONTROL_LEFT)) {
			if(spac == false){
				spac = true;
				Spacman s = new Spacman(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0);
				s.setOwner(-1);
				GameManager.get().getWorld().addEntity(s);
			}
		} else {
			spac = false;
		}
	}
	
	public void addExtraAiSpacMan() {
		if (inputKeys.contains(Input.Keys.G) && inputKeys.contains(Input.Keys.CONTROL_LEFT)) {
			if(aiSpac == false){
				aiSpac = true;
				Spacman s = new Spacman(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0);
				s.setOwner(0);
				GameManager.get().getWorld().addEntity(s);
			}
		} else {
			aiSpac = false;
		}
	}
	
	public void addExtraTank() {
		if (inputKeys.contains(Input.Keys.T)) {
			if(tank == false){
				tank = true;
				Tank t = new Tank(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,-1);
				GameManager.get().getWorld().addEntity(t);
			}
		} else {
			tank = false;
		}
	}
	
	public void addExtraSoldier() {
		if (inputKeys.contains(Input.Keys.J)) {
			if(soldier == false){
				soldier = true;
				Soldier soldier = new Soldier(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(soldier);
			}
		} else {
			soldier = false;
		}
	}
	
	public void addResource() {
		
		
	}
	
	public void quitGame() {
		if(inputKeys.contains(Input.Keys.ESCAPE)) {
			System.exit(0);
		}
		
	}
}