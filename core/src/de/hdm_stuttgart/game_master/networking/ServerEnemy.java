package de.hdm_stuttgart.game_master.networking;

import de.hdm_stuttgart.game_master.character.Enemy;

public class ServerEnemy {

    public int id;
    public float xPos;
    public float yPos;
    public Class<? extends Enemy> type;

    public ServerEnemy(int id, float xPos, float yPos, Class<? extends Enemy> type) {

        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
    }
}
