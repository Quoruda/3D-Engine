package Objects;

import java.util.ArrayList;

public class Scene {

    private Camera mainCamera;
    private ArrayList<Mesh> meshes;

    public Scene(){
        meshes = new ArrayList<>();
        mainCamera = null;
    }

    public void setCamera(Camera camera){
        this.mainCamera = camera;
    }

    public Camera getCamera(){
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

    public ArrayList<Mesh> getMeshes(){
        return meshes;
    }

    public void update(float deltaTime, boolean[] keys){
    }


}
