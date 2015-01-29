package com.lekohd.blockparty.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;


import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Arena;
import com.lekohd.blockparty.system.Config;
import com.lekohd.blockparty.system.Players;
import com.lekohd.blockparty.system.Start;

import me.confuser.barapi.BarAPI;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class WinnerCountdown {
	public static int cd;
	public static double number = 6.0D;

	public static void start(final String arenaName) {
		number = 6.0D;
		cd = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockParty.getInstance(), new Runnable() {
			public void run() {
				if (WinnerCountdown.number != 0.0D) {
					if (WinnerCountdown.number != 1.0D) {
						WinnerCountdown.number -= 1.0D;
						if (BlockParty.getArena.get(arenaName) != null) {
							if (BlockParty.getArena.get(arenaName).getFireworksEnabled()) {
								shootFireworks(arenaName);
							}
						}
					} else {
						Bukkit.getScheduler().cancelTask(WinnerCountdown.cd);
						// Since we are working with an array we dont need to
						// verify if there is 1 or 50

						try {
							for (String playerName : Players.getPlayersOnFloor(arenaName)) {
								System.out.print(BlockParty.messageManager.CONSOLE_OUTPUT_WINNER.replace("$PLAYER", playerName).replace("$ARENANAME$",
										arenaName));

								BlockParty.onFloorPlayers.remove(playerName);
								BlockParty.inLobbyPlayers.put(playerName, arenaName);

								Player p = Bukkit.getPlayer(playerName);
								p.teleport(Arena.getLobbySpawn(arenaName));

								p.getInventory().clear();
								p.getInventory().addItem(new ItemStack[] { ((Config) BlockParty.getArena.get(arenaName)).getVoteItem() });
								p.updateInventory();

								if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
									BarAPI.setMessage(p, BlockParty.messageManager.BAR_WAITING, 100.0F);
								}
							}

						} catch (Exception ex) {
							System.err.println("[BlockParty] " + ex.getMessage());
							// Continue
						}

						if (BlockParty.getArena.get(arenaName) != null)
							if (BlockParty.getArena.get(arenaName).isEnabled) {
								BlockParty.getArena.get(arenaName).setGameProgress("inLobby");
								if (BlockParty.getArena.get(arenaName).getAutoKick()) {
									WinnerCountdown.kickPlayers(arenaName);
								} else if (BlockParty.getArena.get(arenaName).getAutoRestart()) {
									Start.start(arenaName);
								}
							}
					}
				} else {
					System.err.println("[BlockParty] ERROR: The winner countdown is less than 1. Auto Cleanup Run: Fixing Timers!");
					Bukkit.getScheduler().cancelTask(WinnerCountdown.cd);
					if (BlockParty.getArena.get(arenaName).isEnabled) {
						BlockParty.getArena.get(arenaName).setGameProgress("inLobby");
						if (BlockParty.getArena.get(arenaName).getAutoKick()) {
							WinnerCountdown.kickPlayers(arenaName);
						} else if (BlockParty.getArena.get(arenaName).getAutoRestart()) {
							Start.start(arenaName);
						}
					}
				}
			}
		}, 0L, 20L);
	}

	public static void kickPlayers(String arenaName) {
		ArrayList<String> itr = Players.getPlayersInGame(arenaName);
        List<String> list =  new CopyOnWriteArrayList<String>();		
		for (int i=0;i<itr.size();i++) {
			list.add(itr.get(i));
		}
		
		Iterator<String> it = list.iterator();
        while(it.hasNext()) {
            String name = it.next();
            Player p = Bukkit.getPlayer(name);
			//System.out.print(p.getName());
			Arena.leave(p);
			//p.sendMessage(BlockParty.messageManager.STOP_GAME_FORCED);
        }
	}

	public static void shootFireworks(String arenaName) {
		// Spawn the Firework, get the FireworkMeta.
		Firework fw = (Firework) BlockParty.getArena.get(arenaName).getWorld()
				.spawnEntity(BlockParty.getArena.get(arenaName).getGameSpawn(), EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		// Our random generator
		Random r = new Random();

		// Get the type
		int rt = r.nextInt(4) + 1;
		Type type = Type.BALL;
		if (rt == 1)
			type = Type.BALL;
		if (rt == 2)
			type = Type.BALL_LARGE;
		if (rt == 3)
			type = Type.BURST;
		if (rt == 4)
			type = Type.CREEPER;
		if (rt == 5)
			type = Type.STAR;

		// Get our random colours
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);

		// Create our effect with this
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

		// Then apply the effect to the meta
		fwm.addEffect(effect);

		// Generate some random power and set it
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);

		// Then apply this to our rocket
		fw.setFireworkMeta(fwm);

	}

	public static Color getColor(int c) {
		switch (c) {
		default:
		case 1:
			return Color.AQUA;
		case 2:
			return Color.BLACK;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.FUCHSIA;
		case 5:
			return Color.GRAY;
		case 6:
			return Color.GREEN;
		case 7:
			return Color.LIME;
		case 8:
			return Color.MAROON;
		case 9:
			return Color.NAVY;
		case 10:
			return Color.OLIVE;
		case 11:
			return Color.ORANGE;
		case 12:
			return Color.PURPLE;
		case 13:
			return Color.RED;
		case 14:
			return Color.SILVER;
		case 15:
			return Color.TEAL;
		case 16:
			return Color.WHITE;
		case 17:
			return Color.YELLOW;
		}
	}

}