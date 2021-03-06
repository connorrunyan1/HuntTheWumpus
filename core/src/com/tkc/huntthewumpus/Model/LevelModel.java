package com.tkc.huntthewumpus.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.squareup.okhttp.internal.Util;
import com.tkc.huntthewumpus.Entities.Arrow;
import com.tkc.huntthewumpus.Entities.Entity;
import com.tkc.huntthewumpus.Entities.OtherPlayer;
import com.tkc.huntthewumpus.Entities.Player;
import com.tkc.huntthewumpus.NetworkController;
import com.tkc.huntthewumpus.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javax.print.attribute.standard.MediaSize;

// This is the class that keeps track of the state of the level. It will also act as the controller,
// processing input and updating accordingly.
public class LevelModel {
    // brainstorm, what does a level need?
    // metadata: width, height, name, progression stats(wave etc)
    private Utility util;
    OrthographicCamera camera;
    OrthographicCamera hudCamera;
    NetworkController networkController;
    // the actual background map
    // TODO for now background is an image
    Sprite background;
    Sprite hudTest;
    // stuff on top of the map, slime/blood/goo/pits
    // current player
    private Player player;
    private GlyphLayout username;
    private String usernameAsString;
    float arrowCharge = 0;
    float oldCharge = 0;
    boolean arrowCharging = false;
    // list of OTHER players
    private TreeMap<String, OtherPlayer> players;
    // list of wumpuses
    private ArrayList<Entity> wumpuses;
    // list of projectiles
    private ArrayList<Entity> projectiles;

    // info that the renderer will need
    SpriteBatch batch;
    private BitmapFont font;

    // constructor that takes the level name.  name prefixed by mode? ie survival_oasis
    public LevelModel(String levelName, String name) {
        util = new Utility();
        font = new BitmapFont();
        camera = new OrthographicCamera(1600, 900);
        hudCamera = new OrthographicCamera(1600, 900);
        background = new Sprite(new Texture("oasis.png"));
        hudTest = new Sprite(new Texture("hud_heart_alive.png"));
        hudTest.setScale(4.0f, 4.0f);
        background.setScale(2.0f, 2.0f);
        // TODO might want to remove this part? idk
        player = new Player(name, 0, 0);
        players = new TreeMap<String, OtherPlayer>();
        wumpuses = new ArrayList<Entity>();
        projectiles = new ArrayList<Entity>();
        batch = new SpriteBatch();
        networkController = new NetworkController(this);
    }

    float networkDelay =0.1f; // update each tehnth second

    public void update(float dt) {
        networkDelay-= dt;
        // TODO Check user controls and update their player accordingly
        if(player != null){
            player.update(dt);
            arrowCharge = player.getCharge();
            if(arrowCharge > 0){
                arrowCharging = true;
                oldCharge = arrowCharge;
            }
            if(arrowCharge == 0 && arrowCharging){
                // TODO fire arrow
                // calculating direction is fiding angle from mouse to player then normalizing

                Vector3 vector = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1));
                float direction = MathUtils.radiansToDegrees * MathUtils.atan2(player.getPosition().y - vector.y, player.getPosition().x - vector.x);
                float vel = oldCharge;
                Arrow a = new Arrow(player.getPosition().x, player.getPosition().y,direction, vel);
                projectiles.add(a);
                arrowCharging = false;
                oldCharge = 0;
            }
        }

        for (String id : players.keySet()) {
            players.get(id).update(dt);
        }
        for (Entity e : wumpuses) {
            e.update(dt);
        }
        for (Entity e : projectiles) {
            e.update(dt);
        }
        // update the camera
        // TODO right now it focuses on the player, but maybe it should lerp between player
        // and previous camera position?
        Vector2 playerPosition = player.getPosition();
        Vector3 tempCameraPos = camera.position.cpy();
        Vector2 cameraPosition = new Vector2(tempCameraPos.x, tempCameraPos.y);
        // basic lerp from wikipedia
        cameraPosition.x = cameraPosition.x + (0.02f+dt)*(playerPosition.x - cameraPosition.x);
        cameraPosition.y = cameraPosition.y + (0.02f+dt)*(playerPosition.y - cameraPosition.y);
        camera.position.set(cameraPosition.x, cameraPosition.y, 1);
        camera.update();
        if(networkDelay < 0){
            networkDelay = 0.1f; // tenth a second
            networkController.updateServer(playerPosition.x, playerPosition.y);
        }
    }

    public void draw(float dt) {
        // clear the screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // start the sprite batch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // draw the background
        background.draw(batch);
        // render the player
        if(player != null){
            player.render(batch);
        }
        for (String id : players.keySet()) {
            players.get(id).render(batch);
        }
        for (Entity e : wumpuses) {
            e.render(batch);
        }
        for (Entity e : projectiles) {
            e.render(batch);
        }

        if(arrowCharge > 0) {
            int charge = (int) (arrowCharge*100);
            float fin = (charge/100.0f);
            font.draw(batch, "Arrow Charging: " + fin, player.getPosition().x - 35, player.getPosition().y-5);
        }

        float fontX = (player.getPosition().x + 30) - username.width/2;

        font.draw(batch, username, fontX, player.getPosition().y + 75);


        // start the sprite batch again for stuff in the hud layer
        batch.setProjectionMatrix(hudCamera.combined);
        hudTest.setPosition(-770, 405);
        hudTest.draw(batch);
        hudTest.setPosition(-700, 405);
        hudTest.draw(batch);
        hudTest.setPosition(-630, 405);
        hudTest.draw(batch);

        // close the sprite batch
        batch.end();
    }

    public void dispose(){
        batch.dispose();
    }

    public void setUsername(String usernames){
        usernameAsString = usernames;
        username = new GlyphLayout(font, usernames);
    }

    public String getUsername(){
        return usernameAsString;
    }

    public void addOtherPlayer(String id, String username, String skin, float x, float y){
        OtherPlayer p = new OtherPlayer(id, username, skin, x, y);
        players.put(id, p);
    }

    public void addOtherPlayer(String id, float x, float y){
        OtherPlayer p = new OtherPlayer(id, x, y, util);
        players.put(id, p);
    }

    public void updatePlayer(String id, float x, float y){
        OtherPlayer p = players.get(id);
        p.setPosition(x,y);
        Gdx.app.log("levelModel", "level model updating player");
    }
}
