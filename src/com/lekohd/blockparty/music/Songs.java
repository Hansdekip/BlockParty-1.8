package com.lekohd.blockparty.music;
/*
 * Copyright (C) 2014 Leon167, XxChxppellxX and ScriptJunkie 
 */
import com.lekohd.blockparty.BlockParty;
import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import java.io.File;
import org.bukkit.entity.Player;

public class Songs {
	public static void play(Player p, String song) {
		Player target = p;
		File file = new File("plugins/BlockParty/Songs/", song + ".nbs");
		if (!file.exists()) {
			p.sendMessage(BlockParty.messageManager.SONG_NOT_EXIST.replace("$SONG$", song));
		} else {
			playSong(target, song + ".nbs");
			p.sendMessage(BlockParty.messageManager.SONG_NOW_PLAYING.replace("$SONG$", song));
		}
	}

	public static void playSong(Player p, String song) {
		Song s = NBSDecoder.parse(new File("plugins/BlockParty/Songs/" + song));
		SongPlayer sp = new RadioSongPlayer(s);
		sp.setAutoDestroy(true);
		sp.addPlayer(p);
		sp.setPlaying(true);
	}

	public static void stop(Player p) {
		NoteBlockPlayerMain.stopPlaying(p);
	}
}