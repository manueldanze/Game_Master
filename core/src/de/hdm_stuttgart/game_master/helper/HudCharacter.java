package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.hdm_stuttgart.game_master.networking.VirtualPlayer;

import java.util.Objects;

public class HudCharacter {

    private int playerNumber;
    private Image icon;
    private Image healthbar;

    HudCharacter(VirtualPlayer vrPlayer) {

        vrPlayer.hp = 17;//TODO: make dynamic again, somethings wrong with vrPlayer hp over network
        healthbar = new Image(new Texture("hud/HealthBar/17hp.png"));
        playerNumber = vrPlayer.playerNumber;
        setVrPlayerIcon(vrPlayer);
        updateVrPlayerHbar(vrPlayer);
    }

    private void setVrPlayerIcon(VirtualPlayer vrPlayer){

        if (Objects.equals(vrPlayer.playerClass, "rangedDD")){
            icon = new Image(new Texture("hud/icons/RangedDD_frame.png"));
        }
        if (Objects.equals(vrPlayer.playerClass, "meleeDD")){
            icon = new Image(new Texture("hud/icons/MeleeDD_frame.png"));
        }
        if (Objects.equals(vrPlayer.playerClass, "tank")){
            icon = new Image(new Texture("hud/icons/Tank_frame.png"));
        }
        if (Objects.equals(vrPlayer.playerClass, "healer")){
            icon = new Image(new Texture("hud/icons/Healer_frame.png"));
        }
    }

    private void updateVrPlayerHbar(VirtualPlayer vrPlayer){

        int hp = vrPlayer.hp;
        switch (hp){
            case 17 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/17hp.png"))));break;
            case 16 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/16hp.png"))));break;
            case 15 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/15hp.png"))));break;
            case 14 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/14hp.png"))));break;
            case 13 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/13hp.png"))));break;
            case 12 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/12hp.png"))));break;
            case 11 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/11hp.png"))));break;
            case 10 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/10hp.png"))));break;
            case 9 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/9hp.png"))));break;
            case 8 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/8hp.png"))));break;
            case 7 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/7hp.png"))));break;
            case 6 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/6hp.png"))));break;
            case 5 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/5hp.png"))));break;
            case 4 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/4hp.png"))));break;
            case 3 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/3hp.png"))));break;
            case 2 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/2hp.png"))));break;
            case 1 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/1hp.png"))));break;
            case 0 : healthbar.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("hud/HealthBar/0hp.png"))));break;
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Image getIcon() {
        return icon;
    }

    public Image getHealthbar() {
        return healthbar;
    }
}
