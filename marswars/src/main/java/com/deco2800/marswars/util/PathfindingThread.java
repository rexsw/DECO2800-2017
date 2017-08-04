package com.deco2800.marswars.util;

import com.deco2800.marswars.worlds.BaseWorld;

import java.util.List;

/**
 * Created by timhadwen on 4/8/17.
 */
public class PathfindingThread implements Runnable {

	BaseWorld world;
	Point position;
	Point goal;

	List<Point> path;

	public PathfindingThread(BaseWorld world, Point position, Point goal) {
		this.world = world;
		this.position = position;
		this.goal = goal;
	}

	@Override
	public void run() {
		this.path = Pathfinder.aStar(position, goal, world);
	}

	public List<Point> getPath() {
		return path;
	}
}
