import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texture {

    int width, height;
    int[] pixels;

    public Texture() {
        width = 100;
        height = 100;
        pixels = new int[width*height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                if(x < width/2 != y < height/2) {
                    pixels[x + y * width] = 0xFFFFFF;
                }else {
                    //violet
                    pixels[x + y * width] = 0x8A2BE2;
                }
            }
        }
    }

    public int getPixel(float u, float v) {
        return pixels[Math.floorMod((int)(u * width) + (int)(v * height) * width, pixels.length)];
    }
}
