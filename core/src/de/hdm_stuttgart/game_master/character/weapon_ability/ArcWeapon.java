package de.hdm_stuttgart.game_master.character.weapon_ability;

import de.hdm_stuttgart.game_master.helper.WeaponLoader;
import de.hdm_stuttgart.game_master.character.GameCharacter;

public class ArcWeapon extends Weapon {

    private final int radialSides;
    private float spreadFactor;
    public ArcWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime, int burstSize) {
        super(weaponUser, new WeaponLoader("weapon10").rifle, fireRate, magSize, reloadTime);
        radialSides = burstSize;
        spreadFactor = 1;
    }

    public ArcWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime, int burstSize, float spreadFactor) {
        super(weaponUser, new WeaponLoader("weapon10").rifle, fireRate, magSize, reloadTime);
        radialSides = burstSize;
        this.spreadFactor = spreadFactor;
    }

    @Override
    public void shoot(boolean friendly) {

        if (ready && magSize > 0) {
            for (int i = -radialSides/2; i < radialSides/2; i++) {
                velocity.x = -(float)Math.cos(Math.atan2(weaponUser.getPosition().sub(weaponUser.aimingPos).y, weaponUser.getPosition().sub(weaponUser.aimingPos).x) + (float)i/radialSides*spreadFactor);
                velocity.y = -(float)Math.sin(Math.atan2(weaponUser.getPosition().sub(weaponUser.aimingPos).y, weaponUser.getPosition().sub(weaponUser.aimingPos).x) + (float)i/radialSides*spreadFactor);

                if (!friendly) {
                    spawnBullet(false, 1);
                }
                else {
                    spawnBullet(true, 9);
                }
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
        super.discharge();

        if (dischargeReady) {

        }
    }
}
