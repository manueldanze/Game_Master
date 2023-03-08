package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.hdm_stuttgart.game_master.GameWorld;
import de.hdm_stuttgart.game_master.ScreenHandler;
import de.hdm_stuttgart.game_master.networking.VirtualPlayer;
import de.hdm_stuttgart.game_master.huds.HudPlayer;
import de.hdm_stuttgart.game_master.huds.HudGameMaster;
import de.hdm_stuttgart.game_master.screens.EndScreen;
import de.hdm_stuttgart.game_master.screens.GameScreen;

public class HudHelper {

    private static boolean hudClickable = false;
    private static boolean volumeOn = true;

    public static void updateHud(float delta){

        updatePlayerHbar();
        updateVrPlayerIcons();
        updateVrPlayerHbar();
        updateOptionHud();
        updatePlayerTimer(delta);
        updateGmTimer(delta);
    }

    private static void updatePlayerTimer(float delta){

        HudPlayer hudPlayer = GameScreen.getHud();

        if (GameWorld.startTimer){
            hudPlayer.updateTimer(delta);
        }
        if (GameWorld.getTimer() == 0) {
            EndScreen.winner = true;
            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new EndScreen());
        }
    }
    private static void updateGmTimer(float delta){
        HudGameMaster gmHud = GameScreen.getGmHud();

        if (GameWorld.iAmGM){
            gmHud.updateTimer(delta);
        }
        if (HudGameMaster.timer == 0) {
            EndScreen.winner = true;
            ScreenHandler.INSTANCE.dispose();
            ScreenHandler.INSTANCE.setScreen(new EndScreen());
        }
    }

    private static void updateOptionHud(){

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (hudClickable) {
                hudClickable = false;
                GameScreen.getInGameOptions().getMenuTable().setVisible(false);
                GameScreen.getInGameOptions().getBackgroundTable().setVisible(false);
                //resume InputProcessor in GameMasterInput//TODO: make inGameOptions clickable
            } else {
                hudClickable = true;
                GameScreen.getInGameOptions().getMenuTable().setVisible(true);
                GameScreen.getInGameOptions().getBackgroundTable().setVisible(true);
                //pause InputProcessor in GameMasterInput -> and work around with clickListerners
            }
        }
    }

    private static void updatePlayerHbar(){

        HudPlayer hudPlayer = GameScreen.getHud();

        hudPlayer.updateHbarPlayer1("hud/HealthBar/"+mapHealthToHealthBar()+"hp.png");
    }

    private static void updateVrPlayerHbar(){

        HudPlayer hudPlayer = GameScreen.getHud();

        for(VirtualPlayer virtualPlayer : GameWorld.players.values()){

            HudCharacter player = new HudCharacter(virtualPlayer);
            if (player.getPlayerNumber()==2){
                hudPlayer.updateHbarPlayer2(player.getHealthbar());
            }
            if (player.getPlayerNumber()==3){
                hudPlayer.updateHbarPlayer3(player.getHealthbar());
            }
            if (player.getPlayerNumber()==4){
                hudPlayer.updateHbarPlayer4(player.getHealthbar());
            }
        }
    }

    private static void updateVrPlayerIcons(){

        HudPlayer hudPlayer = GameScreen.getHud();

        for(VirtualPlayer virtualPlayer : GameWorld.players.values()){

            HudCharacter player = new HudCharacter(virtualPlayer);
            if (player.getPlayerNumber()==2){
                hudPlayer.setPlayer2Icon(player.getIcon());
            }
            if (player.getPlayerNumber()==3){
                hudPlayer.setPlayer3Icon(player.getIcon());
            }
            if (player.getPlayerNumber()==4){
                hudPlayer.setPlayer4Icon(player.getIcon());
            }
        }
    }

    private static int mapHealthToHealthBar() {
        return GameWorld.getLocalPlayer().getCurrentHP() * 17
                / GameWorld.getLocalPlayer().getHP();
    }

    public static boolean isHudClickable() {
        return hudClickable;
    }
    public static void setHudClickable(boolean hudClickable) {
        HudHelper.hudClickable = hudClickable;
    }
    public static boolean isVolumeOn() {return volumeOn;}
    public static void setVolumeOn(boolean volumeOn) {HudHelper.volumeOn = volumeOn;}
}
