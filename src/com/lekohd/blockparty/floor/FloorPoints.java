package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import org.bukkit.Location;
import org.bukkit.World;

public class FloorPoints {
	public static Location getMin(String arenaName) {
		return ((Config) BlockParty.getArena.get(arenaName)).getLocMin();
	}

	public static Location getMax(String arenaName) {
		return ((Config) BlockParty.getArena.get(arenaName)).getLocMax();
	}

	public static void set(Location min, Location max, String arenaName) {
		((Config) BlockParty.getArena.get(arenaName)).set(min, max);
	}

	public static World getWorld(String arenaName) {
		return ((Config) BlockParty.getArena.get(arenaName)).getWorld();
	}
}