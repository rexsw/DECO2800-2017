package com.deco2800.marswars.util;

import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.List;

/**
 * A pretty basic (and crapola) method of threading the pathfinding
 */
public class PathfindingThread implements Runnable {

	private BaseWorld world;
	private Point position;
	private Point goal;

	private List<Point> path;

	/**
	 * Constructor for the pathfinding thread
	 * @param world the world where to search.
	 * @param position
	 * @param goal
	 */
	public PathfindingThread(BaseWorld world, Point position, Point goal) {
		this.world = world;
		this.position = position;
		this.goal = goal;
	}

	/**
	 * Runs the thread
	 */
	@Override
	public void run() {
		if (GameManager.get().getMainMenu().gameStarted()){
			//System.out.println("I RUN");
			this.path = Pathfinder.aStar(position, goal, world);
		} else{
			//System.out.println("no run");
		}
	}

	/**
	 * Returns the path (only if the thread has completed
	 * @return
	 */
	public List<Point> getPath() {
		return path;
	}
}
