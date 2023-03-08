package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class AnimationHelper {

    static float animationTime = 0;
    public TextureRegion rangedDDIdleCurrentFrame;
    public TextureRegion rangedDDRunCurrentFrame;
    private Animation<TextureRegion> rangedDDIdleAnimation;
    private Animation<TextureRegion> rangedDDRunAnimation;

    public TextureRegion meleeDDIdleCurrentFrame;
    public TextureRegion meleeDDRunCurrentFrame;
    private Animation<TextureRegion> meleeDDIdleAnimation;
    private Animation<TextureRegion> meleeDDRunAnimation;

    public TextureRegion healerIdleCurrentFrame;
    public TextureRegion healerRunCurrentFrame;
    private Animation<TextureRegion> healerIdleAnimation;
    private Animation<TextureRegion> healerRunAnimation;

    public TextureRegion tankIdleCurrentFrame;
    public TextureRegion tankRunCurrentFrame;
    private Animation<TextureRegion> tankIdleAnimation;
    private Animation<TextureRegion> tankRunAnimation;

    public AnimationHelper() {

        rangedDDIdleCurrentFrame = new TextureRegion();
        rangedDDRunCurrentFrame = new TextureRegion();
        TextureAtlas rangedDDIdleAtlas = ScreenHandler.manager.get("game_characters/rangedDDnew/rangedDD_idle.atlas",TextureAtlas.class);
        TextureAtlas rangedDDRunAtlas = ScreenHandler.manager.get("game_characters/rangedDDnew/rangedDD_run.atlas",TextureAtlas.class);
        rangedDDIdleAnimation = new Animation<TextureRegion>(0.1f, rangedDDIdleAtlas.findRegions("rangedDD_idle"), Animation.PlayMode.LOOP);
        rangedDDRunAnimation = new Animation<TextureRegion>(0.1f, rangedDDRunAtlas.findRegions("rangedDD_run"), Animation.PlayMode.LOOP);

        meleeDDIdleCurrentFrame = new TextureRegion();
        meleeDDRunCurrentFrame = new TextureRegion();
        TextureAtlas meleeDDIdleAtlas = ScreenHandler.manager.get("game_characters/meleeDD/meleeDD_idle.atlas",TextureAtlas.class);
        TextureAtlas meleeDDRunAtlas = ScreenHandler.manager.get("game_characters/meleeDD/meleeDD_run.atlas",TextureAtlas.class);
        meleeDDIdleAnimation = new Animation<TextureRegion>(0.1f, meleeDDIdleAtlas.findRegions("meleeDD_idle"), Animation.PlayMode.LOOP);
        meleeDDRunAnimation = new Animation<TextureRegion>(0.1f, meleeDDRunAtlas.findRegions("meleeDD_run"), Animation.PlayMode.LOOP);

        healerIdleCurrentFrame = new TextureRegion();
        healerRunCurrentFrame = new TextureRegion();
        TextureAtlas healerIdleAtlas = ScreenHandler.manager.get("game_characters/healer/healer_idle.atlas",TextureAtlas.class);
        TextureAtlas healerRunAtlas = ScreenHandler.manager.get("game_characters/healer/healer_run.atlas",TextureAtlas.class);
        healerIdleAnimation = new Animation<TextureRegion>(0.1f, healerIdleAtlas.findRegions("healer_idle"), Animation.PlayMode.LOOP);
        healerRunAnimation = new Animation<TextureRegion>(0.1f, healerRunAtlas.findRegions("healer_run"), Animation.PlayMode.LOOP);

        tankIdleCurrentFrame = new TextureRegion();
        tankRunCurrentFrame = new TextureRegion();
        TextureAtlas tankIdleAtlas = ScreenHandler.manager.get("game_characters/tank/tank_idle.atlas",TextureAtlas.class);
        TextureAtlas tankRunAtlas = ScreenHandler.manager.get("game_characters/tank/tank_run.atlas",TextureAtlas.class);
        tankIdleAnimation = new Animation<TextureRegion>(0.1f, tankIdleAtlas.findRegions("tank_idle"), Animation.PlayMode.LOOP);
        tankRunAnimation = new Animation<TextureRegion>(0.1f, tankRunAtlas.findRegions("tank_run"), Animation.PlayMode.LOOP);
    }

    public  void createAnimations(float delta) {

        animationTime += delta;
        rangedDDRunCurrentFrame = rangedDDRunAnimation.getKeyFrame(animationTime);
        rangedDDIdleCurrentFrame = rangedDDIdleAnimation.getKeyFrame(animationTime);

        meleeDDRunCurrentFrame = meleeDDRunAnimation.getKeyFrame(animationTime);
        meleeDDIdleCurrentFrame = meleeDDIdleAnimation.getKeyFrame(animationTime);

        healerRunCurrentFrame = healerRunAnimation.getKeyFrame(animationTime);
        healerIdleCurrentFrame = healerIdleAnimation.getKeyFrame(animationTime);

        tankRunCurrentFrame = tankRunAnimation.getKeyFrame(animationTime);
        tankIdleCurrentFrame = tankIdleAnimation.getKeyFrame(animationTime);
    }
}