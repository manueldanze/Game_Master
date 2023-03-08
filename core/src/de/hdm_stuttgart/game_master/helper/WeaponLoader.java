package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.hdm_stuttgart.game_master.ScreenHandler;

public class WeaponLoader {

    public final Sprite rifle;
    private TextureAtlas weapons = ScreenHandler.manager.get("weapons/weaponAtlas.atlas", TextureAtlas.class);

    public WeaponLoader(String region) {
        rifle = new Sprite(weapons.findRegion(region));
    }
}
