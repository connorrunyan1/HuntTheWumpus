package com.tkc.huntthewumpus.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tkc.huntthewumpus.Utility;

/**
 * Created by connorrunyan on 9/28/2016.
 */
public class OtherPlayer implements Entity {

    String username;
    String uniqueSocketID;

    // graphical info
    Texture texture;
    Sprite playerSprite;

    // physics info
    private Vector2 playerLocation;

    public OtherPlayer(String ID, String username, String skin, float startX, float startY) {
        // graphics set up
        // TODO make this work through Utility's asset manager
        this.username = username;
        this.uniqueSocketID = ID;
        Texture tempTexture;
        if (skin.equals("red")) {
            tempTexture = new Texture("hunter_red.png");
        } else if (skin.equals("yellow")) {
            tempTexture = new Texture("hunter_yellow.png");
        } else if (skin.equals("blue")) {
            tempTexture = new Texture("hunter_blue.png");
        } else {
            // default green
            tempTexture = new Texture("hunter_green.png");
        }

        playerSprite = new Sprite(tempTexture);
        playerLocation = new Vector2(startX, startY);
    }

    public OtherPlayer(String ID, float startX, float startY, Utility util) {
        // graphics set up
        // TODO make this work through Utility's asset manager
        this.uniqueSocketID = ID;
        // default green
        texture = util.getHunter_Red();

        playerSprite = new Sprite(texture);
        playerLocation = new Vector2(startX, startY);
    }

    @Override
    public void render(SpriteBatch batch) {
        // sprite location should already have been set during update.
        // TODO animations, etc.
        playerSprite.draw(batch);
    }

    @Override
    public void update(float dt) {
        // other players do not update based on any world data, only their position being set by
        // network controller
    }

    /**
     * This code used by the network controller to set the position of another player
     */
    public void setPosition(float x, float y){
        playerLocation.x = x;
        playerLocation.y = y;
        playerSprite.setPosition(x, y);
    }

    // Down here are some convenience methods used elsewhere
    public Vector2 getPosition() {
        return new Vector2(playerLocation.x, playerLocation.y);
    }
}
