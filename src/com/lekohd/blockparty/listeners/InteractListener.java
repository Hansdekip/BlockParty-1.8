package com.lekohd.blockparty.listeners;

import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.bonus.Bonus;
import com.lekohd.blockparty.bonus.Item;
import com.lekohd.blockparty.music.Vote;
import com.lekohd.blockparty.system.Arena;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {
	/*
	 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
	 */
	public int duration = 10;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR || (e.getAction() == Action.LEFT_CLICK_AIR || (e.getAction() == Action.LEFT_CLICK_BLOCK)))) {
			if (BlockParty.inLobbyPlayers.containsKey(e.getPlayer().getName())) {
				if (e.getPlayer().getItemInHand().getType() == Material.FIREBALL) {
					Vote.openInv(e.getPlayer(), (String) BlockParty.inLobbyPlayers.get(e.getPlayer().getName()));
				}
			}
		}
		if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR)) {
			Player p = e.getPlayer();
			if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
				if (p.getItemInHand().getType() == Material.DIAMOND_BOOTS) {
					Bonus.playEf(p, "walk");
					p.getInventory().remove(Material.DIAMOND_BOOTS);
				}
				if (e.getPlayer().getItemInHand().getType() == Material.GOLD_BOOTS) {
					Bonus.playEf(p, "jump");
					p.getInventory().remove(Material.GOLD_BOOTS);
				}
				if (BlockParty.inLobbyPlayers.containsKey(e.getPlayer().getName())) {
					if (e.getPlayer().getItemInHand().getType() == Material.FIREBALL) {
						Vote.openInv(e.getPlayer(), (String) BlockParty.inLobbyPlayers.get(e.getPlayer().getName()));
					}
				}
			}
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getClickedBlock() == null) {
					return;
				}
				if ((e.getClickedBlock().getType() == Material.SIGN) || (e.getClickedBlock().getType() == Material.SIGN_POST)
						|| (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
					Sign sign = (Sign) e.getClickedBlock().getState();
					if (sign.getLine(0).equals("§6[BlockParty]")) {
						if (sign.getLine(3).equalsIgnoreCase("§2Join")) {
							if (p.hasPermission("blockparty.user")) {
								Arena.join(p, removeColorCodes(sign.getLine(2)));
							} else {
								p.sendMessage(BlockParty.messageManager.NO_PERMISSION);
							}
						}
					}
					if (sign.getLine(0).equalsIgnoreCase("§6[BlockParty]")) {
						if (sign.getLine(1).equalsIgnoreCase("§4Leave")) {
							if (p.hasPermission("blockparty.user")) {
								Arena.leave(p);
							}
						}
					}
				}
			}
		}
		if (e.getClickedBlock() == null) {
			return;
		}
		if (e.getClickedBlock().getType() == Material.BEACON) {
			Player p = e.getPlayer();
			if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
				Item.give(p);
				p.playEffect(e.getClickedBlock().getLocation(), Effect.MOBSPAWNER_FLAMES, 5);
				e.getClickedBlock().setType(Material.AIR);
			}
		}
	}

	public String removeColorCodes(String str) {
		String h = "";
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '§') {
				i++;
			} else {
				h = h + str.charAt(i);
			}
		}
		return h;
	}
}