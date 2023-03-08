package de.hdm_stuttgart.game_master.huds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.helper.HudHelper;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.networking.ServerDriver;
import de.hdm_stuttgart.game_master.screens.GameScreen;

public class HudInGameOptions {

    private Stage stage;

    private ImageButton startRoundButton;
    private Texture textureStartRoundUnclicked;
    private Texture textureStartRoundClicked;
    private Drawable drawStartRoundClicked;
    private Drawable drawStartRoundUnclicked;

    private ImageButton quitButton;
    private Texture textureQuitClicked;
    private Texture textureQuitUnclicked;
    private Drawable drawQuitClicked;
    private Drawable drawQuitUnclicked;

    private ImageButton mute;
    private Texture textureMuteClicked;
    private Texture textureMuteUnclicked;
    private Drawable drawMuteClicked;
    private Drawable drawMuteUnclicked;

    private ImageButton resumeButton;
    private Texture textureResumeClicked;
    private Texture textureResumeUnclicked;
    private Drawable drawResumeClicked;
    private Drawable drawResumeUnclicked;

    private Label optionLable;
    private Label startRoundLable;
    private Image background;

    private Table menuTable;
    private Table backgroundTable;

    private BitmapFont font;


    public HudInGameOptions() {

        stage = new Stage(ScreenHandler.viewport);

        font = new BitmapFont(Gdx.files.internal("fonts/font3.fnt"));

        textureStartRoundClicked = new Texture("menuscreens/buttons/Start-Button-clicked.png");
        textureStartRoundUnclicked = new Texture("menuscreens/buttons/Start-Button-unclicked.png");

        textureQuitUnclicked = new Texture("menuscreens/buttons/Quit-Button-unclicked.png");
        textureQuitClicked = new Texture("menuscreens/buttons/Quit-Button-clicked.png");

        textureMuteUnclicked = new Texture("menuscreens/buttons/Mute-Button-unclicked.png");
        textureMuteClicked = new Texture("menuscreens/buttons/Mute-Button-clicked.png");


        textureResumeUnclicked = new Texture("menuscreens/buttons/Resume-Button-unclicked.png");
        textureResumeClicked = new Texture("menuscreens/buttons/Resume-Button-clicked.png");

        drawStartRoundClicked = new TextureRegionDrawable(new TextureRegion(textureStartRoundClicked));
        drawStartRoundUnclicked = new TextureRegionDrawable(new TextureRegion(textureStartRoundUnclicked));
        startRoundButton = new ImageButton(drawStartRoundUnclicked, drawStartRoundClicked);

        drawQuitUnclicked = new TextureRegionDrawable(new TextureRegion(textureQuitUnclicked));
        drawQuitClicked = new TextureRegionDrawable(new TextureRegion(textureQuitClicked));
        quitButton = new ImageButton(drawQuitUnclicked, drawQuitClicked);

        drawMuteUnclicked = new TextureRegionDrawable(new TextureRegion(textureMuteUnclicked));
        drawMuteClicked = new TextureRegionDrawable(new TextureRegion(textureMuteClicked));
        mute = new ImageButton(drawMuteUnclicked, drawMuteClicked);

        drawResumeUnclicked = new TextureRegionDrawable(new TextureRegion(textureResumeUnclicked));
        drawResumeClicked = new TextureRegionDrawable(new TextureRegion(textureResumeClicked));
        resumeButton = new ImageButton(drawResumeUnclicked, drawResumeClicked);

        background = new Image(new Texture("menuscreens/Background.png"));

        optionLable = new Label("OPTIONS", new Label.LabelStyle(font, Color.BLACK));
        startRoundLable = new Label("press to start round", new Label.LabelStyle(font, Color.WHITE));

        startRoundButton.setVisible(false);
        startRoundLable.setVisible(false);

        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.center();

        backgroundTable.add().grow();
        backgroundTable.row();
        backgroundTable.add(background);
        backgroundTable.row();
        backgroundTable.add().grow();

        backgroundTable.setVisible(false);

        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.center();

        menuTable.add().grow().colspan(2);
        menuTable.row();
        menuTable.add(startRoundLable).colspan(2);
        menuTable.row();
        menuTable.add(startRoundButton).colspan(2);
        menuTable.row();
        menuTable.add(optionLable).colspan(2).padTop(35);
        menuTable.row();
        menuTable.add(resumeButton).colspan(2).padTop(10);
        menuTable.row();
        menuTable.add(mute).center().colspan(5).padTop(20);
        menuTable.row();
        menuTable.add(quitButton).colspan(2).padTop(20);
        menuTable.row();
        menuTable.add().grow().colspan(2);

        menuTable.setVisible(false);

        stage.addActor(backgroundTable);
        stage.addActor(menuTable);

        Gdx.input.setInputProcessor(stage);

        startRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudHelper.isHudClickable()){
                    ScreenHandler.buttonClickSound.play(0.05f);
                    menuTable.setVisible(false);
                    backgroundTable.setVisible(false);
                    HudHelper.setHudClickable(false);
                    if (GameWorld.getActivePlayers().size()>1){
                        GameWorld.chooseGameMaster = true;
                    }
                    GameWorld.startTimer = true;
                }
            }
        });

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudHelper.isHudClickable()){
                    ScreenHandler.buttonClickSound.play(0.05f);
                    menuTable.setVisible(false);
                    backgroundTable.setVisible(false);
                    HudHelper.setHudClickable(false);
                }
            }
        });

        mute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudHelper.isHudClickable()){
                    ScreenHandler.buttonClickSound.play(0.05f);
                    if (HudHelper.isVolumeOn()){
                        GameScreen.getInGameMusic().setVolume(0);
                        HudHelper.setVolumeOn(false);
                    }else {
                        GameScreen.getInGameMusic().setVolume(0.05f);
                        HudHelper.setVolumeOn(true);
                    }
                }
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (HudHelper.isHudClickable()){
                    Gdx.app.exit();
                    ServerDriver.shutdownServer();
                }
            }
        });
    }

    public Table getMenuTable() {return menuTable;}
    public Table getBackgroundTable() {return backgroundTable;}
    public Stage getStage() { return stage; }
}