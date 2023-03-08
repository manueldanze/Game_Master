package de.hdm_stuttgart.game_master.character.weapon_ability;

import de.hdm_stuttgart.game_master.helper.WeaponLoader;
import de.hdm_stuttgart.game_master.character.GameCharacter;

public class WaveWeapon extends Weapon {

    private final float amplitude;
    private final float frequency;
    private int shotsFired = 0;

    public WaveWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime, float amplitude, float freq) {
        super(weaponUser, new WeaponLoader("weapon3").rifle, fireRate, magSize, reloadTime);
        this.amplitude = amplitude;
        frequency = freq;
    }

    public WaveWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime, float amplitude) {
        super(weaponUser, new WeaponLoader("weapon3").rifle, fireRate, magSize, reloadTime);
        this.amplitude = amplitude;
        frequency = 0.25f;
    }

    public WaveWeapon(GameCharacter weaponUser, float fireRate, int magSize, float reloadTime) {
        super(weaponUser, new WeaponLoader("weapon3").rifle, fireRate, magSize, reloadTime);
        amplitude = 0.5f;
        frequency = 0.25f;
    }

    @Override
    public void shoot(boolean friendly) {
        super.shoot(friendly);

        if (ready && magSize > 0) {
            velocity.x = -(float)Math.cos(Math.atan2(weaponUser.getPosition().sub(weaponUser.aimingPos).y, weaponUser.getPosition().sub(weaponUser.aimingPos).x)) + amplitude*(float)Math.sin(frequency*shotsFired);
            velocity.y = -(float)Math.sin(Math.atan2(weaponUser.getPosition().sub(weaponUser.aimingPos).y, weaponUser.getPosition().sub(weaponUser.aimingPos).x)) + amplitude*(float)Math.sin(frequency*shotsFired);

            spawnBullet(friendly);
            magSize--;

            shotsFired++;
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
