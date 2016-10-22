package com.tkc.huntthewumpus.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sun.glass.ui.Window;
import com.tkc.huntthewumpus.Model.LevelModel;

/**
 * Created by connorrunyan on 9/30/2016.
 */
public class GameScreen implements Screen{

    Game theGame;
    LevelModel levelModel;

    public GameScreen(Game gameReference, String username){
        theGame = gameReference;
        levelModel = new LevelModel("fake_level_name", username);
        levelModel.setUsername(username);
    }

    @Override
    public void show(){

    }

    @Override
    public void render(float dt){
        levelModel.update(dt);
        levelModel.draw(dt);
    }

    @Override
    public void dispose(){
        levelModel.dispose();
    }

    @Override
    public void hide(){

    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){

    }

    @Override
    public void resize(int width, int height){

    }
}
