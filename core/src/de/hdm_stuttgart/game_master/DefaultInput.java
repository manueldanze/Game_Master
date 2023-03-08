package de.hdm_stuttgart.game_master;

import com.badlogic.gdx.InputProcessor;

public class DefaultInput implements InputProcessor {

    private DefaultInput() {}

    private static DefaultInput input = new DefaultInput();

    public static DefaultInput getInputProcessor() { return input; }

    @Override
    public boolean keyDown(int i) {
        return true;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return true;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return true;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return true;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return true;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return true;
    }
}
