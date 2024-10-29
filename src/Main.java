import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        Mesh cube = Mesh.loadFromObjectFile("models/cube.obj", true);
        cube.setTexture(Texture.getGrid(256, 256, 16, 0xFF0000, 0x00FF00));
        cube.setPosition(0, 0, 1);
        cube.setScale(0.75f);

        Mesh sphere = Mesh.loadFromObjectFile("models/sphere.obj", true);
        sphere.setTexture(Texture.getGrid(256, 256, 16, 0xFF0000, 0x00FF00));
        sphere.setPosition(0, 0, -1);

        Scene scene = new Scene(){

            int click = 0;

            @Override
            public void update(float deltaTime, boolean[] keys){

                if(keys[KeyEvent.VK_T]){
                    click++;
                    if(click%2 == 1){
                        cube.setTexture(Texture.getGrid(256, 256, 16, 0x0000FF, 0xFFFFFF));
                    }else{
                        cube.setTexture(Texture.getGrid(256, 256, 16, 0xFF0000, 0x00FF00));

                    }
                }

                float v = deltaTime * 0.3f;
                sphere.rotate(v, v, 0f);
                //sphere.setScale(sphere.getScale()+0.02f);
                cube.rotate(v, v, 0f);
                cube.setPositionX(cube.getPositionX()+v);
            }
        };

        
        Camera camera = new Camera();
        camera.setPosition(0, 0, 5);
        camera.setYawFromDegrees(180);
        scene.setCamera(camera);
        
        scene.addMeshes(cube, sphere);

        engine.setScene(scene);

        engine.mainLoop();

    }
}
