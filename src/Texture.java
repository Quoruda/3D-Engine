public class Texture {

    int width, height;
    int[] pixels;

    public Texture(String filename) {
        width = 10;
        height = 10;
        pixels = new int[width*height];
        for (int i = 0; i < width*height; i++) {
            pixels[i] = 10000;
        }
    }

    public int getPixel(float u, float v) {
        int x = (int)(u * width);
        int y = (int)(v * height);
        return pixels[Math.floorMod(x + y * width, pixels.length)];
    }


}
