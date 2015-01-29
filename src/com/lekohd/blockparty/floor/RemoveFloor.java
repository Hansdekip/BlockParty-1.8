package com.lekohd.blockparty.floor;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.lekohd.blockparty.system.Config;
import java.util.ArrayList;

public class RemoveFloor {
	@SuppressWarnings("rawtypes")
	public static void add(String arenaName, String floorName) {
		BlockParty.floors.clear();
		if (((ArrayList) BlockParty.getFloor.get(arenaName)).size() == 1) {
			BlockParty.floors.add((LoadFloor) ((ArrayList) BlockParty.getFloor.get(arenaName)).get(0));
		}
		if (((ArrayList) BlockParty.getFloor.get(arenaName)).size() > 1) {
			for (LoadFloor f : BlockParty.getFloor.get(arenaName)) {
				BlockParty.floors.add(f);
			}
		}
		BlockParty.floors.remove(floorName);
		BlockParty.getFloor.put(arenaName, BlockParty.floors);
		((Config) BlockParty.getArena.get(arenaName)).removeFloor(floorName);
	}
}