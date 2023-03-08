package de.hdm_stuttgart.game_master.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.hdm_stuttgart.game_master.helper.AnimationHelper;
import de.hdm_stuttgart.game_master.helper.WeaponLoader;
import de.hdm_stuttgart.game_master.helper.HudHelper;
import de.hdm_stuttgart.game_master.helper.WorldCreation;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.character.Enemy;
import de.hdm_stuttgart.game_master.character.Player;
import de.hdm_stuttgart.game_master.networking.VirtualPlayer;
import de.hdm_stuttgart.game_master.huds.HudPlayer;
import de.hdm_stuttgart.game_master.huds.HudGameMaster;
import de.hdm_stuttgart.game_master.huds.HudInGameOptions;

public class GameScreen extends ScreenAdapter {

    public static final int PPM = 16;

    private AnimationHelper animationHelper;
    private WeaponLoader weaponLoader = new WeaponLoader("weapon1");
    private Stage stage;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;

    private static final TiledMap map = new TmxMapLoader().load("background/GameMaster-Final-Dummy.tmx");
    private final OrthogonalTiledMapRenderer tmr;
    private final ShapeRenderer sr = new ShapeRenderer();
    private static World world;
    private Box2DDebugRenderer b2dr;
    private TiledMapTileLayer layer;
    private static int mapHeight;
    private static int mapWidth;
    private static HudPlayer hudPlayer;
    private static HudGameMaster gmHud;
    private static HudInGameOptions inGameOptions;
    private static Music inGameMusic;

    /**
     * Constructor for Screen of the actual game
     */
    public GameScreen() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();

        animationHelper = new AnimationHelper();

        layer = (TiledMapTileLayer) map.getLayers().get("background");
        mapHeight = layer.getHeight();
        mapWidth = layer.getWidth();

        tmr = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        viewport = new ScreenViewport(camera);

        inGameMusic = ScreenHandler.manager.get("audio/music/inGame_music.mp3", Music.class);
        inGameMusic.setVolume(0.05f);
        inGameMusic.setLooping(true);

        hudPlayer = new HudPlayer(batch);
        gmHud = new HudGameMaster(batch);
        inGameOptions = new HudInGameOptions();

        b2dr = new Box2DDebugRenderer();
        world = WorldCreation.createWorld();

        GameWorld.initLogic();

        GameWorld.setCurrentScreen(this);
    }

    /**
     * rendering the game itself (map, characters, etc.)
     * @param delta time since last frame
     */
    @Override
    public void render(float delta) {

        if (GameWorld.setGameMaster){
            GameWorld.becomeGameMaster();
            GameWorld.setGameMaster = false;
        }

        HudHelper.updateHud(delta);// update HUD

        GameWorld.update(delta);// update player information to server

        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        tmr.setView(camera);
        tmr.render();

        //draw all the other connected players
        renderOtherPlayers(batch, delta);
        renderOtherWeapons(batch, delta);

        for (Enemy enemy : GameWorld.getActiveEnemies()) {
            enemy.render(delta, batch);
        }

        GameWorld.getLocalPlayer().render(delta, batch);

        for (int i = 0; i < 3; i++) {
            GameWorld.popBulletQueue();
        }

        camera.position.set(GameWorld.getLocalPlayer().getCameraPosition().x * PPM, GameWorld.getLocalPlayer().getCameraPosition().y * PPM, 0);
        camera.update();
        batch.end();


        inGameMusic.play();
        ScreenHandler.screenMusic.stop();

        hudPlayer.stage.draw();

        if (GameWorld.iAmGM){
            hudPlayer.getPlayer1Icon().setVisible(false);
            hudPlayer.getTimeLabel().setVisible(false);
            hudPlayer.getCountdownLabel().setVisible(false);
            gmHud.stage.draw();
        }

        inGameOptions.getStage().draw();
        if (GameWorld.isHost()){
            inGameOptions.getMenuTable().getChild(0).setVisible(true);
            inGameOptions.getMenuTable().getChild(1).setVisible(true);
        }

        if (GameWorld.getLocalPlayer() instanceof Player) {
            if (GameWorld.getLocalPlayer().getCurrentHP() <= 0) {
                EndScreen.winner = false;
                ScreenHandler.INSTANCE.dispose();
                ScreenHandler.INSTANCE.setScreen(new EndScreen());
            }
        }

        stage.draw();
    }

    /**
     * handle resize of the window
     * @param width width of the window
     * @param height height of the window
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * method for showing specifics
     */
    @Override
    public void show() {
        super.show();
    }

    /**
     * method for hiding game
     */
    @Override
    public void hide() {

        super.hide();
    }

    /**
     * "garbage collector"
     */
    @Override
    public void dispose() {
        super.dispose();
        map.dispose();
        tmr.dispose();
        world.dispose();
        batch.dispose();
    }

    public void renderOtherPlayers(SpriteBatch batch, float delta) {

        animationHelper.createAnimations(delta);
        for(VirtualPlayer virtualPlayer : GameWorld.players.values()){
            if (virtualPlayer.playerClass != null) {
                if (virtualPlayer.playerClass.equals("rangedDD")) {
                    if (virtualPlayer.walking) {
                        batch.draw(animationHelper.rangedDDRunCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.rangedDDRunCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.rangedDDRunCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.rangedDDRunCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? - animationHelper.rangedDDRunCurrentFrame.getRegionWidth() * 2.0f : animationHelper.rangedDDRunCurrentFrame.getRegionWidth() * 2.0f, animationHelper.rangedDDRunCurrentFrame.getRegionHeight() * 2.0f);
                    } else {
                        batch.draw(animationHelper.rangedDDIdleCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.rangedDDIdleCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.rangedDDIdleCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.rangedDDIdleCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? - animationHelper.rangedDDIdleCurrentFrame.getRegionWidth() * 2.0f : animationHelper.rangedDDIdleCurrentFrame.getRegionWidth() * 2.0f, animationHelper.rangedDDIdleCurrentFrame.getRegionHeight() * 2.0f);
                    }
                } else if (virtualPlayer.playerClass.equals("meleeDD")) {
                    if (virtualPlayer.walking) {
                        batch.draw(animationHelper.meleeDDRunCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.meleeDDRunCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.meleeDDRunCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.meleeDDRunCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? - animationHelper.meleeDDRunCurrentFrame.getRegionWidth() * 2.0f : animationHelper.meleeDDRunCurrentFrame.getRegionWidth() * 2.0f, animationHelper.meleeDDRunCurrentFrame.getRegionHeight() * 2.0f);
                    } else {
                        batch.draw(animationHelper.meleeDDIdleCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.meleeDDIdleCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.meleeDDIdleCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.meleeDDIdleCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? - animationHelper.meleeDDIdleCurrentFrame.getRegionWidth() * 2.0f : animationHelper.meleeDDIdleCurrentFrame.getRegionWidth() * 2.0f, animationHelper.meleeDDIdleCurrentFrame.getRegionHeight() * 2.0f);
                    }
                } else if (virtualPlayer.playerClass.equals("healer")) {
                    if (virtualPlayer.walking) {
                        batch.draw(animationHelper.healerRunCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.healerRunCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.healerRunCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.healerRunCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? -animationHelper.healerRunCurrentFrame.getRegionWidth() * 2.0f : animationHelper.healerRunCurrentFrame.getRegionWidth() * 2.0f, animationHelper.healerRunCurrentFrame.getRegionHeight() * 2.0f);
                    } else {
                        batch.draw(animationHelper.healerIdleCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.healerIdleCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.healerIdleCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.healerIdleCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? -animationHelper.healerIdleCurrentFrame.getRegionWidth() * 2.0f : animationHelper.healerIdleCurrentFrame.getRegionWidth() * 2.0f, animationHelper.healerIdleCurrentFrame.getRegionHeight() * 2.0f);
                    }
                } else if (virtualPlayer.playerClass.equals("tank")) {
                    if (virtualPlayer.walking) {
                        batch.draw(animationHelper.tankRunCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.tankRunCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.tankRunCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.tankRunCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? -animationHelper.tankRunCurrentFrame.getRegionWidth() * 2.0f : animationHelper.tankRunCurrentFrame.getRegionWidth() * 2.0f, animationHelper.tankRunCurrentFrame.getRegionHeight() * 2.0f);
                    } else {
                        batch.draw(animationHelper.tankIdleCurrentFrame, virtualPlayer.flipped ? virtualPlayer.middleX + animationHelper.tankIdleCurrentFrame.getRegionWidth() : virtualPlayer.x - animationHelper.tankIdleCurrentFrame.getRegionWidth(), virtualPlayer.middleY - animationHelper.tankIdleCurrentFrame.getRegionHeight(), virtualPlayer.flipped ? -animationHelper.tankIdleCurrentFrame.getRegionWidth() * 2.0f : animationHelper.tankIdleCurrentFrame.getRegionWidth() * 2.0f, animationHelper.tankIdleCurrentFrame.getRegionHeight() * 2.0f);
                    }
                }
            }
        }
    }

    public void renderOtherWeapons(SpriteBatch batch, float delta) {
        Sprite weaponSprite = weaponLoader.rifle;
        for (VirtualPlayer virtualPlayer : GameWorld.players.values()) {
            weaponSprite.setFlip(false, virtualPlayer.flippedWeapon);
            batch.draw(weaponSprite, virtualPlayer.velocityX/8.5f + virtualPlayer.x - animationHelper.rangedDDIdleCurrentFrame.getRegionWidth()/2.4f, virtualPlayer.velocityY/3.5f + virtualPlayer.y - 10, weaponSprite.getOriginX(), weaponSprite.getOriginY(), weaponSprite.getWidth(), weaponSprite.getHeight(), weaponSprite.getScaleX(), weaponSprite.getScaleY(), virtualPlayer.weaponRotation);
        }
    }

    /**
     * Methode, die Fensterkoordinaten in Weltkoordinaten umwandelt.
     * Ist notwendig, damit die unterschiedlichen Waffentypen richtig funktionieren.
     */
    public static Vector2 screenToWorldCoords(Vector2 vec) {
        Vector3 temp = ((GameScreen)GameWorld.getCurrentScreen()).camera.unproject(new Vector3(vec.x, vec.y, 0));

        return new Vector2(temp.x, temp.y);
    }

    /**
     * Methode, die Fensterkoordinaten in Weltkoordinaten umwandelt.
     * Ist notwendig, damit die unterschiedlichen Waffentypen richtig funktionieren.
     */
    public static Vector2 screenToWorldCoords(float x, float y) {
        Vector3 temp = ((GameScreen)GameWorld.getCurrentScreen()).camera.unproject(new Vector3(x, y, 0));

        return new Vector2(temp.x, temp.y);
    }

    public static TiledMap getMap() {
        return map;
    }
    public static int getMapWidth() { return mapWidth; }
    public static int getMapHeight() { return mapHeight; }
    public static World getWorld() {
        return world;
    }
    public static HudPlayer getHud() {
        return hudPlayer;
    }
    public static HudGameMaster getGmHud() {
        return gmHud;
    }
    public static HudInGameOptions getInGameOptions() {
        return inGameOptions;
    }
    public static Music getInGameMusic() {
        return inGameMusic;
    }
}
