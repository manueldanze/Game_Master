package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.character.weapon_ability.ArcWeapon;
import static de.hdm_stuttgart.game_master.helper.BoxHelpers.createBody;
import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class RangedDD extends Player{

    /**
     * Constructor for Ranged Damage Dealer Class
     * @param x x coordinate of spawn
     * @param y y coordinate of spawn
     * initialize animation with TextureRegion from atlas
     */
    public RangedDD(int x, int y, String username) {
        super(x, y);
        currentFrame = new TextureRegion();
        playerBody = createBody(x, y, 100, 95, username, false, false);
        position = new Vector2(x, y);

        //sprite animations
        TextureAtlas idleAtlas = ScreenHandler.manager.get("game_characters/rangedDDnew/rangedDD_idle.atlas",TextureAtlas.class);
        TextureAtlas runAtlas = ScreenHandler.manager.get("game_characters/rangedDDnew/rangedDD_run.atlas",TextureAtlas.class);
        idleAnimation = new Animation<TextureRegion>(0.1f, idleAtlas.findRegions("rangedDD_idle"), Animation.PlayMode.LOOP);
        runAnimation = new Animation<TextureRegion>(0.1f, runAtlas.findRegions("rangedDD_run"), Animation.PlayMode.LOOP);

        currentWeapon = new ArcWeapon(this, 2, 10, 4, 12, 1.5f);
        weaponSprite = currentWeapon.getWeaponSprite();
        this.username = username;
        characterMiddle.x = playerBody.getPosition().x + currentFrame.getRegionWidth()/2.0f;
        characterMiddle.y = playerBody.getPosition().y + currentFrame.getRegionHeight()/2.0f;
        charType = "rangedDD";
        maxHP = 15;
        hp = maxHP;
    }

    @Override
    public void render(float delta, SpriteBatch batch) {
        position.x = playerBody.getPosition().x * PPM;
        position.y = playerBody.getPosition().y * PPM + 15;
        characterMiddle.x = position.x + currentFrame.getRegionWidth()/2.0f;
        characterMiddle.y = position.y + currentFrame.getRegionHeight()/2.0f;

        super.render(delta, batch);
    }

    /**
     * method for changed after each frame
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * character specific movement
     * @param delta time since last frame
     */
    @Override
    public void move(float delta) {
        super.move(delta);
    }

    /**
     * character specific aiming
     */
    @Override
    public void aim() {
        super.aim();
    }
}
