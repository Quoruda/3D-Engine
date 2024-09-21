import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        Mesh mesh;

        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++){
                for(int y = 0; y < 10; y++) {
                    mesh = new Mesh();
                    mesh.loadFromObjectFile("models/cube.obj");
                    mesh.setPosition(x * 2F, y * 2, z * 2f);
                    engine.addMesh(mesh);
                }
            }
        }
        engine.mainLoop();
    }
}
