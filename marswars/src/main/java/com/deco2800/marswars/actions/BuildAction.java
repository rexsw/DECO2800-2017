package com.deco2800.marswars.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deco2800.marswars.entities.Base;
import com.deco2800.marswars.entities.BaseEntity;

public class BuildAction implements DecoAction{
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildAction.class);
	private float speedMultiplier = .07f;
	private float progress = 0;
	private float buildingSpeed = 1;
	private boolean completed = false;
	private BaseEntity base;

	public BuildAction(BaseEntity entity) {
		this.base = entity;
		this.buildingSpeed = entity.getSpeed();

	}
	public void doAction() {
		if (progress >= 100) {
			base.setTexture("Draft_Homebase2");
			this.completed = true;
		}
		else if (progress > 50){
			base.setTexture("Draft_Homebase1");
			progress = progress + (buildingSpeed * speedMultiplier);
		}
		else {
			base.setTexture("grass");
			progress = progress + (buildingSpeed * speedMultiplier);
		}
	}

	@Override
	public boolean completed() {
		return completed;
	}

	@Override
	public int actionProgress() {
		LOGGER.error("running "+ this.buildingSpeed * this.speedMultiplier);

		return (int)progress;
	}
}
