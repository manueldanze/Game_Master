package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.helper.HudHelper;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.character.weapon_ability.LinearWeapon;
import de.hdm_stuttgart.game_master.character.weapon_ability.Weapon;
import de.hdm_stuttgart.game_master.character.weapon_ability.WeaponBullet;
import de.hdm_stuttgart.game_master.screens.GameScreen;

import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public abstract class GameCharacter implements PlayerControllable, AITargetable {

    protected Texture charTexture;
    protected Body playerBody;
    protected float animationTime = 0.0f;
    protected Sprite weaponSprite;
    protected TextureRegion currentFrame;
    protected int maxHP;
    protected int hp;
    protected float moveSpeed = 400.0f;
    private int id;
    public final Vector2 networkPosition = new Vector2(0,0);

    Vector2 position;
    public final Vector2 aimingPos = new Vector2(0, 0); // Analog zu Gdx.input beim Player
    protected final Vector2 characterMiddle;
    protected boolean healable;
    protected boolean flipped;
    protected boolean isWalking;
    protected Weapon currentWeapon;
    protected Array<WeaponBullet> bulletsToRemove;
    protected int width = 200;
    protected int height = 200;
    protected String charType;
    protected boolean iFrame;
    protected float ref;

    protected Animation<TextureRegion> idleAnimation;
    protected Animation<TextureRegion> runAnimation;

    /**
     * constructor of characters
     * @param x x coordinate of spawn
     * @param y y coordinate of spawn
     */
    protected GameCharacter(float x, float y) {
        charTexture = new Texture("badlogic.jpg");
        position = new Vector2(x, y);

        //default asset
        TextureAtlas idleAtlas = ScreenHandler.manager.get("game_characters/enemy/otherEnemy/cyberEnemy_idle.atlas",TextureAtlas.class);
        TextureAtlas runAtlas = ScreenHandler.manager.get("game_characters/enemy/otherEnemy/cyberEnemy_run.atlas",TextureAtlas.class);
        idleAnimation = new Animation<>(0.1f, idleAtlas.findRegions("cyberEnemy_idle"), Animation.PlayMode.LOOP);
        runAnimation = new Animation<>(0.1f, runAtlas.findRegions("cyberEnemy_run"), Animation.PlayMode.LOOP);

        characterMiddle = new Vector2();
        currentWeapon = new LinearWeapon(this, 20, 50, 3);
        bulletsToRemove = new Array<>();
        weaponSprite = currentWeapon.getWeaponSprite();
    }

    /**
     * method for rendering characters
     * @param delta time since last frame
     * @param batch Spritebatch for texture
     */
    public void render(float delta, SpriteBatch batch) {
        if (!HudHelper.isHudClickable()) {
            aim();
        }

        if (iFrame) {
            if (ref > 0) {
                ref -= delta;
            } else {
                iFrame = false;
            }
        }

        move(delta);
        checkHP();
        animationTime += delta;
        if (isWalking) {
            currentFrame = runAnimation.getKeyFrame(animationTime);
        } else {
            currentFrame = idleAnimation.getKeyFrame(animationTime);
        }
        width = currentFrame.getRegionWidth() * 2;
        height = currentFrame.getRegionHeight() * 2;

        batch.draw(currentFrame, !flipped? position.x - currentFrame.getRegionWidth()/2.0f : position.x + currentFrame.getRegionWidth()/2.0f + (currentFrame.getRegionWidth() * 2.0f) / PPM, position.y - currentFrame.getRegionHeight(), !flipped? currentFrame.getRegionWidth() * 2.0f : -currentFrame.getRegionWidth() * 2.0f, currentFrame.getRegionHeight() * 2.0f);
        renderWeapon(batch);


        for (WeaponBullet bullet : GameWorld.getFiredBullets()) {
            bullet.render(delta, batch);
        }
    }

    protected void renderWeapon(SpriteBatch batch) {
        if (!HudHelper.isHudClickable()){
            batch.draw(weaponSprite, currentWeapon.velocity.x/3.5f + position.x - currentFrame.getRegionWidth()/2.4f, currentWeapon.velocity.y/3.5f + position.y - 10, weaponSprite.getOriginX(), weaponSprite.getOriginY(), weaponSprite.getWidth(), weaponSprite.getHeight(), weaponSprite.getScaleX(), weaponSprite.getScaleY(), getWeaponRotation());
            currentWeapon.weaponEnd.x = currentWeapon.velocity.x/3.5f + position.x - currentFrame.getRegionWidth() + 15;
            currentWeapon.weaponEnd.y = currentWeapon.velocity.y/3.5f + position.y - currentFrame.getRegionHeight() + 10;
        }
    }

    /**
     * method for changed after each frame
     */
    public void update() {

    }

    public abstract void kill();

    public abstract void checkHP();

    /**
     * method for aiming: create vector to mouse position and normalize it to make bullet travel at equal speed ignoring the position of the mouse
     */
    public abstract void aim();

    public abstract float getWeaponRotation();

    public void recieveHealth(int amount){

        hp += amount;
        setHp(hp);
    }

    public void takeDamage(int amount) {

        hp -= amount;
        setHp(hp);
    }

    /**
     * checks for solid next to character
     * @param direction direction to check for solid prop
     * @return boolean solid
     */
    protected boolean isNextSolid(String direction) {

        TiledMapTileLayer cellLayer = (TiledMapTileLayer) GameScreen.getMap().getLayers().get("background");
        switch (direction) {
            case("right"):
                return (cellLayer.getCell((int) characterMiddle.x / cellLayer.getTileWidth() + 1, (int) characterMiddle.y / cellLayer.getTileWidth()).getTile().getProperties().containsKey("blocked"));
            case("left"):
                return cellLayer.getCell((int) characterMiddle.x/ cellLayer.getTileWidth() - 1, (int) characterMiddle.y/ cellLayer.getTileWidth()).getTile().getProperties().containsKey("blocked");
            case("up"):
                return cellLayer.getCell((int) characterMiddle.x/ cellLayer.getTileWidth(), (int) characterMiddle.y/ cellLayer.getTileWidth() + 1).getTile().getProperties().containsKey("blocked");
            case("down"):
                return cellLayer.getCell((int) characterMiddle.x/ cellLayer.getTileWidth(), (int) characterMiddle.y/ cellLayer.getTileWidth() - 1).getTile().getProperties().containsKey("blocked");
            default:
                return false;
        }
    }

    @Override public Vector2 getPosition() {
        return new Vector2(position.x, position.y);
    }

    @Override public Vector2 getCameraPosition() { return new Vector2(playerBody.getPosition()); }

    @Override public Vector2 getAimingPos() { return aimingPos; }

    public Body getPlayerBody() { return playerBody; }

    public boolean isFlipped() {
        return flipped;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    @Override public int getHP() { return maxHP; }

    @Override public int getCurrentHP() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getCharType() {
        return charType;
    }
}
