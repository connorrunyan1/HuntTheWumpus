package com.tkc.huntthewumpus;

import com.badlogic.gdx.graphics.Texture;

// this class runs the asset manager, and maybe shares some global constants?
public class Utility {

    private Texture hunter_red;

    public Utility(){
        hunter_red = new Texture("hunter_red.png");
    }

    public int getGameWidth(){
        return 1080;
    }

    public int getGameHeight(){
        return 720;
    }

    public Texture getHunter_Red(){
        return hunter_red;
    }
}
