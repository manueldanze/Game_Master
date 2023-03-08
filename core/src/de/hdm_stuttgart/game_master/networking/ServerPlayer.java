package de.hdm_stuttgart.game_master.networking;

import com.esotericsoftware.kryonet.Connection;

public class ServerPlayer {

    public float x, y;
    public String playerClass;
    public String username;
    public int playerNumber;
    public int hp;
    public boolean startTimer;
    public Connection c;
}
