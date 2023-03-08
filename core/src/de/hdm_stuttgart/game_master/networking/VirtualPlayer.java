package de.hdm_stuttgart.game_master.networking;

import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.character.AITargetable;

public class VirtualPlayer implements AITargetable {

    public float x=100, y=100;
    public int id;
    public String playerClass;
    public String username;
    public int playerNumber;
    public int hp;
    public boolean startTimer;
    public boolean flipped;
    public boolean walking;
    public float middleX;
    public float middleY;

    public String weaponType;
    public float velocityX;
    public float velocityY;
    public float weaponRotation;
    public float weaponEndX;
    public float weaponEndY;
    public boolean flippedWeapon;

    @Override
    public Vector2 getPosition() {
        return new Vector2(x, y);
    }
}
