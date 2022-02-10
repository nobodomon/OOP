package com.mygdx.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.handlers.LabelHandler;
import com.mygdx.game.handlers.PlayerHandler;
import com.mygdx.game.handlers.ResourceHandler;
import com.mygdx.game.supers.Player;

public class Scoreboard extends Table {
    public static Scoreboard INSTANCE = new Scoreboard();

    private Table scoreboard;
    private Table scoreboardBgTable;
    private Stack scoreboardStack;
    private Table hunterBoard;
    private Table ghostBoard;

    public Scoreboard() {
        scoreboard = new Table();
        scoreboardBgTable = new Table();
        Image scoreboardBg = new Image(ResourceHandler.INSTANCE.scoreboardBG);
        scoreboardStack = new Stack();
        hunterBoard = new Table();
        ghostBoard = new Table();

        scoreboardStack.setBounds(0,0,1000,600);
        scoreboardBgTable.setBounds(0,0,1000,600);
        scoreboardBgTable.add(scoreboardBg).size(1000,600);
        scoreboard.setBounds(0, 0, 1000, 600);
        hunterBoard.setBounds(0, 0, 1000, 300);
        ghostBoard.setBounds(0, 0, 1000, 300);
    }

    public Stack getScoreboard() {
        return scoreboardStack;
    }

    public void update() {
        scoreboardStack.clear();
        scoreboard.clear();
        hunterBoard.clear();
        ghostBoard.clear();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                scoreboardStack.add(scoreboardBgTable);
                scoreboardStack.add(scoreboard);
                scoreboard.add(hunterBoard).row();
                Label hunterLabel = LabelHandler.INSTANCE.createLabel("HUNTERS", 26, Color.RED);
                hunterBoard.add(hunterLabel).row();
                scoreboard.add(ghostBoard);
                Label ghostLabel = LabelHandler.INSTANCE.createLabel("GHOSTS", 26, Color.GREEN);
                ghostBoard.add(ghostLabel).row();
                for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
                    final Player currPlayer = PlayerHandler.INSTANCE.getPlayers().get(i);
                    final Label playerLabel = LabelHandler.INSTANCE.createLabel(currPlayer.getUsername() + " " + currPlayer.getHealth(), 24, Color.BLACK);
                    if (Player.getIntByType(currPlayer.getPlayerType()) > 2) {
                        hunterBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(50, 50);
                        hunterBoard.add(playerLabel).row();
                    } else {
                        ghostBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(50, 50);
                        ghostBoard.add(playerLabel).row();
                    }
                }
            }
        });
    }
}
