package Rendering;

import javax.swing.*;
import java.util.*;
import java.awt.event.KeyEvent;
import Objects.*;
import Display.*;
import Inputs.*;


public class Engine{

    private float deltaTime = 1;
    private long lastTime;
    Scene scene;
    Display display;

    public Engine(Display display){
        lastTime = System.nanoTime();
        this.display = display;
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void mainLoop(){
        int fps;
        while(true){
            fps = Math.round(1f/deltaTime);
            //frame.setTitle(title +" fps: "+ fps);
            update();
            display.update();
        }
    }

    private void calculateDeltaTime(){
        long nanoTime = System.nanoTime();
        deltaTime = (nanoTime - lastTime) / 1000000000.0f;
        lastTime = nanoTime;
    }


    public void update(){

        calculateDeltaTime();

        ArrayList<Triangle> trianglesToRaster = new ArrayList<>();
        Scene mainScene = scene;
        if(mainScene != null){
            mainScene.update(deltaTime);
            Camera camera = mainScene.getCamera();
            ArrayList<Mesh> meshes = mainScene.getMeshes();
            if (camera != null){
                camera.update();
                trianglesToRaster = render(camera, meshes);
            }

        }
        display.TrianglesToRaster = trianglesToRaster;
    }

    /*
    public void moveCamera(Camera camera, boolean[] keys){
        if(keys[KeyEvent.VK_UP]) {
            camera.translateY(8*deltaTime);
        }
        if(keys[KeyEvent.VK_DOWN]) {
            camera.translateY(-8*deltaTime);
        }
        if(keys[KeyEvent.VK_LEFT]) {
            camera.translateX(-8*deltaTime);
        }
        if(keys[KeyEvent.VK_RIGHT]) {
            camera.translateX(8*deltaTime);
        }

        float[] vForward = Geometry.VectorMultiply(camera.vLookDir, 8*deltaTime);

        if(keys[KeyEvent.VK_Z]) {
            camera.vCamera = Geometry.VectorAddition(camera.vCamera, vForward);
        }
        if(keys[KeyEvent.VK_S]) {
            camera.vCamera = Geometry.VectorSubstraction(camera.vCamera, vForward);
        }
        if(keys[KeyEvent.VK_Q]) {
            camera.fYaw -= 2*deltaTime;
        }
        if(keys[KeyEvent.VK_D]) {
            camera.fYaw += 2*deltaTime;
        }

        camera.update();
    }

     */

    public ArrayList<Triangle> render(Camera camera, ArrayList<Mesh> meshes){
        float[][] matProj, matView ;
        int height = camera.resolution[1];
        int width = camera.resolution[0];
        matProj = Geometry.matrix_makeProjection(90.0f,(float) height/ (float) width, 0.1f, 1000);

        float[][] matCamera = camera.getMatCamera();

        matView = Geometry.Matrix_QuickInverse(matCamera);

        ArrayList<Triangle> trianglesToRaster = new ArrayList<>();

        float[] light_direction = new float[]{0,1,1,1};
        light_direction = Geometry.vector_normalise(light_direction);

        float[] vOffsetView = new float[]{1,1,0,1};
        float[][] triViewed, triProjected, triTransformed;
        float[] vCameraRay, normal;
        float dp;
        ArrayList<float[][]> clipped;
        ArrayList<Triangle> clippedTriangles = new ArrayList<>();
        Triangle tempTriangle = new Triangle();
        float[][] tri;
        float[][] t;

        for(Mesh mesh : meshes){
            mesh.update(deltaTime);
            for(int iTri = 0; iTri < mesh.getTransformedTriangles().length; iTri++) {
                triTransformed = mesh.getTransformedTriangle(iTri);
                normal = mesh.getNormal(iTri);
                vCameraRay = Geometry.VectorSubstraction(triTransformed[0], camera.vCamera);
                if(Geometry.VectorDotProduct(normal, vCameraRay) < 0){
                    dp = Math.max(0.1f, Geometry.VectorDotProduct(light_direction, normal));
                    triViewed =  Geometry.matrix_multiplyTriangle(matView, triTransformed);

                    tempTriangle.p = triViewed;
                    tempTriangle.t = Geometry.copyTriangle(mesh.getT(iTri));
                    //tempTriangle.t = mesh.getT(iTri);
                    clippedTriangles = Geometry.Triangle_ClipAgainstPlane(new float[]{0,0,camera.clipped_distance,1}, new float[]{0,0,1,1}, tempTriangle);

                    for (Triangle triangle : clippedTriangles) {
                        tri = triangle.p;
                        triProjected =  Geometry.matrix_multiplyTriangle(matProj, tri);

                        t = triangle.t;

                        t[0][0] /= triProjected[0][3];
                        t[1][0] /= triProjected[1][3];
                        t[2][0] /= triProjected[2][3];

                        t[0][1] /= triProjected[0][3];
                        t[1][1] /= triProjected[1][3];
                        t[2][1] /= triProjected[2][3];

                        t[0][2] = 1.0f/triProjected[0][3];
                        t[1][2] = 1.0f/triProjected[1][3];
                        t[2][2] = 1.0f/triProjected[2][3];

                        triProjected[0] = Geometry.VectorDivision(triProjected[0], triProjected[0][3]);
                        triProjected[1] = Geometry.VectorDivision(triProjected[1], triProjected[1][3]);
                        triProjected[2] = Geometry.VectorDivision(triProjected[2], triProjected[2][3]);
                        triProjected[0][0] *= -1;
                        triProjected[1][0] *= -1;
                        triProjected[2][0] *= -1;
                        triProjected[0][1] *= -1;
                        triProjected[1][1] *= -1;
                        triProjected[2][1] *= -1;

                        triProjected =  Geometry.triangleTranslate(triProjected, vOffsetView);

                        triProjected[0][0] *= 0.5f*width;
                        triProjected[0][1] *= 0.5f*height;
                        triProjected[1][0] *= 0.5f*width;
                        triProjected[1][1] *= 0.5f*height;
                        triProjected[2][0] *= 0.5f*width;
                        triProjected[2][1] *= 0.5f*height;

                        trianglesToRaster.add(new Triangle(triProjected, dp, t, mesh.getTexture()));
                    }
                }else{
                    //System.out.println();
                }

            }
        }

        /*
        Collections.sort(trianglesToRaster, new Comparator<Triangle>() {
            public Float calculDistance(float[][] t ) {
                return (t[0][2]+t[1][2]+t[2][2])/3.0f;
            }

            @Override
            public int compare(Triangle t1, Triangle t2) {
                return calculDistance(t2.p).compareTo(calculDistance(t1.p));
            }
        });

         */

        //Collections.reverse(trianglesToRaster);

        return trianglesToRaster;
    }


}