package de.hdm_stuttgart.game_master.character;

import de.hdm_stuttgart.game_master.character.weapon_ability.ArcWeapon;

public class Archer extends Enemy {

    Archer(float x, float y) {
        super(x, y);
        currentWeapon = new ArcWeapon(this, 2, 6, 3, 8);
    }
}
