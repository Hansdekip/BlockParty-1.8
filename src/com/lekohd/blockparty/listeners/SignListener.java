package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (p.hasPermission("blockparty.admin")) {
			if (e.getLine(0).equalsIgnoreCase("[BlockParty]")) {
				if (e.getLine(1).equalsIgnoreCase("join")) {
					if (e.getLine(2) != null) {
						if (BlockParty.getArena.containsKey(e.getLine(2))) {
							String arenaName = e.getLine(2);
							e.setLine(0, "§6[BlockParty]");
							e.setLine(1, "§7Arena:");
							e.setLine(2, ChatColor.AQUA + arenaName);
							e.setLine(3, "§2Join");
							BlockParty.signs.put(arenaName, (Sign) e.getBlock().getState());
						} else {
							p.sendMessage(BlockParty.messageManager.ARENA_DOES_NOT_EXIST.replace("$ARENANAME$", e.getLine(1)));
						}
					}
				}
				if (e.getLine(1).equalsIgnoreCase("leave")) {
					e.setLine(0, "§6[BlockParty]");
					e.setLine(1, "§4Leave");
				}
			}
		}
	}
}