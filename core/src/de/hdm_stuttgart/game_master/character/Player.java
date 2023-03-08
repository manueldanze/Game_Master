package de.hdm_stuttgart.game_master.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.screens.GameScreen;

public class Player extends GameCharacter {

    protected String username;
    protected int horizontalForce = 0;
    protected int verticalForce = 0;


    /**
     * Constructor for player character
     * @param x x coordinate of Spawn
     * @param y y coordinate of Spawn
     */
    public Player(float x, float y) {
        super(x, y);
    }

    @Override
    public void move(float delta) {
        horizontalForce = 0;
        verticalForce = 0;

        if (Gdx.input.isKeyJustPressed(Input.Keys.E) && GameWorld.DEV_MODE_ENABLED) {
            GameWorld.becomeGameMaster();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            MessageManager.getInstance().dispatchMessage(5);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            verticalForce += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            horizontalForce -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            verticalForce -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            horizontalForce += 1;
        }

        playerBody.setLinearVelocity(horizontalForce * 50, verticalForce * 50);
        isWalking = (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A)) || (Gdx.input.isKeyPressed(Input.Keys.S)) || Gdx.input.isKeyPressed(Input.Keys.D);
        characterMiddle.x = playerBody.getPosition().x + width/2.0f;
        characterMiddle.y = playerBody.getPosition().y + height/2.0f;
    }

    public void takeDamage(int amount){

        if (!iFrame) {
            int hp = this.getCurrentHP();
            hp -=amount;
            this.setHp(hp);
            iFrame = true;
            ref = 1;
        }
    }

    public void recieveHealth(int amount){

        int hp = this.getCurrentHP();
        hp +=amount;
        this.setHp(hp);
    }

    @Override
    public void kill() {
        GameWorld.removePlayer(this);
    }

    @Override
    public void checkHP() {}

    @Override
    public void aim() {

        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        aimingPos.set(GameScreen.screenToWorldCoords(mousePosition));

        float diffX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2.0f;
        float diffY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2.0f;
        flipped = diffX < 0;
        float angle = (float) Math.atan2(diffY, diffX);
        float velX = (float) (Math.cos(angle));
        float velY = (float) (Math.sin(angle));
        currentWeapon.velocity.x = velX;
        currentWeapon.velocity.y = -velY;
        currentWeapon.velocity.nor();
        currentWeapon.velocity.scl(200.0f); // Braucht man aus irgendeinem Grund damit die Waffe des Spielers richtig positioniert ist
        weaponSprite.setFlip(false, Gdx.input.getX() < Gdx.graphics.getWidth() / 2.0f);

        if (Gdx.input.isButtonPressed(0)) {
            currentWeapon.shoot(true);
        }
        if (Gdx.input.isButtonPressed(1)) {
            currentWeapon.discharge();
        }
    }

    @Override
    public float getWeaponRotation() {
        float diffX = Gdx.input.getX() - Gdx.graphics.getWidth() / 2.0f;
        float diffY = Gdx.input.getY() - Gdx.graphics.getHeight() / 2.0f;
        float weaponAngle = (float) Math.atan2(diffY, diffX);
        return weaponAngle * -57.3f;
    }

    /**
     * method for rendering player
     * @param delta time since last frame
     * @param batch Spritebatch for texture
     */
    @Override
    public void render(float delta, SpriteBatch batch) {
        super.render(delta, batch);
    }

    public String getUsername() {
        return username;
    }
}
