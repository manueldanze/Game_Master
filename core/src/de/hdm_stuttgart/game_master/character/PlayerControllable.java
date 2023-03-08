package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface PlayerControllable {

    void render(float delta, SpriteBatch batch);
    void move(float delta);
    Vector2 getAimingPos();
    Vector2 getPosition();
    Vector2 getCameraPosition();

    /**
     * @return Maximum Health
     */
    int getHP();

    int getCurrentHP();
}
