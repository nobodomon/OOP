package com.mygdx.game.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
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
        scoreboardStack = new Stack();

        Image scoreboardBg = new Image(ResourceHandler.INSTANCE.scoreboardBG);
        scoreboardBgTable = new Table();

        scoreboard = new Table();
        hunterBoard = new Table();
        ghostBoard = new Table();

        scoreboardStack.setBounds(0,0,1000,600);

        scoreboardStack.setSize(1000,600);

        scoreboardBgTable.setBounds(0,0,1000,600);
        scoreboardBgTable.add(scoreboardBg).size(1000,600);

        scoreboard.setBounds(0, 0, 1000, 600);
        hunterBoard.setBounds(0, 0, 1000, 300);
        ghostBoard.setBounds(0, 0, 1000, 300);


        scoreboardStack.add(scoreboardBgTable);
        scoreboardStack.add(scoreboard);

        hunterBoard.top().left();
        ghostBoard.top().left();
        //scoreboard.align(Align.topLeft);

        //scoreboard.debug();
        scoreboard.add(hunterBoard).expand().size(1000,150).bottom().row();
        scoreboard.add(ghostBoard).expand().size(1000,450).top();
    }

    public Stack getScoreboard() {
        return scoreboardStack;
    }

    public void update() {
        hunterBoard.clear();
        ghostBoard.clear();
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Label hunterLabel = LabelHandler.INSTANCE.createLabel("HUNTERS", 26, Color.RED);
                hunterBoard.add(hunterLabel).colspan(2).align(Align.left).pad(25,25,0,0).row();
                Label ghostLabel = LabelHandler.INSTANCE.createLabel("GHOSTS", 26, Color.GREEN);
                ghostBoard.add(ghostLabel).colspan(2).align(Align.left).pad(25,25,0,0).row();
                Label playerLabel;
                for (int i = 0; i < PlayerHandler.INSTANCE.getPlayers().size(); i++) {
                    final Player currPlayer = PlayerHandler.INSTANCE.getPlayers().get(i);
                    if (Player.getIntByType(currPlayer.getPlayerType()) > 2) {
                        hunterBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(50, 50).align(Align.left).pad(25,25,0,0);
                        playerLabel = LabelHandler.INSTANCE.createLabel(currPlayer.getUsername() + " " + currPlayer.getHealth() + "HP", 24, Color.BLACK);
                        hunterBoard.add(playerLabel).align(Align.left).expandX().pad(25,25,0,0).row();
                    } else {
                        if(currPlayer.getHealth() == 0.0){
                            playerLabel = LabelHandler.INSTANCE.createLabel(currPlayer.getUsername() + " DEAD", 24, Color.RED);
                        }else{
                            playerLabel = LabelHandler.INSTANCE.createLabel(currPlayer.getUsername() + " " + currPlayer.getHealth() + "HP", 24, Color.BLACK);
                        }
                        ghostBoard.add(new Image(Player.getPlayerIcon(currPlayer.getPlayerType()))).size(50, 50).align(Align.left).pad(25,25,0,0);
                        ghostBoard.add(playerLabel).align(Align.left).expandX().pad(25,25,0,0).row();
                    }
                }
            }
        });
    }
}
