package com.lekohd.blockparty.system;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Players {
	public static ArrayList<String> playersInGame = new ArrayList<String>();
	public static ArrayList<String> playersInLobby = new ArrayList<String>();
	public static ArrayList<String> playersOnFloor = new ArrayList<String>();
	public static int playerAmountInGame = 0;
	public static int playerAmountInLobby = 0;
	public static int playerAmountOnFloor = 0;

	@SuppressWarnings("deprecation")
	public static void getAmount(String arenaName, String loc) {
		playerAmountInGame = 0;
		playerAmountInLobby = 0;
		playerAmountOnFloor = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (loc.equals("Lobby")) {
				if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
					if (((String) BlockParty.inLobbyPlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
						playerAmountInLobby += 1;
					}
				}
			} else if (loc.equals("Game")) {
				if (BlockParty.inGamePlayers.containsKey(p.getName())) {
					if (((String) BlockParty.inGamePlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
						playerAmountInGame += 1;
					}
				}
			} else if (loc.equals("Floor")) {
				if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
					if (((String) BlockParty.onFloorPlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
						playerAmountOnFloor += 1;
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void getPlayers(String arenaName, String loc) {
		playersInGame.clear();
		playersInLobby.clear();
		playersOnFloor.clear();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (BlockParty.inLobbyPlayers.containsKey(p.getName())) {
				if (((String) BlockParty.inLobbyPlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
					if (loc.equals("Lobby")) {
						if (!playersInLobby.contains(p.getName())) {
							playersInLobby.add(p.getName());
						}
					}
				}
			}
			if (BlockParty.inGamePlayers.containsKey(p.getName())) {
				if (((String) BlockParty.inGamePlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
					if (loc.equals("Game")) {
						if (!playersInGame.contains(p.getName())) {
							playersInGame.add(p.getName());
						}
					}
				}
			}
			if (BlockParty.onFloorPlayers.containsKey(p.getName())) {
				if (((String) BlockParty.onFloorPlayers.get(p.getName())).equalsIgnoreCase(arenaName)) {
					if (loc.equals("Floor")) {
						if (!playersOnFloor.contains(p.getName())) {
							playersOnFloor.add(p.getName());
						}
					}
				}
			}
		}
	}

	public static ArrayList<String> getPlayersInLobby(String arenaName) {
		getPlayers(arenaName, "Lobby");
		return playersInLobby;
	}

	public static ArrayList<String> getPlayersInGame(String arenaName) {
		getPlayers(arenaName, "Game");
		return playersInGame;
	}

	public static ArrayList<String> getPlayersOnFloor(String arenaName) {
		getPlayers(arenaName, "Floor");
		return playersOnFloor;
	}

	public static int getPlayerAmountInLobby(String arenaName) {
		getAmount(arenaName, "Lobby");
		return playerAmountInLobby;
	}

	public static int getPlayerAmountInGame(String arenaName) {
		getAmount(arenaName, "Game");
		return playerAmountInGame;
	}

	public static int getPlayerAmountOnFloor(String arenaName) {
		getAmount(arenaName, "Floor");
		return playerAmountOnFloor;
	}

	public static boolean reachedMaxPlayers(String arenaName) {
		return ((Config) BlockParty.getArena.get(arenaName)).reachedMaxPlayers();
	}
}