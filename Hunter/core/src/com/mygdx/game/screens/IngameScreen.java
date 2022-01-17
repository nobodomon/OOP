package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.MoveUpdateHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.network.EventListener;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.PlayerType;
import com.mygdx.global.MoveUpdateEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;

public class IngameScreen implements Screen {

    public static final IngameScreen INSTANCE = new IngameScreen();

    private final SpriteBatch batch;
    private final Stage stage;
    private final Table root;

    private final TextButton ghost_one_button;
    private final TextButton ghost_two_button;
    private final TextButton ghost_three_button;
    private final TextButton minotaur_one_button;
    private final TextButton minotaur_two_button;
    private final TextButton minotaur_three_button;
    private final Label playerCount;

    private final boolean hunter;
    private final int characterChoice;

    public IngameScreen(){
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());

        this.root = new Table().left().top();
        this.root.setBounds(0,0,800,600);

        final Skin skin = new Skin (Gdx.files.internal("uiskin.json"));

        this.playerCount = LabelHandler.INSTANCE.createLabel("0", 16, Color.BLACK);
        this.hunter = false;
        this.characterChoice = 0;
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

        this.batch.setProjectionMatrix(MyGdxGame.getInstance().getCamera().combined);

        this.stage.addActor(this.root);

        this.setToDefault();
    }

    public void setToDefault(){
        this.root.clear();
        this.root.add(ghost_one_button).pad(5,5,5,5);
        this.root.add(ghost_two_button).pad(5,5,5,5);
        this.root.add(ghost_three_button).pad(5,5,5,5);
        this.root.add(playerCount).row();
        this.root.add(minotaur_one_button).pad(5,5,5,5);
        this.root.add(minotaur_two_button).pad(5,5,5,5);
        this.root.add(minotaur_three_button).pad(5,5,5,5);

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

        PlayerHandler.INSTANCE.render(this.batch);
        PlayerHandler.INSTANCE.update(delta);
        int[] players = PlayerHandler.INSTANCE.getPlayerCount();
        this.playerCount.setText(" Hunters: " + players[0] + " Ghosts: " + players[1]);
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

    public Label getPlayerCount() {
        return playerCount;
    }

    public void incrementPlayerCount(){
        this.playerCount.setText(Integer.parseInt(this.playerCount.getText().toString()) + 1);
    }

    public void decrementPlayerCount(){
        this.playerCount.setText(Integer.parseInt(this.playerCount.getText().toString()) - 1);
    }
}
