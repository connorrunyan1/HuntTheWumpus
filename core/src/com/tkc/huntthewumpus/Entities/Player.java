package com.tkc.huntthewumpus.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by connorrunyan on 9/28/2016.
 */
public class Player implements Entity {

    String username;
    boolean playable = true;

    // graphical info
    Sprite playerSprite;

    // physics info
    private final float BASE_MOVE_ACCELERATION = 1.0f;
    private Vector2 playerLocation;
    private Vector2 playerSpeed;
    // arrow firing info
    private boolean arrowCharging;
    private float arrowCharge = 0;

    public Player(String username, float startX, float startY) {
        // graphics set up
        // TODO make this work through Utility's asset manager
        Texture tempTexture = new Texture("hunter_green.png");
        playerSprite = new Sprite(tempTexture);

        // physics set up
        playerLocation = new Vector2(startX, startY);
        playerSpeed = new Vector2(0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        // sprite location should already have been set during update.
        // TODO animations, etc.
        playerSprite.draw(batch);
    }

    @Override
    public void update(float dt) {
        if(playable) {
            // first, lets do the arrow logic.
            if (Gdx.input.isTouched()) {
                arrowCharging = true;
                arrowCharge += 1 * dt;
            } else {
                if (arrowCharging) {
                    // TODO fire the arrow, then set charging to false
                    arrowCharge = 0.0f;
                    arrowCharging = false;
                }
            }

            // TODO physics note: I'm writing it right now so that the accel effects the speed effects the
            // location THEN the location changes.  it is possible location should change then we should change
            // physics after for next frame.  we'll see

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                playerSpeed.set(playerSpeed.x, playerSpeed.y + BASE_MOVE_ACCELERATION);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                playerSpeed.set(playerSpeed.x - BASE_MOVE_ACCELERATION, playerSpeed.y);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                playerSpeed.set(playerSpeed.x, playerSpeed.y - BASE_MOVE_ACCELERATION);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                playerSpeed.set(playerSpeed.x + BASE_MOVE_ACCELERATION, playerSpeed.y);
            }

            // so, as long as you press a button your speed in that direction increases. here we have the cap
            // TODO right now this is a hard cap, but I think an asymtote would be better behavior
            float speedCap = 7.5f;

            if (playerSpeed.x > speedCap) {
                playerSpeed.x = speedCap;
            } else if (playerSpeed.x < -speedCap) {
                playerSpeed.x = -speedCap;
            }

            if (playerSpeed.y > speedCap) {
                playerSpeed.y = speedCap;
            } else if (playerSpeed.y < -speedCap) {
                playerSpeed.y = -speedCap;
            }

            // whatever the speed was, reduce it by friction
            // TODO this friction depends on what the ground is. here it is hard coded, but later it should
            // be able to tell
            float friction = 0.5f;
            if (playerSpeed.x == 0) {
                // do nothing
            } else if (playerSpeed.x > 0) {
                playerSpeed.x = playerSpeed.x - friction;
            } else {
                playerSpeed.x = playerSpeed.x + friction;
            }

            if (playerSpeed.y == 0) {
                // do nothing
            } else if (playerSpeed.y > 0) {
                playerSpeed.y = playerSpeed.y - friction;
            } else {
                playerSpeed.y = playerSpeed.y + friction;
            }


            // now that the speed is set, apply it to the player location
            playerLocation.set(playerLocation.x + playerSpeed.x, playerLocation.y + playerSpeed.y);

            // move the sprite to the player location
            playerSprite.setPosition(playerLocation.x, playerLocation.y);
        }
    }

    public void setOther(){
        playable = false;
    }

    public float getCharge(){
        return arrowCharge;
    }

    // Down here are some convenience methods used elsewhere
    public Vector2 getPosition(){
        return new Vector2(playerLocation.x, playerLocation.y);
    }
}
