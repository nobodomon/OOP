package com.mygdx.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.supers.Player;

public class Scoreboard extends Table {
    public static Scoreboard INSTANCE = new Scoreboard();

    private Table scoreboard;
    private Table hunterBoard;
    private Table ghostBoard;

    public Scoreboard() {
        scoreboard = new Table();
        hunterBoard = new Table();
        ghostBoard = new Table();

        scoreboard.setBounds(0, 0, 1000, 600);
        hunterBoard.setBounds(0, 0, 1000, 300);
        ghostBoard.setBounds(0, 0, 1000, 300);
    }

    public Table getTable() {
        return scoreboard;
    }

    public void update() {
        scoreboard.clear();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                scoreboard.add(hunterBoard).row();
                Label hunterLabel = LabelHandler.INSTANCE.createLabel("HUNTERS", 26, Color.RED);
                hunterBoard.add(hunterLabel);
                scoreboard.add(ghostBoard);
                Label ghostLabel = LabelHandler.INSTANCE.createLabel("GHOSTS", 26, Color.GREEN);
                ghostBoard.add(ghostLabel);
                for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
                    final Player currPlayer = PlayerHandler.INSTANCE.getPlayers().get(i);
                    final Label playerLabel = LabelHandler.INSTANCE.createLabel(currPlayer.getUsername() + " " + currPlayer.getHealth(), 24, Color.BLACK);
                    if (Player.getIntByType(currPlayer.getPlayerType()) > 2) {
                        hunterBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(75, 75;
                        hunterBoard.add(playerLabel).row();
                    } else {
                        ghostBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(75, 75);
                        ghostBoard.add(playerLabel).row();
                    }
                }
            }
        });
    }
}
