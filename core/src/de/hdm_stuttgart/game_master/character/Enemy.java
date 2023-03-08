package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.MessageType;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.character.weapon_ability.ArcWeapon;
import de.hdm_stuttgart.game_master.screens.GameScreen;

import static de.hdm_stuttgart.game_master.helper.BoxHelpers.createBody;
import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class Enemy extends GameCharacter implements Telegraph {

    private boolean canMove = false;
    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private final Vector2 aimingPosPrev = new Vector2(aimingPos);
    private float detectionRange = 800.0f;

    private AITargetable curTarget = null;

    public Vector2 dodgePos = new Vector2(position.x, position.y); //TODO Private machen und Debugging-Code aus GameScreen entfernen
    private int dodgeDelay = 20;
    private int dodgeTimer = 0;

    Enemy(float x, float y) {
        super(x, y);
        charType = "enemy";
        moveSpeed = 400.0f;
        playerBody = createBody(x / PPM, y / PPM,10 , 100, 95, "enemy", false, true);
        currentWeapon = new ArcWeapon(this, 2, 20, 2, 8, 1f);
    }

    /**
     * Internal factory method
     * @return New enemy instance
     */
    public static Enemy create(float x, float y) {
        return new Enemy(x, y);
    }

    /**
     * Internal factory method
     * @return New enemy instance
     */
    public static Enemy create(float x, float y, Class<? extends Enemy> type) {
        if (type.equals(TackShooter.class)) {
            return new TackShooter(x, y);
        }

        else if (type.equals(PistolTrooper.class)) {
            return new PistolTrooper(x, y);
        }

        else if (type.equals(Archer.class)) {
            return new Archer(x, y);
        }

        else return new Enemy(x, y);
    }

    @Override
    public void kill() {
        GameWorld.removeEnemy(this);
        GameScreen.getWorld().destroyBody(playerBody);
    }

    @Override
    public void checkHP() {
        if (this.playerBody.getFixtureList().get(0).getUserData().equals(0)) {
            kill();
        }
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case MessageType.STOP_MOVING:
                stopMove();
                return true;

            case MessageType.GO_UP:
                moveUp();
                return true;

            case MessageType.GO_DOWN:
                moveDown();
                return true;

            case MessageType.GO_LEFT:
                moveLeft();
                return true;

            case MessageType.GO_RIGHT:
                moveRight();
                return true;

            case MessageType.SHOOT:
                currentWeapon.shoot(false);
                return true;

            default:
                GdxAI.getLogger().error("AI", "Sent telegram message with unknown message id" + msg.message);
                return false;
        }
    }

    @Override
    public void aim() {

        for (AITargetable player : GameWorld.getActivePlayers()) {
            if (player.getPosition().dst(this.getPosition()) <= detectionRange) {
                curTarget = player;
                currentWeapon.shoot(false);
                break;
            }
            else {
                curTarget = null;
                aimingPos.set(aimingPosPrev); // Behebt einen kleinen Bug, bei dem die Waffe plötzlich aus der Map fliegt, wenn die KI kein Ziel hat
            }
        }

        if (curTarget != null) {
            aimingPos.x = curTarget.getPosition().x;
            aimingPos.y = curTarget.getPosition().y;
            aimingPosPrev.set(aimingPos);
        }

        currentWeapon.velocity.set(new Vector2(aimingPos).sub(currentWeapon.weaponEnd));
    }

    private void dodge() {
        int dodgingRange = 50;
        if (canMove) {
            dodgePos = new Vector2((GameWorld.rand.nextInt((int) position.x + dodgingRange) + position.x - dodgingRange) * 0.5f,
                    (GameWorld.rand.nextInt((int) position.y + dodgingRange) + position.y - dodgingRange) * 0.5f);

            if (dodgePos.x - position.x < 0)
                moveLeft();
            else if (dodgePos.x - position.x > 0)
                moveRight();

            if (dodgePos.y - position.y < 0)
                moveDown();
            else if (dodgePos.y - position.y > 0)
                moveUp();
        }

        dodgeTimer = 0;
        dodgeDelay = GameWorld.rand.nextInt(150)+60;
    }

    @Override
    public void move(float delta) {
        if (canMove) move(delta, isMovingUp, isMovingDown, isMovingLeft, isMovingRight);
    }

    private void move(float delta, boolean up, boolean down, boolean left, boolean right) {
        if (up && !isNextSolid("up")) {
            position.y += delta * moveSpeed;
        }

        if (left && !isNextSolid("left")) {
            position.x -= delta * moveSpeed;
        }

        if (down && !isNextSolid("down")) {
            position.y -= delta * moveSpeed;
        }

        if (right && !isNextSolid("right")) {
            position.x += delta * moveSpeed;
        }

        if (curTarget != null) {
            dodgeTimer++;
            if (dodgeTimer % dodgeDelay == 0) dodge();
        }

        if (Math.abs(dodgePos.y - position.y) < 10)
            stopVerticalMove();

        if (Math.abs(dodgePos.x - position.x) < 10)
            stopHorizontalMove();

        isWalking = isMovingUp || isMovingDown || isMovingLeft || isMovingRight;
        characterMiddle.x = position.x + width/2.0f;
        characterMiddle.y = position.y + height/2.0f;
    }

    private void moveUp(boolean resetMovement) {
        isMovingUp = true;
        isMovingDown = false;

        if (resetMovement) {
            isMovingLeft = false;
            isMovingRight = false;
        }
    }

    private void moveUp() {
        moveUp(false);
    }

    private void moveRight(boolean resetMovement) {
        isMovingLeft = false;
        isMovingRight = true;

        if (resetMovement) {
            isMovingUp = false;
            isMovingDown = false;
        }
    }

    private void moveRight() {
        moveRight(false);
    }

    private void moveDown(boolean resetMovement) {
        isMovingUp = false;
        isMovingDown = true;

        if (resetMovement) {
            isMovingLeft = false;
            isMovingRight = false;
        }
    }

    private void moveDown() {
        moveDown(false);
    }

    private void moveLeft(boolean resetMovement) {
        isMovingLeft = true;
        isMovingRight = false;

        if (resetMovement) {
            isMovingUp = false;
            isMovingDown = false;
        }
    }

    private void moveLeft() {
        moveLeft(false);
    }

    private void stopMove() {
        stopHorizontalMove();
        stopVerticalMove();
    }

    private void stopVerticalMove() {
        isMovingUp = false;
        isMovingDown = false;
    }

    private void stopHorizontalMove() {
        isMovingLeft = false;
        isMovingRight = false;
    }

    private float getWeaponAngle() {
        return (float) Math.atan2(aimingPos.y, aimingPos.x);
    }

    @Override
    public float getWeaponRotation() {
        return getWeaponAngle() * -57.3f;
    }
}
