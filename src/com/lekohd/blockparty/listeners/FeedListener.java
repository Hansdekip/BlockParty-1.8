package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FeedListener implements Listener {
	@EventHandler
	public void onFeed(FoodLevelChangeEvent e) {
		if (BlockParty.onFloorPlayers.containsKey(e.getEntity().getName())) {
			e.setFoodLevel(20);
		}
		if (BlockParty.inLobbyPlayers.containsKey(e.getEntity().getName())) {
			e.setFoodLevel(20);
		}
	}
}