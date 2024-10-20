import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        Mesh mesh;

        /*
        for(int x = 0; x < 16; x++){
            for(int z = 0; z < 16; z++){
                for(int y = 0; y < 10; y++) {
                    mesh = new Mesh();
                    mesh.loadFromObjectFile("models/cube.obj");
                    mesh.setPosition(x * 2F, y * 2, z * 2f);
                    engine.addMesh(mesh);
                }
            }
        }*/

        Mesh sphere = Mesh.loadFromObjectFile("models/sphere.obj");
        sphere.setPosition(0, 0, 0);
        Mesh cube = Mesh.loadFromObjectFile("models/cube.obj");
        cube.setPosition(5, 0, 0);
        Mesh cube2 = Mesh.loadFromObjectFile("models/cube.obj");
        cube2.setPosition(-5, 0, 0);
        Mesh cube3 = Mesh.loadFromObjectFile("models/cube.obj");
        cube3.setPosition(0, 0, 5);
        Mesh cube4 = Mesh.loadFromObjectFile("models/cube.obj");
        cube4.setPosition(0, 0, -5);




        Scene scene = new Scene(){
            @Override
            public void update(float deltaTime){
                float v = deltaTime * 0.1f;
                sphere.rotate(v, 0f, 0f);
                //sphere.setScale(sphere.getScale()+0.02f);
            }
        };

        
        Camera camera = new Camera();
        camera.setPosition(0, 0, -5);
        scene.setCamera(camera);
        scene.addMeshes(sphere, cube, cube2, cube3, cube4);

        engine.setScene(scene);

        engine.mainLoop();
    }
}
