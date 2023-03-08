package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class CreditScreen extends ScreenAdapter {

    private final SpriteBatch batch;
    private final Stage stage;

    private final ImageButton backButton;
    private final Texture textureBackClicked;
    private final Texture textureBackUnclicked;
    private final Drawable drawBackClicked;
    private final Drawable drawBackUnclicked;

    private final Image creditsTextImage;

    private final Label timName;
    private final Label manuelName;
    private final Label georgName;
    private final Label biancaName;
    private final Label furkanName;
    private final Label despoinaName;

    private final Table menuTable;

    private BitmapFont font;

    /**
     * load assets global via AssetManager.
     */
    public CreditScreen(){

        batch = new SpriteBatch();

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        font = new BitmapFont(Gdx.files.internal("fonts/font3.fnt"));

        textureBackUnclicked = new Texture("menuscreens/buttons/Back-Button-unclicked.png");
        textureBackClicked = new Texture("menuscreens/buttons/Back-Button-clicked.png");

        //ImageButtons
        drawBackUnclicked = new TextureRegionDrawable(new TextureRegion(textureBackUnclicked));
        drawBackClicked = new TextureRegionDrawable(new TextureRegion(textureBackClicked));
        backButton = new ImageButton(drawBackUnclicked, drawBackClicked);

        //Images
        creditsTextImage = new Image(new Texture("menuscreens/text/Credits-Text.png"));

        //Labels
        timName = new Label("Tim Drobny", new Label.LabelStyle(font, Color.WHITE));
        manuelName = new Label("Manuel Danze", new Label.LabelStyle(font, Color.WHITE));
        georgName = new Label("Georg Bogdanov", new Label.LabelStyle(font, Color.WHITE));
        biancaName = new Label("Bianca Knuelle", new Label.LabelStyle(font, Color.WHITE));
        furkanName = new Label("Furkan Erdogan", new Label.LabelStyle(font, Color.WHITE));
        despoinaName = new Label("Despoina Sfantou", new Label.LabelStyle(font, Color.WHITE));

        //Table
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top().pad(20);

        menuTable.row().padTop(50).expandX();
        menuTable.add(creditsTextImage);
        menuTable.row().padTop(20);
        menuTable.add(timName);
        menuTable.row().padTop(10);
        menuTable.add(manuelName);
        menuTable.row().padTop(10);
        menuTable.add(georgName);
        menuTable.row().padTop(10);
        menuTable.add(biancaName);
        menuTable.row().padTop(10);
        menuTable.add(furkanName);
        menuTable.row().padTop(10);
        menuTable.add(despoinaName);
        menuTable.row();
        menuTable.add().growY().growX();
        menuTable.row();
        menuTable.add(backButton).right();

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

        if(backButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);

            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new StartScreen());
        }
        stage.draw();
    }

    @Override
    public void show() {
    }
    @Override
    public void resize(int width, int height) {
    }
}

