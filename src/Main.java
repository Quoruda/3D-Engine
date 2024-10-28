import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        Mesh cube = Mesh.loadFromObjectFile("models/cube.obj", true);
        cube.setTexture(Texture.getGrid(256, 256, 16, 0xFF0000, 0x00FF00));

        cube.transformedVertices = new float[cube.vertices.length][3];
        cube.transformedTriangles = new float[cube.triangles.length][3][4];
        cube.normals = new float[cube.triangles.length][4];

        cube.position_changed = true;
        cube.rotation_changed = true;

        Scene scene = new Scene(){
            @Override
            public void update(float deltaTime){
                float v = deltaTime * 0.3f;
                //sphere.rotate(v, 0f, 0f);
                //sphere.setScale(sphere.getScale()+0.02f);
                cube.rotate(v, v, v);
            }
        };

        
        Camera camera = new Camera();
        camera.setPosition(0, 0, -5);
        scene.setCamera(camera);
        scene.addMeshes(cube);

        engine.setScene(scene);

        engine.mainLoop();

    }
}
