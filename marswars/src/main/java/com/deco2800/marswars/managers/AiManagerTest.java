package com.deco2800.marswars.managers;

	import com.deco2800.marswars.actions.GatherAction;
import com.deco2800.marswars.actions.MoveAction;
	import com.deco2800.marswars.entities.AbstractEntity;
	import com.deco2800.marswars.entities.BaseEntity;
	import com.deco2800.marswars.entities.EnemySpacman;
import com.deco2800.marswars.entities.HasOnwer;
import com.deco2800.marswars.entities.Resource;
import com.deco2800.marswars.entities.Spacman;
	import com.deco2800.marswars.util.WorldUtil;

	import java.util.List;
import java.util.Optional;

	public class AiManagerTest extends Manager implements TickableManager, HasTeam {
		private int teamid;

		@Override
		public void onTick(long l) {
			for( BaseEntity e : GameManager.get().getWorld().getEntities()) {
				if(e instanceof HasOnwer) {
				if(e instanceof Spacman && ((HasOnwer) e).getOnwer() == this) {
					Spacman x = (Spacman)e;
					if(!x.isWorking()) {
						for( BaseEntity r : GameManager.get().getWorld().getEntities())
						if(r instanceof Resource) {
							x.setAction(new GatherAction(x, r));
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
			if(otherMember instanceof HasTeam) {
				return this.teamid == ((HasTeam) otherMember).getTeam();
			} else {
				return false;
			}
		}
		
	}
