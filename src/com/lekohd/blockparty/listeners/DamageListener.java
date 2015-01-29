package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent e) {
		if ((e.getEntity() instanceof Player)) {
			Player p = (Player) e.getEntity();
			if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
				e.setCancelled(true);
			}
			if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
				e.setCancelled(true);
			}
		}
	}
}