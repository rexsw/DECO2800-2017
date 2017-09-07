package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.units.Astronaut;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.entities.units.Soldier;
import com.deco2800.marswars.util.WorldUtil;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Scott Whittington on 22/08
 * control the current ai, every tick the ai looks at it's units and 
 * ensures they're doing something useful
 * warning spicy i hope you like meat balls 
 */

public class AiManagerTest extends AbstractPlayerManager implements TickableManager, HasTeam {
		private int teamid;
		private static final Logger LOGGER = LoggerFactory.getLogger(AiManagerTest.class);
		private boolean alive = true;
		private ResourceManager resources = new ResourceManager();

@Override
public void onTick(long l) {
	if(!alive) {
		return;
	}
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof HasOwner && ((HasOwner) e).getOwner() == this) {
			if(e instanceof Astronaut) {
				Astronaut x = (Astronaut)e;
				useSpacman(x);
			} else if(e instanceof Base) {
				Base x = (Base)e;
				generateSpacman(x);
			} else if(e instanceof Soldier) {
				Soldier x = (Soldier)e;
				useEnemy(x);
			}
		}
	}
}
		/**
		 * generate new spacman when a base has more than 30 rocks
		 */
private void generateSpacman(Base x) {
	if(!x.isWorking() && resources.getRocks() > 30) {
		//sets the ai base to make more spacman if possible
		LOGGER.error("ai - set base to make spacman");
		resources.setRocks(resources.getRocks() - 30);
		Astronaut r = new Astronaut(x.getPosX(), x.getPosY(), 0, this);
		x.setAction(new GenerateAction(r));
	}
}
		
private void useEnemy(Soldier x) {
	//lets the ai target player spacman with it's enemyspacmen
	if(x.isWorking()) {
		return;
	}
	for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
		if(r instanceof AttackableEntity && !x.sameOwner(r)) {
			LOGGER.error("ai - setting unit to attack " + r.toString());
			AttackableEntity y = (AttackableEntity) r;
			x.attack(y);
			return;
		}
	}
}

private void useSpacman(Astronaut x) {
	if(!(x.isWorking())) {
		//allow spacmans to collect the closest resources
		Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
		x.setAction(new GatherAction(x, (Resource) resource.get()));
		LOGGER.error("ai - set spacman to grather");
	}
}
		
/**
 * note team methods where a wip system and have been pushed back and as such
 * are not used for now		
 */
@Override
public void setTeam(int teamId) {
	this.teamid = teamId;
}

@Override
public int getTeam() {
	return teamid;
}

@Override
public boolean sameTeam(Manager otherMember) {
	boolean isInstance = otherMember instanceof HasTeam;
	return isInstance && this.teamid == ((HasTeam) otherMember).getTeam();
}
		
/**
 * if the ai has no more spacman under it's control, removes it's units
 * and sets it to "dead" so it won't tick anymore 
 */
public void isKill() {
	//in this case "dead" is an Ai with no spacman
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof AttackableEntity && ((AttackableEntity) e).getOwner() == this) {
			return;
		}
	}
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof HasOwner && ((HasOwner) e).getOwner() == this) {
			GameManager.get().getWorld().removeEntity(e);
		}
	}
	LOGGER.error("ai - is kill");
	alive = false;
}

/**
 * @return true iff Ai is alive else false 
 */
public boolean alive() {
	return alive;
}

/**
 * @return Resourcemanager of this Ai
 */
public ResourceManager getResources() {
	return resources;
}

@Override
public String toString() {
	return "Ai team - " + this.getColour();
}

}
