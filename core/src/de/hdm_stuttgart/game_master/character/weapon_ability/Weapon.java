package de.hdm_stuttgart.game_master.character.weapon_ability;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.GameWorld;
import com.badlogic.gdx.utils.Timer;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.character.GameCharacter;

public abstract class Weapon {

    protected final Sound shootingSound = ScreenHandler.manager.get("audio/music/effects/LaserShort.wav", Sound.class);
    protected final Sound dischargeSound = ScreenHandler.manager.get("audio/music/effects/LaserFireMultiple.wav", Sound.class);

    protected GameCharacter weaponUser;
    public final Vector2 weaponEnd;
    private int dmg;
    protected float fireDelay; // Verzögerung in Sekunden. Im Konstruktor wird stattdessen die Feuerrate in Schüsse/Sekunde angegeben
    protected final int magCapacity;
    protected int magSize;
    protected float reloadTime;
    protected boolean isReloading = false; // Fix für IllegalArgumentException: The same task may not be scheduled twice
    protected boolean ready = true;
    protected boolean dischargeReady = false;
    protected float dischargeCooldown = 40;
    protected Timer fireRateTimer = new Timer();
    protected Timer reloadTimer = new Timer();
    protected Timer dischargeTimer = new Timer();
    protected Timer dischargeShot = new Timer();
    protected Timer.Task reloadTask = new Timer.Task() {
        @Override
        public void run() {
            magSize = magCapacity;
            isReloading = false;
        }
    };
    protected Timer.Task fireRateDelayTask = new Timer.Task() {
        @Override
        public void run() {
            ready = true;
        }
    };
    protected Timer.Task dischargeRefreshTask = new Timer.Task() {
        @Override
        public void run() {
            dischargeReady = true;
        }
    };
    private float bulletSpeed = 10;
    protected int bulletColor = 1;
    private Sprite weaponSprite;
    private TextureRegion weaponTextureRegion;
    public final Vector2 velocity = new Vector2();

    public int getDamage() { return dmg; }

    /**
     * @return Fire rate in shots per second
     */
    public float getFireRate() { return 1000/fireDelay; }

    /**
     * @return Current magazine clip contents
     */
    public int getMagazineSize() { return magSize; }

    /**
     * @return Maximum magazine capacity
     */
    public int getMagazineCapacity() { return magCapacity; }

    public float getBulletSpeed() { return bulletSpeed; }

    protected final void spawnBullet(float speed, int dist, boolean friendly, BulletType type, int col) {
        WeaponBullet b = new WeaponBullet(this, velocity, speed, dist, friendly, type, col);
        GameWorld.addBulletToQueue(b);
        if (b.isFriendly()) {
            GameWorld.sendBullet(weaponEnd.x, weaponEnd.y, b.direction.x, b.direction.y, b.currentRot, b.getSpeed(), dist, true, type, col);
        }
    }

    protected final void spawnBullet(float speed, int dist, boolean friendly) {
        spawnBullet(speed, dist, friendly, BulletType.BULLET, 7);
    }

    protected final void spawnBullet(boolean friendly, BulletType shape, int col) {
        spawnBullet(20, 500, friendly, shape, col);
    }

    protected final void spawnBullet(boolean friendly, int col) {
        spawnBullet(20, 500, friendly, BulletType.BULLET, col);
    }

    protected final void spawnBullet(boolean friendly) {
        spawnBullet(20, 500, friendly, BulletType.BULLET, 1);
    }

    public void shoot(boolean friendly) {
        shootingSound.play(0.2f);
    }
    public void discharge() {
        dischargeSound.play(0.2f);
    }


    protected Weapon(GameCharacter weaponUser, TextureRegion tex, float fireRate, int magCapacity, float reloadTime) {
        if (fireRate == 0) throw new IllegalArgumentException("Fire rate cannot be zero");

        this.weaponUser = weaponUser;
        weaponEnd = new Vector2();
        this.fireDelay = 1/fireRate;
        this.magCapacity = magCapacity;
        magSize = magCapacity;
        this.reloadTime = reloadTime;
        weaponTextureRegion = tex;
        weaponSprite = new Sprite(weaponTextureRegion);

        dischargeTimer.scheduleTask(dischargeRefreshTask, dischargeCooldown);
    }

    public Sprite getWeaponSprite() {
        return weaponSprite;
    }

    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
}
