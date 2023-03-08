package de.hdm_stuttgart.game_master.character.weapon_ability;

import com.badlogic.gdx.utils.Timer;
import de.hdm_stuttgart.game_master.helper.WeaponLoader;
import de.hdm_stuttgart.game_master.character.GameCharacter;

public class RadialWeapon extends Weapon {

    private final int radialSides;
    private int dischargeShotCounter = 0;

    public RadialWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime, int sides) {
        super(weaponUser, new WeaponLoader("weapon2").rifle, fireRate, magSize, reloadTime);
        radialSides = sides;
    }

    @Override
    public void shoot(boolean friendly) {
        super.shoot(friendly);

        if (ready && magSize > 0) {
            for (int i = 0; i < radialSides; i++) {
                velocity.x = (float)Math.cos((float)1/radialSides*Math.PI*i*2); //Falls man will, dass diese Waffe auch bewusst zielt, dann muss man eine Math.atan2()-Funktion einfügen wie bei BurstWeapon
                velocity.y = (float)Math.sin((float)1/radialSides*Math.PI*i*2);

                spawnBullet(friendly, bulletColor);
                bulletColor = i % 10 + 1;
            }

            magSize--;
            ready = false;
            fireRateTimer.scheduleTask(fireRateDelayTask, fireDelay);
            super.shoot(friendly);
        }

        if (magSize <= 0 && !isReloading) {
            isReloading = true;
            reloadTimer.scheduleTask(reloadTask, reloadTime);
        }
    }

    @Override
    public void discharge() {

        if (dischargeReady) {
            dischargeShot.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    velocity.x = (float)Math.cos(dischargeShotCounter*0.4f);
                    velocity.y = (float)Math.sin(dischargeShotCounter*0.4f);

                    spawnBullet(false, bulletColor);
                    dischargeShotCounter++;
                }
            }, 0, 0.01f, 100);

            dischargeShot.postTask(new Timer.Task() {
                @Override
                public void run() {
                    dischargeShotCounter = 0;
                    dischargeTimer.scheduleTask(dischargeRefreshTask, dischargeCooldown);
                }
            });
            dischargeReady = false;
        }
    }
}
