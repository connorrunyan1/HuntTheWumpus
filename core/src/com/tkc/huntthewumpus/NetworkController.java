package com.tkc.huntthewumpus;

import com.badlogic.gdx.Gdx;
import com.tkc.huntthewumpus.Model.LevelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by connorrunyan on 9/29/2016.
 */
public class NetworkController {

    private static String TAG = "NetworkController";
    private String username;
    private String uniqueSocketID;
    private Socket socket;
    private LevelModel level; // grab a reference to which level model we're updating

    public NetworkController(LevelModel level) {
        this.level = level;
        username = level.getUsername();
        connectSocket();
    }

    public void connectSocket() {
        try {
            socket = IO.socket("http://52.35.157.112:8080");
            socket.connect();
            configSocketEvents();
        } catch (Exception e) {
            Gdx.app.log(TAG, "" + e);
        }
    }

    public void configSocketEvents() {
        // when this client connects (this event is system sent, not by me)
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.debug(TAG, "Connected");
            }
        });

        // when the server sends you your ID after you connect, time to send server
        // the rest of the info it wants
        socket.on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    uniqueSocketID = (String) data.get("id");
                    Gdx.app.log(TAG, "My ID is" + uniqueSocketID);
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting ID");
                }

            }
        });

        // when a new player connects
        socket.on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    level.addOtherPlayer(id, 0, 0);
                    Gdx.app.log(TAG, "New Player Connected with ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting New Player ID");
                }
            }
        });

        // when another player disconnects
        /*
        socket.on("playerDisconnected", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = (String) data.get("id");
                    Gdx.app.log(TAG, "Player Disconnected with ID: " + id);
                    level.removeOtherPlayer(); // TODO
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting Player ID To Remove");
                }
            }
        });
        */

        // when the server sends you whole game player state
        socket.on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for (int i = 0; i < objects.length(); i++) {
                        String id = objects.getJSONObject(i).getString("id");
                        double xPos = objects.getJSONObject(i).getDouble("x");
                        double yPos = objects.getJSONObject(i).getDouble("y");
                        Gdx.app.log(TAG, "Player Located:" + id);
                        level.addOtherPlayer(id, (float) xPos, (float) yPos); // TODO
                    }
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting New Player ID");
                }
            }
        });

        // when the server sends you whole game arrow state
        socket.on("getArrows", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });

        // when someone else fires an arrow
        socket.on("arrowFired", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });

        // when someone else changes their position
        socket.on("positionUpdated", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {

                    String id = data.getString("id");
                    double xPos =  data.getDouble("x");
                    double yPos =  data.getDouble("y");
                    level.updatePlayer(id, (float) xPos, (float) yPos);
                    Gdx.app.log(TAG, "Recieved update" + xPos +" " + yPos);
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting New Player ID");
                }
            }
        });
    }

    public void updateServer(float xPos, float yPos) {
        JSONObject data = new JSONObject();
        try {
            data.put("id", uniqueSocketID);
            data.put("x", xPos);
            data.put("y", yPos);
            socket.emit("positionUpdate", data);
            Gdx.app.log(TAG, "Sent update");
        } catch (JSONException e) {
            Gdx.app.log(TAG, "Error Getting New Player ID");
        }


    }
}


/*
events the server might recieve
    connect
    disconnect
    arrow fired
    position updated
*/