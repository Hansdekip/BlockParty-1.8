package com.lekohd.blockparty.music;

import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Vote {
	/*
	 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void openInv(Player p, String arenaName) {
		int lines = 1;
		if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 9) {
			lines = 1;
		} else if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 18) {
			lines = 2;
		} else if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 27) {
			lines = 3;
		} else if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 36) {
			lines = 4;
		} else if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 45) {
			lines = 5;
		} else if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() <= 54) {
			lines = 6;
		}
		Inventory inv = Bukkit.createInventory(null, lines * 9, "Vote for a song!");
		if (((Config) BlockParty.getArena.get(arenaName)).getSongs().size() == 1) {
			ItemStack item = new ItemStack(randomRecord(), 1);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName((String) ((Config) BlockParty.getArena.get(arenaName)).getSongs().get(0));
			List lores = new ArrayList();
			lores.add(BlockParty.messageManager.VOTE_ITEM_FIREBALL_LORE);
			meta.setLore(lores);
			item.setItemMeta(meta);
			inv.setItem(0, item);
		} else {
			for (int i = 0; i < ((Config) BlockParty.getArena.get(arenaName)).getSongs().size(); i++) {
				ItemStack item = new ItemStack(randomRecord(), 1);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName((String) ((Config) BlockParty.getArena.get(arenaName)).getSongs().get(i));
				List lores = new ArrayList();
				lores.add(BlockParty.messageManager.VOTE_ITEM_FIREBALL_LORE);
				meta.setLore(lores);
				item.setItemMeta(meta);
				inv.setItem(i, item);
			}
		}
		p.openInventory(inv);
	}

	public static void closeInv(Player p) {
		p.closeInventory();
	}

	public static Material randomRecord() {
		Random r = new Random();
		int t = r.nextInt(10) + 1;
		switch (t) {
		case 1:
		default:
			return Material.RECORD_10;
		case 2:
			return Material.RECORD_11;
		case 3:
			return Material.RECORD_12;
		case 4:
			return Material.RECORD_3;
		case 5:
			return Material.RECORD_4;
		case 6:
			return Material.RECORD_5;
		case 7:
			return Material.RECORD_6;
		case 8:
			return Material.RECORD_7;
		case 9:
			return Material.RECORD_8;
		}
	}

	public static void voteFor(String song, String arenaName) {
		((Config) BlockParty.getArena.get(arenaName)).vote(song);
	}
}