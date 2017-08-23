package com.deco2800.marswars.managers;

import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.GenerateAction;
import com.deco2800.marswars.actions.MoveAction;
	import com.deco2800.marswars.entities.AbstractEntity;
import com.deco2800.marswars.entities.Base;
import com.deco2800.marswars.entities.BaseEntity;
	import com.deco2800.marswars.entities.EnemySpacman;
import com.deco2800.marswars.entities.HasOwner;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.entities.Rock;
import com.deco2800.marswars.entities.Spacman;
	import com.deco2800.marswars.util.WorldUtil;

	import java.util.List;
import java.util.Optional;

public class AiManagerTest extends Manager implements TickableManager, HasTeam {
		private int teamid;

		@Override
		public void onTick(long l) {
			for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
				if(e instanceof HasOwner) {
				if(e instanceof Spacman && ((HasOwner) e).getOwner() == this) {
					Spacman x = (Spacman)e;
					if(!x.isWorking()) {
						for( BaseEntity r : GameManager.get().getWorld().getEntities())
						if(r instanceof Resource) {
							if(((Resource) r).testResource() == "rock") {
							x.setAction(new GatherAction(x, r));
							break;
							}
						}
					}
				}
				if(e instanceof Base && ((HasOwner) e).getOwner() == this) {
					Base x = (Base)e;
					if(!x.isWorking()) {
						ResourceManager resourceManager = (ResourceManager) GameManager.get().getManager(ResourceManager.class);
						if (resourceManager.getRocks() > 30) {
							resourceManager.setRocks(resourceManager.getRocks() - 30);
							Spacman r = new Spacman(16, 16, 0);
							r.setOwner(this);
							x.setAction(new GenerateAction(r));							
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
			if(otherMember instanceof HasTeam) {
				return this.teamid == ((HasTeam) otherMember).getTeam();
			} else {
				return false;
			}
		}
		
	}
