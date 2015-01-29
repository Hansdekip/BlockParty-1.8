package com.lekohd.blockparty.level;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.bonus.Boosts;
import com.lekohd.blockparty.floor.FloorBlock;
import com.lekohd.blockparty.floor.LoadFloor;
import com.lekohd.blockparty.floor.RandomizeFloor;
import com.lekohd.blockparty.floor.RemoveBlocks;
import com.lekohd.blockparty.music.Songs;
import com.lekohd.blockparty.sign.Signs;
import com.lekohd.blockparty.system.Arena;
import com.lekohd.blockparty.system.Config;
import com.lekohd.blockparty.system.Players;
import com.lekohd.blockparty.system.Start;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Period {
	public static Logger logger;
	public static double number = 16.0D;
	public static int periods = 15;
	public static int period = 0;
	public static double multiplier = 0.5D;
	public static int cd;
	static int num = 6;
	public static int dc;
	public static int cg = 0;
	public static boolean setStart;

	public int getCd() {
		return Period.cd;
	}

	@SuppressWarnings("rawtypes")
	public static void setFloor(String arenaName, boolean fall) {
		String aName = arenaName;
		if ((!((Config) BlockParty.getArena.get(aName)).getAutoGenerateFloors()) && (((Config) BlockParty.getArena.get(aName)).getUseSchematicFloors())) {
			setShFloor(aName);
		}
		if ((((Config) BlockParty.getArena.get(aName)).getAutoGenerateFloors()) && (!((Config) BlockParty.getArena.get(aName)).getUseSchematicFloors())) {
			RandomizeFloor.place(aName);
		}
		if ((((Config) BlockParty.getArena.get(aName)).getAutoGenerateFloors()) && (((Config) BlockParty.getArena.get(aName)).getUseSchematicFloors())) {
			double id = Math.random() * (((ArrayList) BlockParty.getFloor.get(aName)).size() + 1);
			if (fall) {
				setShFloor(aName);
			} else if (id > 1.0D) {
				setShFloor(aName);
			} else {
				RandomizeFloor.place(aName);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void setShFloor(String aName) {
		if (((ArrayList) BlockParty.getFloor.get(aName)).isEmpty()) {
			System.err.println(BlockParty.messageManager.ARENA_FLOOR_ERROR.replace("$ARENANAME$", aName));
		} else if (((ArrayList) BlockParty.getFloor.get(aName)).size() == 1) {
			((LoadFloor) ((ArrayList) BlockParty.getFloor.get(aName)).get(0)).placeFloor(aName);
		} else {
			byte id = (byte) (int) (Math.floor(((Math.random() * ((ArrayList) BlockParty.getFloor.get(aName)).size() * 5) / 5)));
			if (((Config) BlockParty.getArena.get(aName)).getStart()) {
				((LoadFloor) ((ArrayList) BlockParty.getFloor.get(aName)).get(id)).placeFloor(aName);
			}
			if ((!((Config) BlockParty.getArena.get(aName)).getStart()) && (((ArrayList) BlockParty.getFloor.get(aName)).size() > 1)) {
				for (LoadFloor l : BlockParty.getFloor.get(aName)) {
					if (l.getFloorName().equalsIgnoreCase("start")) {
						l.placeFloor(aName);
						((Config) BlockParty.getArena.get(aName)).setStart(true);
					}
				}
				if (!((Config) BlockParty.getArena.get(aName)).getStart()) {
					((LoadFloor) ((ArrayList) BlockParty.getFloor.get(aName)).get(id)).placeFloor(aName);
				}
			}
		}
	}

	public void delayedStart(String arenaName, int c) {
		final String aName = arenaName;
		cg = c;
		num = 6;
		if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
			if (Players.getPlayerAmountOnFloor(aName) != 1) {
				for (String name : Players.getPlayersOnFloor(aName)) {
					Player p = Bukkit.getPlayer(name);
					BarAPI.setMessage(p, BlockParty.messageManager.BAR_WAITING, 100.0F);
				}
			} else {
				BarAPI.setMessage(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)), BlockParty.messageManager.BAR_WAITING, 100.0F);
			}
		}
		dc = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockParty.getInstance(), new Runnable() {
			public void run() {
				if (Period.num != 0) {
					if (Period.num > 1) {
						Period.num -= 1;
					} else {
						Period.this.start(aName, Period.cg, null);
						Bukkit.getScheduler().cancelTask(Period.dc);
					}
				}
			}
		}, 0L, 20L);
	}

	public static void stop(String arenaName) {
		final String aName = arenaName;
		final Boosts b = new Boosts();
		// Bukkit.getScheduler().cancelTasks(BlockParty.instance);
		Bukkit.getScheduler().cancelTask(Period.cd);
		Bukkit.getScheduler().cancelTask(Period.dc);
		Bukkit.getScheduler().cancelTask(WinnerCountdown.cd);
		Bukkit.getScheduler().cancelTask(Start.cd);

		((Config) BlockParty.getArena.get(aName)).setStart(false);
		((Config) BlockParty.getArena.get(aName)).setGameProgress("inLobby");
		Signs.updateGameProgress(aName, true);
		Period.setFloor(aName, true);

		if ((((Config) BlockParty.getArena.get(aName)).getUseBoosts()) && (b != null)) {
			b.remove();
		}

		for (String name : Players.getPlayersOnFloor(aName)) {
			Player p = Bukkit.getPlayer(name);
			p.sendMessage(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_SELF);
			if (Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
				Songs.stop(p);
			}
			giveReward(p, arenaName);
		}

		WinnerCountdown.start(aName);
		if (Players.getPlayerAmountInLobby(aName) >= 1) {
			for (String name : Players.getPlayersInLobby(aName)) {
				Player p = Bukkit.getPlayer(name);
				p.sendMessage(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_ALL.replace("$PLAYER$", (String) Players.getPlayersInGame(aName).get(0)));
			}
		} else if (Players.getPlayerAmountInLobby(aName) != 0) {
			Bukkit.getPlayer((String) Players.getPlayersInLobby(aName).get(0)).sendMessage(
					(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_ALL).replace("$PLAYER$", (String) Players.getPlayersInGame(aName).get(0)));
		}
	}

	@SuppressWarnings("deprecation")
	public void start(String arenaName, int currPeriod, final Boosts bo) {
		final String aName = arenaName;
		final Boosts b = new Boosts();

		// If Arena is disabled go ahead and kick all players in it currently.

		period = currPeriod;
		number = ((Config) BlockParty.getArena.get(arenaName)).getTimeToSearch() + 10;
		multiplier = ((Config) BlockParty.getArena.get(arenaName)).getTimeReductionPerLevel();
		periods = ((Config) BlockParty.getArena.get(arenaName)).getLevel();
		setStart = ((Config) BlockParty.getArena.get(arenaName)).getStart();

		final Block randomBlock = RandomizeFloor.randomizedItem(aName);
		for (String name : Players.getPlayersOnFloor(aName)) {
			Player p = Bukkit.getPlayer(name);
			FloorBlock.givePlayer(p, randomBlock);
			if (Material.STAINED_CLAY == randomBlock.getType() || Material.WOOL == randomBlock.getType()) {
				p.sendMessage(BlockParty.messageManager.PERIOD_NEXT_BLOCK_IS.replace("$BLOCKNAME$",
						FloorBlock.getName(randomBlock.getData(), randomBlock.getType())));
			} else {
				p.sendMessage(BlockParty.messageManager.PERIOD_NEXT_BLOCK_IS.replace("$BLOCKNAME$", randomBlock.getType().name()));
			}
			p.setLevel(period + 1);
		}

		// 18 - 7.5 = 11.5
		number -= period * multiplier;
		final double numb = number;

		// debug - force game to end
		// number = 1.0D;

		if ((period <= periods) && (number > 10.0D)) {
			Period.cd = Bukkit.getScheduler().scheduleSyncRepeatingTask(BlockParty.getInstance(), new Runnable() {
				public void run() {
					if (Period.number != 0.0D) {
						// System.out.print(Period.number);
						if (Period.number > 1.0D) {
							Period.number -= 1.0D;
							if ((Period.number > 10.0D) && (Bukkit.getPluginManager().isPluginEnabled("BarAPI"))) {
								if (Players.getPlayerAmountOnFloor(aName) != 1) {
									for (String name : Players.getPlayersOnFloor(aName)) {
										Player p = Bukkit.getPlayer(name);
										BarAPI.setMessage(p, BlockParty.messageManager.BAR_DANCE, (float) ((Period.number - 10.0D) / (numb - 10.0D) * 100.0D));
									}
								} else {
									BarAPI.setMessage(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)), BlockParty.messageManager.BAR_DANCE,
											(float) ((Period.number - 10.0D) / (numb - 10.0D) * 100.0D));
								}
							}
							if ((Period.number <= 10.0D) && (Period.number > 9.0D)) {
								if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
									if (Players.getPlayerAmountOnFloor(aName) != 1) {
										for (String name : Players.getPlayersOnFloor(aName)) {
											Player p = Bukkit.getPlayer(name);
											BarAPI.setMessage(p, BlockParty.messageManager.BAR_STOP, 5);
										}
									} else {
										BarAPI.setMessage(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)),
												BlockParty.messageManager.BAR_STOP, 5);
									}
								}
								RemoveBlocks.remove(aName, Byte.valueOf(randomBlock.getData()), randomBlock.getType());
								if ((((Config) BlockParty.getArena.get(aName)).getUseBoosts()) && (b != null)) {
									b.remove();
								}
							}
							if ((Period.number <= 5.0D) && (Period.number > 4.0D)) {
								Period.setFloor(aName, false);
								for (String name : Players.getPlayersOnFloor(aName)) {
									Player p = Bukkit.getPlayer(name);
									p.getInventory().clear();
								}
								if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
									if (Players.getPlayerAmountOnFloor(aName) != 1) {
										for (String name : Players.getPlayersOnFloor(aName)) {
											Player p = Bukkit.getPlayer(name);
											BarAPI.setMessage(p, BlockParty.messageManager.BAR_WAITING, 100.0F);
										}
									} else {
										BarAPI.setMessage(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)),
												BlockParty.messageManager.BAR_WAITING, 100.0F);
									}
								}
								if ((((Config) BlockParty.getArena.get(aName)).getUseBoosts())
										&& ((Period.period == 3) || (Period.period == 7) || (Period.period == 11) || (Period.period == 15))) {
									Boosts.place(aName);
									if (Players.getPlayerAmountOnFloor(aName) != 1) {
										for (String name : Players.getPlayersOnFloor(aName)) {
											Player p = Bukkit.getPlayer(name);
											p.sendMessage(BlockParty.messageManager.PERIOD_BOOSTS_SUMMONED);
										}
									}
								}
							}
							if (Players.getPlayerAmountOnFloor(aName) == 1) {
								Bukkit.getScheduler().cancelTask(Period.cd);
								((Config) BlockParty.getArena.get(aName)).setStart(false);
								((Config) BlockParty.getArena.get(aName)).setGameProgress("inLobby");
								Signs.updateGameProgress(aName, true);
								Period.setFloor(aName, true);
								Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)).teleport(Arena.getGameSpawn(aName));
								if (Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
									Songs.stop(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)));
								}
								if ((((Config) BlockParty.getArena.get(aName)).getUseBoosts()) && (b != null)) {
									b.remove();
								}

								Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)).sendMessage(
										BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_SELF);
								Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)).sendMessage(
										BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_REWARD);
								giveReward(Bukkit.getPlayer((String) Players.getPlayersOnFloor(aName).get(0)), aName);

								WinnerCountdown.start(aName);
								Config.broadcastLobby(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_ALL.replace("$PLAYER$", getWinners(aName)), aName);
							}
							// make sure timer stops when arena is empty
							if (Players.getPlayerAmountOnFloor(aName) == 0) {
								Bukkit.getScheduler().cancelTask(Period.cd);
								((Config) BlockParty.getArena.get(aName)).setStart(false);
								((Config) BlockParty.getArena.get(aName)).setGameProgress("inLobby");
								Signs.updateGameProgress(aName, true);
								Period.setFloor(aName, true);
							}
						} else {
							Period.period += 1;
							Bukkit.getScheduler().cancelTask(Period.cd);
							Period.this.start(aName, Period.period, bo);
						}
					} else {
						System.out.println("[BlockParty] ERROR: Period countdown is less than 1 | start number = " + Period.number
								+ ": Auto Restart Initiated.");

						Period.period += 1;
						Bukkit.getScheduler().cancelTask(Period.cd);
						Period.this.start(aName, Period.period, bo);

						// System.out.println("[BlockParty] ERROR: The countdown is less than 1 (period)");
					}
				}
			}, 0L, 20L);
		} else if (Players.getPlayerAmountOnFloor(aName) > 1) {
			((Config) BlockParty.getArena.get(aName)).setStart(false);
			setFloor(aName, true);
			if ((((Config) BlockParty.getArena.get(aName)).getUseBoosts()) && (b != null)) {
				b.remove();
			}
			((Config) BlockParty.getArena.get(aName)).setGameProgress("inLobby");
			Signs.updateGameProgress(aName, true);
			for (String name : Players.getPlayersOnFloor(aName)) {
				Player p = Bukkit.getPlayer(name);
				p.sendMessage(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_SELF);
				if (Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
					Songs.stop(p);
				}
				giveReward(p, arenaName);
			}

			if (Players.getPlayerAmountInLobby(aName) != 1) {
				for (String name : Players.getPlayersInLobby(aName)) {
					Player p = Bukkit.getPlayer(name);
					p.sendMessage(BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_ALL.replace("$PLAYER$", getWinners(aName)));
				}
			} else {
				Bukkit.getPlayer((String) Players.getPlayersInLobby(aName).get(0)).sendMessage(
						BlockParty.messageManager.PERIOD_WINNER_ANNOUNCE_ALL.replace("$PLAYER$", getWinners(aName)));
			}

			WinnerCountdown.start(aName);
		}
	}

	public String getWinners(String arenaName) {
		String names = "";
		for (String name : Players.getPlayersOnFloor(arenaName)) {
			names = names + " " + name;
		}
		return names;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(int id) {
		ItemStack item = new ItemStack(id, 1);
		return item;
	}

	public static int getRandomInt(int lower, int upper) {
		Random random = new Random();
		return random.nextInt((upper - lower)) + lower;
	}

	public static void giveReward(Player p, String arenaName) {
		BlockParty.inventoryManager.restoreInv(p);
		BlockParty.inventoriesToRestore.remove(p.getPlayer().getName());

		int totalRewardsAvail = ((Config) BlockParty.getArena.get(arenaName)).getRewardItems().size();

		if (totalRewardsAvail > 1) {
			p.getInventory()
					.addItem(
							new ItemStack[] { getItem(((Integer) ((Config) BlockParty.getArena.get(arenaName)).getRewardItems().get(
									getRandomInt(0, totalRewardsAvail))).intValue()) });
		} else {
			p.getInventory().addItem(new ItemStack[] { getItem(((Integer) ((Config) BlockParty.getArena.get(arenaName)).getRewardItems().get(0)).intValue()) });
		}

		BlockParty.inventoryManager.storeInv(p, false);
		BlockParty.inventoriesToRestore.add(p.getPlayer().getName());
	}
}