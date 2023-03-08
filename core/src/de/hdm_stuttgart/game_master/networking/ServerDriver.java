package de.hdm_stuttgart.game_master.networking;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.badlogic.gdx.utils.Array;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.character.weapon_ability.BulletType;

import java.io.IOException;
import java.util.*;

public class ServerDriver extends Listener {

    static Server server = new Server();
    static Map<Integer, ServerPlayer> players = new HashMap<>();

    public static final Array<Integer> connectionIDs = new Array<>();

    public static void callServerDriver() throws IOException {
        main(null);
    }

    public static void shutdownServer() {
        server.close();
        try { server.dispose(); }
        catch (IOException ignored) {}
    }

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.getKryo().register(UpdateX.class);
        server.getKryo().register(UpdateY.class);
        server.getKryo().register(AddPlayer.class);
        server.getKryo().register(RemovePlayer.class);
        server.getKryo().register(UpdatePlayer.class);
        server.getKryo().register(UpdateWeapon.class);
        server.getKryo().register(UpdateBullet.class);
        server.getKryo().register(TextureRegion.class);
        server.getKryo().register(EnemySpawn.class);
        server.getKryo().register(UpdateGameMaster.class);
        server.getKryo().register(BulletType.class);
        server.bind(80, 80);
        server.start();
        server.addListener(new ServerDriver());
    }

    public static int getRandomConnectionId(){

        return connectionIDs.get(GameWorld.rand.nextInt(connectionIDs.size));
    }
    public static void chooseGameMaster(){

        UpdateGameMaster updateGameMaster = new UpdateGameMaster();
        updateGameMaster.becomeGameMaster = true;
        int randomConnectionId = getRandomConnectionId();
        server.sendToTCP(randomConnectionId, updateGameMaster);
    }

    @Override public void connected(Connection connection){

        ServerPlayer serverPlayer = new ServerPlayer();
        serverPlayer.x = 100;
        serverPlayer.y = 100;
        serverPlayer.c = connection;

        AddPlayer addedPlayer = new AddPlayer();
        addedPlayer.id = connection.getID();
        server.sendToAllExceptTCP(connection.getID(), addedPlayer);

        for(ServerPlayer player : players.values()){
            AddPlayer addPlayer = new AddPlayer();
            addPlayer.id = player.c.getID();
            connection.sendTCP(addPlayer);
        }

        players.put(connection.getID(), serverPlayer);

        connectionIDs.add(connection.getID());
    }

    @Override public void received(Connection connection, Object object){

        if (object instanceof EnemySpawn) {

            EnemySpawn enemySpawn = (EnemySpawn) object;
            enemySpawn.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), enemySpawn);

        } else if (object instanceof UpdatePlayer) {

            UpdatePlayer updatePlayer = (UpdatePlayer) object;
            players.get(connection.getID()).playerClass = updatePlayer.playerClass;
            players.get(connection.getID()).username = updatePlayer.username;
            players.get(connection.getID()).playerNumber = updatePlayer.playerNumber;
            players.get(connection.getID()).hp = updatePlayer.hp;
            players.get(connection.getID()).startTimer = updatePlayer.startTimer;
            updatePlayer.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), updatePlayer);

        } else if (object instanceof UpdateGameMaster) {

            UpdateGameMaster updateGameMaster = (UpdateGameMaster) object;
            if (updateGameMaster.chooseGameMaster) {chooseGameMaster();}

        } else if (object instanceof UpdateWeapon) {

            UpdateWeapon updateWeapon = (UpdateWeapon) object;
            updateWeapon.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), updateWeapon);

        } else if (object instanceof UpdateBullet) {

            UpdateBullet updateBullet = (UpdateBullet) object;
            updateBullet.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), updateBullet);

        }else if(object instanceof UpdateX){

            UpdateX updateX = (UpdateX) object;
            players.get(connection.getID()).x = updateX.x;
            updateX.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), updateX);

        }else if(object instanceof UpdateY){

            UpdateY updateY = (UpdateY) object;
            players.get(connection.getID()).y = updateY.y;

            updateY.id = connection.getID();
            server.sendToAllExceptUDP(connection.getID(), updateY);
        }
    }



    @Override public void disconnected(Connection c){

        players.remove(c.getID());
        RemovePlayer packet = new RemovePlayer();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);
    }
}
