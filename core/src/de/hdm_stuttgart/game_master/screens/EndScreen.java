package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class EndScreen extends ScreenAdapter {

    private final SpriteBatch batch;
    private final Stage stage;
    private final Table menuTable;

    private final ImageButton quitButton;
    private final Texture textureQuitClicked;
    private final Texture textureQuitUnclicked;
    private final Drawable drawQuitClicked;
    private final Drawable drawQuitUnclicked;

    private final ImageButton menuButton;
    private final Texture textureMenuClicked;
    private final Texture textureMenuUnclicked;
    private final Drawable drawMenuClicked;
    private final Drawable drawMenuUnclicked;

    private final Image winnerLoser;
    private final Image playAgain;

    private final Music winnerMusic;
    private final Music gameOverMusic;

    public static boolean winner;

    public EndScreen() {

        batch = new SpriteBatch();

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        winnerMusic = ScreenHandler.manager.get("audio/music/winnerMusic.mp3", Music.class);
        gameOverMusic = ScreenHandler.manager.get("audio/music/gameOver_music.mp3", Music.class);

        //Textures
        textureQuitUnclicked = new Texture("menuscreens/buttons/Quit-Button-unclicked.png");
        textureQuitClicked = new Texture("menuscreens/buttons/Quit-Button-clicked.png");

        textureMenuUnclicked = new Texture("menuscreens/buttons/Menu-Button-unclicked.png");
        textureMenuClicked = new Texture("menuscreens/buttons/Menu-Button-clicked.png");

        //ImageButtons
        drawQuitUnclicked = new TextureRegionDrawable(new TextureRegion(textureQuitUnclicked));
        drawQuitClicked = new TextureRegionDrawable(new TextureRegion(textureQuitClicked));
        quitButton = new ImageButton(drawQuitUnclicked, drawQuitClicked);

        drawMenuUnclicked = new TextureRegionDrawable(new TextureRegion(textureMenuUnclicked));
        drawMenuClicked = new TextureRegionDrawable(new TextureRegion(textureMenuClicked));
        menuButton = new ImageButton(drawMenuUnclicked, drawMenuClicked);

        //Images
        playAgain = new Image(new Texture("menuscreens/text/PlayAgain-Text.png"));

        if (winner == true) {
            winnerLoser = new Image(new Texture("menuscreens/text/Winner-Text.png"));
            winnerMusic.play();
            winnerMusic.setVolume(0.05f);
        } else {
            winnerLoser = new Image(new Texture("menuscreens/text/Loser-Text.png"));
            gameOverMusic.play();
            gameOverMusic.setVolume(0.05f);

        }

        //Table
        menuTable = new Table();

        menuTable.setFillParent(true);
        menuTable.center();

        menuTable.add(winnerLoser).colspan(2);
        menuTable.row().padTop(50);
        menuTable.add(playAgain).colspan(2);
        menuTable.row().padTop(20);
        menuTable.add(menuButton);
        menuTable.add(quitButton);

        stage.addActor(menuTable);
    }

    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);
        ScreenHandler.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        GameScreen.getInGameMusic().stop();
        batch.begin();
        batch.end();

        if (menuButton.isChecked()) {

            ScreenHandler.buttonClickSound.play(0.05f);
            winnerMusic.stop();
            gameOverMusic.stop();
            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new StartScreen());
        }

        if (quitButton.isChecked()) {

            ScreenHandler.buttonClickSound.play(0.05f);

            Gdx.app.exit();
        }
        stage.draw();
    }
}
