package com.tkc.huntthewumpus.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tkc.huntthewumpus.HuntTheWumpusGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final int gameWidth= 1600;
		final int gameHeight= 900;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = gameWidth;
		config.height = gameHeight;
		config.title = "Battle Wumpus ALPHA";
		new LwjglApplication(new HuntTheWumpusGame(), config);
	}
}
