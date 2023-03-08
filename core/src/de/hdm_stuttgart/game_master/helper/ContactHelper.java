package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.physics.box2d.*;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.character.Player;
import de.hdm_stuttgart.game_master.networking.VirtualPlayer;
import de.hdm_stuttgart.game_master.screens.ConnectionScreen;

import java.util.Objects;

public class ContactHelper implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        if (fA == null || fB == null) {return;}
        if (fA.getUserData() == null || fB.getUserData() == null) {return;}

        Player player = null;

        if ((GameWorld.getLocalPlayer() instanceof Player)) player = (Player) GameWorld.getLocalPlayer();

        if (bulletHitPlayer(contact)){
            player.takeDamage(1);
            //System.out.println("Contact start: player got hit by a bullet!");
        }
        if (playerHitWall(contact)){
            //player.getDamage(1);
            //System.out.println("You: Current health of " + player.getUsername() + "/" + player.getCharType() + " is " + player.getHp());

            for(VirtualPlayer virtualPlayer : GameWorld.players.values()){
                //System.out.println("Virtual player: Current health of " + virtualPlayer.username + " / " + virtualPlayer.playerClass + " is " + virtualPlayer.hp);
            }
        }
        if (bulletHitEnemy(contact)) {
            int hp;
            if (Objects.equals(getContactName(contact.getFixtureA()),NameHelper.ENEMY_NAME)) {
                hp = (int) contact.getFixtureA().getUserData();
                contact.getFixtureA().setUserData(hp - 1);
                //.out.println(contact.getFixtureA().getUserData());
            } else if (Objects.equals(getContactName(contact.getFixtureB()),NameHelper.ENEMY_NAME)) {
                //System.out.println("fixB enemy" + contact.getFixtureB().getUserData());
                hp = (int) contact.getFixtureB().getUserData();
                contact.getFixtureB().setUserData(hp - 1);
                //System.out.println(contact.getFixtureB().getUserData());
            }
        }

//        Fixture fPlayer = findPlayer(contact);
//        Fixture fBullet = findBullet(contact);
//        Fixture fObject = findObject(contact);
//        INFO:
//        fixture.getBody().getUserData -> outputs data from playerBody.setUserData(name) from BoxHelpers ->  gives back the string "name"
//        ffixture.getUserData() -> outputs data from playerBody.createFixture(shape, 1.0f).setUserData(body); from BoxHelpers -> gives back the object "body"
//        other possible compare cases for example: if (fA.getUserData() instanceof Body && fB.getUserData() instanceof Body){}
    }

    @Override
    public void endContact(Contact contact) {
        //WorldCreation.hitsWall = false;

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();
        if (fA == null || fB == null) {return;}
        if (fA.getUserData() == null || fB.getUserData() == null) {return;}

        if (bulletHitWall(contact)){
            //System.out.println("Contact end: bullet hit a wall!");
        }
        if (bulletHitPlayer(contact)){
            //System.out.println("Contact end: player hit by a bullet!");
        }
        if (playerHitWall(contact)){
            //System.out.println("Contact end: player ran into the wall!");
        }
    }

    private String getContactName(Fixture fixture){

        return (String) fixture.getBody().getUserData();
    }
    private Fixture findPlayer(Contact contact){

        Body fA = contact.getFixtureA().getBody();
        Body fB = contact.getFixtureB().getBody();
        if (fA.getUserData() == NameHelper.WALL_NAME || fA.getUserData() == NameHelper.ENEMY_NAME || fA.getUserData() == NameHelper.BULLET_NAME){
            return contact.getFixtureB();
        }
        else {return contact.getFixtureA();}
    }

    private Fixture findObject(Contact contact){

        Body fA = contact.getFixtureA().getBody();
        Body fB = contact.getFixtureB().getBody();
        if (fA.getUserData() == NameHelper.WALL_NAME || fA.getUserData() == NameHelper.ENEMY_NAME || fA.getUserData() == NameHelper.FRIENDLY_BULLET){
            return contact.getFixtureA();
        }
        else {return contact.getFixtureB();}
    }

    private Fixture findBullet(Contact contact){

        Body fA = contact.getFixtureA().getBody();
        Body fB = contact.getFixtureB().getBody();
        if (fA.getUserData() == NameHelper.WALL_NAME || fA.getUserData() == NameHelper.ENEMY_NAME || fA.getUserData() == ConnectionScreen.getUsername()){
            return contact.getFixtureB();
        }
        else {
            return contact.getFixtureA();
        }
    }

    private boolean bulletHitWall(Contact contact){

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if (Objects.equals(getContactName(fA),NameHelper.BULLET_NAME) && Objects.equals(getContactName(fB),NameHelper.WALL_NAME) ||
                Objects.equals(getContactName(fB),NameHelper.BULLET_NAME) && Objects.equals(getContactName(fA),NameHelper.WALL_NAME)){
            return true;
        }
        return false;
    }

    private boolean bulletHitPlayer(Contact contact){

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if (Objects.equals(getContactName(fA),NameHelper.ENEMY_BULLET) && Objects.equals(getContactName(fB),ConnectionScreen.getUsername()) ||
                Objects.equals(getContactName(fB),NameHelper.ENEMY_BULLET) && Objects.equals(getContactName(fA),ConnectionScreen.getUsername())){
            return true;
        }
        return false;
    }

    private boolean playerHitWall(Contact contact){

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if (Objects.equals(getContactName(fA),ConnectionScreen.getUsername()) && Objects.equals(getContactName(fB),NameHelper.WALL_NAME) ||
                Objects.equals(getContactName(fB),ConnectionScreen.getUsername()) && Objects.equals(getContactName(fA),NameHelper.WALL_NAME)){
            return true;
        }
        return false;
    }

    private boolean bulletHitEnemy(Contact contact) {

        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if (Objects.equals(getContactName(fA),NameHelper.FRIENDLY_BULLET) && Objects.equals(getContactName(fB),NameHelper.ENEMY_NAME) ||
                Objects.equals(getContactName(fB),NameHelper.FRIENDLY_BULLET) && Objects.equals(getContactName(fA),NameHelper.ENEMY_NAME)){
            return true;
        }
        return false;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
