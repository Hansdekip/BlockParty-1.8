package com.lekohd.blockparty.scoreboardsystem;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardSys {
	@SuppressWarnings("deprecation")
	public static void setScore(Player p) {
		Scoreboard playerboard = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = playerboard.registerNewObjective("Score", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("§6BlockParty");
		Score s1 = objective.getScore(Bukkit.getOfflinePlayer("§1" + ChatColor.GREEN + "------------"));
		Score s2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Level:"));
		Score s3 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Server"));
		Score s4 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "2.0"));
		Score s5 = objective.getScore(Bukkit.getOfflinePlayer("§2" + ChatColor.GREEN + "------------"));
		Score s6 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Kit:"));
		Score s7 = objective.getScore(Bukkit.getOfflinePlayer(""));
		Score s8 = objective.getScore(Bukkit.getOfflinePlayer("§3" + ChatColor.GREEN + "------------"));

		Score s12 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Just some"));
		Score s13 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "random text"));
		Score s14 = objective.getScore(Bukkit.getOfflinePlayer("§5" + ChatColor.GREEN + "------------"));
		s1.setScore(14);
		s2.setScore(13);
		s3.setScore(12);
		s4.setScore(11);
		s5.setScore(10);
		s6.setScore(9);
		s7.setScore(8);
		s8.setScore(7);
		s12.setScore(3);
		s13.setScore(2);
		s14.setScore(1);
		p.setScoreboard(playerboard);
	}
}