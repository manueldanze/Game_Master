package de.hdm_stuttgart.game_master.character;

import de.hdm_stuttgart.game_master.character.weapon_ability.ArcWeapon;

public class SuperArcher extends Enemy {
    SuperArcher(float x, float y) {
        super(x, y);
        currentWeapon = new ArcWeapon(this, 5, 12, 3.5f, 12);
    }
}
