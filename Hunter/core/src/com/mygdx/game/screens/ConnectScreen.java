package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.network.ConnectionStateListener;
import com.mygdx.game.network.EventListener;
import com.mygdx.global.CapturePointCreateEvent;
import com.mygdx.global.CapturePointDeleteEvent;
import com.mygdx.global.CapturePointUpdateEvent;
import com.mygdx.global.GameRestartEvent;
import com.mygdx.global.GameStartEvent;
import com.mygdx.global.JoinRequestEvent;
import com.mygdx.global.JoinResponseEvent;
import com.mygdx.global.MoveUpdateEvent;
import com.mygdx.global.PlayerAddEvent;
import com.mygdx.global.PlayerCapturingEvent;
import com.mygdx.global.PlayerCharacterChangeEvent;
import com.mygdx.global.PlayerHPupdateEvent;
import com.mygdx.global.PlayerHitEvent;
import com.mygdx.global.PlayerKilledEvent;
import com.mygdx.global.PlayerReadyEvent;
import com.mygdx.global.PlayerRemoveEvent;
import com.mygdx.global.PlayerTransferEvent;
import com.mygdx.global.PlayerUpdateEvent;
import com.mygdx.server.ServerFoundation;


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

    private final Label errorLabel;

    public ConnectScreen() {
        this.stage = new Stage();
        this.batch = new SpriteBatch();
        this.stage.getViewport().setCamera(MyGdxGame.getInstance().getCamera());

        this.root = new Table();
        this.root.setBounds(0, 0, 1200, 800);

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.rootGroup = new HorizontalGroup();
        this.inputGroup = new Table();
        this.ipAddressLabel = new TextField("", skin);
        this.portLabel = new TextField("", skin);
        this.usernameLabel = new TextField("", skin);
        this.horizontalGroup = new HorizontalGroup().expand();
        this.hostButton = new TextButton("Host", skin);
        this.hostButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    ServerFoundation.main(Integer.parseInt(portLabel.getText()), Integer.parseInt(portLabel.getText()));
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                    return super.touchDown(event, x, y, pointer, button);
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
                    return super.touchDown(event, x, y, pointer, button);
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
        this.connectButton.addListener(new ClickListener() {
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
                    client.connect(15000, ipAddressLabel.getText(), Integer.parseInt(portLabel.getText()), Integer.parseInt(portLabel.getText()));
                } catch (Exception e) {
                    errorLabel.setText(e.getMessage());
                    return super.touchDown(event, x, y, pointer, button);
                }
                // Success
                MyGdxGame.getInstance().setClient(client);

                JoinRequestEvent joinRequestEvent = new JoinRequestEvent();
                joinRequestEvent.username = usernameLabel.getText();

                client.sendTCP(joinRequestEvent);

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.errorLabel = LabelHandler.INSTANCE.createLabel(null, 16, Color.RED);

        this.stage.addActor(this.root);

        this.setToDefault();
    }

    public void setToDefault() {
        this.root.clear();
        Label ipAddressLabel = LabelHandler.INSTANCE.createLabel("IP address", 24, Color.WHITE);
        this.inputGroup.add(new Image(ResourceHandler.INSTANCE.title)).row();
        this.inputGroup.add(ipAddressLabel).left().row();
        this.inputGroup.add(this.ipAddressLabel).fillX().colspan(2).row();
        Label portLabel = LabelHandler.INSTANCE.createLabel("Port number", 24, Color.WHITE);
        this.inputGroup.add(portLabel).left().row();
        this.inputGroup.add(this.portLabel).fillX().colspan(2).row();
        Label usernameLabel = LabelHandler.INSTANCE.createLabel("Username", 24, Color.WHITE);
        this.inputGroup.add(usernameLabel).left().row();
        this.inputGroup.add(this.usernameLabel).fillX().colspan(2).row();
        this.inputGroup.add(this.hostButton).size(250, 50).padTop(25);
        this.inputGroup.add(this.connectButton).size(250, 50).padTop(25);
        // this.inputGroup.add(this.horizontalGroup).size(250,50).padTop(100);
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
        this.batch.begin();
        for (int x = 0; x < Gdx.graphics.getWidth() / 100; x++) {
            // y axis render
            for (int y = 0; y < Gdx.graphics.getHeight() / 100; y++) {
                Texture mapTexture = ResourceHandler.INSTANCE.grass_one;
                this.batch.draw(mapTexture, mapTexture.getWidth() * x, mapTexture.getHeight() * y);
            }
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

    }

    @Override
    public void dispose() {

    }

    public Label getErrorLabel() {
        return errorLabel;
    }
}
