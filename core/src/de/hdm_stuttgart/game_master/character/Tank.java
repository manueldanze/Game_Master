package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.ScreenHandler;

import static de.hdm_stuttgart.game_master.helper.BoxHelpers.createBody;
import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class Tank extends Player{

    /**
     * Constructor for Tank class
     * @param x x coordinate of spawn
     * @param y y coordinate of spawn
     */
    public Tank(int x, int y, String username) {
        super(x, y);
        currentFrame = new TextureRegion();
        playerBody = createBody(x, y, 100, 95, username, false, false);
        position = new Vector2(x, y);

        //sprite animations
        TextureAtlas idleAtlas = ScreenHandler.manager.get("game_characters/tank/tank_idle.atlas",TextureAtlas.class);
        TextureAtlas runAtlas = ScreenHandler.manager.get("game_characters/tank/tank_run.atlas",TextureAtlas.class);
        idleAnimation = new Animation<>(0.1f, idleAtlas.findRegions("tank_idle"), Animation.PlayMode.LOOP);
        runAnimation = new Animation<>(0.1f, runAtlas.findRegions("tank_run"), Animation.PlayMode.LOOP);

        weaponSprite = currentWeapon.getWeaponSprite();
        this.username = username;
        characterMiddle.x = playerBody.getPosition().x + currentFrame.getRegionWidth()/2.0f;
        characterMiddle.y = playerBody.getPosition().y + currentFrame.getRegionHeight()/2.0f;
        charType = "tank";
        maxHP = 40;
        hp = maxHP;
    }

    /**
     * method for rendering Tank character
     * @param delta time since last frame
     * @param batch Spritebatch for texture
     */
    @Override
    public void render(float delta, SpriteBatch batch) {
        position.x = playerBody.getPosition().x * PPM;
        position.y = playerBody.getPosition().y * PPM + 15;
        characterMiddle.x = position.x + currentFrame.getRegionWidth()/2.0f;
        characterMiddle.y = position.y + currentFrame.getRegionHeight()/2.0f;

        super.render(delta, batch);
    }

    /**
     * character specific movement
     * @param delta time since last frame
     */
    @Override
    public void move(float delta) {
        super.move(delta);
        playerBody.setLinearVelocity(horizontalForce * 50, verticalForce * 50);
    }
}
