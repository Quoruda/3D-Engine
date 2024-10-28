import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        /*
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

         */
        /*
        Mesh cube = new Mesh();
        cube.vertices = new float[][]{
                {0.0f, 0.0f, 0.0f},//0
                {1.0f, 0.0f, 0.0f},//1
                {1.0f, 1.0f, 0.0f},//2
                {0.0f, 1.0f, 0.0f},//3
                {0.0f, 0.0f, 1.0f},//4
                {1.0f, 0.0f, 1.0f},//5
                {1.0f, 1.0f, 1.0f},//6
                {0.0f, 1.0f, 1.0f}//7
        };

        cube.triangles = new int[][]{
                {0, 3, 2},
                {0, 2, 1},
                {1, 2, 6},
                {1, 6, 5},
                {5, 6, 7},
                {5, 7, 4},
                {4, 7, 3},
                {4, 3, 0},
                {3, 7, 6},
                {3, 6, 2},
                {5, 4, 0},
                {5, 0, 1}
        };

        cube.tList = new float[][][]{
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}},
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}},
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}},
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}},
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}},
                {{0, 1, 1}, {0, 0, 1}, {1, 0, 1}},
                {{0, 1, 1}, {1, 0, 1}, {1, 1, 1}}
        };

         */

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
