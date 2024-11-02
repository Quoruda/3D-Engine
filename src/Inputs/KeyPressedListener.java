package Inputs;

import Rendering.Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyPressedListener implements KeyListener {

    Engine engine;
    public boolean[] keys = new boolean[255];

    public KeyPressedListener(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keys[keyCode] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
