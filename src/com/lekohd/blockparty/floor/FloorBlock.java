package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FloorBlock {
	public static String getName(Byte b, Material material) {
		if (material == Material.STAINED_CLAY) {
			if (b.byteValue() == 0) {
				return "§lWhite Stained Clay";
			}
			if (b.byteValue() == 1) {
				return "§lOrange Stained Clay";
			}
			if (b.byteValue() == 2) {
				return "§lMagenta Stained Clay";
			}
			if (b.byteValue() == 3) {
				return "§lLight Blue Stained Clay";
			}
			if (b.byteValue() == 4) {
				return "§lYellow Stained Clay";
			}
			if (b.byteValue() == 5) {
				return "§lLime Stained Clay";
			}
			if (b.byteValue() == 6) {
				return "§lPink Stained Clay";
			}
			if (b.byteValue() == 7) {
				return "§lGray Stained Clay";
			}
			if (b.byteValue() == 8) {
				return "§lLight Gray Stained Clay";
			}
			if (b.byteValue() == 9) {
				return "§lCyan Stained Clay";
			}
			if (b.byteValue() == 10) {
				return "§lPurple Stained Clay";
			}
			if (b.byteValue() == 11) {
				return "§lBlue Stained Clay";
			}
			if (b.byteValue() == 12) {
				return "§lBrown Stained Clay";
			}
			if (b.byteValue() == 13) {
				return "§lGreen Stained Clay";
			}
			if (b.byteValue() == 14) {
				return "§lRed Stained Clay";
			}
			if (b.byteValue() == 15) {
				return "§lBlack Stained Clay";
			}
			return null;
		}
		if (material == Material.WOOL) {
			if (b.byteValue() == 0) {
				return "§lWhite Wool";
			}
			if (b.byteValue() == 1) {
				return "§lOrange Wool";
			}
			if (b.byteValue() == 2) {
				return "§lMagenta Wool";
			}
			if (b.byteValue() == 3) {
				return "§lLight Blue Wool";
			}
			if (b.byteValue() == 4) {
				return "§lYellow Wool";
			}
			if (b.byteValue() == 5) {
				return "§lLime Wool";
			}
			if (b.byteValue() == 6) {
				return "§lPink Wool";
			}
			if (b.byteValue() == 7) {
				return "§lGray Wool";
			}
			if (b.byteValue() == 8) {
				return "§lLight Gray Wool";
			}
			if (b.byteValue() == 9) {
				return "§lCyan Wool";
			}
			if (b.byteValue() == 10) {
				return "§lPurple Wool";
			}
			if (b.byteValue() == 11) {
				return "§lBlue Wool";
			}
			if (b.byteValue() == 12) {
				return "§lBrown Wool";
			}
			if (b.byteValue() == 13) {
				return "§lGreen Wool";
			}
			if (b.byteValue() == 14) {
				return "§lRed Wool";
			}
			if (b.byteValue() == 15) {
				return "§lBlack Wool";
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public static void givePlayer(Player p, Block b) {
		if (Material.STAINED_CLAY == b.getType() || Material.WOOL == b.getType()) {
			ItemStack block = new ItemStack(b.getType(), 1, (short) 0, b.getData());
			ItemMeta bmeta = block.getItemMeta();
			bmeta.setDisplayName(getName(b.getData(), b.getType()));
			block.setItemMeta(bmeta);
			p.getInventory().setItem(4, block);
		}else{
			ItemStack block = new ItemStack(b.getType(), 1, (short) 0, b.getData());
			ItemMeta bmeta = block.getItemMeta();
			bmeta.setDisplayName(b.getType().name());
			block.setItemMeta(bmeta);
			p.getInventory().setItem(4, block);
		}
		
	}
}