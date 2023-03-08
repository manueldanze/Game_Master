package de.hdm_stuttgart.game_master.character.weapon_ability;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.screens.GameScreen;

import static de.hdm_stuttgart.game_master.helper.BoxHelpers.createBody;
import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class WeaponBullet {

    private TextureAtlas bullets = ScreenHandler.manager.get("weapons/bullets.atlas", TextureAtlas.class);
    private Sprite bulletSprite = new Sprite(bullets.findRegion("bullet"));
    Vector2 direction = new Vector2();
    float currentRot;
    private float bulletSpeed;
    private int range;
    private float travelledDist;
    public final Body bulletBody;
    private final boolean friendly;

    /**
     * Creates a rogue bullet not fired from a specific weapon. Location, direction and speed can be determined arbitrarily.
     * Used for networking.
     */
    public WeaponBullet(float posX, float posY, float dirX, float dirY, float rotation, float speed, int dist, boolean friendly, BulletType bulletType, int bulletCol) {
        this.friendly = friendly;
        bulletSpeed = speed;
        range = dist;
        Vector2 dir = new Vector2(dirX, dirY);
        bulletBody = createBody(posX / PPM, posY / PPM, 20, 20, "bullet", false, true, friendly);
        bulletSprite.setRegion(bullets.findRegion(bulletType.fileName + bulletCol));
        bulletSprite.setX(bulletBody.getPosition().x);
        bulletSprite.setY(bulletBody.getPosition().y);
        direction.x += dir.nor().x;
        direction.y += dir.nor().y;
        bulletBody.setLinearVelocity(dir.scl(bulletSpeed));
        currentRot = rotation;
    }

    public WeaponBullet(float posX, float posY, float dirX, float dirY, float rotation, float speed, boolean friendly) {
        this(posX, posY, dirX, dirY, rotation, speed, 500, friendly, BulletType.BULLET, 7);
    }

    WeaponBullet(Weapon originWeapon, Vector2 dir, float speed, int dist, boolean friendly, BulletType bulletType, int color) {
        this.friendly = friendly;
        bulletSpeed = speed;
        range = dist;
        bulletBody = createBody(originWeapon.weaponEnd.x / PPM, originWeapon.weaponEnd.y / PPM, 20, 20, "bullet", false, true, friendly);
        bulletSprite.setRegion(bullets.findRegion(bulletType.fileName + color));
        bulletSprite.setX(bulletBody.getPosition().x);
        bulletSprite.setY(bulletBody.getPosition().y);
        direction.x += dir.nor().x;
        direction.y += dir.nor().y;
        bulletBody.setLinearVelocity(direction.scl(bulletSpeed));
        currentRot = originWeapon.weaponUser.getWeaponRotation();
    }

    /**
     * render shot bullets
     * @param delta time since last frame
     * @param batch spritebatch for drawing texture
     */
    public void render(float delta, SpriteBatch batch) {

        batch.draw(bulletSprite, bulletBody.getPosition().x * PPM, bulletBody.getPosition().y * PPM, bulletSprite.getOriginX(), bulletSprite.getOriginY(), bulletSprite.getWidth(), bulletSprite.getHeight(), bulletSprite.getScaleX(), bulletSprite.getScaleY(), currentRot);
        travelledDist += bulletSpeed / range;

        if ((this.bulletBody.getPosition().y > GameScreen.getMapHeight() * 4 - 13 || this.bulletBody.getPosition().y < 5) || (this.bulletBody.getPosition().x > GameScreen.getMapWidth() * 4 - 10 || this.bulletBody.getPosition().x < 2)) {
            GameWorld.getFiredBullets().removeValue(this, true);
            GameScreen.getWorld().destroyBody(this.bulletBody);
        }

        if (travelledDist >= range) {
            GameWorld.getFiredBullets().removeValue(this, true);
            GameScreen.getWorld().destroyBody(this.bulletBody);
        }
    }

    public float getSpeed() { return bulletSpeed; }
    public boolean isFriendly() { return friendly; }

    public void setColor(int num) {
        bulletSprite.setRegion(bullets.findRegion("bullet"+num));
    }
}
