import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine();

        Mesh cube = Mesh.loadFromObjectFile("models/cube.obj", true);
        cube.setTexture(Texture.getGrid(256, 256, 16, 0xFF0000, 0xFFFF00));
        cube.setScale(0.5f);

        Mesh sphere = Mesh.loadFromObjectFile("models/sphere.obj", true);
        sphere.setTexture(Texture.getGrid(256, 256, 16, 0xFFFF00, 0x00FFFF));
        sphere.setPosition(0, 0, 0);

        Mesh monkey = Mesh.loadFromObjectFile("models/monkey.obj", true);
        monkey.setTexture(Texture.getGrid(256, 256, 16, 0xFF00FF, 0x00FFFF));
        monkey.setPositionX(3);
        monkey.setScale(0.75f);

        Scene scene = new Scene(){

            int click = 0;
            float t = 0;

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
                sphere.rotate(v, 0f, 0f);

                t += deltaTime;
                cube.rotate(v, v, 0f);
                cube.setPositionX((float) Math.cos(t*0.5)* 3);
                cube.setPositionZ((float) Math.sin(t*0.5)* 3);

                monkey.rotate(v,0f,v);
                monkey.setPositionX((float) Math.cos(t*0.5+Math.PI)* 3);
                monkey.setPositionZ((float) Math.sin(t*0.5+Math.PI)* 3);
            }
        };

        
        Camera camera = new Camera();
        camera.setPosition(0, 0, 5);
        camera.setYawFromDegrees(180);
        scene.setCamera(camera);

        scene.addMeshes(cube, sphere, monkey);

        engine.setScene(scene);

        engine.mainLoop();

    }
}
