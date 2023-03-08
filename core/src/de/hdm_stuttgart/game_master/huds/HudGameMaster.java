package de.hdm_stuttgart.game_master.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class HudGameMaster {

    public final Stage stage;
    private Table table;
    private final BitmapFont font;

    public static Integer timer;
    private float timeCount;

    private final Label timeLabel;
    private final Label countdownLabel;

    public HudGameMaster(SpriteBatch sb) {
        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("fonts/font3.fnt"));

        timer = 120;
        timeCount = 0;
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));

        countdownLabel = new Label(String.format("%03d", timer), new Label.LabelStyle(font, Color.WHITE));

        createStage();
    }
    public void updateTimer(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            timer--;
            countdownLabel.setText(String.format("%03d", timer));
            timeCount = 0;
        }
    }

    public void createStage(){

        //table for time and icons
        table = new Table();
        table.setFillParent(true);
        table.top().pad(20);

        table.add(timeLabel);
        table.row();
        table.add(countdownLabel);

        stage.addActor(table);
    }
}