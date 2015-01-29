package com.lekohd.blockparty.system;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.floor.FloorPoints;
import com.lekohd.blockparty.level.Period;
import com.lekohd.blockparty.level.WinnerCountdown;
import com.lekohd.blockparty.music.Songs;
import com.lekohd.blockparty.sign.Signs;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import me.confuser.barapi.BarAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Config {
	public File arena;
	public static String arenaName;
	public FileConfiguration cfg;
	public Location gameSpawn;
	public Location lobbySpawn;
	public Location locMax;
	public Location locMin;
	public int lessMinimum;
	public World world;
	public int floorLength;
	public int floorWidth;
	public int minPlayers;
	public int maxPlayers;
	public int countdown;
	public int timeToSearch;
	public int level;
	public int boostDuration;
	public double timeReductionPerLevel;
	public boolean autoGenerateFloors;
	public boolean useSchematicFloors;
	public boolean isEnabled = false;
	public boolean useBoosts = true;
	public boolean fallingBlock = true;
	public boolean useSongs = true;
	public boolean autoRestart = true;
	public boolean autoKick = false;
	public boolean fireworksEnabled = true;
	public boolean timerResetOnPlayerJoin = true;
	public boolean allowJoinDuringGame = true;
	public ArrayList<String> enabledFloors = new ArrayList<String>();
	public ArrayList<Integer> rewardItems = new ArrayList<Integer>();
	public ArrayList<String> songs = new ArrayList<String>();
	public int outBlock;
	public HashMap<String, Integer> votedSongs = new HashMap<String, Integer>();

	public String getMostVotedSong() {
		String song = null;
		int ri;
		if (this.votedSongs.size() == 0) {
			Random r = new Random();
			ri = r.nextInt(this.songs.size());
			return (String) this.songs.get(ri);
		}
		if (this.votedSongs.size() == 1) {
			if (this.songs.size() > 1) {
				for (String name : this.songs) {
					if (this.votedSongs.containsKey(name)) {
						return name;
					}
				}
			} else {
				return (String) this.songs.get(0);
			}
		} else if (this.votedSongs.size() > 1) {
			for (String name : this.songs) {
				if (this.votedSongs.containsKey(name)) {
					if (song == null) {
						song = name;
					} else if (((Integer) this.votedSongs.get(name)).intValue() > ((Integer) this.votedSongs.get(song)).intValue()) {
						song = name;
					}
				}
			}
			return song;
		}
		return null;
	}

	public void vote(String song) {
		int value = 0;
		if (this.votedSongs.containsKey(song)) {
			value = ((Integer) this.votedSongs.get(song)).intValue();
			this.votedSongs.put(song, Integer.valueOf(value + 1));
		} else {
			this.votedSongs.put(song, Integer.valueOf(1));
		}
	}

	public Location getGameSpawn() {
		return this.gameSpawn;
	}

	public Location getLobbySpawn() {
		return this.lobbySpawn;
	}

	public Config(String aName) {
		Config.arenaName = aName;
		this.arena = new File("plugins//BlockParty//Arenas//" + Config.arenaName + ".yml");
	}

	public void loadCfg() {
		if (!this.arena.exists()) {
			System.err.print("[BlockParty] ERROR : Arena " + Config.arenaName + " doesn't exists!");
		} else {
			this.cfg = YamlConfiguration.loadConfiguration(this.arena);
		}
	}

	public String create() {
		if (this.arena.exists()) {
			return (BlockParty.messageManager.ARENA_EXISTS_ALREADY).replace("$ARENANAME$", Config.arenaName);
		}
		this.cfg = YamlConfiguration.loadConfiguration(this.arena);
		this.cfg.set("configuration.MinPlayers", Integer.valueOf(2));
		this.cfg.set("configuration.MaxPlayers", Integer.valueOf(15));
		this.cfg.set("configuration.Countdown", Integer.valueOf(30));
		this.cfg.set("configuration.AutoRestart", Boolean.valueOf(true));
		this.cfg.set("configuration.AutoKick", Boolean.valueOf(false));
		this.cfg.set("configuration.OutBlock", Integer.valueOf(7));
		this.cfg.set("configuration.TimeToSearch", Integer.valueOf(8));
		this.cfg.set("configuration.TimeReductionPerLevel", Double.valueOf(0.5D));
		this.cfg.set("configuration.Level", Integer.valueOf(15));
		this.cfg.set("configuration.UseBoosts", Boolean.valueOf(true));
		this.cfg.set("configuration.BoostDuration", Integer.valueOf(10));
		this.cfg.set("configuration.FallingBlocks", Boolean.valueOf(true));
		this.cfg.set("configuration.AutoGenerateFloors", Boolean.valueOf(true));
		this.cfg.set("configuration.UseSchematicFloors", Boolean.valueOf(true));
		this.enabledFloors.add("example");
		this.cfg.set("configuration.EnabledFloors", this.enabledFloors);
		this.rewardItems.add(Integer.valueOf(264));
		this.cfg.set("configuration.RewardItems", this.rewardItems);
		this.cfg.set("configuration.UseSongs", Boolean.valueOf(true));
		this.songs.add("Hold The Line");
		this.cfg.set("configuration.Songs", this.songs);
		this.cfg.set("dontChange.Game", Boolean.valueOf(false));
		this.cfg.set("dontChange.Lobby", Boolean.valueOf(false));
		this.cfg.set("floor.floorPoints", Boolean.valueOf(false));
		this.cfg.set("configuration.EnableFireworksOnWon", Boolean.valueOf(true));
		this.cfg.set("configuration.TimerResetOnPlayerJoin", Boolean.valueOf(true));
		this.cfg.set("configuration.AllowJoinDuringGame", Boolean.valueOf(true));

		try {
			this.cfg.save(this.arena);
		} catch (IOException e) {
			e.printStackTrace();
		}
		load();
		return (BlockParty.messageManager.ARENA_CREATED).replace("$ARENANAME$", Config.arenaName);
	}

	public String setSpawn(Player p, String pos) {
		if (this.isEnabled) {
			if (!this.arena.exists()) {
				return (BlockParty.messageManager.ARENA_DOES_NOT_EXIST).replace("$ARENANAME$", Config.arenaName);
			}
			Location loc = p.getLocation();
			this.cfg.set("spawns." + pos + ".xPos", Double.valueOf(loc.getX()));
			this.cfg.set("spawns." + pos + ".yPos", Double.valueOf(loc.getY()));
			this.cfg.set("spawns." + pos + ".zPos", Double.valueOf(loc.getZ()));
			this.cfg.set("spawns." + pos + ".Yaw", Float.valueOf(loc.getYaw()));
			this.cfg.set("spawns." + pos + ".Pitch", Float.valueOf(loc.getPitch()));
			this.cfg.set("spawns." + pos + ".World", loc.getWorld().getName());
			if (pos.equalsIgnoreCase("Lobby")) {
				if (this.cfg.getBoolean("dontChange.Game")) {
					this.cfg.set("dontChange." + pos, Boolean.valueOf(true));
					this.cfg.set("dontChange.Game", Boolean.valueOf(true));
				} else {
					this.cfg.set("dontChange." + pos, Boolean.valueOf(true));
					this.cfg.set("dontChange.Game", Boolean.valueOf(false));
				}
				this.lobbySpawn = loc;
				this.world = Bukkit.getWorld(this.cfg.getString("spawns.Lobby.World"));
			} else {
				if (this.cfg.getBoolean("dontChange.Lobby")) {
					this.cfg.set("dontChange." + pos, Boolean.valueOf(true));
					this.cfg.set("dontChange.Lobby", Boolean.valueOf(true));
				} else {
					this.cfg.set("dontChange." + pos, Boolean.valueOf(true));
					this.cfg.set("dontChange.Lobby", Boolean.valueOf(false));
				}
				this.gameSpawn = loc;
				this.world = Bukkit.getWorld(this.cfg.getString("spawns.Game.World"));
			}
			this.cfg.set("dontChange." + pos, Boolean.valueOf(true));
			try {
				this.cfg.save(this.arena);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "§3[BlockParty] §8" + pos + " Spawn was set for Arena " + Config.arenaName;
		}
		return (BlockParty.messageManager.ARENA_DISABLED.replace("$ARENANAME$", Config.arenaName));
	}

	public String delete() {
		if (this.arena.exists()) {
			this.arena.delete();
			return (BlockParty.messageManager.ARENA_DELETED).replace("$ARENANAME$", Config.arenaName);
		}
		return (BlockParty.messageManager.ARENA_DOES_NOT_EXIST).replace("$ARENANAME$", Config.arenaName);
	}

	public boolean exists(Player p) {
		if (this.arena.exists()) {
			if ((this.cfg.getBoolean("dontChange.Lobby")) && (this.cfg.getBoolean("dontChange.Game"))) {
				if (this.cfg.getBoolean("floor.floorPoints")) {
					return true;
				}
				p.sendMessage(BlockParty.messageManager.ARENA_FLOOR_ERROR);
				return false;
			}
			p.sendMessage(BlockParty.messageManager.ARENA_SPAWN_ERROR);
			return false;
		}
		p.sendMessage(BlockParty.messageManager.ARENA_DOES_NOT_EXIST.replace("$ARENANAME$", arenaName));
		return false;
	}

	public void loadGameSpawn() {
		if ((this.isEnabled) && (this.arena.exists())) {
			if (this.cfg.getBoolean("dontChange.Game")) {
				try {
					this.cfg.load(this.arena);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
				double xPos = ((Double) this.cfg.get("spawns.Game.xPos")).doubleValue();
				double yPos = ((Double) this.cfg.get("spawns.Game.yPos")).doubleValue();
				double zPos = ((Double) this.cfg.get("spawns.Game.zPos")).doubleValue();
				double yaw = ((Double) this.cfg.get("spawns.Game.Yaw")).doubleValue();
				double pitch = ((Double) this.cfg.get("spawns.Game.Pitch")).doubleValue();
				String w = (String) this.cfg.get("spawns.Game.World");
				Location loc = new Location(Bukkit.getWorld(w), xPos, yPos, zPos, (float) yaw, (float) pitch);
				this.world = Bukkit.getWorld(this.cfg.getString("spawns.Game.World"));
				this.gameSpawn = loc;
			} else {
				this.gameSpawn = null;
			}
		}
	}

	public void loadLobbySpawn() {
		if ((this.isEnabled) && (this.arena.exists())) {
			if (this.cfg.getBoolean("dontChange.Lobby")) {
				try {
					this.cfg.load(this.arena);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {
					e.printStackTrace();
				}
				double xPos = ((Double) this.cfg.get("spawns.Lobby.xPos")).doubleValue();
				double yPos = ((Double) this.cfg.get("spawns.Lobby.yPos")).doubleValue();
				double zPos = ((Double) this.cfg.get("spawns.Lobby.zPos")).doubleValue();
				double yaw = ((Double) this.cfg.get("spawns.Lobby.Yaw")).doubleValue();
				double pitch = ((Double) this.cfg.get("spawns.Lobby.Pitch")).doubleValue();
				String w = (String) this.cfg.get("spawns.Lobby.World");
				Location loc = new Location(Bukkit.getWorld(w), xPos, yPos, zPos, (float) yaw, (float) pitch);
				this.world = Bukkit.getWorld(this.cfg.getString("spawns.Game.World"));
				this.lobbySpawn = loc;
			} else {
				this.lobbySpawn = null;
			}
		}
	}

	public ItemStack getVoteItem() {
		ItemStack item = new ItemStack(Material.FIREBALL, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(BlockParty.messageManager.VOTE_ITEM_FIREBALL_DISPLAY_NAME);
		List<String> lores = new ArrayList<String>();
		lores.add(BlockParty.messageManager.VOTE_ITEM_FIREBALL_LORE);
		meta.setLore(lores);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getItem(int id) {
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(id, 1);
		return item;
	}

	public static void leave(Player p) {
		// if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
		// p.sendMessage(BlockParty.messageManager.LEAVE_CANNOT);
		// return;
		// }

		if (!BlockParty.inGamePlayers.containsKey(p.getName())) {
			BlockParty.inventoryManager.restoreInv(p);
			BlockParty.inventoriesToRestore.remove(p.getPlayer().getName());

			p.sendMessage(BlockParty.messageManager.LEAVE_NOT_IN_ARENA);
			return;
		}

		// Remove player from lobby since they left
		BlockParty.inLobbyPlayers.remove(p.getName());
		BlockParty.inGamePlayers.remove(p.getName());
		BlockParty.onFloorPlayers.remove(p.getName());

		broadcastInGame(BlockParty.messageManager.LEAVE_ARENA_BROADCAST.replace("$PLAYER$", p.getName()), (String) BlockParty.inGamePlayers.get(p.getName()));

		if (BlockParty.getArena.get(arenaName) != null) {
			if ((Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) && (((Config) BlockParty.getArena.get(arenaName)).getUseSongs())) {
				Songs.stop(p);
			}
		} else {
			if ((Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI"))) {
				Songs.stop(p);
			}

		}

		p.teleport((Location) BlockParty.locs.get(p.getName()));
		BlockParty.locs.remove(p.getName());

		// Due to 1.8 glitch we have to force gamemode or they cant break blocks
		// when they leave arena.
		p.setGameMode(GameMode.SURVIVAL);
		// p.setGameMode((GameMode) BlockParty.gm.get(p.getName()));
		BlockParty.gm.remove(p.getName());

		BlockParty.inventoryManager.restoreInv(p);
		BlockParty.inventoriesToRestore.remove(p.getPlayer().getName());

		if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
			BarAPI.removeBar(p);
		}

		p.sendMessage(BlockParty.messageManager.LEAVE_ARENA_PLAYER);
		return;
	}

	public void join(Player p) {
		if (this.isEnabled) {
			if (!BlockParty.inGamePlayers.containsKey(p.getName())) {
				if (exists(p)) {
					if (!Players.reachedMaxPlayers(Config.arenaName)) {
						if ((allowJoinDuringGame == false && (Players.getPlayerAmountOnFloor(arenaName) <= 1)) || allowJoinDuringGame) {
							// Save Player Info
							BlockParty.locs.put(p.getName(), p.getLocation());
							BlockParty.gm.put(p.getName(), p.getGameMode());

							// Reset game mode so they cannot fly
							p.setGameMode(GameMode.ADVENTURE);

							// TP to arena
							p.teleport(this.lobbySpawn);

							// notify everyone someone joined
							broadcastInGame(BlockParty.messageManager.JOIN_SUCCESS_BROADCAST.replace("$PLAYER$", p.getName()), Config.arenaName);

							// Add to game at this point
							BlockParty.inGamePlayers.put(p.getName(), Config.arenaName);
							BlockParty.inLobbyPlayers.put(p.getName(), Config.arenaName);

							// Archive Inventory
							BlockParty.inventoryManager.storeInv(p, true);
							BlockParty.inventoriesToRestore.add(p.getPlayer().getName());
							p.getInventory().clear();
							p.getInventory().addItem(new ItemStack[] { getVoteItem() });
							p.updateInventory();

							// Play music while they wait :D
							String song = ((Config) BlockParty.getArena.get(arenaName)).getMostVotedSong();
							if ((Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) && (((Config) BlockParty.getArena.get(arenaName)).getUseSongs())) {
								Songs.stop(p);
								Songs.play(p, song);
							}

							if (Bukkit.getPluginManager().isPluginEnabled("BarAPI")) {
								BarAPI.setMessage(p, BlockParty.messageManager.BAR_WAITING, 100.0F);
							}

							// Allow players to watch while game in progress
							if (((Config) BlockParty.getArena.get(arenaName)).getGameProgress().equalsIgnoreCase("inLobby")) {
								Start.start(Config.arenaName);
								Signs.updateJoin(Config.arenaName, false);
							} else {
								// Something broke if this ever happens :\
								if (Players.getPlayerAmountOnFloor(arenaName) == 0) {
									((Config) BlockParty.getArena.get(arenaName)).setStart(false);
									((Config) BlockParty.getArena.get(arenaName)).setGameProgress("inLobby");

									Start.start(Config.arenaName);
									Signs.updateJoin(Config.arenaName, false);
								}
							}

							p.sendMessage(BlockParty.messageManager.JOIN_SUCCESS_PLAYER.replace("$ARENANAME$", Config.arenaName));

						} else {
							p.sendMessage(BlockParty.messageManager.JOIN_ERROR_FULL.replace("$ARENANAME$", Config.arenaName));
						}
					} else {
						Signs.updateJoin(Config.arenaName, true);
						p.sendMessage(BlockParty.messageManager.JOIN_ERROR_FULL.replace("$ARENANAME$", Config.arenaName));
					}
				}
			} else {
				p.sendMessage(BlockParty.messageManager.JOIN_ERROR_FULL.replace("$ARENANAME$", Config.arenaName));
			}
		} else {
			p.sendMessage(BlockParty.messageManager.JOIN_ARENA_IS_DISABLED.replace("$ARENANAME$", Config.arenaName));
		}
	}

	public static void stopGameInProgress(String arenaName, boolean inGame) {
		// Remove all users in games and end them.
		ArrayList<String> tempPlayers;

		tempPlayers = Players.getPlayersInLobby(arenaName);
		for (String name : tempPlayers) {
			Player p = Bukkit.getPlayer(name);
			Arena.leave(p);
			p.sendMessage(BlockParty.messageManager.STOP_GAME_FORCED);
		}

		tempPlayers = Players.getPlayersInGame(arenaName);

		for (String name : tempPlayers) {
			Player p = Bukkit.getPlayer(name);
			Arena.leave(p);
			p.sendMessage(BlockParty.messageManager.STOP_GAME_FORCED);
		}

		tempPlayers = Players.getPlayersOnFloor(arenaName);
		for (String name : tempPlayers) {
			Player p = Bukkit.getPlayer(name);
			Arena.leave(p);
			p.sendMessage(BlockParty.messageManager.STOP_GAME_FORCED);
		}

		try {
			Bukkit.getScheduler().cancelTask(Period.cd);
		} catch (Exception ex) {
		}
		try {
			Bukkit.getScheduler().cancelTask(Period.dc);
		} catch (Exception ex) {
		}

		try {
			Bukkit.getScheduler().cancelTask(Start.cd);
		} catch (Exception ex) {
		}

		try {
			Bukkit.getScheduler().cancelTask(WinnerCountdown.cd);
		} catch (Exception ex) {
		}
	}

	@SuppressWarnings("null")
	public ArrayList<String> loadFloors() {
		ArrayList<String> response = null;
		File dir = new File("plugins//BlockParty//floors//", Config.arenaName);

		if (!dir.exists()) {
			dir.mkdir();
			return null;
		}
		// System.out.print("[BlockParty] Loading Inventories To Restore");

		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".schematic")) {
				response.add(f.getName().substring(0, f.getName().indexOf(".")));
				// inventoriesToRestore.add(f.getName().substring(0,
				// f.getName().indexOf(".")));
				// System.out.print("[BlockParty] Inv. to Restore = " +
				// f.getName().substring(0, f.getName().indexOf(".")));
			}
		}
		return response;
	}

	public boolean lessThanMinimum() {
		if (Players.getPlayerAmountInLobby(Config.arenaName) >= this.lessMinimum) {
			return false;
		}
		return true;
	}

	public void loadMinPlayers() {
		if ((this.isEnabled) && (this.arena.exists())) {
			try {
				this.cfg.load(this.arena);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			this.lessMinimum = this.cfg.getInt("configuration.MinPlayers");
		}
	}

	public void loadMin() {
		if (this.isEnabled) {
			if (!this.arena.exists()) {
				System.out.println(BlockParty.messageManager.ARENA_DOES_NOT_EXIST.replace("$ARENANAME$", Config.arenaName));
			} else if (this.cfg.getBoolean("floor.floorPoints")) {
				World world = Bukkit.getWorld("floor.min.World");
				double xPos = ((Double) this.cfg.get("floor.min.xPos")).doubleValue();
				double yPos = ((Double) this.cfg.get("floor.min.yPos")).doubleValue();
				double zPos = ((Double) this.cfg.get("floor.min.zPos")).doubleValue();
				Location loc = new Location(world, xPos, yPos, zPos);
				this.locMin = loc;
			}
		}
	}

	public void loadMax() {
		if (this.isEnabled) {
			if (!this.arena.exists()) {
				System.out.println(BlockParty.messageManager.ARENA_DOES_NOT_EXIST.replace("$ARENANAME$", Config.arenaName));
			} else if (this.cfg.getBoolean("floor.floorPoints")) {
				World world = Bukkit.getWorld("floor.max.World");
				double xPos = ((Double) this.cfg.get("floor.max.xPos")).doubleValue();
				double yPos = ((Double) this.cfg.get("floor.max.yPos")).doubleValue();
				double zPos = ((Double) this.cfg.get("floor.max.zPos")).doubleValue();
				Location loc = new Location(world, xPos, yPos, zPos);
				this.locMax = loc;
			}
		}
	}

	public void set(Location min, Location max) {
		this.cfg.set("floor.min.xPos", Double.valueOf(min.getX()));
		this.cfg.set("floor.min.yPos", Double.valueOf(min.getY()));
		this.cfg.set("floor.min.zPos", Double.valueOf(min.getZ()));
		this.cfg.set("floor.min.World", min.getWorld().getName());

		this.cfg.set("floor.max.xPos", Double.valueOf(max.getX()));
		this.cfg.set("floor.max.yPos", Double.valueOf(max.getY()));
		this.cfg.set("floor.max.zPos", Double.valueOf(max.getZ()));
		this.cfg.set("floor.max.World", max.getWorld().getName());

		this.cfg.set("floor.floorPoints", Boolean.valueOf(true));
		try {
			this.cfg.save(this.arena);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadMax();
		loadMin();
	}

	public Location getLocMin() {
		return this.locMin;
	}

	public Location getLocMax() {
		return this.locMax;
	}

	public World getWorld() {
		return this.world;
	}

	public boolean checkConditions(Player p) {
		WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		Selection selection = worldEdit.getSelection(p);
		if (selection != null) {
			World world = selection.getWorld();
			if (selection.getHeight() == 1) {
				if (world.equals(getWorld())) {
					Location min = selection.getMinimumPoint();
					Location max = selection.getMaximumPoint();
					FloorPoints.set(min, max, Config.arenaName);
					this.cfg.set("configuration.floor.length", Integer.valueOf(selection.getLength()));
					this.cfg.set("configuration.floor.width", Integer.valueOf(selection.getWidth()));
					this.floorLength = selection.getLength();
					this.floorWidth = selection.getWidth();

					p.sendMessage(BlockParty.messageManager.SETUP_FLOOR_SET.replace("$ARENANAME$", Config.arenaName));
					return true;
				}
				p.sendMessage(BlockParty.messageManager.SETUP_FLOOR_ERROR_SAME_WORLD);
			} else {
				p.sendMessage(BlockParty.messageManager.SETUP_FLOOR_ERROR_MIN_HEIGHT);
			}
		} else {
			p.sendMessage(BlockParty.messageManager.SETUP_FLOOR_ERROR_WORLD_EDIT_SELECT);
		}
		return false;
	}

	public static void broadcastLobby(String mes, String arenaName) {
		for (String name : Players.getPlayersInLobby(arenaName)) {
			Player p = Bukkit.getPlayer(name);
			p.sendMessage(mes);
		}
	}

	public static void broadcastFloor(String mes, String arenaName) {
		for (String name : Players.getPlayersOnFloor(arenaName)) {
			Player p = Bukkit.getPlayer(name);
			p.sendMessage(mes);
		}
	}

	public static void broadcastInGame(String mes, String arenaName) {
		for (String name : Players.getPlayersInGame(arenaName)) {
			Player p = Bukkit.getPlayer(name);
			p.sendMessage(mes);
		}
	}

	@SuppressWarnings("unchecked")
	public void load() {
		if ((this.isEnabled) && (this.arena.exists())) {
			try {
				this.cfg.load(this.arena);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
			this.enabledFloors.clear();
			this.rewardItems.clear();
			this.floorLength = this.cfg.getInt("configuration.floor.length");
			this.floorWidth = this.cfg.getInt("configuration.floor.width");
			this.minPlayers = this.cfg.getInt("configuration.MinPlayers");
			this.maxPlayers = this.cfg.getInt("configuration.MaxPlayers");
			this.countdown = this.cfg.getInt("configuration.Countdown");
			this.outBlock = this.cfg.getInt("configuration.OutBlock");
			this.timeToSearch = this.cfg.getInt("configuration.TimeToSearch");
			this.timeReductionPerLevel = this.cfg.getDouble("configuration.TimeReductionPerLevel");
			this.level = this.cfg.getInt("configuration.Level");
			this.useBoosts = this.cfg.getBoolean("configuration.UseBoosts");
			this.boostDuration = this.cfg.getInt("configuration.BoostDuration");
			this.fallingBlock = this.cfg.getBoolean("configuration.FallingBlocks");
			this.autoGenerateFloors = this.cfg.getBoolean("configuration.AutoGenerateFloors");
			this.useSchematicFloors = this.cfg.getBoolean("configuration.UseSchematicFloors");
			this.enabledFloors = ((ArrayList<String>) this.cfg.get("configuration.EnabledFloors"));
			// this.enabledFloors = ((ArrayList<String>) loadFloors());
			this.rewardItems = ((ArrayList<Integer>) this.cfg.get("configuration.RewardItems"));
			this.songs = ((ArrayList<String>) this.cfg.get("configuration.Songs"));
			this.cfg.set("configuration.useSongs", Boolean.valueOf(true));
			this.useSongs = this.cfg.getBoolean("configuration.UseSongs");
			this.autoRestart = this.cfg.getBoolean("configuration.AutoRestart");
			this.autoKick = this.cfg.getBoolean("configuration.AutoKick");
			this.fireworksEnabled = this.cfg.getBoolean("configuration.EnableFireworksOnWon");
			this.timerResetOnPlayerJoin = this.cfg.getBoolean("configuration.TimerResetOnPlayerJoin");
			this.allowJoinDuringGame = this.cfg.getBoolean("configuration.AllowJoinDuringGame");

			if (this.cfg.getString("spawns.Game.World") != null) {
				this.world = Bukkit.getWorld(this.cfg.getString("spawns.Game.World"));
			}
		}
	}

	public boolean getUseSongs() {
		return this.useSongs;
	}

	public boolean getAutoRestart() {
		return this.autoRestart;
	}

	public boolean getAutoKick() {
		return this.autoKick;
	}

	public boolean getFireworksEnabled() {
		return this.fireworksEnabled;
	}

	public boolean getTimerResetOnPlayerJoin() {
		return this.timerResetOnPlayerJoin;
	}

	public ArrayList<String> getSongs() {
		return this.songs;
	}

	public int getOutBlock() {
		return this.outBlock;
	}

	public boolean getFallingBlocks() {
		return this.fallingBlock;
	}

	public boolean getUseBoosts() {
		return this.useBoosts;
	}

	public int getFloorLength() {
		return this.floorLength;
	}

	public int getBoostDuration() {
		return this.boostDuration;
	}

	public int getFloorWidth() {
		return this.floorWidth;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public int getCountdown() {
		return this.countdown;
	}

	public int getMinPlayers() {
		return this.minPlayers;
	}

	public int getTimeToSearch() {
		return this.timeToSearch;
	}

	public double getTimeReductionPerLevel() {
		return this.timeReductionPerLevel;
	}

	public int getLevel() {
		return this.level;
	}

	public boolean getAutoGenerateFloors() {
		return this.autoGenerateFloors;
	}

	public boolean getUseSchematicFloors() {
		return this.useSchematicFloors;
	}

	public boolean reachedMaxPlayers() {
		if (this.arena.exists()) {
			int totalPlayersInGame = Players.getPlayerAmountInLobby(Config.arenaName) + Players.getPlayerAmountOnFloor(Config.arenaName);
			if (totalPlayersInGame >= this.maxPlayers) {
				return true;
			}
			return false;
		}
		return true;
	}

	public void enable() {
		this.isEnabled = true;
	}

	public void disable() {
		this.isEnabled = false;
	}

	public boolean ex() {
		return this.arena.exists();
	}

	public ArrayList<String> getFloors() {
		return this.enabledFloors;
	}

	public ArrayList<Integer> getRewardItems() {
		return this.rewardItems;
	}

	public void addFloor(String floorName) {
		this.enabledFloors.add(floorName);
		this.cfg.set("configuration.EnabledFloors", this.enabledFloors);
		try {
			this.cfg.save(this.arena);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeFloor(String floorName) {
		this.enabledFloors.remove(floorName);
		this.cfg.set("configuration.EnabledFloors", this.enabledFloors);
		try {
			this.cfg.save(this.arena);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean aborted() {
		return this.abort;
	}

	public void abort() {
		this.abort = true;
	}

	public void unAbort() {
		this.abort = false;
	}

	public boolean abort = false;
	public String gameProgress = "inLobby";

	public String getGameProgress() {
		return this.gameProgress;
	}

	public void setGameProgress(String pro) {
		this.gameProgress = pro;
	}

	public boolean getStart() {
		return this.setStart;
	}

	public boolean setStart = false;

	public void setStart(boolean s) {
		this.setStart = s;
	}
}