package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;

@SuppressWarnings("deprecation")
public class LoadFloor {
	private Vector size;
	public CuboidClipboard sh = null;
	public String floorName;

	public LoadFloor(String fn) {
		this.floorName = fn;
		loadSchematic();
	}

	public void loadSchematic() {
		String formatName = null;

		File f = new File("plugins//BlockParty//Floors//" + this.floorName + ".schematic");
		if (!f.exists()) {
			return;
		}
		SchematicFormat format = formatName == null ? null : SchematicFormat.getFormat(formatName);
		if (format == null) {
			format = SchematicFormat.getFormat(f);
		}
		if (format == null) {
			return;
		}
		try {
			this.size = format.load(f).getSize();
			this.sh = format.load(f);
		} catch (DataException localDataException) {
		} catch (IOException localIOException) {
		}
	}

	public void placeFloor(String arenaName) {
		if (this.sh == null) {
			loadSchematic();

			System.out.println("sh = null");
		} else {
			this.size = this.sh.getSize();

			place(this.sh.getSize(), this.sh, arenaName);
		}
	}

	public String getFloorName() {
		return this.floorName;
	}

	public Vector getSize() {
		return this.size;
	}

	public void place(Vector size, CuboidClipboard cc, String arenaName) {
		for (int x = 0; x < size.getBlockX(); x++) {
			for (int z = 0; z < size.getBlockZ(); z++) {
				World world = Bukkit.getWorld(((Config) BlockParty.getArena.get(arenaName)).getWorld().getName());
				world.getBlockAt(((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockX() + x,
						((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockY(),
						((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockZ() + z).setTypeId(cc.getBlock(new Vector(x, 0, z)).getId());
				world.getBlockAt(((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockX() + x,
						((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockY(),
						((Config) BlockParty.getArena.get(arenaName)).getLocMin().getBlockZ() + z).setData((byte) cc.getBlock(new Vector(x, 0, z)).getData());
			}
		}
	}
}