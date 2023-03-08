package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.DefaultInput;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.screens.GameScreen;

public class GameMaster implements PlayerControllable {

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Vector2 cameraPos = new Vector2(10, 10);
    private final Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    private static final float CAMERA_SPEED_SHIFT_FACTOR = 2;
    private float cameraMoveSpeed = 1f;
    private float actionHeatLevel = 0;
    private float heatLevelCap = 300;
    private float heatLevelCooldownInterval = 0.5f;
    private GameMasterAction currentAction = GameMasterAction.PISTOL_TROOPER;
    private final GameMasterInput gmInput = new GameMasterInput();

    private final Sound spawnSound;

    public GameMaster() {
        spawnSound = ScreenHandler.manager.get("audio/music/effects/Teleporting.wav", Sound.class);
    }

    public GameMasterInput getInputProcessor() { return gmInput; }

    @Override
    public void move(float delta) {
        mousePos.set(GameScreen.screenToWorldCoords(Gdx.input.getX(), Gdx.input.getY()));

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cameraPos.add(0, cameraMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cameraPos.add(-cameraMoveSpeed, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cameraPos.add(0, -cameraMoveSpeed);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cameraPos.add(cameraMoveSpeed, 0);
        }

        if (actionHeatLevel > 0) {
            actionHeatLevel -= Math.min(heatLevelCooldownInterval, actionHeatLevel);
        }
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        camera.position.set(GameWorld.getLocalPlayer().getCameraPosition().x, GameWorld.getLocalPlayer().getCameraPosition().y, 0);
        camera.update();
    }

    @Override
    public Vector2 getCameraPosition() {
        return new Vector2(cameraPos);
    }

    @Override
    public Vector2 getPosition() {
        return getCameraPosition();
    }

    @Override
    public Vector2 getAimingPos() {
        return new Vector2(GameScreen.screenToWorldCoords(Gdx.input.getX(), Gdx.input.getY()));
    }

    @Override
    public int getHP() {
        return (int)heatLevelCap;
    }

    @Override
    public int getCurrentHP() {
        return (int)actionHeatLevel;
    }


    private class GameMasterInput implements InputProcessor {

        @Override
        public boolean keyDown(int i) {

            if (i == Input.Keys.SPACE && GameWorld.DEV_MODE_ENABLED) {
                GameWorld.becomePlayer();
                Gdx.input.setInputProcessor(DefaultInput.getInputProcessor());
            }

            if (i > 7 && i <= GameMasterAction.values().length + 7) {
                currentAction = GameMasterAction.values()[i-8];
                return true;
            }

            if (i == Input.Keys.SHIFT_LEFT) {
                cameraMoveSpeed *= CAMERA_SPEED_SHIFT_FACTOR;
                return true;
            }

            return false;
        }

        @Override
        public boolean keyUp(int i) {
            if (i == Input.Keys.SHIFT_LEFT) {
                cameraMoveSpeed /= CAMERA_SPEED_SHIFT_FACTOR;
                return true;
            }

            return false;
        }

        @Override
        public boolean keyTyped(char c) {
            return false;
        }

        @Override
        public boolean touchDown(int i, int i1, int i2, int i3) {
            if (i3 == Input.Buttons.LEFT && actionHeatLevel + currentAction.getCost() <= heatLevelCap) {
                spawnSound.play(0.3f);
                GameWorld.addEnemy(Enemy.create(mousePos.x, mousePos.y, currentAction.enemyType));
                GameWorld.sendEnemy(mousePos.x, mousePos.y, currentAction.ordinal());
                actionHeatLevel += currentAction.getCost();
                return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int i, int i1, int i2, int i3) {
            return false;
        }

        @Override
        public boolean touchDragged(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean mouseMoved(int i, int i1) {
            return true;
        }

        @Override
        public boolean scrolled(float v, float v1) {
            return false;
        }
    }
}
