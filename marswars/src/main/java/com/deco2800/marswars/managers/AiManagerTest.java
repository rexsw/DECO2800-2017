package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;
import com.deco2800.marswars.entities.Selectable.EntityType;
import com.deco2800.marswars.util.WorldUtil;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by Scott Whittington on 22/08
 * control the current ai, every tick the ai looks at it's units and 
 * ensures they're doing something useful 
 */
public class AiManagerTest extends Manager implements TickableManager, HasTeam {
		private int teamid;
		private static final Logger LOGGER = LoggerFactory.getLogger(AiManagerTest.class);
		private int cooldown = 0;
		private int time = 0;
		private boolean alive = true;

@Override
public void onTick(long l) {
	if(!alive) {
		return;
	}
	time += 5;
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof HasOwner) {
			if(e instanceof Spacman && ((HasOwner) e).getOwner() == this) {
				//set all spacmen the ai owns to gather rocks
				Spacman x = (Spacman)e;
				if(!x.isWorking()) {
					//allow spacmans to collect the closest resources
					Optional<BaseEntity> resource = WorldUtil.getClosestEntityOfClass(Resource.class, x.getPosX(),x.getPosY());
					x.setAction(new GatherAction(x, (Resource) resource.get()));
					LOGGER.error("ai - set spacman to grather");
				
				}
			}
			generateSpacman(e);
			if(e instanceof EnemySpacman && ((HasOwner) e).getOwner() == this) {
				//lets the ai target player spacman with it's enemyspacmen
				EnemySpacman x = (EnemySpacman)e;
				for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
					if(r instanceof Spacman && !((Spacman) r).sameOwner(x) && !(((Spacman) r).getOwner() instanceof PlayerManager)) {
						if (e.distance(r) < 3f) {
							if(cooldown + 60 < time) {
								LOGGER.error("ai - attacked spacman");
								Spacman y = (Spacman)r;
								(y).setHealth(y.getHealth() - 2);
								cooldown = time;
							}
							break;
						} else if(cooldown + 10 < time) {
							x.setAction(new MoveAction(r.getPosX(), r.getPosY(), x));
							cooldown = time;
							break;
						}
						break;
					}
				}
			}
		}
	}
}
		/**
		 * generate new spacman when a base have more than 30 rocks
		 */
		private void generateSpacman(BaseEntity e) {
			if(e instanceof Base && ((HasOwner) e).getOwner() == this) {
				Base x = (Base)e;
				if(!x.isWorking()) {
					//sets the ai base to make more spacman if possible
					ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
					if (resourceManager.getRocks() > 30) {
						LOGGER.error("ai - set base to make spacman");
						resourceManager.setRocks(resourceManager.getRocks() - 30);
						Spacman r = new Spacman(x.getPosX() + 1, x.getPosY(), 0);
						r.setEntityType(EntityType.AISPACMAN);
						r.setOwner(this);
						x.setAction(new GenerateAction(r));							
					}
				}
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
