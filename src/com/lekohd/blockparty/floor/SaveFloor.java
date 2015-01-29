package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SaveFloor {
	public static void setFloor(Player p, String arenaName) {
		File arena = new File("plugins//BlockParty//Arenas//" + arenaName + ".yml");
		if (!arena.exists()) {
			p.sendMessage(BlockParty.messageManager.ARENA_DOES_NOT_EXIST.replace("$ARENANAME$", arenaName));
		} else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(arena);
			if (checkConditions(p, arenaName)) {
				try {
					cfg.save(arena);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean checkConditions(Player p, String arenaName) {
		if (BlockParty.getArena.get(arenaName) != null) {
			if (((Config) BlockParty.getArena.get(arenaName)).checkConditions(p)) {
				return true;
			}
			return false;
		}
		return false;
	}
}