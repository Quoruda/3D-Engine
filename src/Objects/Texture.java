package Objects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        return pixels[(int)(u * width) + (int)(v * height) * width];

        //return pixels[Math.floorMod((int)(u * this.width-1) + (int)((1f-v) * (this.height-1)) * (this.width-1), pixels.length)];
    }

    public static Texture getUniformColor(int color){
        Texture texture = new Texture();
        texture.pixels[0] = color;
        return texture;
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

    public static Texture loadFromFile(String fileName){
        Texture texture = new Texture();
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            int width = image.getWidth();
            int height = image.getHeight();
            texture.height = height;
            texture.width = width;
            texture.pixels = new int[height*width];

            for(int x = 0; x < width; x++){
                for(int y = 0; y < height; y++){
                    int pixel = image.getRGB(x,y);
                    texture.pixels[(height-y-1)*width+x] = pixel;
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return texture;
    }

    public void update(float deltaTime){

    }

}
