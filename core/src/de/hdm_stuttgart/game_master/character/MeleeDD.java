package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.ScreenHandler;

import static de.hdm_stuttgart.game_master.helper.BoxHelpers.createBody;
import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class MeleeDD extends Player{

    /**
     * Constructor for Melee Damage Dealer Class
     * @param x x coordinate of spawn
     * @param y y coordinate of spawn
     */
    public MeleeDD(int x, int y, String username) {
        super(x, y);
        currentFrame = new TextureRegion();
        playerBody = createBody(x, y, 100, 95, username, false, false);
        position = new Vector2(x, y);

        //sprite animations
        TextureAtlas idleAtlas = ScreenHandler.manager.get("game_characters/meleeDD/meleeDD_idle.atlas",TextureAtlas.class);
        TextureAtlas runAtlas = ScreenHandler.manager.get("game_characters/meleeDD/meleeDD_run.atlas",TextureAtlas.class);
        idleAnimation = new Animation<TextureRegion>(0.1f, idleAtlas.findRegions("meleeDD_idle"), Animation.PlayMode.LOOP);
        runAnimation = new Animation<TextureRegion>(0.1f, runAtlas.findRegions("meleeDD_run"), Animation.PlayMode.LOOP);

        weaponSprite = currentWeapon.getWeaponSprite();
        this.username = username;
        characterMiddle.x = playerBody.getPosition().x + currentFrame.getRegionWidth()/2.0f;
        characterMiddle.y = playerBody.getPosition().y + currentFrame.getRegionHeight()/2.0f;
        charType = "meleeDD";
        maxHP = 17;
        hp = maxHP;
    }

    /**
     * method for rendering MeleeDD character
     * @param delta time since last frame
     * @param batch batch for texture
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
