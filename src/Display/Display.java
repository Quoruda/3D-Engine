package Display;

import Rendering.Engine;
import Rendering.Triangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Display {

    public ArrayList<Triangle> TrianglesToRaster;
    protected float[][] pDepthBuffer;
    protected BufferedImage frame;

    public Engine engine;

    public Display(){}

    public abstract void update();

}
