package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import de.hdm_stuttgart.game_master.screens.GameScreen;

import static de.hdm_stuttgart.game_master.screens.GameScreen.PPM;

public class BoxHelpers {

    public static Body createBody(float x, float y, int hitboxWidth, int hitboxHeight, String objectName, boolean isStatic, boolean sensor) {

        BodyDef bodyProperties = new BodyDef();

        if(isStatic) {
            bodyProperties.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyProperties.type = BodyDef.BodyType.DynamicBody;
        }

        bodyProperties.position.set(x, y);
        bodyProperties.fixedRotation = true;

        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(hitboxWidth/2f/PPM, hitboxHeight/2f/PPM);

        Body body = GameScreen.getWorld().createBody(bodyProperties);
        body.createFixture(hitbox, 1.0f).setUserData(body);//set fixture data to this.body -> 2DBox body object
        body.setUserData(objectName);//set body data to GameObjectName -> String
        if (sensor) {
            for(int i=0; i < body.getFixtureList().size; i++){
                body.getFixtureList().get(i).setSensor(true);
            }
        }

        hitbox.dispose();
        return body;
    }

    public static Body createBody(float x, float y, int hitboxWidth, int hitboxHeight, String objectName, boolean isStatic, boolean sensor, boolean friendlyBullet) {

        BodyDef bodyProperties = new BodyDef();

        if(isStatic) {
            bodyProperties.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyProperties.type = BodyDef.BodyType.DynamicBody;
        }

        bodyProperties.position.set(x, y);
        bodyProperties.fixedRotation = true;

        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(hitboxWidth/2f/PPM, hitboxHeight/2f/PPM);

        Body body = GameScreen.getWorld().createBody(bodyProperties);
        body.createFixture(hitbox, 1.0f).setUserData(body);//set fixture data to this.body -> 2DBox body object
        if (friendlyBullet) {
            body.setUserData("friendlyBullet");//set body data to GameObjectName -> String
        } else {
            body.setUserData("hostileBullet");
        }
        if (sensor) {
            for(int i=0; i < body.getFixtureList().size; i++){
                body.getFixtureList().get(i).setSensor(true);
            }
        }

        hitbox.dispose();
        return body;
    }

    public static Body createBody(float x, float y, int hp, int hitboxWidth, int hitboxHeight, String objectName, boolean isStatic, boolean sensor) {

        BodyDef bodyProperties = new BodyDef();

        if(isStatic) {
            bodyProperties.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyProperties.type = BodyDef.BodyType.DynamicBody;
        }

        bodyProperties.position.set(x, y);
        bodyProperties.fixedRotation = true;

        PolygonShape hitbox = new PolygonShape();
        hitbox.setAsBox(hitboxWidth/2f/PPM, hitboxHeight/2f/PPM);

        Body body = GameScreen.getWorld().createBody(bodyProperties);
        body.createFixture(hitbox, 1.0f).setUserData(hp);//set fixture data to this.body -> 2DBox body object
        body.setUserData(objectName);//set body data to GameObjectName -> String
        if (sensor) {
            for(int i=0; i < body.getFixtureList().size; i++){
                body.getFixtureList().get(i).setSensor(true);
            }
        }

        hitbox.dispose();
        return body;
    }
}
