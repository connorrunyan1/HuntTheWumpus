package com.tkc.huntthewumpus.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tkc.huntthewumpus.Entities.*;

import javax.net.ssl.SSLProtocolException;

/**
 * Created by connorrunyan on 9/28/2016.
 */
public class Arrow implements Entity{

    Vector2 position; // lets try for a direction/velocity style vector?
    Vector2 vector;
    Sprite arrowSprite;

    public Arrow(float originX, float originY, float direction, float velocity){
        position = new Vector2(originX, originY);
        vector = new Vector2(direction + 180, 400+velocity*750);
        arrowSprite = new Sprite(new Texture("Arrow.png"));
        arrowSprite.setRotation(direction + 180);
    }

    @Override
    public void update(float dt) {
        // find out how much of the velocity should be added to each direction
        float xNess = (MathUtils.cosDeg(vector.x));
        float yNess = (MathUtils.sinDeg(vector.x));
        float txNess = xNess;
        float tyNess = yNess;
        if(xNess < 0){
            txNess = -xNess;
        }
        if(yNess < 0){
            tyNess = -yNess;
        }
        float total = txNess + tyNess;
        xNess = xNess/total;
        yNess = yNess/total;
        arrowSprite.setPosition(position.x, position.y);
        position.x = position.x + dt*xNess*vector.y;
        position.y = position.y + dt*yNess*vector.y;
    }

    @Override
    public void render(SpriteBatch batch) {
        arrowSprite.draw(batch);
    }
}