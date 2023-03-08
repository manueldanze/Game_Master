package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.networking.ServerDriver;

public class StartScreen extends ScreenAdapter {

    private final SpriteBatch batch;
    private final Stage stage;
    private final Label text;
    private final Image gameMaster;
    private final Image gameMaster2;

    private final ImageButton newGameButton;
    private final Texture textureNewGameClicked;
    private final Texture textureNewGameUnclicked;
    private final Drawable drawNewGameClicked;
    private final Drawable drawNewGameUnclicked;

    private ImageButton mute;
    private Texture textureMuteClicked;
    private Texture textureMuteUnclicked;
    private Drawable drawMuteClicked;
    private Drawable drawMuteUnclicked;

    private ImageButton creditsButton;
    private Texture textureCreditsClicked;
    private Texture textureCreditsUnclicked;
    private Drawable drawCreditsClicked;
    private Drawable drawCreditsUnclicked;

    private final ImageButton quitButton;
    private final Texture textureQuitClicked;
    private final Texture textureQuitUnclicked;
    private final Drawable drawQuitClicked;
    private final Drawable drawQuitUnclicked;

    public final Table startTable;
    public final Table menuTable;

    private BitmapFont font;

    /**
     * load assets global via AssetManager.
     */
    public StartScreen(){

        batch = new SpriteBatch();

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("fonts/font3.fnt"));

        textureNewGameUnclicked = new Texture("menuscreens/buttons/NewGame-Button-unclicked.png");
        textureNewGameClicked = new Texture("menuscreens/buttons/NewGame-Button-clicked.png");

        textureMuteUnclicked = new Texture("menuscreens/buttons/Mute-Button-unclicked.png");
        textureMuteClicked = new Texture("menuscreens/buttons/Mute-Button-clicked.png");

        textureCreditsUnclicked = new Texture("menuscreens/buttons/Credits-Button-unclicked.png");
        textureCreditsClicked = new Texture("menuscreens/buttons/Credits-Button-clicked.png");
        textureQuitUnclicked = new Texture("menuscreens/buttons/Quit-Button-unclicked.png");
        textureQuitClicked = new Texture("menuscreens/buttons/Quit-Button-clicked.png");

        text = new Label("Press any key to start!", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        gameMaster = new Image(new Texture("menuscreens/text/GameMaster-Text.png"));
        gameMaster2 = new Image(new Texture("menuscreens/text/GameMaster-Text.png"));

        //ImageButtons
        drawNewGameUnclicked = new TextureRegionDrawable(new TextureRegion(textureNewGameUnclicked));
        drawNewGameClicked = new TextureRegionDrawable(new TextureRegion(textureNewGameClicked));
        newGameButton = new ImageButton(drawNewGameUnclicked, drawNewGameClicked);

        drawMuteUnclicked = new TextureRegionDrawable(new TextureRegion(textureMuteUnclicked));
        drawMuteClicked = new TextureRegionDrawable(new TextureRegion(textureMuteClicked));
        mute = new ImageButton(drawMuteUnclicked, drawMuteClicked);

        drawCreditsUnclicked = new TextureRegionDrawable(new TextureRegion(textureCreditsUnclicked));
        drawCreditsClicked = new TextureRegionDrawable(new TextureRegion(textureCreditsClicked));
        creditsButton = new ImageButton(drawCreditsUnclicked, drawCreditsClicked);

        drawQuitUnclicked = new TextureRegionDrawable(new TextureRegion(textureQuitUnclicked));
        drawQuitClicked = new TextureRegionDrawable(new TextureRegion(textureQuitClicked));
        quitButton = new ImageButton(drawQuitUnclicked, drawQuitClicked);

        //Start table
        startTable = new Table();

        startTable.setFillParent(true);
        startTable.center();

        startTable.add(gameMaster).padTop(180);
        startTable.row();
        startTable.add(text).padTop(180);

        stage.addActor(startTable);

        //Menu table
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.center();

        menuTable.add().grow().colspan(2);
        menuTable.row();
        menuTable.add(gameMaster2).colspan(2).width(300).height(45);
        menuTable.row();
        menuTable.add(newGameButton).colspan(2).padTop(20);
        menuTable.row();
        menuTable.row().padTop(20);
        menuTable.add(mute).center().colspan(5);
        menuTable.row();
        menuTable.add(creditsButton).colspan(2).padTop(20);
        menuTable.row();
        menuTable.add(quitButton).colspan(2).padTop(20);
        menuTable.row();
        menuTable.add().grow().colspan(2);

        menuTable.setVisible(false);

        stage.addActor(menuTable);
    }

    /**
     * rendering the MainMenu screen
     * @param delta time since last frame
     */
    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0,0,1);
        ScreenHandler.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        ScreenHandler.screenMusic.setVolume(0.05f);
        ScreenHandler.screenMusic.setLooping(true);
        ScreenHandler.screenMusic.play();

        if(newGameButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);

            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new SelectionScreen());
        }

        if(mute.isChecked()) {
            ScreenHandler.screenMusic.setVolume(0);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                ScreenHandler.buttonClickSound.play(0.05f);

            }
        }

        if(creditsButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);

            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new CreditScreen());
        }

        if(quitButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);
            Gdx.app.exit();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)) {
            startTable.setVisible(false);
            menuTable.setVisible(true);
        }
        stage.draw();
    }
}
