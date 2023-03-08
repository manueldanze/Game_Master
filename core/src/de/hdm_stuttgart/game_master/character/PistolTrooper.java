package de.hdm_stuttgart.game_master.character;

import de.hdm_stuttgart.game_master.character.weapon_ability.LinearWeapon;

public class PistolTrooper extends Enemy {

    PistolTrooper(float x, float y) {
        super(x, y);
        currentWeapon = new LinearWeapon(this, 3f, 10, 60, 2);
    }
}
