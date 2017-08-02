package com.deco2800.marswars.util;

import com.deco2800.marswars.World;
import com.deco2800.marswars.entities.BaseEntity;

import java.util.*;

public class Pathfinder {
	public static List<Point> aStar(Point start, Point goal, World world) {
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
				if (path.size() > 0) {
					//Replace last node with untruncated goal
					path.remove(path.size() - 1);
					path.add(goal);
				}
				return path;
			}
			openSet.remove(current);
			closedSet.add(current);

			for (Point p : getAdjacentNodes(current, world)) {
				List<BaseEntity> entities = world.getEntities((int)p.getX(), (int)p.getY());

				int cost = 0;
				for (BaseEntity e : entities) {
						cost += e.getCost();
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

	private static Point getMinPoint(Set<Point> Points, Map<Point, Double> fScores) {
		double minF = Integer.MAX_VALUE;
		Point min = null;

		for (Point Point : Points) {
			double PointFScore =
					fScores.getOrDefault(Point, Double.MAX_VALUE / 2);
			if (PointFScore < minF) {
				minF = PointFScore;
				min = Point;
			}
		}

		return min;
	}

	private static double heuristicCostEstimate(Point node, Point goal) {
		return Math.pow(node.getX() - goal.getX(), 2) +  Math.pow(node.getY() - goal.getY(), 2);
	}

	private static List<Point> getAdjacentNodes(Point p, World world) {
		List<Point> adjacencies = new ArrayList<>();

		// Up
		if (p.getX() >= 0 && p.getX() < world.getWidth() && p.getY() - 1 >= 0 && p.getY() - 1 < world.getLength()) {
			adjacencies.add(new Point(p.getX(), p.getY() - 1));
		}

		// Down
		if (p.getX() >= 0 && p.getX() < world.getWidth() && p.getY() + 1 >= 0 && p.getY() + 1 < world.getLength()) {
			adjacencies.add(new Point(p.getX(), p.getY() + 1));
		}

		// Left
		if (p.getX() - 1 >= 0 && p.getX() - 1 < world.getWidth() && p.getY() >= 0 && p.getY() < world.getLength()) {
			adjacencies.add(new Point(p.getX() - 1, p.getY()));
		}

		// Right
		if (p.getX() + 1 >= 0 && p.getX() + 1 < world.getWidth() && p.getY() >= 0 && p.getY() < world.getLength()) {
			adjacencies.add(new Point(p.getX() + 1, p.getY()));
		}

		return adjacencies;
	}

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