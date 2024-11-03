package Objects;

import java.util.ArrayList;

public class Scene {

    private ArrayList<Camera> cameras;
    private Camera mainCamera;
    private ArrayList<Mesh> meshes;

    public Scene(){
        meshes = new ArrayList<>();
        mainCamera = null;
        cameras = new ArrayList<>();
    }

    public void setMainCamera(Camera camera){
        addCamera(camera);
        if(mainCamera != null) mainCamera.setActive(false);
        this.mainCamera = camera;
        camera.setActive(true);
    }

    public Camera getMainCamera(){
        return mainCamera;
    }

    public void addMesh(Mesh mesh){
        meshes.add(mesh);
    }

    public void addMeshes(Mesh... meshes){
        for (Mesh mesh : meshes){
            addMesh(mesh);
        }
    }

    public void addCamera(Camera cam){
        if(cameras.contains(cam)) return;
        cameras.add(cam);
    }

    public void addCameras(Camera... cameras){
        for (Camera cam : cameras){
            addCamera(cam);
        }
    }

    public ArrayList<Camera> getCameras(){
        return cameras;
    }

    public ArrayList<Mesh> getMeshes(){
        return meshes;
    }

    public void update(float deltaTime){
    }


}
