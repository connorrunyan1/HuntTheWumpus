package com.tkc.huntthewumpus;

import com.badlogic.gdx.Gdx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by connorrunyan on 9/29/2016.
 */
public class NetworkController extends Observable{

    private static String TAG = "NetworkController";
    Boolean host = false;
    private String uniqueSocketID;
    private Socket socket;

    public NetworkController(){
        connectSocket();
    }

    public void connectSocket(){
        try{
            socket = IO.socket("http://52.35.157.112:8080");
            socket.connect();
            configSocketEvents();
        }catch (Exception e){
            Gdx.app.log(TAG, ""+e);
        }
    }

    public void configSocketEvents(){
        socket.on("connect", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                Gdx.app.debug(TAG, "Connected");
            }
        }).on("socketID", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    uniqueSocketID = (String) data.get("id");
                    Gdx.app.log(TAG, "My ID is" + uniqueSocketID);
                } catch (JSONException e){
                    Gdx.app.log(TAG, "Error Getting ID");
                }

            }
        }).on("newPlayer", new Emitter.Listener(){
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = (String) data.get("id");
                    Gdx.app.log(TAG, "New Player Connected with ID: " + id);
                    setChanged();
                    notifyObservers(data); // TODO make object have what we want
                } catch (JSONException e) {
                    Gdx.app.log(TAG, "Error Getting New Player ID");
                }
            }
        });
    }
}
