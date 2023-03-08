package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
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

import java.util.ArrayList;

public class SelectionScreen extends ScreenAdapter {

    private static String charType;
    private final SpriteBatch batch;
    private final Stage stage;

    private final Image characterSelectionTextImage;

    private final Image arrayCharacterImageChange;
    private Image helperArrayImageChange;

    private final Image arrayCharacterTextChange;
    private Image helperArrayCharacterTextChange;

    private final ImageButton leftButton;
    private final Texture textureLeftImageClicked;
    private final Texture textureLeftImageUnclicked;
    private final Drawable drawLeftImageClicked;
    private final Drawable drawLeftImageUnclicked;

    private final ImageButton rightButton;
    private final Texture textureRightImageClicked;
    private final Texture textureRightImageUnclicked;
    private final Drawable drawRightImageClicked;
    private final Drawable drawRightImageUnclicked;

    private final ImageButton backButton;
    private final Texture textureBackClicked;
    private final Texture textureBackUnclicked;
    private final Drawable drawBackClicked;
    private final Drawable drawBackUnclicked;

    private final ImageButton nextButton;
    private final Texture textureNextClicked;
    private final Texture textureNextUnclicked;
    private final Drawable drawNextClicked;
    private final Drawable drawNextUnclicked;

    private final Table menuTable;

    private int count = 0;
    private final ArrayList<Texture> imageArray;
    private final ArrayList<Texture> characterTextImageArray;

    /**
     * load assets global via AssetManager.
     */
    public SelectionScreen(){

        batch = new SpriteBatch();

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        textureBackUnclicked = new Texture("menuscreens/buttons/Back-Button-unclicked.png");
        textureBackClicked = new Texture("menuscreens/buttons/Back-Button-clicked.png");
        textureNextUnclicked = new Texture("menuscreens/buttons/Next-Button-unclicked.png");
        textureNextClicked = new Texture("menuscreens/buttons/Next-Button-clicked.png");

        textureLeftImageUnclicked = new Texture("menuscreens/text/Left-Text.png");
        textureLeftImageClicked = new Texture("menuscreens/text/Left-Text.png");

        textureRightImageUnclicked = new Texture("menuscreens/text/Right-Text.png");
        textureRightImageClicked = new Texture("menuscreens/text/Right-Text.png");

        //ImageButtons
        drawBackUnclicked = new TextureRegionDrawable(new TextureRegion(textureBackUnclicked));
        drawBackClicked = new TextureRegionDrawable(new TextureRegion(textureBackClicked));
        backButton = new ImageButton(drawBackUnclicked, drawBackClicked);

        drawNextUnclicked = new TextureRegionDrawable(new TextureRegion(textureNextUnclicked));
        drawNextClicked = new TextureRegionDrawable(new TextureRegion(textureNextClicked));
        nextButton = new ImageButton(drawNextUnclicked, drawNextClicked);

        drawLeftImageUnclicked = new TextureRegionDrawable(new TextureRegion(textureLeftImageUnclicked));
        drawLeftImageClicked = new TextureRegionDrawable(new TextureRegion(textureLeftImageClicked));
        leftButton = new ImageButton(drawLeftImageUnclicked, drawLeftImageClicked);

        drawRightImageUnclicked = new TextureRegionDrawable(new TextureRegion(textureRightImageUnclicked));
        drawRightImageClicked = new TextureRegionDrawable(new TextureRegion(textureRightImageClicked));
        rightButton = new ImageButton(drawRightImageUnclicked, drawRightImageClicked);

        //Images
        characterSelectionTextImage = new Image(new Texture("menuscreens/text/CharacterSelection-Text.png"));

        //Arrays
        imageArray = new ArrayList<>();
        imageArray.add(new Texture("menuscreens/characters/RangedDD.png"));
        imageArray.add(new Texture("menuscreens/characters/MeleeDD.png"));
        imageArray.add(new Texture("menuscreens/characters/Tank.png"));
        imageArray.add(new Texture("menuscreens/characters/Healer.png"));

        arrayCharacterImageChange = new Image(imageArray.get(count));

        characterTextImageArray = new ArrayList<>();
        characterTextImageArray.add(new Texture("menuscreens/text/RangedDD-Text.png"));
        characterTextImageArray.add(new Texture("menuscreens/text/MeleeDD-Text.png"));
        characterTextImageArray.add(new Texture("menuscreens/text/Tank-Text.png"));
        characterTextImageArray.add(new Texture("menuscreens/text/Healer-Text.png"));

        arrayCharacterTextChange = new Image(characterTextImageArray.get(count));

        //Table
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top().pad(20);

        menuTable.add(characterSelectionTextImage).colspan(5).center().width(350).height(30).padTop(50);
        menuTable.row();
        menuTable.add(arrayCharacterTextChange).colspan(5).padTop(50);
        menuTable.row().padTop(20);
        menuTable.add().width(backButton.getWidth());
        menuTable.add(leftButton).right().expandX();
        menuTable.add(arrayCharacterImageChange).size(150).expandX();
        menuTable.add(rightButton).left().expandX();
        menuTable.add().width(nextButton.getWidth());
        menuTable.row();
        menuTable.add().growY().growX().colspan(5);
        menuTable.row().padTop(20);
        menuTable.add(backButton).left();
        menuTable.add(nextButton).right().colspan(4);

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

        menuTable.setFillParent(true);
        menuTable.invalidate();

        //Event handling
        if (backButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);

            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new StartScreen());
        }

        if (nextButton.isChecked()) {
            ScreenHandler.buttonClickSound.play(0.05f);

            setSelectedCharType();
            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new ConnectionScreen());
        }

        if (rightButton.isPressed() || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                ScreenHandler.buttonClickSound.play(0.05f);
                if (imageArray.size() - 1 == count) {
                    count = 0;

                    helperArrayImageChange = new Image(imageArray.get(count));
                    arrayCharacterImageChange.setDrawable(helperArrayImageChange.getDrawable());

                    helperArrayCharacterTextChange = new Image(characterTextImageArray.get(count));
                    arrayCharacterTextChange.setDrawable(helperArrayCharacterTextChange.getDrawable());

                } else {
                    count++;

                    helperArrayImageChange = new Image(imageArray.get(count));
                    arrayCharacterImageChange.setDrawable(helperArrayImageChange.getDrawable());

                    helperArrayCharacterTextChange = new Image(characterTextImageArray.get(count));
                    arrayCharacterTextChange.setDrawable(helperArrayCharacterTextChange.getDrawable());

                    if (count == 0) {
                        count++;
                    }
                }
            }
        }

        if (leftButton.isPressed() || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                ScreenHandler.buttonClickSound.play(0.05f);
                if (count == 0) {
                    count = imageArray.size()-1;
                    helperArrayImageChange = new Image(imageArray.get(count));
                    arrayCharacterImageChange.setDrawable(helperArrayImageChange.getDrawable());

                    helperArrayCharacterTextChange = new Image(characterTextImageArray.get(count));
                    arrayCharacterTextChange.setDrawable(helperArrayCharacterTextChange.getDrawable());
                } else {
                    count--;

                    helperArrayImageChange = new Image(imageArray.get(count));
                    arrayCharacterImageChange.setDrawable(helperArrayImageChange.getDrawable());

                    helperArrayCharacterTextChange = new Image(characterTextImageArray.get(count));
                    arrayCharacterTextChange.setDrawable(helperArrayCharacterTextChange.getDrawable());

                    if (count == imageArray.size()-1) {
                        count--;
                    }
                }
            }
        }
        stage.draw();
    }

    private void setSelectedCharType(){

        if (count == 0){
            charType = "rangedDD";
        }
        if (count == 1){
            charType = "meleeDD";
        }
        if (count == 2){
            charType = "tank";
        }
        if( count == 3) {
            charType = "healer";
        }
    }

    public static String getCharType() {
        return charType;
    }
    @Override
    public void resize(int width, int height) {

    }
}