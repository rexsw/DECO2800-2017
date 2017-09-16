package com.deco2800.marswars.entities.items;

public enum SpecialType {

    // radius, magnitude, duration (0 is instant), cost(rcwb), level, type
    AOEHEAL1 (3, 25, 0, new int[]{0, 10, 30, 30}, 1, "Heal"),
    AOEHEAL2 (4, 50, 0, new int[]{10, 20, 50, 50}, 2, "Heal"),
    AOEHEAL3 (5, 75, 0, new int[]{20, 30, 100, 100}, 3, "Heal"),
    AOEDAMAGE1 (3, -25, 0, new int[]{0, 10, 30, 30}, 1, "Damage"),
    AOEDAMAGE2 (4, -50, 0, new int[]{10, 20, 50, 50}, 2, "Damage"),
    AOEDAMAGE3 (5, -75, 0, new int[]{20, 30, 100, 100}, 3, "Damage"),
    AOESPEED1 (3, 25, 0, new int[]{20, 20, 0, 0}, 1, "Speed"),
    AOESPEED2 (4, 50, 0, new int[]{40, 40, 10, 10}, 2, "Speed"),
    AOESPEED3 (5, 75, 0, new int[]{60, 60, 30, 30}, 3, "Speed");

    private int radius;
    private int magnitude;
    private int duration;
    private int[] cost;
    private int level;
    private String type;

    SpecialType(int radius, int magnitude,  int duration, int[] cost, int
            level, String type) {
        this.radius = radius;
        this.magnitude = magnitude;
        this.duration = duration;
        this.cost = cost;
        this.level = level;
        this.type = type;
    }

    int getRadius() { return radius; }

    int getMagnitude() {return magnitude; }

    int  getDuration() { return duration; }

    int getLevel() { return level; }

    int[] getCost() { return cost; }

    String getType() { return type; }
}
