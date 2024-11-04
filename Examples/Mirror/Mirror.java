package Mirror;

import Display.Screen;
import Inputs.KeyMap;
import Objects.Camera;
import Objects.Mesh;
import Objects.Scene;
import Objects.Texture;
import Rendering.Engine;

import java.awt.event.KeyEvent;

public class Mirror {

    public static void main(String[] args) {
        Screen display = new Screen();
        display.setResolution(700,700);
        Engine engine = new Engine(display);

        Camera camera1 = new Camera();
        camera1.setWidth(700);
        camera1.setRatio(1);
        camera1.setPosition(0f, 1f, 0f);
        camera1.setYawFromDegrees(180);

        Mesh mirror = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        mirror.setPositionZ(-5F+0.1F);
        mirror.setPositionY(1.5f);
        mirror.setScaleY(2);
        Camera cameraMirror = new Camera();
        cameraMirror.setPosition(0f, 1f, 0f);
        cameraMirror.setActive(true);
        cameraMirror.setWidth(200);
        camera1.setYawFromDegrees(180);
        cameraMirror.setRatio(2);

        KeyMap keyMap = new KeyMap();
        display.setKeyMap(keyMap);

        keyMap.add("forward", KeyEvent.VK_Z);
        keyMap.add("backward", KeyEvent.VK_S);
        keyMap.add("right", KeyEvent.VK_D);
        keyMap.add("left", KeyEvent.VK_Q);
        keyMap.add("lookRight", KeyEvent.VK_RIGHT);
        keyMap.add("lookLeft", KeyEvent.VK_LEFT);
        keyMap.add("lookUp", KeyEvent.VK_UP);
        keyMap.add("lookDown", KeyEvent.VK_DOWN);
        keyMap.add("switch", KeyEvent.VK_C);


        Mesh ground = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        ground.setTexture(Texture.getUniformColor(0xFF0000));
        ground.setPosition(0f, 0f, 0f);
        ground.setRotationX((float)(-Math.PI/2));
        ground.setScaleX(10);
        ground.setScaleY(10);
        ground.setTexture(Texture.getGrid(256, 256, 8, 0x000000, 0xFFFFFF));

        Mesh wall1 = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        wall1.setPosition(0f, 5, -5f);
        wall1.setScaleX(10);
        wall1.setScaleY(10);
        wall1.setTexture(Texture.getGrid(256, 256, 8, 0x00FFFF, 0xFFFFFF));

        Mesh wall2 = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        wall2.setPosition(0f, 5, 5f);
        wall2.setRotationY((float) Math.PI);
        wall2.setScaleX(10);
        wall2.setScaleY(10);
        wall2.setTexture(Texture.getGrid(256, 256, 8, 0xFF00FF, 0x000000));

        Mesh wall3 = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        wall3.setPosition(5f, 5, 0f);
        wall3.setRotationY((float) Math.PI/2);
        wall3.setScaleX(10);
        wall3.setScaleY(10);
        wall3.setTexture(Texture.getGrid(256, 256, 8, 0xFFFF00, 0x000000));

        Mesh wall4 = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        wall4.setPosition(-5f, 5, 0f);
        wall4.setRotationY((float) -Math.PI/2);
        wall4.setScaleX(10);
        wall4.setScaleY(10);
        wall4.setTexture(Texture.getGrid(256, 256, 8, 0xFFFFFF, 0xFF0000));

        Scene scene = new Scene(){
            @Override
            public void update(float deltaTime) {
                //ground.rotate(-0.4f*deltaTime, 0f, 0f);
                mirror.setTexture(Texture.loadFromBufferedImage(cameraMirror.getFrame()));

                //freecam
                float v = 2*deltaTime;
                if(keyMap.is("lookRight")){
                    camera1.rotateYaw(v);
                }
                if(keyMap.is("lookLeft")){
                    camera1.rotateYaw(-v);
                }
                if(keyMap.is("lookUp")){
                    camera1.rotatePitch(v);
                }
                if(keyMap.is("lookDown")){
                    camera1.rotatePitch(-v);
                }
                v = 5f*v;
                if(keyMap.is("forward")){
                    camera1.moveForward(v);
                }
                if(keyMap.is("backward")){
                    camera1.moveForward(-v);
                }
                if(keyMap.is("right")){
                    camera1.moveRight(v);
                }
                if(keyMap.is("left")){
                    camera1.moveRight(-v);
                }

                //move cameraMirror
                cameraMirror.setPosition(
                        0,
                        mirror.getPositionY(),
                        -5f
                );

                cameraMirror.setYaw(camera1.getYaw()+(float)Math.PI);
                //cameraMirror.setPitch(camera1.getPitch()+(float)Math.PI);

            }

        };

        scene.addCameras(cameraMirror, camera1);
        scene.setMainCamera(camera1);
        scene.addMeshes(mirror, ground, wall1, wall2, wall3, wall4);

        engine.setScene(scene);
        engine.mainLoop();
    }

}
