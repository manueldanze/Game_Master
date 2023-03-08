package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.networking.ServerDriver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class ConnectionScreen extends ScreenAdapter {

    private static String username;
    private final TextField usernameField;
    private static String ipAddress;
    private final TextField ipAddressField;
    private final SpriteBatch batch;
    private final Stage stage;
    private final Image connectionTextImage;

    private final ImageButton backButton;
    private final Texture textureBackClicked;
    private final Texture textureBackUnclicked;
    private final Drawable drawBackClicked;
    private final Drawable drawBackUnclicked;

    private final ImageButton startButton;
    private final Texture textureStartClicked;
    private final Texture textureStartUnclicked;
    private final Drawable drawStartClicked;
    private final Drawable drawStartUnclicked;

    private final ImageButton hostButton;
    private final Texture textureHostClicked;
    private final Texture textureHostUnclicked;
    private final Drawable drawHostClicked;
    private final Drawable drawHostUnclicked;


    private final ImageButton joinButton;
    private final Texture textureJoinClicked;
    private final Texture textureJoinUnclicked;
    private final Drawable drawJoinClicked;
    private final Drawable drawJoinUnclicked;
    private final Table menuTable;

    private final Image usernameTextImage;
    private final Image ipTextImage;

    /**
     * load assets global via AssetManager.
     */
    public ConnectionScreen(){

        batch = new SpriteBatch();

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        //Textures
        textureBackUnclicked = new Texture("menuscreens/buttons/Back-Button-unclicked.png");
        textureBackClicked = new Texture("menuscreens/buttons/Back-Button-clicked.png");

        textureStartUnclicked = new Texture("menuscreens/buttons/Start-Button-unclicked.png");
        textureStartClicked = new Texture("menuscreens/buttons/Start-Button-clicked.png");

        textureHostUnclicked = new Texture("menuscreens/buttons/Host-Button-unclicked.png");
        textureHostClicked = new Texture("menuscreens/buttons/Host-Button-clicked.png");

        textureJoinUnclicked = new Texture("menuscreens/buttons/Join-Button-unclicked.png");
        textureJoinClicked = new Texture("menuscreens/buttons/Join-Button-clicked.png");

        //ImageButtons
        drawBackUnclicked = new TextureRegionDrawable(new TextureRegion(textureBackUnclicked));
        drawBackClicked = new TextureRegionDrawable(new TextureRegion(textureBackClicked));
        backButton = new ImageButton(drawBackUnclicked, drawBackClicked);

        drawStartUnclicked = new TextureRegionDrawable(new TextureRegion(textureStartUnclicked));
        drawStartClicked = new TextureRegionDrawable(new TextureRegion(textureStartClicked));
        startButton = new ImageButton(drawStartUnclicked, drawStartClicked);
        startButton.setVisible(false);

        drawHostUnclicked = new TextureRegionDrawable(new TextureRegion(textureHostUnclicked));
        drawHostClicked = new TextureRegionDrawable(new TextureRegion(textureHostClicked));
        hostButton = new ImageButton(drawHostUnclicked, drawHostClicked);

        drawJoinUnclicked = new TextureRegionDrawable(new TextureRegion(textureJoinUnclicked));
        drawJoinClicked = new TextureRegionDrawable(new TextureRegion(textureJoinClicked));
        joinButton = new ImageButton(drawJoinUnclicked, drawJoinClicked);

        //Images
        connectionTextImage = new Image(new Texture("menuscreens/text/Connection-Text.png"));
        usernameTextImage = new Image(new Texture("menuscreens/text/Username-Text.png"));

        ipTextImage = new Image(new Texture("menuscreens/text/IP-Address-Text.png"));
        ipTextImage.setVisible(false);

        //Textfields
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        style.fontColor = Color.WHITE;

        usernameField = new TextField("", style);
        usernameField.setMessageText("enter your username");

        ipAddressField = new TextField("", style);
        ipAddressField.setMessageText("enter host IP-address");
        ipAddressField.setVisible(false);

        //Table
        menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top().pad(20);

        menuTable.row().padTop(30);
        menuTable.add(connectionTextImage).colspan(5).center().width(200).height(30);
        menuTable.row().padTop(50).left();
        menuTable.add(usernameTextImage).width(70).height(15);
        menuTable.add(usernameField);
        menuTable.row().padTop(50);
        menuTable.add();
        menuTable.add(hostButton);
        menuTable.add(joinButton);
        menuTable.row().padTop(50).left();
        menuTable.add(ipTextImage).width(70).height(15);
        menuTable.add(ipAddressField);
        menuTable.row();
        menuTable.add().growY().growX().colspan(4);
        menuTable.row().padTop(20);
        menuTable.add(backButton).left().colspan(4);
        menuTable.add(startButton).right();

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
            ScreenHandler.INSTANCE.setScreen(new SelectionScreen());
            startButton.setVisible(false);
        }

        if(startButton.isChecked()) {

            ScreenHandler.buttonClickSound.play(0.05f);

            setSelectedUsername();

            if (Objects.equals(ipAddress, "")){
                setSelectediPAddress();
            }

            try {
                if (Objects.equals(ipAddress, getYourIP())){
                    ServerDriver.callServerDriver();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new LoadingScreen());
        }

        if(hostButton.isPressed()) {

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                ScreenHandler.buttonClickSound.play(0.05f);
                try {
                    ipAddress = getYourIP();
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }

                ipAddressField.setText(ipAddress);
                ipTextImage.setVisible(true);
                ipAddressField.setVisible(true);

                GameWorld.setHost(true);
                startButton.setVisible(true);
            }
        }

        if(joinButton.isPressed()) {

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                ScreenHandler.buttonClickSound.play(0.05f);

                ipAddress = "";
                ipAddressField.setText("");
                ipAddressField.setMessageText("enter host ip");
                ipTextImage.setVisible(true);
                ipAddressField.setVisible(true);

                GameWorld.setHost(false);
                startButton.setVisible(true);
            }
        }
        stage.draw();
    }

    private String getYourIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }
    private void setSelectedUsername() {
        username = usernameField.getText();
    }
    private void setSelectediPAddress() {
        ipAddress = ipAddressField.getText();
    }
    public static String getUsername() {
        return username;
    }
    public static String getIpAddress() {
        return ipAddress;
    }
}