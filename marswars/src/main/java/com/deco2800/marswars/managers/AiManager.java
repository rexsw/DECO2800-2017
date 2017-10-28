package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.ActionSetter;
import com.deco2800.marswars.actions.AttackAction;
import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.buildings.Barracks;
import com.deco2800.marswars.buildings.Base;
import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.EntityID;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.terrainelements.Resource;
import com.deco2800.marswars.entities.terrainelements.ResourceType;
import com.deco2800.marswars.entities.units.*;
import com.deco2800.marswars.entities.units.AmbientAnimal.AmbientState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Scott Whittington on 22/08
 * control the current ai, every tick the ai looks at it's units and 
 * ensures they're doing something useful
 * States added by  Ross Webster
 * Ambient Animal AI added by Michelle Mo
 */
public class AiManager extends AbstractPlayerManager implements TickableManager {
	private List<Integer> teamid = new LinkedList<Integer>();
	private static final Logger LOGGER = LoggerFactory.getLogger(AiManager.class);
	private List<State> state = new LinkedList<State>();
	private List<AiType> aiType = new LinkedList<AiType>();
	private long timeAtStateChange;
	private TimeManager tm = (TimeManager) GameManager.get().getManager(TimeManager.class);
	private Random rand = new Random();
	private GameBlackBoard black = (GameBlackBoard) GameManager.get().getManager(GameBlackBoard.class);
	private int tickNumber = 0;
	private int unitTrackRange = 10;
	private int unitGroupRange = 3;
	private int unitPatrolRange = 10;
	private int unitJoinGroupRange = 1000;
	private Difficulty aiDifficulty = Difficulty.NORMAL;
	
	
	public enum State {
		AGGRESSIVE, DEFENSIVE, ECONOMIC
	}
	
	// The personality and strategy type an Ai will use
	public enum AiType {
		STANDARD, HOSTILE, PROTECTIVE, EXPANSIVE
	}
	
	public enum Difficulty {
		EASY(0),
		NORMAL(1),
		HARD(2);
		
		private final int difficultValue;
		Difficulty(int difficultValue) {
	        this.difficultValue = difficultValue;
	    }
	    public int getDifficultValue() {
	    	return difficultValue;
	    }
	}

	@Override
	public void onTick(long l) {
		//Increase the current tick count
		tickNumber++;
		//Check for state change every 10 ticks
		if (tickLock(10,5)) {
			decideChangeState();
		}
		for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
			if(e instanceof AttackableEntity && ((HasOwner) e).isAi()) {
				if(e instanceof AmbientAnimal){
					animalController((AmbientAnimal)e);
				} else {
					// run decider functions
					decider((AttackableEntity) e);
				}
			}
		}
	}
	
	private void decider(AttackableEntity unit) {
		if (!unit.isAi()) {
			return;
		}
		int team = unit.getOwner();
		State state = getState(team);
		if(unit instanceof Astronaut) {
			// send astronauts to work
			Astronaut x = (Astronaut)unit;
			useSpacman(x);
		} else if(unit instanceof Base) {
			Base x = (Base)unit;
			if(state == State.ECONOMIC) {
				generateEntity(x, EntityID.ASTRONAUT);
			} else {
				generateEntity(x, EntityID.SOLDIER);
			}
		} else if(unit instanceof Barracks) {
			Barracks x = (Barracks)unit;
			if(state != State.ECONOMIC) {
				//generateEntity(x, EntityID.HACKER);
			}
		}
		else if(unit instanceof Soldier) {
			Soldier x = (Soldier)unit;
			// Action depends on current state
			switch (getState(unit.getOwner())) {
			case AGGRESSIVE:
				soldierGroupAttack(x);
				//soldierSearchAttack(x);
				break;
			case DEFENSIVE:
				soldierDefend(x);
				break;
			case ECONOMIC:
				soldierDefend(x);
				break;
			}
		}
	}
	
	/**
	 * A controller which control the ambient animal's actions
	 * @param ambient
	 */
	public void animalController(AmbientAnimal animal){
		AmbientState as = animal.getState();
		switch(as){
		case TRAVEL:
			animal.move();
			break;
		case ATTACKBACK:
			animal.attack();
			break;
		default:
			animal.move();
			break;
		}
	}
	
	/**
	 * Sets an animals Ai state based if it's been attacked or waiting for long enough
	 * 
	 * @param animal the animals state to change
	 */
	public void animalDefaultState(AmbientAnimal animal){
		if (animal.gotHit()) {
			animal.setState(AmbientState.ATTACKBACK);
		}
		if (animal.getWaitingTime() < 60*60) {
			animal.setWaitingTime(animal.getWaitingTime() + 1);
		} else {
			animal.setWaitingTime(0);
			animal.setState(AmbientState.TRAVEL);
		}
	}
	
	/**
	 *  Sets an animals Ai state based if it's been attacked or waiting for long enough
	 *  for the travel state
	 * 
	 * @param animal the animals state to change
	 */
	public void animalTravelState(AmbientAnimal animal){
		if (animal.gotHit()) {
			animal.setState(AmbientState.ATTACKBACK);
			return;
		}
		if (animal.showProgress())
			return;
		if (animal.getTravelTime() < animal.getMaxTravelTime()) {
			animal.move();
			animal.setTravelTime(animal.getTravelTime() + 1);
		} else {
			animal.setTravelTime(0);
			animal.setState(AmbientState.DEFAULT);
		}
	}
	
	/**
	 * generate new entity if a base can afford it
	 */
	private void generateEntity(BuildingEntity x, EntityID entityID) {
		ResourceManager rm = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
		if(!x.showProgress() && ActionSetter.canAfford(x.getOwner(), true, entityID, rm)) {
			LOGGER.info("ai - set base to make " + entityID);
			//ActionSetter.payForEntity(x.getOwner(), true, entityID, rm);
			//Astronaut r = new Astronaut(x.getPosX(), x.getPosY(), 0, x.getOwner());
			//x.setAction(new GenerateAction(r));
			ActionSetter.setGenerate(x, entityID);
			LOGGER.info("ai - base has an action: " + x.showProgress());
		}
	}
	
	/**
	 * Tasks a soldier to attack, but if they cannot then to join a group, and if not then
	 * just search
	 * @param soldier
	 */
	public void soldierGroupAttack(Soldier soldier) {
		// try to attack a target
		if (soldierAttack(soldier)) {
			return;
		}
		// if no valid target or current action, group to commander
		if (soldierGroup(soldier)) {
			return;
		} else {
			// if cannot, then just search
			soldierSearch(soldier);
		}
	}
	/**
	 * Tasks a soldier to attack, but if they cannot then just search
	 * @param soldier
	 */
	public void soldierSearchAttack(Soldier soldier) {
		// try to attack a target
		if (soldierAttack(soldier)) {
			return;
		}
		// if no valid target or current action, search for a target
		soldierSearch(soldier);
	}
	
	/**
	 * Tasks a soldier to stay with a nearby commander
	 * @param soldier
	 * @return If the soldier now has a task
	 */
	private boolean soldierGroup(Soldier soldier) {
		// if soldier already has an action
		if(soldier.showProgress()) {
			return true;
		}
		// if no valid target or current action, group to commander
		BaseEntity commander = null;
		for( BaseEntity newCommander : GameManager.get().getWorld().getEntities()) {
			if (newCommander instanceof HasOwner && newCommander.sameOwner(soldier)) {
				if (newCommander instanceof Commander || (soldier instanceof Dino && newCommander instanceof Dinoking)) {
					// found a commander; choose if none selected already or is closer
					if (commander == null) {
						commander = newCommander;
					} else if (getDistanceLinear(soldier, newCommander) + 2
							< getDistanceLinear(soldier, commander)) {
						// if new commander is closer (with buffer), chose them
						commander = newCommander;
					}
				}
			}
		}
		// if there is a commander close by, and soldier is not a commander, join that group
		if (commander != null && !(soldier instanceof Commander)
				&& getDistanceLinear(soldier, commander) < unitJoinGroupRange) {
			float posX = commander.getPosX() + rand.nextInt(unitGroupRange * 2) - unitGroupRange;
			float posY = commander.getPosY() + rand.nextInt(unitGroupRange * 2) - unitGroupRange;
			soldier.setAction(new MoveAction(posX, posY, (AttackableEntity)soldier));
			LOGGER.info("ai - grouping to commander");
			return true;
		}
		return false;
	}
	
	/**
	 * Tasks a soldier to wander to a random spot on the map
	 * @param soldier
	 */
	public void soldierSearch(Soldier soldier) {
		// if soldier already has an action
		if(soldier.showProgress()) {
			return;
		}
		// wander around the world (i.e. move to a random location)
		int width = GameManager.get().getWorld().getWidth();
		int length = GameManager.get().getWorld().getLength();
		int posX = rand.nextInt(width);
		int posY = rand.nextInt(length);
		soldier.setAction(new MoveAction(posX, posY, (AttackableEntity)soldier));
		return;
	}
	
	/**
	 * Orders a soldier to attack an enemy
	 * @param soldier
	 * @return true if soldier now has a task
	 */
	private boolean soldierAttack(Soldier soldier) {
		//lets the ai target player spacman with it's enemyspacmen
		if(soldier.showProgress()) {
			return true;
		}
		for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
			if (validAttackableEntity(soldier, r)) {
				if (betterTarget(soldier, r)) {
					LOGGER.info("ai - setting unit to attack " + r.toString());
					AttackableEntity y = (AttackableEntity) r;
					soldier.attack(y);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns whether or not a entity is a valid target
	 * @param soldier
	 * @param target
	 * @return boolean
	 */
	private boolean validAttackableEntity(BaseEntity soldier, BaseEntity target) {
		// if target is an attackable entity
		if (target instanceof AttackableEntity) {
			// if target has a different owner to soldier
			if (!soldier.sameOwner(target)) {
				// if target is not in a vehicle
				if (((AttackableEntity) target).getLoadStatus()!=1) {
					// if target is close enough to soldier
					if (getDistanceLinear(soldier, target) <= unitTrackRange) {
						return true;
					}
				}
			}
		}
		// Else
		return false;
	}
	
	/**
	 * Decides if this new target is better than the soldier's current target
	 * @param soldier
	 * @param target
	 * @return if it is better
	 */
	private boolean betterTarget(BaseEntity soldier, BaseEntity target) {
		// if there is no current action, then this is a better target
		if (!soldier.getAction().isPresent()) {
			return true;
		}
		AttackAction attack = (AttackAction)soldier.getAction().get();
		// if there is no current target, then this one is good
		if (attack.completed()) {
			return true;
		}
		// If the new distance is closer than the current target's, then this is a better target
		//   There is a buffer added to prevent constant target swaps
		if (attack.getDistanceToEnemy() + 2 < getDistanceLinear(soldier, target)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Finds the diagonal distance between two BaseEntity objects, excluding vertical
	 * @param entity1
	 * @param entity2
	 * @return distance
	 */
	public double getDistanceDiagonal(BaseEntity entity1, BaseEntity entity2) {
		double lenX = entity1.getPosX() - entity2.getPosX();
		double lenY = entity1.getPosY() - entity2.getPosY();
		return Math.sqrt( Math.pow(lenX, 2) + Math.pow(lenY, 2) );
	}
	
	/**
	 * Finds the linear distance between two BaseEntity objects, excluding vertical
	 * @param entity1
	 * @param entity2
	 * @return distance
	 */
	public double getDistanceLinear(BaseEntity entity1, BaseEntity entity2) {
		double lenX = entity1.getPosX() - entity2.getPosX();
		double lenY = entity1.getPosY() - entity2.getPosY();
		return Math.abs(lenX) + Math.abs(lenY);
	}
	
	/**
	 * Tasks all soldiers to move back to base
	 * @param x
	 */
	public void soldierDefend(Soldier soldier) {
		if(soldier.showProgress()) {
			return;
		}
		// try to attack a target
		if (soldierAttack(soldier)) {
			return;
		}
		// if no current action, patrol around the nearest base
		BaseEntity base = null;
		for( BaseEntity newBase : GameManager.get().getWorld().getEntities()) {
			if (newBase instanceof HasOwner && newBase.sameOwner(soldier)) {
				if (newBase instanceof Base) {
					// found a base; choose if none selected already or is closer
					if (base == null) {
						base = newBase;
					} else if (getDistanceLinear(soldier, newBase) + 10
							< getDistanceLinear(soldier, base)) {
						// if new base is closer (with buffer), chose them
						base = newBase;
					}
				}
			}
		}
		// if there is a base, patrol there
		if (base != null) {
			float posX = base.getPosX() + rand.nextInt(unitPatrolRange * 2) - unitPatrolRange;
			float posY = base.getPosY() + rand.nextInt(unitPatrolRange * 2) - unitPatrolRange;
			soldier.setAction(new MoveAction(posX, posY, (AttackableEntity)soldier));
			LOGGER.info("ai - patrolling base");
		}
		// else: no action
		return;
	}
	
	/**
	 * Orders an Astronaut to gather resources
	 * @param x
	 */
	public void useSpacman(Astronaut x) {
		if(x.showProgress()) {
			return;
		}
		AiBuilder build = (AiBuilder) GameManager.get().getManager(AiBuilder.class);
		build.build(x);
		if(x.showProgress()) {
			return;
		}
		spacmanGather(x, resourceChoice(x));
	}
	
	private ResourceType resourceChoice(Astronaut x) {
		// 50% of the time, pick favored resource
		if (rand.nextInt(2) == 0) {
			switch(getState(x.getOwner())) {
			case ECONOMIC:
				return ResourceType.BIOMASS;
			case AGGRESSIVE:
				return ResourceType.CRYSTAL;
			default:	//case DEFENSIVE:
				return ResourceType.ROCK;
			}
		} else { // otherwise pick random
			switch(rand.nextInt(3)) {
			case 0:
				return ResourceType.BIOMASS;
			case 1:
				return ResourceType.CRYSTAL;
			default:	//2
				return ResourceType.ROCK;
			}
		}
	}
	
	/**
	 * Tasks an astronaut to gather a type of resource
	 * @param x
	 * @param type
	 */
	private void spacmanGather(Astronaut x, ResourceType type) {
		//allow spacmans to collect the closest resources of the specified type
		Optional<Resource> resource = getClosestEntityOfResourseType(x.getPosX(), x.getPosY(), type);
		x.setAction(new GatherAction(x, resource.get()));
		LOGGER.info("ai - set spacman to gather resource " + type);
	}
	
	/**
	 * Gets the closest resource entity of a particular type (or any if null)
	 * Largely copied from WorldUtil.getClosestEntityOfClassAndOwner()
	 * @param x the x co-ords to search from
	 * @param y the x co-ords to search from
	 * @param type the type of resource to search for
	 * @return an entity of type c if one is found 
	 */
	private static Optional<Resource> getClosestEntityOfResourseType(float x, float y, ResourceType type) {
		Resource closest = null;
		float dist = Float.MAX_VALUE;
		for( BaseEntity newResource : GameManager.get().getWorld().getEntities()) {
			float tmp_distance = (float)(Math.sqrt(Math.pow(newResource.getPosX() - x, 2) + Math.pow(newResource.getPosY() - y, 2)));
			if ((closest == null || dist > tmp_distance) && (newResource instanceof Resource)) {
				if (type == null || ((Resource)newResource).getType() == type) {
					dist = tmp_distance;
					closest = (Resource)newResource;
				}
			}
		}

		if (closest == null) {
			return Optional.empty();
		} else {
			return Optional.of(closest);
		}
	}
	
	/**
	 * Adds an Ai team to the AiManager, and gives it a random AiType
	 * @param id
	 */
	public void addTeam(Integer id) {
		teamid.add(id);
		state.add(State.ECONOMIC);
		switch(rand.nextInt(2) + aiDifficulty.getDifficultValue()) {
		case 0:
			aiType.add(AiType.STANDARD);
			break;
		case 1:
			aiType.add(AiType.EXPANSIVE);
			break;
		case 2:
			aiType.add(AiType.PROTECTIVE);
			break;
		case 3:	// Hardest
			aiType.add(AiType.HOSTILE);
			break;
		}
		
	}
	
	/**
	 * A list of integers of Ai team ids
	 * @return List<Integer> 
	 */
	public List<Integer> getAiTeam(){
		return teamid;
	}
	
	/**
	 * Removes a team from the list of Ai players
	 * @param id int the team's id to kill
	 */
	public void kill(Integer id) {
		if(teamid.contains(id)) {
			state.remove(getStateIndex(id));
			aiType.remove(getStateIndex(id));
			teamid.remove(id);
		}
	}
	
	/**
	 * Sets a restriction boolean to only run every nth tick
	 * Returns true if on the specified tick number
	 * @param modulus
	 * @param offset
	 * @return if open (true) or locked (false)
	 */
	public boolean tickLock(int modulus, int offset){
		if (((this.tickNumber + offset) % modulus) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns a team's number of economic units
	 * @param teamID
	 * @return integer
	 */
	private int teamCountEconomicUnits(Integer teamID) {
		GameBlackBoard blackboard = (GameBlackBoard) GameManager.get().
				getManager(GameBlackBoard.class);
		// test if GameBlackBoard exists
		if (blackboard != null) {
			return blackboard.count(teamID, GameBlackBoard.Field.ASTRONAUTS);
		}
		// if blackboard does not exist, return -1
		return -1;
	}
	
	/**
	 * Returns a team's number of buildings
	 * @param teamID
	 * @return integer
	 */
	private int teamCountBuildings(Integer teamID) {
		GameBlackBoard blackboard = (GameBlackBoard) GameManager.get().
				getManager(GameBlackBoard.class);
		// test if GameBlackBoard exists
		if (blackboard != null) {
			return blackboard.count(teamID, GameBlackBoard.Field.BUILDINGS);
		}
		// if blackboard does not exist, return -1
		return -1;
	}
	
	/**
	 * Returns a team's number of combat units
	 * @param teamID
	 * @return integer
	 */
	private int teamCountCombatUnits(Integer teamID) {
		GameBlackBoard blackboard = (GameBlackBoard) GameManager.get().
				getManager(GameBlackBoard.class);
		// test if GameBlackBoard exists
		if (blackboard != null) {
			return blackboard.count(teamID, GameBlackBoard.Field.COMBAT_UNITS);
		}
		// if blackboard does not exist, return -1
		return -1;
	}
	
	/**
	 * Returns the specified team's strength (number of units + buildings)
	 * @param teamID
	 * @return team's strength rating
	 */
	private int teamStrengthCount(Integer teamID) {
		GameBlackBoard blackboard = (GameBlackBoard) GameManager.get().
				getManager(GameBlackBoard.class);
		// test if GameBlackBoard exists
		if (blackboard != null) {
			return blackboard.count(teamID, GameBlackBoard.Field.UNITS)
					+ blackboard.count(teamID, GameBlackBoard.Field.BUILDINGS);
		}
		// if blackboard does not exist, return -1
		return -1;
	}
	
	/**
	 * Returns the highest team's strength (number of units + buildings)
	 * Used to consider hostility
	 * @return max strength value
	 */
	private int highestStrengthCount() {
		int strength;
		//Assumed 1 player
		strength = teamStrengthCount(-1);
		for (int i = 1; i <= teamid.size(); i++) {
			strength = Math.max(teamStrengthCount(i) , strength);
		}
		return strength;
	}
	
	/**
	 * Determines Ai team state depending on their AiType
	 * @param teamID
	 * @return State
	 */
	private State updateState(Integer teamID) {
		AiType teamAiType = getAiType(teamID);
		int numSoldiers = teamCountCombatUnits(teamID);
		int numAstronauts = teamCountEconomicUnits(teamID);
		int numBuildings = teamCountBuildings(teamID);
		double ratioMilitary = 0.0;
		double ratioStructure = 0.0; //Not currently used
		if (numSoldiers + numAstronauts != 0) {
			ratioMilitary = (double)numSoldiers / (double)(numSoldiers + numAstronauts);
			ratioStructure = (double)numBuildings / (double)(numSoldiers + numAstronauts);
		} else {
			ratioMilitary = 0.0;
			ratioStructure = 1.0;
		}
		LOGGER.info("AI [" + teamID + "] military ratio is [" + ratioMilitary + "]");
		LOGGER.info("AI [" + teamID + "] structure ratio is [" + ratioStructure + "]");
		//
		LOGGER.info("AI [" + teamID + "] military count is [" + numSoldiers + "]");
		LOGGER.info("AI [" + teamID + "] astro count is [" + numAstronauts + "]");
		LOGGER.info("AI [" + teamID + "] building count is [" + numBuildings + "]");
		// Depending on AiType, ratio and minimums, decide state
		// If few astronauts or buildings, then get more
		if (numAstronauts < 3 || numBuildings < 1) {
			return State.ECONOMIC;
		}
		// If few soldiers, then get more
		if (ratioMilitary < minimumMilitaryRatio(teamAiType)) {
			return State.DEFENSIVE;
		}
		// If many soldiers, then attack
		if (numSoldiers > maximumMilitaryCount(teamAiType)) {
			return State.AGGRESSIVE;
		}
		// else
		return State.ECONOMIC;
	}
	
	/**
	 * Determines the minimum Military to Economic Ratio for an AiType
	 * @param aiType
	 * @return
	 */
	private double minimumMilitaryRatio(AiType aiType) {
		switch (aiType) {
		case HOSTILE:
			return 0.75;
		case PROTECTIVE:
			return 0.85;
		case EXPANSIVE:
			return 0.2;
		default: //case: STANDARD
			return 0.6;
		}
	}
	
	/**
	 * Determines the maximum number of military units for an AiType to have before attacking
	 * @param aiType
	 * @return
	 */
	private int maximumMilitaryCount(AiType aiType) {
		switch (aiType) {
		case HOSTILE:
			return 2;
		case PROTECTIVE:
			return 15;
		case EXPANSIVE:
			return 7;
		default: //case: STANDARD
			return 6;
		}
	}
	
	/**
	 * Decides whether or not to change state
	 * Will check every (in-game) hour
	 */
	public void decideChangeState() {
		for (Integer teamID = 1; teamID <= teamid.size(); teamID++) {
			if (getTimeSinceStateChange() > 60*60) {
				setState(teamID, updateState(teamID));
				LOGGER.info("AI [" + teamID + "] State updated to " + state);
			}
		}
	}
	
	/**
	 * Gets the state index in its Linked List that corresponds to the TeamID
	 * @param teamID
	 * @return state index position
	 */
	public int getStateIndex(Integer teamID) {
		int position = teamid.indexOf(teamID);
		if (position == -1) {
			LOGGER.error("Invalid Team Id");
		}
		return position;
	}
	
	/**
	 * Sets the current state of the AI Manager
	 * @param newState
	 */
	public void setState(Integer teamID, State newState) {
		state.set(getStateIndex(teamID), newState);
		timeAtStateChange = tm.getGameSeconds();	//  note that timeAtStateChange is
														// one variable for all states
	}
	
	/**
	 * Get the current state of the team id of the AI Manager
	 * @return
	 */
	public State getState(Integer teamID) {
		return state.get(getStateIndex(teamID));
	}

	/**
	 * Gets the aiType index in its Linked List that corresponds to the TeamID
	 * @param teamID
	 * @return aiType index position
	 */
	public int getAiTypeIndex(Integer teamID) {
		int position = teamid.indexOf(teamID);
		if (position == -1) {
			LOGGER.error("Invalid Team Id");
		}
		return position;
	}
	
	/**
	 * Get the current aiType of the team id of the AI Manager
	 * @return
	 */
	public AiType getAiType(Integer teamID) {
		return aiType.get(getAiTypeIndex(teamID));
	}
	
	/**
	 * Get the time at which the AI Manager last changed state
	 * @return
	 */
	public long getTimeAtStateChange() {
		return timeAtStateChange;
	}
	
	/**
	 * Get the time difference since the AI Manager last changed state
	 * @return
	 */
	public long getTimeSinceStateChange() {
		return tm.getGameSeconds() - timeAtStateChange;
	}
	
	public Difficulty getDifficulty() {
		return aiDifficulty;
	}
	
	public void setDifficulty(Difficulty dif) {
		aiDifficulty = dif;
	}
}
