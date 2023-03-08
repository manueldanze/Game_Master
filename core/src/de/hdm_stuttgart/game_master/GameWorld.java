package de.hdm_stuttgart.game_master;

import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import de.hdm_stuttgart.game_master.helper.WorldCreation;
import de.hdm_stuttgart.game_master.character.*;
import de.hdm_stuttgart.game_master.character.weapon_ability.BulletType;
import de.hdm_stuttgart.game_master.character.weapon_ability.WeaponBullet;
import de.hdm_stuttgart.game_master.networking.*;
import de.hdm_stuttgart.game_master.networking.VirtualPlayer;
import de.hdm_stuttgart.game_master.networking.NetworkHandler;
import de.hdm_stuttgart.game_master.networking.UpdatePlayer;
import de.hdm_stuttgart.game_master.networking.UpdateX;
import de.hdm_stuttgart.game_master.networking.UpdateY;
import de.hdm_stuttgart.game_master.screens.ConnectionScreen;
import de.hdm_stuttgart.game_master.screens.GameScreen;
import de.hdm_stuttgart.game_master.screens.SelectionScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GameWorld {

    public static final Random rand = new Random();

    public static boolean iAmGM = false;
    public static PlayerControllable localPlayer;
    private static int timer = 120;
    static NetworkHandler network = new NetworkHandler();
    private static final Array<Body> firedBulletBodies = new Array<>();
    private static final Array<WeaponBullet> firedBullets = new Array<>();
    private static final Queue<WeaponBullet> bulletQueue = new Queue<>();
    private static ScreenAdapter currentScreen;
    public static final boolean DEV_MODE_ENABLED = false;

    public static Map<Integer, VirtualPlayer> players = new HashMap<>();

    private static final Array<Enemy> activeEnemies = new Array<>();
    private static final Map<Integer, ServerEnemy> virtualEnemies = new HashMap<>();

    private static final Array<AITargetable> activePlayers = new Array<>();

    public static boolean chooseGameMaster = false;

    public static boolean setGameMaster = false;
    private static boolean iAmHost = false;
    public static boolean startTimer = false;

    private GameWorld() {}

    public static void initLogic() {

        WorldCreation.createBounds();
        createSelectedChar(SelectionScreen.getCharType());
        connect();

        for (Enemy enemy : GameWorld.getActiveEnemies()) {
            MessageManager.getInstance().addListeners(enemy, 0, 1, 2, 3, 4, 5); // Siehe Klasse "MessageType"
        }
    }

    public static void createSelectedChar(String charType) {

        if (Objects.equals(charType, "rangedDD")){
            localPlayer = new RangedDD(50, 50, ConnectionScreen.getUsername());
        }
        if (Objects.equals(charType, "meleeDD")){
            localPlayer = new MeleeDD(50, 50, ConnectionScreen.getUsername());
        }
        if (Objects.equals(charType, "tank")){
            localPlayer = new Tank(50, 50, ConnectionScreen.getUsername());
        }
        else if (Objects.equals(charType, "healer")) {
            localPlayer = new Healer(50, 50, ConnectionScreen.getUsername());
        }
        activePlayers.add((AITargetable) localPlayer);
    }

    public static void connect() {
        network.connect();
    }

    public static void update(float delta){

        GameWorld.localPlayer.move(delta);

        if (localPlayer instanceof Player player) {

            UpdatePlayer updatePlayer = new UpdatePlayer();
            updatePlayer.playerClass = player.getCharType();
            updatePlayer.flipped = player.isFlipped();
            updatePlayer.walking = player.isWalking();
            updatePlayer.middleX = player.getPosition().x;
            updatePlayer.middleY = player.getPosition().y;

            if (GameWorld.players.isEmpty()){
                updatePlayer.playerNumber = 1;
            }
            if (GameWorld.players.size()==1){
                updatePlayer.playerNumber = 2;
            }
            if (GameWorld.players.size()==2){
                updatePlayer.playerNumber = 3;
            }
            if (GameWorld.players.size()==3){
                updatePlayer.playerNumber = 4;
            }
            updatePlayer.startTimer = GameWorld.startTimer;

            network.getClient().sendUDP(updatePlayer);

            UpdateGameMaster updateGameMaster = new UpdateGameMaster();
            updateGameMaster.chooseGameMaster = chooseGameMaster;
            network.getClient().sendUDP(updateGameMaster);
            chooseGameMaster = false;

            UpdateWeapon updateWeapon = new UpdateWeapon();
            updateWeapon.weaponType = "rifle";
            updateWeapon.velocityX = player.getCurrentWeapon().velocity.x;
            updateWeapon.velocityY = player.getCurrentWeapon().velocity.y;
            updateWeapon.weaponRotation = player.getWeaponRotation();
            updateWeapon.weaponEndX = player.getCurrentWeapon().weaponEnd.x;
            updateWeapon.weaponEndY = player.getCurrentWeapon().weaponEnd.y;
            updateWeapon.flipped = Gdx.input.getX() < Gdx.graphics.getWidth() / 2.0f;
            network.getClient().sendUDP(updateWeapon);

            //Update position
            if (player.networkPosition.x != player.getPosition().x) {
                //Send the player's X value
                UpdateX packet = new UpdateX();
                packet.x = player.getPosition().x;
                network.getClient().sendUDP(packet);

                player.networkPosition.x = player.getPosition().x;
            }
            if (player.networkPosition.y != player.getPosition().y) {
                //Send the player's Y value
                UpdateY packet = new UpdateY();
                packet.y = player.getPosition().y;
                network.getClient().sendUDP(packet);

                player.networkPosition.y = player.getPosition().y;
            }
        }

        GameScreen.getWorld().step(1/60f, 6, 2);
        renderEnemies();
    }

    public static void countDownTime() { timer--; }

    /**
     * @param type The integer that determines which type of enemy will be spawned
     */
    public static void sendEnemy(float posX, float posY, int type) {
        EnemySpawn enemySpawn = new EnemySpawn();
        enemySpawn.xPos = posX;
        enemySpawn.yPos = posY;
        enemySpawn.type = type;
        network.getClient().sendUDP(enemySpawn);
    }

    /**
     * Creates temporary {@link ServerEnemy} data entry to be converted into a real Enemy entity
     * @param type ID of enemy type
     */
    public static void createEnemy(int id, float xPos, float yPos, int type) {

        virtualEnemies.put(id, new ServerEnemy(id, xPos, yPos, GameMasterAction.values()[type].enemyType));
    }

    /**
     * Converts enemy data entries from the respective HashMap into real {@link Enemy} entities
     */
    public static void renderEnemies() {

        if (virtualEnemies.size() > 0) {
            for (ServerEnemy serverEnemy : virtualEnemies.values()) {
                addEnemy(Enemy.create(serverEnemy.xPos, serverEnemy.yPos, serverEnemy.type));
                virtualEnemies.remove(serverEnemy.id);
            }
        }
    }

    public static void sendBullet(float posX, float posY, float dirX, float dirY, float rotation, float speed, int dist, boolean friendly, BulletType type, int color) {

        UpdateBullet updateBullet = new UpdateBullet();
        updateBullet.init(posX, posY, dirX, dirY, rotation, speed, dist, friendly, type, color);
        network.getClient().sendUDP(updateBullet);
    }

    public static void addBulletToQueue(WeaponBullet b) {
        bulletQueue.addLast(b);
    }

    public static void popBulletQueue() {

        if (!GameScreen.getWorld().isLocked() && !bulletQueue.isEmpty()) {
            WeaponBullet b = bulletQueue.first();
            bulletQueue.removeFirst();

            GameWorld.firedBulletBodies.add(b.bulletBody);
            GameWorld.firedBullets.add(b);
        }
    }

    public static void becomeGameMaster() {

        iAmGM = true;
        GameScreen.getWorld().destroyBody(((Player)localPlayer).getPlayerBody());
        activePlayers.removeValue((AITargetable) localPlayer, true);
        network.getClient().sendUDP(new RemovePlayer());
        localPlayer = new GameMaster();
        Gdx.input.setInputProcessor(((GameMaster)localPlayer).getInputProcessor());
    }

    public static void becomePlayer() {
        createSelectedChar(SelectionScreen.getCharType());
    }

    public static PlayerControllable getLocalPlayer() { return localPlayer; }

    public static ImmutableArray<Enemy> getActiveEnemies() {
        return new ImmutableArray<>(activeEnemies);
    }

    public static ImmutableArray<AITargetable> getActivePlayers() {
        return new ImmutableArray<>(activePlayers);
    }

    public static Array<WeaponBullet> getFiredBullets() { return firedBullets; }

    public static void addPlayer(AITargetable pl) {
        activePlayers.add(pl);
    }

    public static void removePlayer(AITargetable pl) {
        activePlayers.removeValue(pl, true);
    }

    public static void addEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public static void removeEnemy(Enemy enemy) {
        activeEnemies.removeValue(enemy, true);
    }

    public static void removeEnemyAt(int index) {
        if (index >= activeEnemies.size) return;activeEnemies.get(index).kill();
    }
    public static ScreenAdapter getCurrentScreen() { return currentScreen; }
    public static void setCurrentScreen(ScreenAdapter screen) { currentScreen = screen; }

    public static boolean isHost() { return iAmHost; }
    public static void setHost(boolean val) { iAmHost = val; }
    public static int getTimer() { return timer; }
}
