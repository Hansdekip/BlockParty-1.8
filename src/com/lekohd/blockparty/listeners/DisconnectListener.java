package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener {
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		if (BlockParty.inGamePlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.inGamePlayers.remove(e.getPlayer().getName());
		}
		if (BlockParty.inLobbyPlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.inLobbyPlayers.remove(e.getPlayer().getName());
		}
		if (BlockParty.onFloorPlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.onFloorPlayers.remove(e.getPlayer().getName());
		}
		BlockParty.inventoriesToRestore.add(e.getPlayer().getPlayer().getName());
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		if (BlockParty.inGamePlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.inGamePlayers.remove(e.getPlayer().getName());
		}
		if (BlockParty.inLobbyPlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.inLobbyPlayers.remove(e.getPlayer().getName());
		}
		if (BlockParty.onFloorPlayers.containsKey(e.getPlayer().getName())) {
			BlockParty.onFloorPlayers.remove(e.getPlayer().getName());
		}
		BlockParty.inventoriesToRestore.add(e.getPlayer().getPlayer().getName());
	}
}