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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.MoveUpdateHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.supers.GameState;
import com.mygdx.global.GameRestartEvent;

import java.util.concurrent.TimeUnit;

public class GameInProgressScreen implements Screen {

    public static final GameInProgressScreen INSTANCE = new GameInProgressScreen();
    private final SpriteBatch batch;
    private final Stage stage;
    private final Table root;
    private final Table endMsg;
    private final Stack rootStack;
    private GameState gameState;
    private long gameEndTime;
    private long gameCurrentTime;

    private final Label gameProgressTime;
    private final Label survivorsWinLabel;
    private final Label huntersWinLabel;

    private final TextButton restart_button;
    //private final Label endCauseLabel;

    public GameInProgressScreen() {
        this.batch = new SpriteBatch();
        this.batch.setProjectionMatrix(MyGdxGame.getInstance().getCamera().combined);
        this.stage = new Stage();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());
        this.rootStack = new Stack();
        this.rootStack.setBounds(0,0,800,600);
        this.root = new Table().top().left();
        this.root.setBounds(0, 0, 800, 600);
        this.endMsg = new Table().center();
        this.endMsg.setBounds(0,0,800,600);
        this.gameState = GameState.RUNNING;
        this.gameEndTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        this.gameProgressTime = LabelHandler.INSTANCE.createLabel("0", 32, Color.RED);

        this.survivorsWinLabel = LabelHandler.INSTANCE.createLabel(null, 32, Color.GREEN);
        this.huntersWinLabel = LabelHandler.INSTANCE.createLabel(null, 32, Color.RED);

        final Skin skin = new Skin (Gdx.files.internal("uiskin.json"));
        this.restart_button = new TextButton("Back to lobby", skin);
        this.restart_button.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                CapturePointHandler.instance.resetCapturePoints();
                PlayerHandler.INSTANCE.resetPlayerHP();
                GameRestartEvent gameRestartEvent = new GameRestartEvent();
                MyGdxGame.getInstance().getClient().sendTCP(gameRestartEvent);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.stage.addActor(this.root);

        this.setToDefault();
    }

    @Override
    public void show() {
        MoveUpdateHandler.INSTANCE.start();
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        this.batch.begin();

        for (int x = 0; x < Gdx.graphics.getWidth() / ResourceHandler.INSTANCE.grass.getWidth(); x++) {
            // y axis render
            for (int y = 0; y < Gdx.graphics.getHeight() / ResourceHandler.INSTANCE.grass.getHeight(); y++) {
                this.batch.draw(ResourceHandler.INSTANCE.grass, ResourceHandler.INSTANCE.grass.getWidth() * x, ResourceHandler.INSTANCE.grass.getHeight() * y);
            }
        }
        long minutes, seconds;
        if(gameState != GameState.ENDED){
            gameCurrentTime = gameEndTime - System.currentTimeMillis();
        }
        minutes = (gameCurrentTime / 1000) / 60;
        seconds = (gameCurrentTime / 1000) % 60;
        this.gameProgressTime.setText("" + minutes + ":" + seconds + "left");

        CapturePointHandler.instance.render(this.batch);
        CapturePointHandler.instance.update(delta);

        PlayerHandler.INSTANCE.render(this.batch);
        PlayerHandler.INSTANCE.update(delta);

        if(gameCurrentTime > 0){
            if(PlayerHandler.INSTANCE.areAllSurvivorsDead()){
                gameState = GameState.HUNTERSWIN;
                showEndMsg(gameState,"All survivors are dead!");
            }else if(CapturePointHandler.instance.isAllCapturePointsCaptured()){
                gameState = GameState.SURVIVORSWIN;
                showEndMsg(gameState,"All capture points secured!");
            }
        }else if(gameCurrentTime <= 0 ){
            if(CapturePointHandler.instance.isAllCapturePointsCaptured() == false){
                gameState = GameState.HUNTERSWIN;
                showEndMsg(gameState,"Time's up!");

            }
        }else{
            gameState = GameState.RUNNING;
        }

        if(gameState == GameState.HUNTERSWIN || gameState == GameState.SURVIVORSWIN){
            gameState = GameState.ENDED;
        }

        this.stage.addActor(this.rootStack);
        this.rootStack.add(this.root);
        this.rootStack.add(this.endMsg);

        this.batch.end();
        this.stage.draw();
        this.stage.act(delta);
    }

    public void setToDefault() {
        this.root.clear();
        this.root.add(gameProgressTime);
    }

    public void showEndMsg(GameState state, String endCause){
        switch(state){
            case SURVIVORSWIN:
                this.survivorsWinLabel.setText("SURVIVORS WIN!");
                this.endMsg.clear();
                this.endMsg.add(survivorsWinLabel).row();
                this.endMsg.add(LabelHandler.INSTANCE.createLabel(endCause,24,Color.GREEN)).row();
                this.endMsg.add(restart_button);
                break;
            case HUNTERSWIN:
                this.huntersWinLabel.setText("HUNTERS WIN!");
                this.endMsg.clear();
                this.endMsg.add(huntersWinLabel).row();
                this.endMsg.add(LabelHandler.INSTANCE.createLabel(endCause,24,Color.RED)).row();
                this.endMsg.add(restart_button);
                break;
        }
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void resetGame(){
        this.gameState = GameState.RUNNING;
        this.gameEndTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        this.endMsg.clear();
    }
}
