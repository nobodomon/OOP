package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.MoveUpdateHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.network.EventListener;
import com.mygdx.game.supers.CapturePoint;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.global.MoveUpdateEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerReadyEvent;

public class IngameScreen implements Screen {

    public static final IngameScreen INSTANCE = new IngameScreen();

    private final SpriteBatch batch;
    private final Stage stage;
    private final Stack rootStack;
    private final Table root;
    private final Table deadMsg;

    private final TextButton ghost_one_button;
    private final TextButton ghost_two_button;
    private final TextButton ghost_three_button;
    private final TextButton minotaur_one_button;
    private final TextButton minotaur_two_button;
    private final TextButton minotaur_three_button;

    private String readyButtonText;
    private final TextButton ready_button;
    private final TextButton start_button;

    private final Label playerCount;
    private final Label totalPlayerCount;
    private final Label deadMsgLabel;

    private boolean ready;
    private boolean alive;

    private boolean allPlayersReady;


    public IngameScreen(){
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());

        this.rootStack = new Stack();
        this.rootStack.setBounds(0,0,800,600);
        this.root = new Table().left().top();
        this.root.setBounds(0,0,800,600);
        this.deadMsg = new Table();
        this.deadMsg.setBounds(0,0,800,600);

        final Skin skin = new Skin (Gdx.files.internal("uiskin.json"));

        this.playerCount = LabelHandler.INSTANCE.createLabel("0", 16, Color.BLACK);
        this.totalPlayerCount = LabelHandler.INSTANCE.createLabel("0", 16, Color.BLACK);
        this.deadMsgLabel = LabelHandler.INSTANCE.createLabel(null, 34, Color.RED);

        this.ready = false;
        this.allPlayersReady = false;

        this.ghost_one_button = new TextButton("Ghost One", skin);
        this.ghost_one_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 0;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.ghost_two_button = new TextButton("Ghost Two", skin);
        this.ghost_two_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 1;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.ghost_three_button = new TextButton("Ghost Three", skin);
        this.ghost_three_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 2;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.minotaur_one_button = new TextButton("Minotaur One", skin);
        this.minotaur_one_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 3;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.minotaur_two_button = new TextButton("Minotaur Two", skin);
        this.minotaur_two_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 4;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.minotaur_three_button = new TextButton("Minotaur Three", skin);
        this.minotaur_three_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = 5;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.ready_button = new TextButton("Ready", skin);
        this.ready_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final PlayerReadyEvent playerReadyEvent = new PlayerReadyEvent();
                toggleReady();
                playerReadyEvent.ready = ready;
                MyGdxGame.getInstance().getClient().sendTCP(playerReadyEvent);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.start_button = new TextButton("Start",skin);

        this.batch.setProjectionMatrix(MyGdxGame.getInstance().getCamera().combined);

        this.stage.addActor(this.rootStack);

        this.rootStack.add(this.root);
        this.rootStack.add(this.deadMsg.center());

        this.setToDefault();
    }

    public void setToDefault(){
        this.root.clear();
        this.root.add(ghost_one_button).pad(5,5,5,5);
        this.root.add(ghost_two_button).pad(5,5,5,5);
        this.root.add(ghost_three_button).pad(5,5,5,5);
        this.root.add(playerCount).padLeft(15).row();
        this.root.add(minotaur_one_button).pad(5,5,5,5);
        this.root.add(minotaur_two_button).pad(5,5,5,5);
        this.root.add(minotaur_three_button).pad(5,5,5,5);
        this.root.add(totalPlayerCount).padLeft(15).row();
        this.root.add(ready_button).pad(5,5,5,5);
        this.root.add(start_button).pad(5,5,5,5);

        this.deadMsg.add(deadMsgLabel);
    }

    @Override
    public void show() {
        MoveUpdateHandler.INSTANCE.start();
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        this.batch.begin();
        // x axis render
        for(int x = 0; x< Gdx.graphics.getWidth() / ResourceHandler.INSTANCE.grass.getWidth(); x++){
            // y axis render
            for(int y = 0; y< Gdx.graphics.getHeight() / ResourceHandler.INSTANCE.grass.getHeight(); y++){
                this.batch.draw(ResourceHandler.INSTANCE.grass, ResourceHandler.INSTANCE.grass.getWidth() * x,  ResourceHandler.INSTANCE.grass.getHeight() * y);
            }
        }

        CapturePointHandler.instance.render(this.batch);
        CapturePointHandler.instance.update(delta);

        PlayerHandler.INSTANCE.render(this.batch);
        PlayerHandler.INSTANCE.update(delta);


        int[] players = PlayerHandler.INSTANCE.getPlayerCount();
        this.playerCount.setText(" Hunters: " + players[0] + " Ghosts: " + players[1]);
        int[] totalPlayers = PlayerHandler.INSTANCE.getReadyCount();
        this.totalPlayerCount.setText(totalPlayers[0] + "/" + totalPlayers[1] + " Players are ready");
        if(ready){
            this.ready_button.setText("Unready");
        }else{
            this.ready_button.setText("Ready");
        }

        if(totalPlayers[0] == totalPlayers[1] && (players[0] > 0 && players[0] <= players[1])){
            start_button.setDisabled(false);
        }else{
            start_button.setDisabled(true);
        }
        this.batch.end();
        this.stage.draw();
        this.stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        MoveUpdateHandler.INSTANCE.stop();
    }

    @Override
    public void dispose() {

    }

    public Label getDeadMsgLabel() {
        return deadMsgLabel;
    }

    public void toggleReady(){
        if(this.ready == false){
            this.ready = true;
        }else{
            this.ready = false;
        }
    }

    public void showStartButton(){
    }

    public void hideStartButton(){
        this.start_button.remove();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
