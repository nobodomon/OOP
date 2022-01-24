package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.supers.PlayerState;
import com.mygdx.global.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.network.ConnectionStateListener;
import com.mygdx.game.network.EventListener;
import com.mygdx.server.ServerFoundation;
import com.mygdx.server.handlers.PlayerHandler;
import com.mygdx.server.supers.ServerCapturePoint;
import com.mygdx.server.supers.ServerPlayer;

import java.util.LinkedList;


public class ConnectScreen implements Screen {

    public static final ConnectScreen INSTANCE = new ConnectScreen();

    private final SpriteBatch batch;

    private final Stage stage;
    private final Table root;

    private final TextField ipAddressLabel;
    private final TextField portLabel;
    private final TextField usernameLabel;

    private final HorizontalGroup rootGroup;
    private final Table inputGroup;
    private final HorizontalGroup horizontalGroup;
    private final TextButton connectButton;
    private final TextButton hostButton;

    private float pastTime;
    private final Label errorLabel;

    public ConnectScreen(){
        this.stage = new Stage();
        this.batch = new SpriteBatch();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());

        this.root = new Table();
        this.root.setBounds(0,0,1200, 800);

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.rootGroup = new HorizontalGroup();
        this.inputGroup = new Table();
        this.ipAddressLabel = new TextField("localhost", skin);
        this.portLabel = new TextField("6334", skin);
        this.usernameLabel = new TextField("Username", skin);
        this.horizontalGroup = new HorizontalGroup().expand();
        this.hostButton = new TextButton("Host", skin);
        this.hostButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try{

                    ServerFoundation.main(Integer.parseInt(portLabel.getText()), Integer.parseInt(portLabel.getText()));
                }catch (Exception e){
                    errorLabel.setText(e.getMessage());
                    return super.touchDown(event,x,y,pointer,button);
                }
                final Client client = new Client();

                client.addListener(new ConnectionStateListener());
                client.addListener(new EventListener());

                client.getKryo().register(JoinRequestEvent.class);
                client.getKryo().register(JoinResponseEvent.class);
                client.getKryo().register(PlayerAddEvent.class);
                client.getKryo().register(CapturePointCreateEvent.class);
                client.getKryo().register(CapturePointDeleteEvent.class);
                client.getKryo().register(PlayerRemoveEvent.class);
                client.getKryo().register(PlayerUpdateEvent.class);
                client.getKryo().register(PlayerTransferEvent.class);
                client.getKryo().register(PlayerCapturingEvent.class);
                client.getKryo().register(CapturePointUpdateEvent.class);
                client.getKryo().register(MoveUpdateEvent.class);
                client.getKryo().register(PlayerCharacterChangeEvent.class);
                client.getKryo().register(PlayerReadyEvent.class);
                client.getKryo().register(PlayerHitEvent.class);
                client.getKryo().register(PlayerKilledEvent.class);
                client.getKryo().register(PlayerHPupdateEvent.class);
                client.getKryo().register(GameStartEvent.class);
                client.getKryo().register(GameRestartEvent.class);
                client.getKryo().register(String.class);
                client.getKryo().register(Color.class);

                try {
                    client.start();
                    client.connect(15000, ipAddressLabel.getText(), Integer.parseInt(portLabel.getText()), Integer.parseInt(portLabel.getText()));
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                    return super.touchDown(event,x,y,pointer,button);
                }
                // Success
                MyGdxGame.getInstance().setClient(client);

                JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
                joinRequestEvent.username = usernameLabel.getText();

                client.sendTCP(joinRequestEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.connectButton = new TextButton("Connect", skin);
        this.connectButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                final Client client = new Client();

                client.addListener(new ConnectionStateListener());
                client.addListener(new EventListener());

                client.getKryo().register(JoinRequestEvent.class);
                client.getKryo().register(JoinResponseEvent.class);
                client.getKryo().register(PlayerAddEvent.class);
                client.getKryo().register(CapturePointCreateEvent.class);
                client.getKryo().register(CapturePointDeleteEvent.class);
                client.getKryo().register(PlayerRemoveEvent.class);
                client.getKryo().register(PlayerUpdateEvent.class);
                client.getKryo().register(PlayerTransferEvent.class);
                client.getKryo().register(PlayerCapturingEvent.class);
                client.getKryo().register(CapturePointUpdateEvent.class);
                client.getKryo().register(MoveUpdateEvent.class);
                client.getKryo().register(PlayerCharacterChangeEvent.class);
                client.getKryo().register(PlayerReadyEvent.class);
                client.getKryo().register(PlayerHitEvent.class);
                client.getKryo().register(PlayerKilledEvent.class);
                client.getKryo().register(PlayerHPupdateEvent.class);
                client.getKryo().register(GameStartEvent.class);
                client.getKryo().register(GameRestartEvent.class);
                client.getKryo().register(String.class);
                client.getKryo().register(Color.class);

                try {
                    client.start();
                    if (PlayerHandler.INSTANCE.getPlayerByUsername(usernameLabel.getText()) != null) {
                        return super.touchDown(event,x,y,pointer,button);
                    }
                    client.connect(15000, ipAddressLabel.getText(), Integer.parseInt(portLabel.getText()), Integer.parseInt(portLabel.getText()));

                    // Success
                    MyGdxGame.getInstance().setClient(client);

                    JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
                    joinRequestEvent.username = usernameLabel.getText();

                    client.sendTCP(joinRequestEvent);
                    return super.touchDown(event, x, y, pointer, button);

                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                    return super.touchDown(event,x,y,pointer,button);
                }


            }
        });

        this.errorLabel = LabelHandler.INSTANCE.createLabel(null, 16, Color.RED);

        this.stage.addActor(this.root);

        this.setToDefault();
    }

    public void setToDefault(){
        this.root.clear();
        this.inputGroup.add(this.ipAddressLabel).width(250).row();
        this.inputGroup.add(this.portLabel).width(250).padTop(25).row();
        this.inputGroup.add(this.usernameLabel).width(250).padTop(50).row();
        this.hostButton.setSize(250,50);
        this.connectButton.setSize(250,50);
        this.horizontalGroup.addActor(this.hostButton);
        this.horizontalGroup.addActor(this.connectButton);
        this.inputGroup.add(this.horizontalGroup).size(250,50).padTop(100);
//        this.root.add(this.hostButton).size(250,50).padTop(100).row();
//        this.root.add(this.connectButton).size(250,50).padTop(25).row();
        this.rootGroup.addActor(inputGroup);
        this.root.add(rootGroup);
        this.root.add(this.errorLabel).padTop(50);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        this.stage.draw();
        this.stage.act(delta);
    }

    public void update(final float delta){
        this.pastTime += delta;
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

    }

    @Override
    public void dispose() {

    }

    public Label getErrorLabel(){
        return errorLabel;
    }
}
