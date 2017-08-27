package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
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

public class AiManagerTest extends Manager implements TickableManager, HasTeam {
		private int teamid;
		private static final Logger LOGGER = LoggerFactory.getLogger(AiManagerTest.class);
		private int cooldownattack = 0;
		private int cooldownmove = 0;
		private int time = 0;
		private boolean alive = true;

@Override
public void onTick(long l) {
	if(!alive) {
		return;
	}
	time += 5;
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof HasOwner && ((HasOwner) e).getOwner() == this) {
			if(e instanceof Spacman) {
				Spacman x = (Spacman)e;
				useSpacman(x);
			} else if(e instanceof Base) {
				Base x = (Base)e;
				generateSpacman(x);
			} else if(e instanceof EnemySpacman) {
				EnemySpacman x = (EnemySpacman)e;
				useEnemy(x);
			}
		}
	}
}
		/**
		 * generate new spacman when a base has more than 30 rocks
		 */
private void generateSpacman(Base x) {
	ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
	if(!x.isWorking() && resourceManager.getRocks() > 30) {
		//sets the ai base to make more spacman if possible
		LOGGER.error("ai - set base to make spacman");
		resourceManager.setRocks(resourceManager.getRocks() - 30);
		Spacman r = new Spacman(x.getPosX(), x.getPosY(), 0);
		r.setOwner(this);
		x.setAction(new GenerateAction(r));							
	}
}
		
private void useEnemy(EnemySpacman x) {
	//lets the ai target player spacman with it's enemyspacmen
	for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
		if(r instanceof Spacman && !((Spacman) r).sameOwner(x) && !(((Spacman) r).getOwner() == this)) {
			if ((x.distance(r) < 2f) && (cooldownattack + 60 < time)) {
				LOGGER.error("ai - attacked spacman");
				Spacman y = (Spacman)r;
				(y).setHealth(y.getHealth() - 2);
				cooldownattack = time;
			} else if(cooldownmove + 10 < time) {
				x.setAction(new MoveAction(r.getPosX(), r.getPosY(), x));
				cooldownmove = time;
			}
			break;
		}
	}
}

public void useSpacman(Spacman x) {
	if(!x.isWorking()) {
		//allow spacmans to collect the closest resources
		Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
		x.setAction(new GatherAction(x, (Resource) resource.get()));
		LOGGER.error("ai - set spacman to grather");
	}
}
		
		
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
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof Spacman && ((HasOwner) e).getOwner() == this) {
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

}
