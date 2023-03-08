package de.hdm_stuttgart.game_master.character;

import de.hdm_stuttgart.game_master.character.weapon_ability.RadialWeapon;

public class TackShooter extends Enemy {

    TackShooter(float x, float y) {
        super(x, y);
        currentWeapon = new RadialWeapon(this, 1, 10, 3, 12);
    }
}
