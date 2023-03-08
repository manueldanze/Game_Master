package de.hdm_stuttgart.game_master.character.weapon_ability;

public enum BulletType {

    BULLET("bullet"), EMBER("embers"), RAY("ray");

    public final String fileName;

    BulletType(String name) { fileName = name; }
}
