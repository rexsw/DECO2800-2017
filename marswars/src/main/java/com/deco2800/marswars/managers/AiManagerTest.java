package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
import com.deco2800.marswars.entities.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiManagerTest extends Manager implements TickableManager, HasTeam {
		private int teamid;
		private static final Logger LOGGER = LoggerFactory.getLogger(AiManagerTest.class);

@Override
public void onTick(long l) {
	for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
		if(e instanceof HasOwner) {
			if(e instanceof Spacman && ((HasOwner) e).getOwner() == this) {
				//set all spacmen the ai owns to gather rocks
				Spacman x = (Spacman)e;
				if(!x.isWorking()) {
					for( BaseEntity r : GameManager.get().getWorld().getEntities())
						if(r instanceof Resource) {
							if(((Resource) r).getType() == ResourceType.ROCK) { //Simple call getType() for the type of resource
								x.setAction(new GatherAction(x, r));
								LOGGER.error("ai - set spacman to grather");
								break;
							}
					}
				}
			}
			if(e instanceof Base && ((HasOwner) e).getOwner() == this) {
				Base x = (Base)e;
				if(!x.isWorking()) {
					//sets the ai base to make more spacman if possible
					ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
					if (resourceManager.getRocks() > 30) {
						LOGGER.error("ai - set base to make spacman");
						resourceManager.setRocks(resourceManager.getRocks() - 30);
						Spacman r = new Spacman(16, 16, 0);
						r.setOwner(this);
						x.setAction(new GenerateAction(r));							
					}
				}
			}
			if(e instanceof EnemySpacman && ((HasOwner) e).getOwner() == this) {
				//lets the ai target player spacman with it's enemyspacmen
				EnemySpacman x = (EnemySpacman)e;
				for( BaseEntity r : GameManager.get().getWorld().getEntities()) {
					if(r instanceof Spacman && ((HasOwner) r).getOwner() instanceof PlayerManager) {
						x.setAction(new MoveAction(r.getPosX(), r.getPosY(), x));
						break;
					}
				}
			}
		}
	}
}

		@Override
		public void setTeam(int teamid) {
			teamid = teamid;
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
		
	}
