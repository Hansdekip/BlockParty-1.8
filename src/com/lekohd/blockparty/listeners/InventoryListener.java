package com.lekohd.blockparty.listeners;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.music.Vote;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
	@EventHandler
	public void onInventoryClick(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
			event.setCancelled(true);
		}
		if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getSlot() == e.getRawSlot()) {
			Player p = (Player) e.getWhoClicked();
			if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
				e.setCancelled(true);
				p.updateInventory();
				try {
					ItemStack item = e.getCurrentItem();
					if (item != null) {
						String song = ChatColor.stripColor(item.getItemMeta().getDisplayName());
						Vote.voteFor(song, (String) BlockParty.inLobbyPlayers.get(p.getName()));
						p.sendMessage(BlockParty.messageManager.VOTE_FOR_SONG.replace("$SONG$", song));
						p.getInventory().remove(Material.FIREBALL);
						Vote.closeInv(p);
					}
				} catch (Exception ex) {
					System.out.print(ex.getMessage());
				}
			}

			if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// upon reboot inventory will be written to file in order to restore
		if (BlockParty.inventoryManager.restoreFromFile(event.getPlayer())) {
			// System.out.print("[BlockParty] Restored Player Inventory");
			BlockParty.inventoriesToRestore.remove(event.getPlayer().getName());
		}
	}
}