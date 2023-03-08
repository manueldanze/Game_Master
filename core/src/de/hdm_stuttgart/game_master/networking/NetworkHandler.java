package de.hdm_stuttgart.game_master.networking;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.character.weapon_ability.BulletType;
import de.hdm_stuttgart.game_master.character.weapon_ability.WeaponBullet;
import de.hdm_stuttgart.game_master.screens.ConnectionScreen;
import de.hdm_stuttgart.game_master.screens.GameScreen;

import java.io.IOException;

public class NetworkHandler extends Listener {

    private Client client;

    public void connect() {

        client = new Client();
        client.getKryo().register(UpdateX.class);
        client.getKryo().register(UpdateY.class);
        client.getKryo().register(AddPlayer.class);
        client.getKryo().register(RemovePlayer.class);
        client.getKryo().register(UpdatePlayer.class);
        client.getKryo().register(UpdateWeapon.class);
        client.getKryo().register(UpdateBullet.class);
        client.getKryo().register(TextureRegion.class);
        client.getKryo().register(EnemySpawn.class);
        client.getKryo().register(UpdateGameMaster.class);
        client.getKryo().register(BulletType.class);
        client.addListener(this);

        client.start();
        try {
            client.connect(5000, ConnectionScreen.getIpAddress(),80,80);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void received(Connection connection, Object object){

        if (object instanceof EnemySpawn) {

            EnemySpawn enemySpawn = (EnemySpawn) object;
            if (!GameScreen.getWorld().isLocked()) {
                GameWorld.createEnemy(enemySpawn.id, enemySpawn.xPos, enemySpawn.yPos, enemySpawn.type);
            }

        } else if(object instanceof UpdatePlayer) {

            UpdatePlayer updatePlayer = (UpdatePlayer) object;
            GameWorld.players.get(updatePlayer.id).playerClass = updatePlayer.playerClass;
            GameWorld.players.get(updatePlayer.id).username = updatePlayer.username;
            GameWorld.players.get(updatePlayer.id).playerNumber = updatePlayer.playerNumber;
            GameWorld.players.get(updatePlayer.id).hp = updatePlayer.hp;
            if (updatePlayer.startTimer){GameWorld.startTimer = true;}
            GameWorld.players.get(updatePlayer.id).flipped = updatePlayer.flipped;
            GameWorld.players.get(updatePlayer.id).walking = updatePlayer.walking;
            GameWorld.players.get(updatePlayer.id).middleX = updatePlayer.middleX;
            GameWorld.players.get(updatePlayer.id).middleY = updatePlayer.middleY;

        } else if(object instanceof UpdateGameMaster) {

            UpdateGameMaster updateGameMaster = (UpdateGameMaster) object;
            if (updateGameMaster.becomeGameMaster){
                GameWorld.setGameMaster = true;
                updateGameMaster.becomeGameMaster = false;
            }

        }else if(object instanceof UpdateWeapon) {

            UpdateWeapon updateWeapon = (UpdateWeapon) object;
            GameWorld.players.get(updateWeapon.id).weaponType = updateWeapon.weaponType;
            GameWorld.players.get(updateWeapon.id).velocityX = updateWeapon.velocityX;
            GameWorld.players.get(updateWeapon.id).velocityY = updateWeapon.velocityY;
            GameWorld.players.get(updateWeapon.id).weaponRotation = updateWeapon.weaponRotation;
            GameWorld.players.get(updateWeapon.id).weaponEndX = updateWeapon.weaponEndX;
            GameWorld.players.get(updateWeapon.id).weaponEndY = updateWeapon.weaponEndY;
            GameWorld.players.get(updateWeapon.id).flippedWeapon = updateWeapon.flipped;

        } else if(object instanceof UpdateBullet) {

            UpdateBullet updateBullet = (UpdateBullet) object;
            WeaponBullet netBullet = new WeaponBullet(updateBullet.bulletXPos, updateBullet.bulletYPos, updateBullet.bulletDirX, updateBullet.bulletDirY, updateBullet.bulletRotation, updateBullet.bulletSpeed, updateBullet.distance, false, updateBullet.type, updateBullet.color);
            GameWorld.addBulletToQueue(netBullet);

        }else if(object instanceof AddPlayer){

            AddPlayer player = (AddPlayer) object;
            VirtualPlayer virtualPlayer = new VirtualPlayer();
            GameWorld.players.put(player.id, virtualPlayer);
            GameWorld.addPlayer(virtualPlayer);

        }else if(object instanceof RemovePlayer){

            RemovePlayer player = (RemovePlayer) object;
            GameWorld.removePlayer(GameWorld.players.get(player.id));
            GameWorld.players.remove(player.id);

        }else if(object instanceof UpdateX){

            UpdateX updateX = (UpdateX) object;
            GameWorld.players.get(updateX.id).x = updateX.x;

        }else if(object instanceof UpdateY){

            UpdateY updateY = (UpdateY) object;
            GameWorld.players.get(updateY.id).y = updateY.y;
        }
    }

    public Client getClient() { return client; }
}
