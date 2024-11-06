package Portal;

import Display.Screen;
import Inputs.KeyMap;
import Objects.Camera;
import Objects.Mesh;
import Objects.Scene;
import Objects.Texture;
import Rendering.Engine;

import java.awt.event.KeyEvent;

public class Portal {

    public static void main(String[] args) {
        Screen display = new Screen();
        display.setResolution(700,700);
        Engine engine = new Engine(display);

        Camera camera1 = new FreeCam();
        camera1.setWidth(700);
        camera1.setRatio(1);
        camera1.setPosition(0f, 1f, 0f);
        camera1.setYawFromDegrees(180);


        //blue Portal
        Mesh PortalBlueView = Mesh.loadFromObjectFile("Examples/Mirror/face.obj", true);
        PortalBlueView.setPositionZ(-5F+0.1F);
        PortalBlueView.setPositionY(1.5f);
        PortalBlueView.setScaleY(2);

        //orange portal
        Camera PortalOrangeCamera = new Camera();
        PortalOrangeCamera.setPosition(0f, 1.5f, 5F-0.1F);
        PortalOrangeCamera.setActive(true);
        PortalOrangeCamera.setWidth(200);
        PortalOrangeCamera.setRatio(2);
        PortalOrangeCamera.setYawFromDegrees(180);
        float PortalOrangePosX = 0f;
        float PortalOrangePosY = 1.5f;
        float PortalOrangePosZ = 5f;



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

                camera1.update(keyMap, deltaTime);

                PortalBlueView.setTexture(Texture.loadFromBufferedImage(PortalOrangeCamera.getFrame()));

                PortalOrangeCamera.setPositionZ(PortalOrangePosZ+(PortalBlueView.getPositionZ()-camera1.getPositionZ()));
                PortalOrangeCamera.setPositionY(PortalOrangePosY+(PortalBlueView.getPositionY()-camera1.getPositionY()));
                PortalOrangeCamera.setPositionX(PortalOrangePosX+(PortalBlueView.getPositionX()-camera1.getPositionX()));

                PortalOrangeCamera.setPitch(camera1.getPitch());

            }

        };

        scene.addCameras(PortalOrangeCamera, camera1);
        scene.setMainCamera(camera1);
        scene.addMeshes(PortalBlueView, ground, wall1, wall2, wall3, wall4);

        engine.setScene(scene);
        engine.mainLoop();
    }

}
