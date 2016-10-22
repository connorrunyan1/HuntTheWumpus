package com.tkc.huntthewumpus.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by connorrunyan on 9/30/2016.
 */
public class MenuScreen implements Screen{

    private Game game;
    private Stage stage;
    private Table table;
    private Button refreshButton;
    private Button loginButton;
    private TextField usernameField;
    Skin skin;
    List roomsList;

    public MenuScreen(Game theGame){
        game = theGame;
        show();
    }

    @Override
    public void show(){

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        table = new Table();
        table.setSkin(skin);
        table.setPosition(0, 300, Align.center);
        table.setFillParent(true);
        stage.addActor(table);
        table.add(new Table(skin));
        table.add(new Label("Enter A Username:", skin));
        table.row();

        refreshButton = new Button(skin);
        refreshButton.add(new Label("Refresh Button", skin));
        refreshButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                refreshRoomsList();
            }
        });

        table.add(refreshButton);
        usernameField = new TextField("", skin);
        table.add(usernameField);
        loginButton = new Button(skin);
        loginButton.add(new Label("Login Button", skin));
        loginButton.addListener(new ChangeListener() {
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                attemptLogin();
            }
        });
        table.add(loginButton);

        //table.setDebug(true); // This is optional, but enables debug lines for tables.
        table.row();
        table.add(new Table(skin));

        table.row();
        table.add(new Table(skin));

        table.add(new Label("Select A Room:", skin));
        table.row();

        roomsList = new List(skin);
        roomsList.setItems("  Room 1 - Only Room  ");
        table.add(roomsList);
        table.add(roomsList);
    }

    @Override
    public void render(float dt){
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void dispose(){
        stage.dispose();
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
        stage.getViewport().update(width, height, true);
    }

    private void refreshRoomsList(){
        // TODO
    }

    private void attemptLogin(){
        String username = usernameField.getText();
        game.setScreen(new GameScreen(game, username));
    }

}
