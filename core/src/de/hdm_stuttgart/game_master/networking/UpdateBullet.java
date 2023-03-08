package de.hdm_stuttgart.game_master.networking;

import de.hdm_stuttgart.game_master.character.weapon_ability.BulletType;

public class UpdateBullet {

    public int id;
    public float bulletXPos;
    public float bulletYPos;
    public float bulletDirX;
    public float bulletDirY;
    public float bulletRotation;
    public float bulletSpeed;
    public boolean isFriendly;
    public BulletType type;
    public int color;
    public int distance;

    public void init(float x, float y, float dirx, float diry, float rot, float speed, int dist, boolean friendly, BulletType type, int col) {

        bulletXPos = x;
        bulletYPos = y;
        bulletDirX = dirx;
        bulletDirY = diry;
        bulletRotation = rot;
        distance = dist;
        bulletSpeed = speed;
        this.type = type;
        color = col;
    }
}
