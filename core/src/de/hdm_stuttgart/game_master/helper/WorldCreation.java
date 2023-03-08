package de.hdm_stuttgart.game_master.helper;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import de.hdm_stuttgart.game_master.screens.GameScreen;

public class WorldCreation {

    public static World world;
    public static boolean hitsWall;
    private static TiledMapTileLayer layer;
    public static Array<Body> bulletsToDelete;

    public static World createWorld() {
        world = new World(new Vector2(), false);
        bulletsToDelete = new Array();
        world.setContactListener(new ContactHelper());

        return world;
    }

    public static void createBounds() {
        layer = (TiledMapTileLayer) GameScreen.getMap().getLayers().get("background");
        System.out.println(layer.getTileHeight());
        for (int height = 0; height < layer.getHeight(); height++) {
            for (int width = 0; width < layer.getWidth(); width++) {
                if (layer.getCell(width, height).getTile().getProperties().containsKey("blocked")) {
                    BoxHelpers.createBody(width * 4, height * 4, layer.getTileWidth(), layer.getTileHeight(), NameHelper.WALL_NAME, true, false);
                }
            }
        }
    }
}
