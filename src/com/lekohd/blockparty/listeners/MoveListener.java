package com.lekohd.blockparty.listeners;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.music.Songs;
import com.lekohd.blockparty.system.Arena;
import com.lekohd.blockparty.system.Config;
import com.lekohd.blockparty.system.Players;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MoveListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (BlockParty.onFloorPlayers.containsKey(e.getPlayer().getName())) {
			Location loc = e.getTo();
			loc.setY(e.getTo().getBlockY() - 1);
			try {
				if (loc.getBlock().getTypeId() == (BlockParty.getArena.get(BlockParty.onFloorPlayers.get(e.getPlayer().getName()))).getOutBlock()) {
					if (Players.getPlayerAmountOnFloor((String) BlockParty.onFloorPlayers.get(e.getPlayer().getName())) > 1) {
						if (Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
							Songs.stop(e.getPlayer());
						}
						e.getPlayer().getInventory().clear();
						e.getPlayer()
								.getInventory()
								.addItem(
										new ItemStack[] { ((Config) BlockParty.getArena.get(BlockParty.onFloorPlayers.get(e.getPlayer().getName())))
												.getVoteItem() });
						e.getPlayer().updateInventory();
						BlockParty.inLobbyPlayers.put(e.getPlayer().getName(), (String) BlockParty.onFloorPlayers.get(e.getPlayer().getName()));
						World world = e.getPlayer().getWorld();
						world.strikeLightning(e.getPlayer().getLocation());
						Config.broadcastInGame(BlockParty.messageManager.PERIOD_ELIMINATED.replace("$PLAYER$", e.getPlayer().getName()), Config.arenaName);

						// e.getPlayer().sendMessage("§3[BlockParty] §8You were §4ELIMINATED");
						e.getPlayer().teleport(Arena.getLobbySpawn((String) BlockParty.onFloorPlayers.get(e.getPlayer().getName())));
						BlockParty.onFloorPlayers.remove(e.getPlayer().getName());
						if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
							BarAPI.setMessage(e.getPlayer(), BlockParty.messageManager.BAR_WAITING, 100.0F);
						}
					}
				}
			} catch (Exception ex) {
				Config.broadcastInGame(BlockParty.messageManager.PERIOD_ELIMINATED.replace("$PLAYER$", e.getPlayer().getName()), Config.arenaName);

				// e.getPlayer().sendMessage("§3[BlockParty] §8You were §4ELIMINATED");
				if (BlockParty.onFloorPlayers.containsKey(e.getPlayer().getName())) {
					BlockParty.onFloorPlayers.remove(e.getPlayer().getName());
				}

				BlockParty.inLobbyPlayers.put(e.getPlayer().getName(), Config.arenaName);
				Arena.leave(e.getPlayer());
			}
		}
	}
}