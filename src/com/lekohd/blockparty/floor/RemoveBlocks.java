package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class RemoveBlocks {
	@SuppressWarnings("deprecation")
	public static void remove(String arenaName, Byte value, Material material) {
		Location locMax = FloorPoints.getMax(arenaName);
		Location locMin = FloorPoints.getMin(arenaName);
		boolean fall = ((Config) BlockParty.getArena.get(arenaName)).getFallingBlocks();
		for (int x = RandomizeFloor.getxMin(arenaName, locMax, locMin); x <= RandomizeFloor.getxMax(arenaName, locMax, locMin); x++) {
			for (int z = RandomizeFloor.getzMin(arenaName, locMax, locMin); z <= RandomizeFloor.getzMax(arenaName, locMax, locMin); z++) {
				int y = RandomizeFloor.getyMax(arenaName, locMax, locMin);

				World world = FloorPoints.getWorld(arenaName);
				if (world != null) {
					if (world.getBlockAt(x, y, z).getData() != (value.byteValue()) || world.getBlockAt(x, y, z).getType() != material) {
						Location location = new Location(world, x, y, z);
						Material ma = world.getBlockAt(x, y, z).getType();
						world.getBlockAt(x, y, z).setType(Material.AIR);
						Byte blockData = Byte.valueOf(world.getBlockAt(x, y, z).getData());
						if (fall) {
							world.spawnFallingBlock(location, ma, blockData.byteValue()).setFallDistance(7.0F);
						}
					}
				}
			}
		}
	}
}