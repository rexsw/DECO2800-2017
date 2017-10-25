package com.deco2800.marswars.functionkeys;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.Clickable;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.managers.MouseHandler;
import com.deco2800.marswars.renderers.Renderable;
import com.deco2800.marswars.worlds.BaseWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * This Class is used to deal with different keyboard inputs as
 * command of the game and it might have some special functions.
 * 
 * @author Matthew Lee
 * @co-author haoxuan
 */
public class ShortCut {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShortCut.class);
	
	// inputKeys is used to stored the user input.
	private Set<Integer> inputKeys = new HashSet<>();
	
	private static final int SPEED = 10;
	
	// cameraPosition will store different list of positions of camera.
	private ArrayList<ArrayList<Float>> cameraPosition = new ArrayList<ArrayList<Float>>();
	
	// These ArrayList<BaseEntity> will be used to store a group of BaseEntity.
	private ArrayList<BaseEntity> baseEntityList = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList1 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList2 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList3 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList4 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList5 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList6 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList7 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList8 = new ArrayList<BaseEntity>();
	private ArrayList<BaseEntity> baseEntityList9 = new ArrayList<BaseEntity>();
	
	// cameraPointer is a counter of the cameraPosition
	private int cameraPointer = 0;
	
	private int pauseCount = 0;
	
	// These boolean variables are used to avoid user having a long press of the
	// input keys and make the functions implement multiple times that affect
	// the performance of different functions.
	private boolean n = false;
	private boolean c = false;
	private boolean b =  false;
	private boolean spac = false;
	private boolean aiSpac = false;
	private boolean tank = false;
	private boolean soldier = false;
	private boolean sniper = false;
	private boolean carrier = false;
	private boolean hacker = false;
	private boolean medic = false;
	private boolean tankdes = false;
	private boolean select = false;
	private boolean moveToEntity = false;
	private boolean delete = false;
	private boolean numberKey = false;
	
	/**
	 * This methods will implement different methods in ChortCut class.
	 * 
	 * @param OrthographicCamera camera.
	 */
	public void process(OrthographicCamera camera) {
		moveCamera(camera);
		storeCameraPosition(camera);
		cameraGoToTheStoredPosition(camera);
		cameraBackToLastPosition(camera);
		cameraDeleteLastPosition();
		addExtraSpacMan();
		addExtraAiSpacMan();
		addExtraTank();
		addExtraTankDestroyer();
		addExtraCarrier();
		addExtraSoldier();
		addExtraSniper();
		addExtraHacker();
		addExtraMedic();
		storeTeam();
		teamOnClick();
		//moveCameraToBase(camera);
	}
	
	/**
	 * This methods will move the camera and change the zoom size.
	 * 
	 * @param OrthographicCamera camera.
	 */
	public void moveCamera(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.UP) || inputKeys.contains(Input.Keys.W)) {
			camera.translate(0, 1 * SPEED * camera.zoom, 0);
		}
		if (inputKeys.contains(Input.Keys.DOWN) || inputKeys.contains(Input.Keys.S)) {
			camera.translate(0, -1 * SPEED * camera.zoom, 0);
		}
		if (inputKeys.contains(Input.Keys.LEFT) || inputKeys.contains(Input.Keys.A)) {
			camera.translate(-1 * SPEED * camera.zoom, 0, 0);
		}
		if (inputKeys.contains(Input.Keys.RIGHT) || inputKeys.contains(Input.Keys.D)) {
			camera.translate(1 * SPEED * camera.zoom, 0, 0);
		}
		if ((inputKeys.contains(Input.Keys.EQUALS)) && (camera.zoom > 0.5)) {
			camera.zoom /= 1.05;
		}
		if ((inputKeys.contains(Input.Keys.MINUS)) && (camera.zoom < 10)) {
			camera.zoom *= 1.05;
		}
	}
	
	/**
	 * This method adds a key that user type in to inputKeys.
	 * 
	 * @param int key
	 */
	public void addKey(int key) {
		inputKeys.add(key);
	}
	
	/**
	 * This method removes a key that exists in inputKeys.
	 * 
	 * @param int key
	 */
	public void removeKey(int key) {
		if (inputKeys.contains(key)) {
			inputKeys.remove(key);
		}
	}
	
	/**
	 * This method will store the current position of the camera into an ArrayList.
	 * 
	 * @param OrthographicCamera camera
	 */
	public void storeCameraPosition(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.C) && !inputKeys.contains(Input.Keys.CONTROL_LEFT)){
			if(!c){
				ArrayList<Float> xyPosition = new ArrayList<Float>();
				xyPosition.add(camera.position.x);
				xyPosition.add(camera.position.y);
				cameraPosition.add(xyPosition);
				c = true;
				LOGGER.error("cameraX " + Float.toString(camera.position.x) + " cameraY " + Float.toString(camera.position.y));
			}
		}else{
			c = false;
		}
	}
	
	/**
	 * This method calculate the distance of x position of the camera and y position of the camera 
	 * between current position of the camera and the position that had been stored in the ArrayList.
	 * Then move the camera using the result of x and y. After that if the player keep using this function
	 * then the camera will move to the other position that had been stored in the ArrayList.
	 * 
	 * @param OrthographicCamera camera
	 */
	public void cameraGoToTheStoredPosition(OrthographicCamera camera) {
		if(inputKeys.contains(Input.Keys.N)){
			if(!cameraPosition.isEmpty()){
				ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
				if(n){
					return;
				}
				float x = camera.position.x - nextPosition.get(0);
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
				n = true;
				cameraPointer++;
				cameraPointer = cameraPointer % cameraPosition.size();
			}
		}else{
			n = false;
		}
	}
	
	/**
	 * This method calculate the distance of x position of the camera and y position of the camera 
	 * between current position of the camera and the position that had been stored in the ArrayList.
	 * Then move the camera using the result of x and y. After that if the player keep using this function
	 * then the camera will move to the other position that had been stored in the ArrayList that in a
	 * backward order.
	 * 
	 * @param OrthographicCamera camera
	 */
	public void cameraBackToLastPosition(OrthographicCamera camera) {
		if(inputKeys.contains(Input.Keys.B)){
			if(!cameraPosition.isEmpty()){
				if(b){
					return;
				}
				cameraPointer--;
				if (cameraPointer < 0) {
					cameraPointer = cameraPosition.size() - 1;
				}
				cameraPointer = cameraPointer % cameraPosition.size();
				ArrayList<Float> nextPosition = cameraPosition.get(cameraPointer);
				float x = camera.position.x - nextPosition.get(0);
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
				b = true;
			}
		} else {
			b = false;
		}
	}
	
	/**
	 * This method will delete one of the position that had been stored in the ArrayList.
	 * 
	 * @param OrthographicCamera camera
	 */
	public void cameraDeleteLastPosition() {
		if((inputKeys.contains(Input.Keys.C)) && inputKeys.contains(Input.Keys.CONTROL_LEFT)){
			if(!cameraPosition.isEmpty()){
				if (delete) {
					return;
				}
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
		}else{
			delete = false;
		}
	}
	
	// still working on it
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
	
	// still working on it
	public void moveToOneOfTheEntity(OrthographicCamera camera) {
		if (inputKeys.contains(Input.Keys.Z) && !baseEntityList.isEmpty()) {
			if (!moveToEntity) {
				int length = GameManager.get().getWorld().getLength();
				int width = GameManager.get().getWorld().getWidth();
				float x = (camera.position.x / 32) - (baseEntityList.get(0).getPosX()) * width / length ;
				float y = camera.position.y - baseEntityList.get(0).getPosY();
				x *= -1;
				y *= -1;
				camera.translate(x, y, 0);
				moveToEntity = true;
			}
		} else {
			moveToEntity = false;
		}
	}

	/**
	 * this method is to move the camera to the home base by clicking the backspace button
	 * has not differentiated player's and ai's home base yet
	 * @param g the gamemanager.
	 * @param camera the orthographicCamera.
	 */
	public void moveCameraToBase(OrthographicCamera camera) {
		BaseWorld  w = GameManager.get().getWorld();
		List<BaseEntity> l = w.getEntities();
		float xNow = camera.position.x;
		float yNow = camera.position.y;
		float baseX;
		float baseY;
		if (inputKeys.contains(Input.Keys.BACKSPACE)) {
			for(BaseEntity i: l) {
				if(i instanceof  Base) {
					baseX = i.getPosX();
					baseY = i.getPosY();
					LOGGER.info(baseX+"   "+baseY);
					LOGGER.info(xNow+"   "+yNow);
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
	
	/**
	 * This method is used to recognize user's inputs and call entityOnClick() method by
	 * using different ArrayList as parameter.
	 */
	public void storeTeam() {
		if (!select) {
			if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_1)){
				entityOnClick(baseEntityList);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_2)){
				entityOnClick(baseEntityList1);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_3)){
				entityOnClick(baseEntityList2);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_4)){
				entityOnClick(baseEntityList3);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_5)){
				entityOnClick(baseEntityList4);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_6)){
				entityOnClick(baseEntityList5);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_7)){
				entityOnClick(baseEntityList6);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_8)){
				entityOnClick(baseEntityList7);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_9)){
				entityOnClick(baseEntityList8);
			} else if (inputKeys.contains(Input.Keys.SHIFT_LEFT) && inputKeys.contains(Input.Keys.NUM_0)){
				entityOnClick(baseEntityList9);
			} 
			select = true;
		} else {
			select = false;
		}
	}
	
	/**
	 * This method is used to recognize user's inputs and call entityOnClick() method by
	 * using different ArrayList as parameter.
	 */
	public void teamOnClick() {
		if (!numberKey && !inputKeys.isEmpty()) {
			if (inputKeys.contains(Input.Keys.NUM_1) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList);
			} else if (inputKeys.contains(Input.Keys.NUM_2) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList1);
			} else if (inputKeys.contains(Input.Keys.NUM_3) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList2);
			} else if (inputKeys.contains(Input.Keys.NUM_4) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList3);
			} else if (inputKeys.contains(Input.Keys.NUM_5) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList4);
			} else if (inputKeys.contains(Input.Keys.NUM_6) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList5);
			} else if (inputKeys.contains(Input.Keys.NUM_7) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList6);
			} else if (inputKeys.contains(Input.Keys.NUM_8) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList7);
			} else if (inputKeys.contains(Input.Keys.NUM_9) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList8);
			} else if (inputKeys.contains(Input.Keys.NUM_0) && !inputKeys.contains(Input.Keys.SHIFT_LEFT)) {
				setOnClick(baseEntityList9);
			} 
			numberKey = true;
		} else {
			numberKey = false;
		}
		
	}
	
	/**
	 * This method will take an ArrayList<BaseEntity> as parameter and iterate the entity in the list
	 * and set them to be onClicked. 
	 * 
	 * @param ArrayList<BaseEntity> list
	 */
	public void setOnClick(List<BaseEntity> list) {
		MouseHandler mouseHandler = (MouseHandler) (GameManager.get().getManager(MouseHandler.class));
		for (BaseEntity entity: list) {
			entity.makeSelected();
			((Clickable) entity).onClick(mouseHandler);
		}
	}
	
	/**
	 * This method will take an ArrayList<BaseEntity> as parameter and iterate the entities that
	 * exist in the game world. The entities which had been selected by users will be added to a list.
	 * 
	 * @param ArrayList<BaseEntity> list
	 */
	public void entityOnClick(List<BaseEntity> list) {
		for (Renderable e : GameManager.get().getWorld().getEntities()) {
			if ((e instanceof Clickable) && (e instanceof BaseEntity) && ((BaseEntity) e).isSelected() && !list.contains((BaseEntity) e)) {
				list.add((BaseEntity) e);
				LOGGER.error("stored a BaseEntity");
			}
		}
	}
	
	/**
	 * This method will create a Spacman that owned by user into the game world.
	 * 
	 */
	public void addExtraSpacMan() {
		if (inputKeys.contains(Input.Keys.G) && !inputKeys.contains(Input.Keys.CONTROL_LEFT)) {
			if(!spac){
				spac = true;
				Astronaut s = new Astronaut(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,-1);
				s.setOwner(-1);
				GameManager.get().getWorld().addEntity(s);
			}
		} else {
			spac = false;
		}
	}
	
	/**
	 * This method will create an Ai Spacman into the game world.
	 * 
	 */
	public void addExtraAiSpacMan() {
		if (inputKeys.contains(Input.Keys.G) && inputKeys.contains(Input.Keys.CONTROL_LEFT)) {
			if(!aiSpac){
				aiSpac = true;
				Astronaut s = new Astronaut(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,1);
				s.setOwner(0);
				GameManager.get().getWorld().addEntity(s);
			}
		} else {
			aiSpac = false;
		}
	}
	
	/**
	 * This method will create a Tank that owned by user into the game world.
	 * 
	 */
	public void addExtraTank() {
		if (inputKeys.contains(Input.Keys.K)) {
			if(!tank){
				tank = true;
				Tank t = new Tank(GameManager.get().getWorld().getLength()/2, GameManager.get().getWorld().getWidth()/2,0,-1);
				GameManager.get().getWorld().addEntity(t);
			}
		} else {
			tank = false;
		}
	}
	
	/**
	 * This method will create a Soldier that owned by user into the game world.
	 * 
	 */
	public void addExtraSoldier() {
		if (inputKeys.contains(Input.Keys.J)) {
			if(!soldier){
				soldier = true;
				Soldier soldier1 = new Soldier(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(soldier1);
			}
		} else {
			soldier = false;
		}
	}
	
	/**
	 * This method will create a Sniper that owned by user into the game world.
	 * 
	 */
	public void addExtraSniper() {
		if (inputKeys.contains(Input.Keys.I)) {
			if(!sniper){
				sniper = true;
				Sniper sniper1 = new Sniper(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(sniper1);
			}
		} else {
			sniper = false;
		}
	}
	
	/**
	 * This method will create a Hacker that owned by user into the game world.
	 * 
	 */
	public void addExtraHacker() {
		if (inputKeys.contains(Input.Keys.L)) {
			if(!hacker){
				hacker = true;
				Hacker hacker1 = new Hacker(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(hacker1);
			}
		} else {
			hacker = false;
		}
	}
	
	/**
	 * This method will create a Medic that owned by user into the game world.
	 * 
	 */
	public void addExtraMedic() {
		if (inputKeys.contains(Input.Keys.P)) {
			if(!medic){
				medic = true;
				Medic medic1 = new Medic(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(medic1);
			}
		} else {
			medic = false;
		}
	}
	
	/**
	 * This method will create a Carrier that owned by user into the game world.
	 * 
	 */
	public void addExtraCarrier() {
		if (inputKeys.contains(Input.Keys.R)) {
			if(!carrier){
				carrier = true;
				Carrier c1 = new Carrier(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/3,0,-1);
				GameManager.get().getWorld().addEntity(c1);
			}
		} else {
			carrier = false;
		}
	}
	
	/**
	 * This method will create a TankDestroyer that owned by user into the game world.
	 * 
	 */
	public void addExtraTankDestroyer() {
		if (inputKeys.contains(Input.Keys.O)) {
			if(!tankdes){
				tankdes = true;
				TankDestroyer t1 = new TankDestroyer(GameManager.get().getWorld().getLength()/3, GameManager.get().getWorld().getWidth()/2,0,-1);
				GameManager.get().getWorld().addEntity(t1);
			}
		} else {
			tankdes = false;
		}
	}
}