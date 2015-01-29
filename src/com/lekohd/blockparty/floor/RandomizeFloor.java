package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class RandomizeFloor {
	public static int xMax;
	public static int yMax;
	public static int zMax;
	public static int xMin;
	public static int yMin;
	public static int zMin;

	@SuppressWarnings("deprecation")
	public static void place(String arenaName) {
		Location locMax = FloorPoints.getMax(arenaName);
		Location locMin = FloorPoints.getMin(arenaName);
		for (int x = getxMin(arenaName, locMax, locMin); x <= getxMax(arenaName, locMax, locMin); x++) {
			for (int z = getzMin(arenaName, locMax, locMin); z <= getzMax(arenaName, locMax, locMin); z++) {
				int y = locMax.getBlockY();

				World world = FloorPoints.getWorld(arenaName);
				if (world != null) {
					world.getBlockAt(x, y, z).setTypeId(159);
					world.getBlockAt(x, y, z).setData(randomizedData(arenaName));
				}
			}
		}
	}

	public static byte randomizedData(String arenaName) {
		byte id = (byte) (int) (Math.random() * 16.0D);
		return id;
	}

	public static int getRandomLoc(int lower, int upper) {
		Random random = new Random();
		return random.nextInt((upper - lower) + 1) + lower;
	}

	public static Block randomizedItem(String arenaName) {
		Location locMax = FloorPoints.getMax(arenaName);
		Location locMin = FloorPoints.getMin(arenaName);
		xMin = getxMin(arenaName, locMax, locMin);
		xMax = getxMax(arenaName, locMax, locMin);
		zMin = getzMin(arenaName, locMax, locMin);
		zMax = getzMax(arenaName, locMax, locMin);

		int x = getRandomLoc(xMin, xMax);
		int y = locMax.getBlockY();
		int z = getRandomLoc(zMin, zMax);

		World world = FloorPoints.getWorld(arenaName);
		//System.out.print(x + " - " + y + " - " + z + " - " + world.getBlockAt(x, y, z).getType().toString());
		return world.getBlockAt(x, y, z);
	}

	public static int getxMax(String arenaName, Location locMax, Location locMin) {
		if (locMax.getBlockX() < locMin.getBlockX()) {
			return locMin.getBlockX();
		}
		return locMax.getBlockX();
	}

	public static int getyMax(String arenaName, Location locMax, Location locMin) {
		if (locMax.getBlockY() < locMin.getBlockY()) {
			return locMin.getBlockY();
		}
		return locMax.getBlockY();
	}

	public static int getzMax(String arenaName, Location locMax, Location locMin) {
		if (locMax.getBlockZ() < locMin.getBlockZ()) {
			return locMin.getBlockZ();
		}
		return locMax.getBlockZ();
	}

	public static int getxMin(String arenaName, Location locMax, Location locMin) {
		if (locMin.getBlockX() < locMax.getBlockX()) {
			return locMin.getBlockX();
		}
		return locMax.getBlockX();
	}

	public static int getyMin(String arenaName, Location locMax, Location locMin) {
		if (locMin.getBlockY() < locMax.getBlockY()) {
			return locMin.getBlockY();
		}
		return locMax.getBlockY();
	}

	public static int getzMin(String arenaName, Location locMax, Location locMin) {
		if (locMin.getBlockZ() < locMax.getBlockZ()) {
			return locMin.getBlockZ();
		}
		return locMax.getBlockZ();
	}
}