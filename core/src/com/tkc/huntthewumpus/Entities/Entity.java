package com.tkc.huntthewumpus.Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// This is the interface for entities.  Entities should be anything that moves within the world.
// Examples are players, arrows, wupuses, etc.
public interface Entity {

    public void update(float dt);

    public void render(SpriteBatch batch);

}
