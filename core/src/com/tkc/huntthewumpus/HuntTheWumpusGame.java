package com.tkc.huntthewumpus;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.tkc.huntthewumpus.Model.LevelModel;
import com.tkc.huntthewumpus.Screens.GameScreen;
import com.tkc.huntthewumpus.Screens.MenuScreen;

public class HuntTheWumpusGame extends Game {

	Screen currentScreen;

	@Override
	public void create () {
		currentScreen = new MenuScreen(this);
	}

	@Override
	public void render () {
		currentScreen.render(Gdx.graphics.getDeltaTime());
	}
	
	@Override
	public void dispose () {
		currentScreen.dispose();
	}

	@Override
	public void setScreen(Screen newScreen){
		currentScreen = newScreen;
	}
}
