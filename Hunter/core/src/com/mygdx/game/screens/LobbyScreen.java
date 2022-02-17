package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.BackgroundMusic;
import com.mygdx.game.handlers.CapturePointHandler;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.MoveUpdateHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.supers.GameState;
import com.mygdx.game.supers.Player;
import com.mygdx.game.supers.Skill;
import com.mygdx.game.supers.Skills;
import com.mygdx.global.GameStartEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerReadyEvent;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class LobbyScreen implements Screen {

    public static final LobbyScreen INSTANCE = new LobbyScreen();
    private final String[] charSelectionText = new String[]{"Ghost 1", "Ghost 2", "Ghost 3", "Hunter 1", "Hunter 2", "Hunter 3"};

    public GameState gameState;

    private String playingPlayer;

    private final SpriteBatch batch;
    private final Stage stage;
    private final Stack rootStack;
    private final Table root;
    private final Table deadMsg;
    private final Table skillBar;
    private final TextButton previous;
    private final TextButton next;
    private final Table controlsGuide;


    private final TextButton ready_button;

    private final Label playerCount;
    private final Label charSelectionLabel;
    private final Label totalPlayerCount;
    private final Label deadMsgLabel;
    private final Label startingCountdown;
    private final Label blinkCooldown;
    private final Label skillDescription;
    private final Label controlsLabel;
    private final Label movement;
    private final Label skill;
    private final Label interact;
    private final Label scoreboard;
    private final TextButton skillShift;
    private final TextButton interactSpacebar;
    private final TextButton scoreboardTab;
    private Texture skillIcon;

    private boolean ready;
    private boolean countdownTimerLock;
    private long gameStartingTime;
    private int charSelectionIndex;

    private BackgroundMusic bgm = new BackgroundMusic("lobbyMusic");

    public LobbyScreen() {
        this.playingPlayer = "";
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());
        this.gameState = GameState.LOBBY;
        this.rootStack = new Stack();
        this.rootStack.setBounds(0, 0, 1200, 800);
        this.root = new Table().left().top();
        this.root.setBounds(0, 0, 1200, 800);
        this.deadMsg = new Table();
        this.deadMsg.setBounds(0, 0, 1200, 800);
        this.skillBar = new Table().right().bottom();
        this.skillBar.setBounds(0, 0, 1200, 800);
        this.controlsGuide = new Table().right().top();
        this.controlsGuide.setBounds(0, 0, 1200, 800);


        this.playerCount = LabelHandler.INSTANCE.createLabel("0", 16, Color.BLACK);
        this.totalPlayerCount = LabelHandler.INSTANCE.createLabel("0", 16, Color.BLACK);
        this.deadMsgLabel = LabelHandler.INSTANCE.createLabel(null, 34, Color.RED);
        this.startingCountdown = LabelHandler.INSTANCE.createLabel(null, 34, Color.RED);
        this.blinkCooldown = LabelHandler.INSTANCE.createLabel(null, 24, Color.RED);
        this.skillDescription = LabelHandler.INSTANCE.createLabel(null, 16, Color.BLACK);
        this.charSelectionLabel = LabelHandler.INSTANCE.createLabel(charSelectionText[charSelectionIndex], 16, Color.BLACK);
        this.controlsLabel = LabelHandler.INSTANCE.createLabel("CONTROLS", 26, Color.BLACK);
        this.movement = LabelHandler.INSTANCE.createLabel("Movement: W,A,S,D", 18, Color.BLACK);
        this.skill = LabelHandler.INSTANCE.createLabel("Skill: ", 18, Color.BLACK);
        this.interact = LabelHandler.INSTANCE.createLabel("Interact: ", 18, Color.BLACK);
        this.scoreboard = LabelHandler.INSTANCE.createLabel("Scoreboard: ", 18, Color.BLACK);
        this.skillIcon = Skill.getSkillIcon(Skills.DASH, false);
        this.ready = false;
        this.countdownTimerLock = false;
        this.charSelectionIndex = 0;

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.skillShift = new TextButton("L-shift", skin);
        this.skillShift.setTouchable(Touchable.disabled);
        this.scoreboardTab = new TextButton("tab", skin);
        this.scoreboardTab.setTouchable(Touchable.disabled);
        this.interactSpacebar = new TextButton("spacebar", skin);
        this.interactSpacebar.setTouchable(Touchable.disabled);
        this.previous = new TextButton("<", skin);
        this.previous.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (charSelectionIndex == 0) {
                    charSelectionIndex = 5;
                } else {
                    charSelectionIndex--;
                }
                charSelectionLabel.setText(charSelectionText[charSelectionIndex]);
                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = charSelectionIndex;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.next = new TextButton(">", skin);
        this.next.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (charSelectionIndex == 5) {
                    charSelectionIndex = 0;
                } else {
                    charSelectionIndex++;
                }
                charSelectionLabel.setText(charSelectionText[charSelectionIndex]);
                final PlayerCharacterChangeEvent playerCharacterChangeEvent = new PlayerCharacterChangeEvent();
                playerCharacterChangeEvent.characterType = charSelectionIndex;
                MyGdxGame.getInstance().getClient().sendTCP(playerCharacterChangeEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.ready_button = new TextButton("Ready", skin);
        this.ready_button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final PlayerReadyEvent playerReadyEvent = new PlayerReadyEvent();
                toggleReady();
                playerReadyEvent.ready = ready;
                MyGdxGame.getInstance().getClient().sendTCP(playerReadyEvent);
                return super.touchDown(event, x, y, pointer, button);
            }
        });


        this.batch.setProjectionMatrix(MyGdxGame.getInstance().getCamera().combined);

        this.stage.addActor(this.rootStack);
        this.rootStack.add(this.root);
        this.rootStack.add(this.skillBar);
        this.rootStack.add(this.controlsGuide);
        this.controlsGuide.add(controlsLabel).padRight(20).colspan(2).row();
        this.controlsGuide.add(movement).padRight(20).padBottom(5).padTop(5).colspan(2).row();
        this.controlsGuide.add(interact).colspan(1);
        this.controlsGuide.add(interactSpacebar).padRight(20).colspan(1).padBottom(5).padTop(5).row();
        this.controlsGuide.add(skill).colspan(1);
        this.controlsGuide.add(skillShift).padRight(20).colspan(1).padBottom(5).padTop(5).row();
        this.controlsGuide.add(scoreboard).colspan(1);
        this.controlsGuide.add(scoreboardTab).padRight(20).colspan(1).padBottom(5).padTop(5).row();

        this.rootStack.add(this.deadMsg.center());

        this.setToDefault();
    }
    public void setToDefault() {
        Player player = PlayerHandler.INSTANCE.getPlayerByUsername(this.playingPlayer);
        this.root.clear();
        switch (gameState) {
            case LOBBY:
                this.root.add(previous).width(25).pad(5, 5, 5, 5);
                this.root.add(charSelectionLabel).fillX();
                this.root.add(next).width(25).pad(5, 5, 5, 5).row();
                this.root.add(playerCount).pad(5, 5, 5, 5).colspan(3).row();
                this.root.add(totalPlayerCount).pad(5, 5, 5, 5).colspan(3).row();
                this.root.add(ready_button).pad(5, 5, 5, 5).colspan(3).row();
                this.root.add(skillDescription).colspan(3);
                break;
            case READY:
                this.root.add(ready_button).pad(5, 5, 5, 5);
                this.root.add(playerCount).padLeft(15).row();
                this.root.add(totalPlayerCount).padLeft(15).row();
                startingCountdown.remove();
                break;
            case ALLPLAYERSREADY:
                this.root.add(ready_button).pad(5, 5, 5, 5);
                this.root.add(playerCount).padLeft(15).row();
                this.root.add(totalPlayerCount).padLeft(15).row();
                this.deadMsg.clear();
                this.deadMsg.add(startingCountdown);
                break;
            case STARTED:
                break;
        }

        //this.deadMsg.add(deadMsgLabel);
    }

    @Override
    public void show() {
        MoveUpdateHandler.INSTANCE.start();
        Gdx.input.setInputProcessor(this.stage);
        this.bgm.playMusic();
    }

    @Override
    public void render(float delta) {
        this.batch.begin();
        // x axis render
        for (int x = 0; x < Gdx.graphics.getWidth() / 100; x++) {
            // y axis render
            for (int y = 0; y < Gdx.graphics.getHeight() / 100; y++) {
                Texture mapTexture = ResourceHandler.INSTANCE.grass_one;
                this.batch.draw(mapTexture, mapTexture.getWidth() * x, mapTexture.getHeight() * y);
            }
        }
        int[] players = PlayerHandler.INSTANCE.getPlayerCount();
        this.playerCount.setText(" Hunters: " + players[0] + " Ghosts: " + players[1]);
        int[] totalPlayers = PlayerHandler.INSTANCE.getReadyCount();
        this.totalPlayerCount.setText(totalPlayers[0] + "/" + totalPlayers[1] + " Players are ready");
        this.skillDescription.setText(PlayerHandler.INSTANCE.getPlayerByUsername(playingPlayer).getSkillDescription());
        if (totalPlayers[0] == totalPlayers[1] && (players[0] > 0 && players[0] <= players[1])) {
            gameState = GameState.ALLPLAYERSREADY;
            if (countdownTimerLock) {

            } else {
                gameStartingTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5);
                countdownTimerLock = true;
            }
            long seconds = ((gameStartingTime - System.currentTimeMillis()) / 1000) % 60;
            startingCountdown.setText("Game starting in " + seconds);
            if (seconds <= 0) {
                gameState = GameState.STARTED;
            }
        } else {
            if (ready) {
                this.ready_button.setText("Unready");
                gameState = GameState.READY;

            } else {
                this.ready_button.setText("Ready");
                startingCountdown.setText(null);
                countdownTimerLock = false;
                gameState = GameState.LOBBY;
            }
        }
        if (gameState == GameState.STARTED) {
            System.out.println("game starting");
            CapturePointHandler.instance.resetCapturePoints();
            PlayerHandler.INSTANCE.resetPlayerHP();
            GameInProgressScreen.INSTANCE.resetGame();
            MyGdxGame.getInstance().getClient().sendTCP(new GameStartEvent());
            this.gameState = GameState.RUNNING;
        }

        PlayerHandler.INSTANCE.render(this.batch);
        PlayerHandler.INSTANCE.update(delta);

        showBlinkCdTimer();

        setToDefault();

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
        this.bgm.stopMusic();
    }

    @Override
    public void dispose() {

    }

    public Label getDeadMsgLabel() {
        return deadMsgLabel;
    }

    public void toggleReady() {
        this.ready = this.ready == false;
    }

    public void showBlinkCdTimer() {
        Player player = PlayerHandler.INSTANCE.getPlayerByUsername(this.playingPlayer);
        Image skillImage = new Image(skillIcon);
        skillImage.setAlign(Align.center);
        skillBar.clear();
        Stack stack = new Stack();
        float blinkCdSeconds = (player.getSkillCD() - System.currentTimeMillis()) / 1000;
        float milliseconds = (player.getSkillCD() - System.currentTimeMillis()) % 1000;
        blinkCdSeconds += milliseconds / 1000;
        DecimalFormat format = new DecimalFormat("#.##");
        if (blinkCdSeconds <= 0) {
            blinkCooldown.setText("");
            skillIcon = Skill.getSkillIcon(player.getSkill(), false);
        } else {
            blinkCooldown.setText(format.format(blinkCdSeconds));
            skillIcon = Skill.getSkillIcon(player.getSkill(), true);
        }
        stack.add(new Image(skillIcon));
        stack.add(new Image(ResourceHandler.INSTANCE.skill_frame));
        blinkCooldown.setAlignment(Align.center);
        stack.add(blinkCooldown);
        skillBar.add(stack).pad(15, 15, 15, 15);
    }

    public void reset() {
        this.ready = false;
    }

    public String getPlayingPlayer() {
        return playingPlayer;
    }

    public void setPlayingPlayer(String playingPlayer) {
        this.playingPlayer = playingPlayer;
    }
}
