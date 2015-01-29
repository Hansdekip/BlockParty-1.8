package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
			e.setCancelled(true);
		}
		if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
			e.setCancelled(true);
		}
	}
}