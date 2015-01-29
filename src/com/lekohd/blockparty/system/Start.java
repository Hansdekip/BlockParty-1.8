package com.lekohd.blockparty.system;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import java.util.logging.Logger;

import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.level.Period;
import com.lekohd.blockparty.music.Songs;
import com.lekohd.blockparty.sign.Signs;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Start {
	public static Logger logger;
	public static int number = 31;
	public static int cd;

	public static void start(String arenaName) {
		if (!((Config) BlockParty.getArena.get(arenaName)).lessThanMinimum()) {
			if (((Config) BlockParty.getArena.get(arenaName)).getGameProgress().equalsIgnoreCase("inLobby")) {
				if (((Config) BlockParty.getArena.get(arenaName)).timerResetOnPlayerJoin) {
					Bukkit.getScheduler().cancelTask(cd);
				}
				countdown(arenaName);
			} else {
				// Reset progress here as its glitched.
				Bukkit.getScheduler().cancelTask(cd);
				countdown(arenaName);
				// BlockParty.logger.info("Game Progress is NOT inLobby");
			}
		}
	}

	public static void level(String arenaName, int lev) {
		for (String name : Players.getPlayersInLobby(arenaName)) {
			Player p = Bukkit.getPlayer(name);
			p.setLevel(lev);
		}
	}

	public static void startGame(String arenaName, Player sender) {
		if (BlockParty.getArena.get(arenaName) != null) {
			if ((Players.getPlayerAmountInLobby(arenaName) == 0) && (sender != null)) {
				sender.sendMessage(BlockParty.messageManager.START_ERROR_ZERO_PAYERS);
			} else {
				String song = ((Config) BlockParty.getArena.get(arenaName)).getMostVotedSong();
				((Config) BlockParty.getArena.get(arenaName)).votedSongs.clear();

				for (String name : Players.getPlayersInLobby(arenaName)) {
					Player p = Bukkit.getPlayer(name);
					p.teleport(Arena.getGameSpawn(arenaName));
					p.getInventory().clear();
					p.updateInventory();
					BlockParty.inLobbyPlayers.remove(p.getName());
					BlockParty.onFloorPlayers.put(name, arenaName);
					p.sendMessage(BlockParty.messageManager.START_SUCCESS);
					if ((Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) && (((Config) BlockParty.getArena.get(arenaName)).getUseSongs())) {
						Songs.stop(p);
						Songs.play(p, song);
					}
					Bukkit.getScheduler().cancelTask(cd);
				}
				((Config) BlockParty.getArena.get(arenaName)).setGameProgress("inGame");
				Signs.updateGameProgress(arenaName, false);
				((Config) BlockParty.getArena.get(arenaName)).setStart(false);
				((Config) BlockParty.getArena.get(arenaName)).unAbort();
				Period pe = new Period();
				Period.setFloor(arenaName, true);
				pe.delayedStart(arenaName, 0);
			}
		}
	}

	public static void telOutOfArena(String arenaName) {
		for (String name : Players.getPlayersInLobby(arenaName)) {
			Player p = Bukkit.getPlayer(name);
			BlockParty.inLobbyPlayers.remove(p.getName());
			if (BlockParty.locs.containsKey(p.getName())) {
				p.teleport((Location) BlockParty.locs.get(p.getName()));
				BlockParty.locs.remove(p.getName());
			} else {
				p.sendMessage(BlockParty.messageManager.LEAVE_LOCATION_NOT_FOUND);
			}
		}
	}

	public static void countdown(final String arenaName) {
		if (((Config) BlockParty.getArena.get(arenaName)).getGameProgress().equalsIgnoreCase("Countdown")) {
			return;
		}

		((Config) BlockParty.getArena.get(arenaName)).setGameProgress("Countdown");
		String aName = arenaName;
		number = ((Config) BlockParty.getArena.get(arenaName)).getCountdown();
		level(aName, number);
		cd = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockParty.getInstance(), new Runnable() {
			public void run() {
				if (Start.number != 0) {
					if (Start.number != 1) {
						Start.number -= 1;

						if (BlockParty.getArena.get(arenaName) == null) {
							// Remove all users in games and end them.
							//
							for (String name : Players.getPlayersInLobby(arenaName)) {
								Player p = Bukkit.getPlayer(name);
								Arena.leave(p);
								p.sendMessage(BlockParty.messageManager.STOP_GAME_FORCED);
							}

							Bukkit.getScheduler().cancelTask(Start.cd);
						} else {
							if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
								if (Players.getPlayerAmountInLobby(arenaName) != 1) {
									for (String name : Players.getPlayersInLobby(arenaName)) {
										Player p = Bukkit.getPlayer(name);
										BarAPI.setMessage(p, BlockParty.messageManager.BAR_STARTS_SOON, Start.number * 10.0F / 3.0F);
									}
								} else {
									BarAPI.setMessage(Bukkit.getPlayer((String) Players.getPlayersInLobby(arenaName).get(0)), "Game starts soon",
											Start.number * 10.0F / 3.0F);
								}
							}
							if ((Start.number == 20) || (Start.number == 30)) {
								Config.broadcastLobby(BlockParty.messageManager.BAR_TIMER.replace("$TIMER$", "" + Start.number), arenaName);
							}
							Start.level(arenaName, Start.number);
							if (Start.number < 11) {
								Config.broadcastLobby(BlockParty.messageManager.BAR_TIMER.replace("$TIMER$", "" + Start.number), arenaName);
							}
							if (((Config) BlockParty.getArena.get(arenaName)).lessThanMinimum()) {
								Config.broadcastLobby(BlockParty.messageManager.START_ERROR_LESS_THEN_MIN_PLAYERS, arenaName);
								for (String name : Players.getPlayersInLobby(arenaName)) {
									Player p = Bukkit.getPlayer(name);
									if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
										BarAPI.setMessage(p, BlockParty.messageManager.BAR_WAITING, 100.0F);
									}
								}

								((Config) BlockParty.getArena.get(arenaName)).setGameProgress("inLobby");
								start(arenaName);
								Bukkit.getScheduler().cancelTask(Start.cd);
							}
							if (((Config) BlockParty.getArena.get(arenaName)).aborted()) {
								Bukkit.getScheduler().cancelTask(Start.cd);
								((Config) BlockParty.getArena.get(arenaName)).unAbort();
								((Config) BlockParty.getArena.get(arenaName)).setGameProgress("inLobby");
								startGame(arenaName, null);
							}
						}
					} else {
						Bukkit.getScheduler().cancelTask(Start.cd);
						startGame(arenaName, null);
					}
				} else {
					Bukkit.getScheduler().cancelTask(Start.cd);
					startGame(arenaName, null);
					System.err.println("[BlockParty] ERROR: Start countdown is less than 1 | start number = " + Start.number);
				}
			}
		}, 0L, 20L);
	}

}