package com.lekohd.blockparty.bonus;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bonus {
	public static int dc;
	public static int duration = 10;

	public static void playEf(final Player p, String ef) {
		duration = ((Config) BlockParty.getArena.get(BlockParty.onFloorPlayers.get(p.getName()))).getBoostDuration() + 1;
		if (duration > 0) {
			if (ef.equalsIgnoreCase("walk")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * 20, 2));
				p.sendMessage(BlockParty.messageManager.EFFECTS_WALKING);
			}
			if (ef.equalsIgnoreCase("jump")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration * 20, 3));
				p.sendMessage(BlockParty.messageManager.EFFECTS_JUMPING);
			}
			if (ef.equalsIgnoreCase("nausea")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration * 20, 1));
				p.sendMessage(BlockParty.messageManager.EFFECTS_NAUSEA);
				return;
			}
			if (ef.equalsIgnoreCase("blindness")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration * 20, 1));
				p.sendMessage(BlockParty.messageManager.EFFECTS_BLINDNESS);
				return;
			}
			dc = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockParty.getInstance(), new Runnable() {
				public void run() {
					if (Bonus.duration != 0) {
						if (Bonus.duration > 1) {
							Bonus.duration -= 1;
						} else if (Bonus.duration > -1) {
							p.sendMessage(BlockParty.messageManager.EFFECTS_EXPIRED);
							p.getInventory().remove(Material.DIAMOND_BOOTS);
							p.getInventory().remove(Material.GOLD_BOOTS);
							Bukkit.getScheduler().cancelTask(Bonus.dc);
						} else {
							Bukkit.getScheduler().cancelTask(Bonus.dc);
						}
					}
				}
			}, 0L, 20L);
		}
	}
}