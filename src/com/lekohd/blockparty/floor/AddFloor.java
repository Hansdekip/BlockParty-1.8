package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import java.util.ArrayList;

public class AddFloor {
	@SuppressWarnings("rawtypes")
	public static void add(String arenaName, String floorName) {
		BlockParty.floors.clear();
		LoadFloor lf = new LoadFloor(floorName);
		BlockParty.floors.add(lf);
		((Config) BlockParty.getArena.get(arenaName)).addFloor(floorName);
		if (((ArrayList) BlockParty.getFloor.get(arenaName)).size() > 1) {
			for (LoadFloor f : BlockParty.getFloor.get(arenaName)) {
				BlockParty.floors.add(f);
			}
		}
		if (((ArrayList) BlockParty.getFloor.get(arenaName)).size() == 1) {
			BlockParty.floors.add((LoadFloor) ((ArrayList) BlockParty.getFloor.get(arenaName)).get(0));
		}
		BlockParty.getFloor.put(arenaName, BlockParty.floors);
		((Config) BlockParty.getArena.get(arenaName)).addFloor(floorName);
	}
}