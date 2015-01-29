package com.lekohd.blockparty.system;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import com.lekohd.blockparty.BlockParty;

public class InventoryManager {
	private File dir;
	private Map<Player, ItemStack[]> items;
	private Map<Player, ItemStack[]> armor;
	private Map<Player, Integer> exp;
	//private Map<Player, Location> locs;

	public InventoryManager(BlockParty blockparty) {
		this.dir = new File("plugins//BlockParty//Inventories//");
		this.dir.mkdir();

		this.items = new HashMap<Player, ItemStack[]>();
		this.armor = new HashMap<Player, ItemStack[]>();
		this.exp = new HashMap<Player, Integer>();
	}

	public void storeInv(Player p, Boolean setLocation) {
		try {
			if (this.items.containsKey(p)) {
				return;
			}
			ItemStack[] items = p.getInventory().getContents();
			ItemStack[] armor = p.getInventory().getArmorContents();
			int exp = p.getLevel();
			//Location locs = p.getLocation();

			this.items.put(p, items);
			this.armor.put(p, armor);
			this.exp.put(p, exp);
			//if (setLocation) {
			//	this.locs.put(p, locs);
			//}

			File file = new File(this.dir, p.getName() + ".inv");
			YamlConfiguration config = new YamlConfiguration();
			config.set("items", items);
			config.set("armor", armor);
			config.set("exp", exp);
			//if (setLocation) {
			//	config.set("locs", locs);
			//}
			config.save(file);

			clearInventory(p);
			p.updateInventory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restoreInv(Player p) {
		try {
			File file = new File(this.dir, p.getName() + ".inv");
			ItemStack[] items = (ItemStack[]) this.items.remove(p);
			ItemStack[] armor = (ItemStack[]) this.armor.remove(p);
			
			int exp = 0;
			YamlConfiguration config = new YamlConfiguration();

			if (file.exists()) {
				config.load(file);
				List<?> itemsList = config.getList("items");
				List<?> armorList = config.getList("armor");
				exp = config.getInt("exp");
				items = (ItemStack[]) itemsList.toArray(new ItemStack[itemsList.size()]);
				armor = (ItemStack[]) armorList.toArray(new ItemStack[armorList.size()]);
				p.getInventory().setContents(items);
				p.getInventory().setArmorContents(armor);
				p.setLevel(exp);
				p.updateInventory();
				file.delete();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	public void clearInventory(Player p) {
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
		InventoryView view = p.getOpenInventory();
		if (view != null) {
			view.setCursor(null);
			Inventory i = view.getTopInventory();
			if (i != null) {
				i.clear();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean hasEmptyInventory(Player p) {
		ItemStack[] inventory = p.getInventory().getContents();
		ItemStack[] armor = p.getInventory().getArmorContents();
		for (ItemStack stack : inventory) {
			if (stack != null) {
				return false;
			}
		}
		for (ItemStack stack : armor) {
			if (stack.getTypeId() != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean restoreFromFile(Player p) {
		try {
			File file = new File(this.dir, p.getName() + ".inv");
			// System.out.print(this.dir);
			ItemStack[] items = (ItemStack[]) this.items.remove(p);
			ItemStack[] armor = (ItemStack[]) this.armor.remove(p);
			int exp = 0;
			YamlConfiguration config = new YamlConfiguration();

			if (file.exists()) {
				config.load(file);
				List<?> itemsList = config.getList("items");
				List<?> armorList = config.getList("armor");
				exp = config.getInt("exp");
				items = (ItemStack[]) itemsList.toArray(new ItemStack[itemsList.size()]);
				armor = (ItemStack[]) armorList.toArray(new ItemStack[armorList.size()]);
				p.getInventory().setContents(items);
				p.getInventory().setArmorContents(armor);
				p.setLevel(exp);
				p.updateInventory();
				file.delete();
				return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
