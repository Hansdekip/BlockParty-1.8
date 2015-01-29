package com.lekohd.blockparty.listeners;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class ChangeBlockListener implements Listener {
	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntityType() == EntityType.FALLING_BLOCK) {
			FallingBlock fallingBlock = (FallingBlock) event.getEntity();
			if (fallingBlock.getMaterial() == Material.STAINED_CLAY) {
				event.setCancelled(true);
			}
			if (fallingBlock.getMaterial() == Material.WOOL) {
				event.setCancelled(true);
			}
		}
	}
}