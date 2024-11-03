package Display;

import Inputs.KeyMap;
import Objects.Camera;
import Rendering.Engine;
import Rendering.Triangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Display {

    public BufferedImage frame;
    public KeyMap keyMap;

    public Engine engine;

    public Display(){
        this.keyMap = new KeyMap();
    }

    public abstract void update();

    public void setKeyMap(KeyMap keyMap) {
        this.keyMap = keyMap;
    }
}
