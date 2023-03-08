package de.hdm_stuttgart.game_master.character.weapon_ability;

import de.hdm_stuttgart.game_master.helper.WeaponLoader;
import de.hdm_stuttgart.game_master.character.GameCharacter;

public class LinearWeapon extends Weapon {
    public LinearWeapon(GameCharacter weaponUser, int tex, float fireRate, int magSize, float reloadTime) {
        super(weaponUser, new WeaponLoader("weapon"+tex).rifle, fireRate, magSize, reloadTime);
    }

    public LinearWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime) {
        this(weaponUser, 1, fireRate, magSize, reloadTime);
    }

    public LinearWeapon(GameCharacter weaponUser, float fireRate, int magSize, int speed, float reloadTime) {
        this(weaponUser, 1, fireRate, magSize, reloadTime);
        setBulletSpeed(speed);
    }

    @Override
    public void shoot(boolean friendly) {

        if (ready && magSize > 0) {
            if (!friendly) {
                spawnBullet(getBulletSpeed(), 500, false, BulletType.BULLET, 1);
            }
            else {
                spawnBullet(true, bulletColor);
                magSize--;
                bulletColor = ++bulletColor % 10 + 1;
                super.shoot(friendly);
            }

            ready = false;
            fireRateTimer.scheduleTask(fireRateDelayTask, fireDelay);
        }

        if (magSize <= 0 && !isReloading) {
            isReloading = true;
            reloadTimer.scheduleTask(reloadTask, reloadTime);
        }
    }

    @Override
    public void discharge() {
        super.discharge();
        if (dischargeReady) {

        }
    }
}
