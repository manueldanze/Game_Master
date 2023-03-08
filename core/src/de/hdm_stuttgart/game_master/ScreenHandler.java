package de.hdm_stuttgart.game_master;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.hdm_stuttgart.game_master.screens.StartScreen;

public class ScreenHandler extends Game {

    public static int V_WIDTH;
    public static int V_HEIGHT;
    public static Viewport viewport;
    public static Camera camera;
    public static int screenSettingsBackHandler = 0;

    public static Sound buttonClickSound;
    public static Music screenMusic;

    public static ScreenHandler INSTANCE;
    public static final AssetManager manager = new AssetManager();

    public ScreenHandler() {
        INSTANCE = this;
    }

    @Override
    public void create () {

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        V_WIDTH = 1920;
        V_HEIGHT = 1080;

        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/music/effects/ComputerBlurp.wav"));

        screenMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/screen_music.mp3"));

        setScreen(new StartScreen());
    }
}
