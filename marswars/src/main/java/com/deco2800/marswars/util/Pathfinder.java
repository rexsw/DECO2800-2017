package com.deco2800.marswars.util;

import com.deco2800.marswars.buildings.BuildingEntity;
import com.deco2800.marswars.entities.BaseEntity;
import com.deco2800.marswars.entities.terrainelements.Obstacle;
import com.deco2800.marswars.entities.units.AttackableEntity;
import com.deco2800.marswars.managers.GameManager;
import com.deco2800.marswars.worlds.BaseWorld;

import java.util.*;

public class Pathfinder {
	/**
	 * Runs an A* Pathfinding algorithimn from start to goal on the baseWorld.
	 * This code uses a lot of run time so its a good idea to multithread its use
	 *
	 * @param start the starting coordinate
	 * @param goal the end coordinate
	 * @param baseWorld the world where the search is run.
	 * @return a path from start to goal
	 */
	
	public static List<Point> aStar(Point start, Point goal, BaseWorld baseWorld) {
		AttackableEntity actor = null;
		if (0 <= (int)goal.getX() && 0 <= (int)goal.getY() && (int)goal.getX() < baseWorld.getWidth() && (int)goal.getY() < baseWorld.getLength()) {
			List<BaseEntity> entitiesAtStart = GameManager.get().getWorld().getEntities((int)goal.getX(), (int)goal.getY());
			for (BaseEntity b: entitiesAtStart) {
				if (b instanceof AttackableEntity) {
					actor = (AttackableEntity)b;
				}
			}
		}
		//Truncate points to the tile grid
		Point truncGoal = new Point((int)goal.getX(), (int)goal.getY());

		// Set of points already evaluated
		Set<Point> closedSet = new HashSet<>();

		// Set of discovered nodes yet to be evaluated
		Set<Point> openSet = new HashSet<>();
		openSet.add(start);

		// Most efficient path to a node
		Map<Point, Point> cameFrom = new HashMap<>();

		// Cost of getting to node
		Map<Point, Integer> gScore = new HashMap<>();

		gScore.put(start, 0);

		/*
		 * For each node, the total cost of getting from the start node to the
		 * goal by passing by that node. That value is partly known, partly
		 * heuristic.
		 */
		Map<Point, Double> fScore = new HashMap<>();

		fScore.put(start, heuristicCostEstimate(start, truncGoal));

		while (!openSet.isEmpty()) {
			Point current = getMinPoint(openSet, fScore);
			Point truncCurrent = new Point((int)current.getX(), (int)current.getY());
			if (truncCurrent.equals(truncGoal)) {
				List<Point> path = reconstructPath(cameFrom, current);
				if (!path.isEmpty()) {
					//Replace last node with untruncated goal
					path.remove(path.size() - 1);
					path.add(goal);
				}
				return path;
			}
			openSet.remove(current);
			closedSet.add(current);

			for (Point p : getAdjacentNodes(current, baseWorld, actor)) {
				List<BaseEntity> entities = new ArrayList<BaseEntity>(baseWorld.getEntities((int)p.getX(), (int)p.getY()));

				int cost = 0;
				for (BaseEntity e : entities) {
					if (e != null) {
						cost += e.getCost();
					}
				}

				if (closedSet.contains(p)) {
					continue;
				}

				// gScore default value is infinite, distance from current to
				int tentativeGScore = gScore.getOrDefault(current, Integer.MAX_VALUE / 2) + cost;

				if (!openSet.contains(p)) {
					openSet.add(p);
				} else if (tentativeGScore >= gScore.getOrDefault(p, Integer.MAX_VALUE / 2)) {
					continue;
				}

				// This path is the best so far
				cameFrom.put(p, current);
				gScore.put(p, tentativeGScore);
				fScore.put(p, gScore.get(p) + heuristicCostEstimate(p, truncGoal));
			}
		}
		return null;
	}

	/**
	 * Gets the minimum point in a set of points
	 * @param points a set of points
	 * @param fScores
	 * @return
	 */
	private static Point getMinPoint(Set<Point> points, Map<Point, Double> fScores) {
		double minF = Integer.MAX_VALUE;
		Point min = null;

		for (Point Point : points) {
			double pointFScore =
					fScores.getOrDefault(Point, Double.MAX_VALUE / 2);
			if (pointFScore < minF) {
				minF = pointFScore;
				min = Point;
			}
		}

		return min;
	}

	/**
	 * Estimates a heursitic
	 * @param node
	 * @param goal
	 * @return a cost estimate.
	 */
	private static double heuristicCostEstimate(Point node, Point goal) {
		return Math.pow(node.getX() - goal.getX(), 2) +  Math.pow(node.getY() - goal.getY(), 2);
	}

	/**
	 * Gets the adjacent nodes in the BaseWorld.
	 *
	 * @param p middle point.
	 * @param baseWorld the world that contains all adjacent nodes.
	 * @return  a list of adjacent node points.
	 */
	private static List<Point> getAdjacentNodes(Point p, BaseWorld baseWorld, AttackableEntity actor) {
		List<Point> adjacencies = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j<= 1; j++) {
				if (!(i == 0 && j == 0)) {
					if (p.getX() + i >= 0 && p.getX() + i < baseWorld.getWidth() && p.getY() + j >= 0 && p.getY() + j < baseWorld.getLength()){
						List<BaseEntity> checkWall = baseWorld.getEntities((int)p.getX() + i, (int)p.getY() + j);
						boolean add = true;
						for (BaseEntity b : checkWall) {
							if (b instanceof BuildingEntity) {
								if(((BuildingEntity) b).getbuilding() == "Wall") {
									add = false;
								}else if (((BuildingEntity) b).getbuilding() == "Gate" && actor != null) {
									if (!(actor.sameOwner((BuildingEntity)b))) {
										add = false;
									}
								}
							}else if (b instanceof Obstacle) {
								add = false;
							}
						}
						if (add)
							adjacencies.add(new Point(p.getX() + i, p.getY() + j));
					}
				}
			}
		}

		/*// Up
		if (p.getX() >= 0 && p.getX() < baseWorld.getWidth() && p.getY() - 1 >= 0 && p.getY() - 1 < baseWorld.getLength()) {
			adjacencies.add(new Point(p.getX(), p.getY() - 1));
		}

		// Down
		if (p.getX() >= 0 && p.getX() < baseWorld.getWidth() && p.getY() + 1 >= 0 && p.getY() + 1 < baseWorld.getLength()) {
			adjacencies.add(new Point(p.getX(), p.getY() + 1));
		}

		// Left
		if (p.getX() - 1 >= 0 && p.getX() - 1 < baseWorld.getWidth() && p.getY() >= 0 && p.getY() < baseWorld.getLength()) {
			adjacencies.add(new Point(p.getX() - 1, p.getY()));
		}

		// Right
		if (p.getX() + 1 >= 0 && p.getX() + 1 < baseWorld.getWidth() && p.getY() >= 0 && p.getY() < baseWorld.getLength()) {
			adjacencies.add(new Point(p.getX() + 1, p.getY()));
		}*/

		return adjacencies;
	}
	
	
	
	

	/**
	 * Reconstructs a path from point to point
	 * @param cameFrom initial point.
	 * @param current current point
	 * @return the list of points that make up the path.
	 */
	private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
		List<Point> result = new LinkedList<>();

		while (cameFrom.containsKey(current)) {
			result.add(current);
			current = cameFrom.get(current);
		}
		Collections.reverse(result);
		return result;
	}
}