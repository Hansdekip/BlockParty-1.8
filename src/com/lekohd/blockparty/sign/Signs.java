package com.lekohd.blockparty.sign;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import org.bukkit.block.Sign;

public class Signs {
	public static void updateJoin(String arenaName, boolean full) {
		if (BlockParty.signs.containsKey(arenaName)) {
			if (full) {
				((Sign) BlockParty.signs.get(arenaName)).setLine(3, BlockParty.messageManager.SIGN_FULL);
			}
			if (!full) {
				((Sign) BlockParty.signs.get(arenaName)).setLine(3, BlockParty.messageManager.SIGN_JOIN);
			}
		}
	}

	public static void updateGameProgress(String arenaName, boolean inLobby) {
		if (BlockParty.signs.containsKey(arenaName)) {
			if (inLobby) {
				((Sign) BlockParty.signs.get(arenaName)).setLine(1, BlockParty.messageManager.SIGN_IN_LOBBY);
			}
			if (!inLobby) {
				((Sign) BlockParty.signs.get(arenaName)).setLine(1, BlockParty.messageManager.SIGN_IN_GAME);
			}
		}
	}
}