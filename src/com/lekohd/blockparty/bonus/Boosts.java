package com.lekohd.blockparty.bonus;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Boosts {
	public static Block b;

	public static void place(String arenaName) {
		Random r = new Random();
		int x = r.nextInt(((Config) BlockParty.getArena.get(arenaName)).getFloorLength());
		int z = r.nextInt(((Config) BlockParty.getArena.get(arenaName)).getFloorWidth());
		int y = ((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockY() + 1;
		World world = ((Config) BlockParty.getArena.get(arenaName)).getWorld();
		b = world.getBlockAt(((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockX() + x, y, ((Config) BlockParty.getArena.get(arenaName))
				.getLocMin().getBlockZ() + z);
		b.setType(Material.BEACON);
	}

	public void remove() {
		if (b != null) {
			b.setType(Material.AIR);
		}
	}
}