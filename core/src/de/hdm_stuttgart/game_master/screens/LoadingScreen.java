package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class LoadingScreen extends ScreenAdapter {

    private Stage stage;
    private SpriteBatch batch;
    private Vector2 position;
    private TextureAtlas atlas;
    private float animationTime = 0.0f;
    private Animation<TextureRegion> animation;

    /**
     * load assets global via AssetManager
     */
    public LoadingScreen(){

        stage = new Stage(ScreenHandler.viewport);
        Gdx.input.setInputProcessor(stage);

        //new version
        ScreenHandler.manager.load("game_characters/rangedDDnew/rangedDD_idle.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("game_characters/rangedDDnew/rangedDD_run.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("game_characters/healer/healer_idle.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("game_characters/healer/healer_run.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("game_characters/meleeDD/meleeDD_idle.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("game_characters/meleeDD/meleeDD_run.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("game_characters/tank/tank_idle.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("game_characters/tank/tank_run.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("game_characters/enemy/otherEnemy/cyberEnemy_idle.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("game_characters/enemy/otherEnemy/cyberEnemy_run.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("weapons/weaponAtlas.atlas", TextureAtlas.class);
        ScreenHandler.manager.load("weapons/bullets.atlas", TextureAtlas.class);

        ScreenHandler.manager.load("audio/music/inGame_music.mp3", Music.class);
        ScreenHandler.manager.load("audio/music/winnerMusic.mp3", Music.class);
        ScreenHandler.manager.load("audio/music/gameOver_music.mp3", Music.class);

        ScreenHandler.manager.load("audio/music/effects/LaserShort.wav", Sound.class);
        ScreenHandler.manager.load("audio/music/effects/LaserFireMultiple.wav", Sound.class);
        ScreenHandler.manager.load("audio/music/effects/Teleporting.wav", Sound.class);

        atlas = new TextureAtlas("loadingscreen/loadingscreen.atlas");
        animation = new Animation<TextureRegion>(0.05f, atlas.findRegions("loading"), Animation.PlayMode.LOOP);
        batch = new SpriteBatch();
        position = new Vector2(-150.0f,-100.0f);
    }

    /**
     * rendering the loading screen
     * @param delta time since last frame
     */
    @Override
    public void render(float delta) {

        ScreenUtils.clear(0,0,0,1);

        ScreenHandler.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        batch.begin();
        animationTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(animationTime);
        batch.draw(currentFrame,position.x,position.y);
        batch.end();


        if (ScreenHandler.manager.update()) {
            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new GameScreen());
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenHandler.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
    }
}
