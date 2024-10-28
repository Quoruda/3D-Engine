import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.awt.event.KeyEvent;


public class Engine{

    private float deltaTime = 1;
    private float lastTime;
    int width,height;
    int maxNbTriangleToRender;
    Scene scene;

    public static int[] getColour(float lum){
        int pixel = (int)(lum*255);
        return new int[] {pixel, pixel, pixel} ;
    }

    public static class TriangleMesh{
        public float[][] p;
        public int[] color;
        public float[][] t;

        public TriangleMesh(float[][] p, float lum, float[][] t){
            this.p = p;
            this.color = getColour(lum);
            this.t = t;
        }

    }

    public Engine(){
        width = 700;
        height = 600;
        lastTime = System.nanoTime();
    }

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void mainLoop(){
        Screen screen = new Screen(this);
        screen.drawLines = true;
        JFrame frame = new JFrame("Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(screen);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        KeyPressedListener keyPressedListener = new KeyPressedListener(this);
        frame.addKeyListener(keyPressedListener);
        Timer timerScreen = new Timer(1000/30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.repaint();
            }
        });
        timerScreen.start();

        Timer timerRender = new Timer(1000/30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(screen, keyPressedListener);
            }
        });
        timerRender.start();
    }

    private void calculateDeltaTime(){
        float nanoTime = System.nanoTime();
        deltaTime = (nanoTime - lastTime) / 1000000000.0f;
        lastTime = nanoTime;
    }


    public void update(Screen screen, KeyPressedListener keyPressedListener){

        calculateDeltaTime();

        ArrayList<TriangleMesh> trianglesToRaster = new ArrayList<>();
        Scene mainScene = scene;
        if(mainScene != null){
            mainScene.update(deltaTime);
            Camera camera = mainScene.getCamera();
            ArrayList<Mesh> meshes = mainScene.getMeshes();
            if (camera != null){
                moveCamera(camera, keyPressedListener);
                trianglesToRaster = render(screen, camera, meshes);
            }

        }
        screen.TrianglesToRaster = trianglesToRaster;
    }

    public void moveCamera(Camera camera,KeyPressedListener keyPressedListener){
        if(keyPressedListener.keys[KeyEvent.VK_UP]) {
            camera.vCamera[1] += 8*deltaTime;
        }
        if(keyPressedListener.keys[KeyEvent.VK_DOWN]) {
            camera.vCamera[1] -= 8*deltaTime;
        }
        if(keyPressedListener.keys[KeyEvent.VK_LEFT]) {
            camera.vCamera[0] -= 8*deltaTime;
        }
        if(keyPressedListener.keys[KeyEvent.VK_RIGHT]) {
            camera.vCamera[0] += 8*deltaTime;
        }

        float[] vForward = Geometry.VectorMultiply(camera.vLookDir, 8*deltaTime);

        if(keyPressedListener.keys[KeyEvent.VK_Z]) {
            camera.vCamera = Geometry.VectorAddition(camera.vCamera, vForward);
        }
        if(keyPressedListener.keys[KeyEvent.VK_S]) {
            camera.vCamera = Geometry.VectorSubstraction(camera.vCamera, vForward);
        }
        if(keyPressedListener.keys[KeyEvent.VK_Q]) {
            camera.fYaw -= 2*deltaTime;
        }
        if(keyPressedListener.keys[KeyEvent.VK_D]) {
            camera.fYaw += 2*deltaTime;
        }
        float[] vTarget = new float[]{0,0,1,1};
        float[][] matCameraRot = Geometry.getMatrixRotationY(camera.fYaw);
        camera.vLookDir = Geometry.matrix_multiplyVector(matCameraRot, vTarget);
    }

    public ArrayList<TriangleMesh> render(Screen screen, Camera camera, ArrayList<Mesh> meshes){
        float[][] matProj, matView ;
        height = screen.getHeight();
        width = screen.getWidth();
        matProj = Geometry.matrix_makeProjection(90.0f,(float) height/ (float) width, 0.1f, 1000);

        float[][] matCamera = camera.getMatCamera();

        matView = Geometry.Matrix_QuickInverse(matCamera);

        ArrayList<TriangleMesh> trianglesToRaster = new ArrayList<>();

        float[] light_direction = new float[]{0,1,-1,1};
        light_direction = Geometry.vector_normalise(light_direction);

        float[] vOffsetView = new float[]{1,1,0,1};
        float[][] triViewed, triProjected, triTransformed;
        float[] vCameraRay, normal;
        float dp;
        float clipped_distance;
        ArrayList<float[][]> clipped;
        ArrayList<Triangle> clippedTriangles = new ArrayList<>();
        Triangle tempTriangle = new Triangle();
        float[][] tri;
        float[][] t;

        for(Mesh mesh : meshes){
            mesh.update();
            for(int iTri = 0; iTri < mesh.getTransformedTriangles().length; iTri++) {
                triTransformed = mesh.getTransformedTriangle(iTri);
                normal = mesh.getNormal(iTri);
                vCameraRay = Geometry.VectorSubstraction(triTransformed[0], camera.vCamera);
                if(Geometry.VectorDotProduct(normal, vCameraRay) < 0){
                    dp = Math.max(0.1f, Geometry.VectorDotProduct(light_direction, normal));
                    triViewed =  Geometry.matrix_multiplyTriangle(matView, triTransformed);

                    clipped_distance = 0.1f;
                    tempTriangle.p = triViewed;
                    tempTriangle.t = Geometry.copyTriangle(mesh.getT(iTri));
                    //tempTriangle.t = mesh.getT(iTri);
                    clippedTriangles = Geometry.Triangle_ClipAgainstPlane(new float[]{0,0,clipped_distance,1}, new float[]{0,0,1,1}, tempTriangle);

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

                        trianglesToRaster.add(new TriangleMesh(triProjected, dp, t));
                    }
                }else{
                    //System.out.println();
                }

            }
        }

        Collections.sort(trianglesToRaster, new Comparator<TriangleMesh>() {
            public Float calculDistance(float[][] t ) {
                return (t[0][2]+t[1][2]+t[2][2])/3.0f;
            }

            @Override
            public int compare(TriangleMesh t1, TriangleMesh t2) {
                return calculDistance(t2.p).compareTo(calculDistance(t1.p));
            }
        });
        //Collections.reverse(trianglesToRaster);

        return trianglesToRaster;
    }


}