package de.hdm_stuttgart.game_master.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.screens.SelectionScreen;

import java.util.Objects;

public class HudPlayer {

    public final Stage stage;

    private Table table;
    private Table tableBars;

    private final Image player1Icon;
    private Image player1HealthBar;
    private Image player1StaminaBar;
    private Image player2Icon;
    private Image player2HealthBar;
    private Image player2StaminaBar;
    private Image player3Icon;
    private Image player3HealthBar;
    private Image player3StaminaBar;
    private Image player4Icon;
    private Image player4HealthBar;
    private Image player4StaminaBar;

    private final BitmapFont font;

    private float timeCount;

    private  Label timeLabel;
    private  Label countdownLabel;

    public HudPlayer(SpriteBatch sb) {
        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("fonts/font3.fnt"));

        timeCount = 0;
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));

        countdownLabel = new Label(String.format("%03d", GameWorld.getTimer()), new Label.LabelStyle(font, Color.WHITE));

        if(Objects.equals(SelectionScreen.getCharType(), "rangedDD")) {
            player1Icon = new Image(new Texture("hud/icons/RangedDD_frame.png"));
        } else if (Objects.equals(SelectionScreen.getCharType(), "meleeDD")) {
            player1Icon = new Image(new Texture("hud/icons/MeleeDD_frame.png"));
        } else if (Objects.equals(SelectionScreen.getCharType(), "tank")) {
            player1Icon = new Image(new Texture("hud/icons/Tank_frame.png"));
        } else {
            player1Icon = new Image(new Texture("hud/icons/Healer_frame.png"));
        }


        player2Icon = new Image(new Texture("hud/alpha_icon.png"));
        player3Icon = new Image(new Texture("hud/alpha_icon.png"));
        player4Icon = new Image(new Texture("hud/alpha_icon.png"));

        player1HealthBar = new Image(new Texture("hud/HealthBar/17hp.png"));
        player2HealthBar = new Image(new Texture("hud/alpha_healthBar.png"));
        player3HealthBar = new Image(new Texture("hud/alpha_healthBar.png"));
        player4HealthBar = new Image(new Texture("hud/alpha_healthBar.png"));
        player1StaminaBar = new Image(new Texture("hud/alpha_staminaBar.png"));
        player2StaminaBar = new Image(new Texture("hud/alpha_staminaBar.png"));
        player3StaminaBar = new Image(new Texture("hud/alpha_staminaBar.png"));
        player4StaminaBar = new Image(new Texture("hud/alpha_staminaBar.png"));

        createStage();
    }

    public void updateHbarPlayer1(String url){
        player1HealthBar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(url))));
        createStage();
    }
    public void updateHbarPlayer2(Image img){
        player2HealthBar.setDrawable(img.getDrawable());
        createStage();
    }
    public void updateHbarPlayer3(Image img){
        player3HealthBar.setDrawable(img.getDrawable());
        createStage();
    }
    public void updateHbarPlayer4(Image img){
        player4HealthBar.setDrawable(img.getDrawable());
        createStage();
    }

    public void updateTimer(float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            GameWorld.countDownTime();
            countdownLabel.setText(String.format("%03d", GameWorld.getTimer()));
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

        table.row();
        table.add(player2Icon).left().size(33);

        table.row().padTop(15);
        table.add(player3Icon).left().size(33);

        table.row().padTop(15);
        table.add(player4Icon).left().size(33);

        table.row();
        table.add().growY().growX();

        table.row().padTop(15);
        table.add(player1Icon).left().size(70);

        tableBars = new Table();
        tableBars.setFillParent(true);
        tableBars.top().left().pad(20).padLeft(54).padTop(90);

        tableBars.add(player2HealthBar).left().width(33).height(8);
        tableBars.row().padTop(1);
        tableBars.add(player2StaminaBar).left().width(24).height(6);

        tableBars.row().padTop(33);
        tableBars.add(player3HealthBar).left().width(33).height(8);
        tableBars.row().padTop(1);
        tableBars.add(player3StaminaBar).left().width(24).height(6);

        tableBars.row().padTop(34);
        tableBars.add(player4HealthBar).left().width(33).height(8);
        tableBars.row().padTop(1);
        tableBars.add(player4StaminaBar).left().width(24).height(6);

        tableBars.row();
        tableBars.add().growY().growX().colspan(2);

        tableBars.row().padLeft(38);
        tableBars.add(player1HealthBar).left();
        tableBars.row().padTop(1).padLeft(38).padBottom(30);
        tableBars.add(player1StaminaBar).left();

        stage.addActor(table);
        stage.addActor(tableBars);
    }

    public void setPlayer2Icon(Image player2Icon) {
        this.player2Icon.setDrawable(player2Icon.getDrawable());
    }

    public void setPlayer3Icon(Image player3Icon) {
        this.player3Icon.setDrawable(player3Icon.getDrawable());
    }

    public void setPlayer4Icon(Image player4Icon) {
        this.player4Icon.setDrawable(player4Icon.getDrawable());
    }

    public Image getPlayer1Icon() {
        return player1Icon;
    }

    public Label getCountdownLabel() {
        return countdownLabel;
    }
    public Label getTimeLabel() {
        return timeLabel;
    }
}