package com.lekohd.blockparty;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.system.Metrics;
import com.lekohd.blockparty.commands.BlockPartyCommand;
import com.lekohd.blockparty.floor.LoadFloor;
import com.lekohd.blockparty.listeners.BlockPlaceListener;
import com.lekohd.blockparty.listeners.ChangeBlockListener;
import com.lekohd.blockparty.listeners.CommandListener;
import com.lekohd.blockparty.listeners.DamageListener;
import com.lekohd.blockparty.listeners.DisconnectListener;
import com.lekohd.blockparty.listeners.FeedListener;
import com.lekohd.blockparty.listeners.InteractListener;
import com.lekohd.blockparty.listeners.InventoryListener;
import com.lekohd.blockparty.listeners.MoveListener;
import com.lekohd.blockparty.listeners.SignListener;
import com.lekohd.blockparty.music.Songs;
import com.lekohd.blockparty.system.Config;
import com.lekohd.blockparty.system.InventoryManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockParty extends JavaPlugin {
	public File inventoryFolder;
	public static PluginDescriptionFile pdfFile;
	public static Player p;
	public static HashMap<String, String> inGamePlayers = new HashMap<String, String>();
	public static HashMap<String, String> inLobbyPlayers = new HashMap<String, String>();
	public static HashMap<String, String> onFloorPlayers = new HashMap<String, String>();
	public static HashMap<String, Location> locs = new HashMap<String, Location>();
	public static HashMap<String, GameMode> gm = new HashMap<String, GameMode>();
	public static HashMap<String, ItemStack[]> awards = new HashMap<String, ItemStack[]>();
	public static HashMap<String, Config> getArena = new HashMap<String, Config>();
	public static HashMap<String, Integer> exp = new HashMap<String, Integer>();
	public static HashMap<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();
	
	public static ArrayList<LoadFloor> floors = new ArrayList<LoadFloor>();
	public static HashMap<String, ArrayList<LoadFloor>> getFloor = new HashMap<String, ArrayList<LoadFloor>>();
	public static HashMap<String, Sign> signs = new HashMap<String, Sign>();
	public static BlockParty instance;
	public static int playerAmount = 0;
	public static int lessMinimum = 0;
	public static boolean abort = false;
	public static ArrayList<String> arenaNames = new ArrayList<String>();
	private static boolean noteBlockAPI;
	public static Set<String> inventoriesToRestore;
	public static Logger logger;
	public static InventoryManager inventoryManager;
	public static String defaultLanguage = "en";
	public static MessageManager messageManager;
	
	
	@SuppressWarnings("deprecation")
	public void onEnable() {
		logger = Logger.getLogger("Minecraft");

		messageManager = new MessageManager();

		instance = this;
		File file = new File(getDataFolder(), "locale_en.yml");
		if (!file.exists()) {
			System.out.print("[BlockParty] Default language file not found. Creating English locale_en.yml");
			this.saveResource("locale_en.yml", false);
		}
		
		loadConfig();
		
		messageManager.log("Plugin by " + getDescription().getAuthors());

		getCommand("blockparty").setExecutor(new BlockPartyCommand());

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new DisconnectListener(), this);
		pm.registerEvents(new CommandListener(), this);
		pm.registerEvents(new MoveListener(), this);
		pm.registerEvents(new SignListener(), this);
		pm.registerEvents(new InteractListener(), this);
		pm.registerEvents(new FeedListener(), this);
		pm.registerEvents(new ChangeBlockListener(), this);
		pm.registerEvents(new BlockPlaceListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new InventoryListener(), this);

		noteBlockAPI = pm.isPluginEnabled("NoteBlockAPI");
		pm.isPluginEnabled("BarAPI");
		if (noteBlockAPI) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				Songs.stop(p);
			}
		}
		
		pdfFile = getDescription();
		
		registerInventories();
		
		inventoryManager = new InventoryManager(instance);
		
		try {
			new Metrics(this).start();
		} catch (IOException e) {
		}
	}

	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(BlockParty.instance);
		messageManager.log("Plugin disabled!");
	}

	public static BlockParty getInstance() {
		return instance;
	}

	private void registerInventories() {
		inventoriesToRestore = new HashSet<String>();

		File dir = new File(getDataFolder(), "//Inventories");
		inventoryFolder = new File(getDataFolder(), "//Inventories");

		if (!dir.exists()) {
			dir.mkdir();
			return;
		}
		System.out.print("[BlockParty] Loading Inventories To Restore");

		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".inv")) {
				inventoriesToRestore.add(f.getName().substring(0, f.getName().indexOf(".")));
				System.out.print("[BlockParty] Inv. to Restore = " + f.getName().substring(0, f.getName().indexOf(".")));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void loadConfig() {
		FileConfiguration cfg = instance.getConfig();
		cfg.options().copyDefaults(true);
		instance.saveConfig();
		defaultLanguage = cfg.getString("defaultLanguage");
		messageManager.loadLocale(defaultLanguage);
		arenaNames = (ArrayList<String>) cfg.get("enabledArenas");
		if (arenaNames != null) {
			if (!arenaNames.isEmpty()) {
				for (String name : arenaNames) {
					Config conf = new Config(name);
					conf.enable();
					conf.loadCfg();
					conf.loadGameSpawn();
					conf.loadLobbySpawn();
					conf.loadMinPlayers();
					conf.loadMax();
					conf.loadMin();
					conf.load();
					getArena.put(name, conf);
					if (conf.getFloors() != null) {
						floors.clear();
						for (String floor : conf.getFloors()) {
							floors.add(new LoadFloor(floor));
							getFloor.put(name, floors);
						}
					}
					messageManager.log("Arena " + name + " loaded!");
				}
			}
		}
	}

	public static void sendInfoToConsole(String msg) {
		logger.info("[BlockParty] " + msg);
	}
}