import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Texture {

    int width, height;
    int[] pixels;

    public Texture() {
        width = 1;
        height = 1;
        pixels = new int[]{0xFFFFFFFF};
    }

    public int getPixel(float u, float v) {
        return pixels[Math.floorMod((int)(u * width) + (int)(v * height) * width, pixels.length)];
    }

    public static Texture getGrid(int width, int height, int l, int color1, int color2) {
        Texture texture = new Texture();
        int[] pixels = new int[width*height];

        for(int i = 0; i < pixels.length; i++){
            if((i / l) % 2 == (i / (l*height)) % 2) {
                pixels[i] = color1;
            }else {
                pixels[i] = color2;
            }
        }

        texture.pixels = pixels;
        texture.width = width;
        texture.height = height;
        return texture;
    }

    public void update(float deltaTime){

    }

}
