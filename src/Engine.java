import javax.swing.*;
import java.util.*;
import java.awt.event.KeyEvent;


public class Engine{

    public static int[] getColour(float lum){
        int pixel = (int)(lum*255);
        return new int[] {pixel, pixel, pixel} ;
    }

    public static class TriangleMesh{
        public float[][] t;
        public int[] color;

        public TriangleMesh(float[][] t, float lum){
            this.t = t;
            this.color = getColour(lum);
        }

    }

    ArrayList<Mesh> meshes;


    int width,height;
    int maxNbTriangleToRender;

    public Engine(){
        width = 700;
        height = 600;
        meshes = new ArrayList<>();
    }


    public void mainLoop(){
        Screen screen = new Screen();
        JFrame frame = new JFrame("Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.add(screen);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        KeyPressedListener keyPressedListener = new KeyPressedListener(this);
        frame.addKeyListener(keyPressedListener);

        float[][] matProj ;

        float[] vCamera = {0,1,0,1};
        float[] vLookDir = {0,0,0,1};
        float fYaw = 0;

        float ftheta = 0;
        float timeElapsed = 0;
        float deltaTime = 0;

        float calculationTime = 0;
        float renderingTime = 0;
        float sortTime = 0;
        long startTime = 0;
        long endTime = 0;
        long LastTickTime = 0;
        long tempTime = 0;

        float dp, clipped_distance;
        float[] vForward, vUp, vTarget,  normal, vCameraRay,light_direction, vOffsetView  ;
        float[][] matCameraRot, matCamera, matView, triViewed, triProjected, triTransformed;
        ArrayList<float[][]> clipped;

        ArrayList<TriangleMesh> trianglesToRaster = new ArrayList<TriangleMesh>();

        while(true) {
            height = screen.getHeight();
            width = screen.getWidth();
            matProj = Geometry.matrix_makeProjection(90.0f,(float) height/ (float) width, 0.1f, 1000);

            if(keyPressedListener.keys[KeyEvent.VK_UP]) {
                vCamera[1] += 8*deltaTime;
            }
            if(keyPressedListener.keys[KeyEvent.VK_DOWN]) {
                vCamera[1] -= 8*deltaTime;
            }
            if(keyPressedListener.keys[KeyEvent.VK_LEFT]) {
                vCamera[0] -= 8*deltaTime;
            }
            if(keyPressedListener.keys[KeyEvent.VK_RIGHT]) {
                vCamera[0] += 8*deltaTime;
            }

            vForward = Geometry.VectorMultiply(vLookDir, 8*deltaTime);

            if(keyPressedListener.keys[KeyEvent.VK_Z]) {
                vCamera = Geometry.VectorAddition(vCamera, vForward);
            }
            if(keyPressedListener.keys[KeyEvent.VK_S]) {
                vCamera = Geometry.VectorSubstraction(vCamera, vForward);
            }
            if(keyPressedListener.keys[KeyEvent.VK_Q]) {
                fYaw -= 2*deltaTime;
            }
            if(keyPressedListener.keys[KeyEvent.VK_D]) {
                fYaw += 2*deltaTime;
            }

            vUp = new float[]{0,1,0,1};
            vTarget = new float[]{0,0,1,1};
            matCameraRot = Geometry.getMatrixRotationY(fYaw);
            vLookDir = Geometry.matrix_multiplyVector(matCameraRot, vTarget);
            vTarget = Geometry.VectorAddition(vCamera, vLookDir);
            matCamera = Geometry.Matrix_PointAt(vCamera, vTarget, vUp);

            matView = Geometry.Matrix_QuickInverse(matCamera);

            trianglesToRaster = new ArrayList<TriangleMesh>();

            startTime = System.nanoTime();

            light_direction = new float[]{0,1,-1,1};
            light_direction = Geometry.vector_normalise(light_direction);

            vOffsetView = new float[]{1,1,0,1};

            for(Mesh mesh : meshes){
                mesh.update();
                for(int iTri = 0; iTri < mesh.getTransformedTriangles().length; iTri++) {
                //for (float[][] triTransformed : mesh.getTransformedTriangles()) {
                    triTransformed = mesh.getTransformedTriangle(iTri);

                    normal = mesh.getNormal(iTri);

                    vCameraRay = Geometry.VectorSubstraction(triTransformed[0], vCamera);

                    if(Geometry.VectorDotProduct(normal, vCameraRay) < 0){

                        dp = (float) Math.max(0.1, Geometry.VectorDotProduct(light_direction, normal));

                        triViewed =  Geometry.matrix_multiplyTriangle(matView, triTransformed);

                        clipped_distance = 0.1f;
                        clipped = Geometry.Triangle_ClipAgainstPlane(new float[]{0,0,clipped_distance,1}, new float[]{0,0,1,1}, triViewed);

                        for (float[][] tri : clipped) {

                            triProjected =  Geometry.matrix_multiplyTriangle(matProj, tri);

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

                            trianglesToRaster.add(new TriangleMesh(triProjected, dp));
                        }
                    }else{
                        //System.out.println();
                    }

                }
            }
            endTime = System.nanoTime();
            calculationTime += (float) (endTime - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            Collections.sort(trianglesToRaster, new Comparator<TriangleMesh>() {
                public Float calculDistance(float[][] t ) {
                    return (t[0][2]+t[1][2]+t[2][2])/3.0f;
                }

                @Override
                public int compare(TriangleMesh t1, TriangleMesh t2) {
                    return calculDistance(t2.t).compareTo(calculDistance(t1.t));
                }
            });
            //Collections.reverse(trianglesToRaster);
            endTime = System.nanoTime();
            sortTime += (float) (endTime - startTime) / 1000000000.0f;
            startTime = System.nanoTime();
            screen.TrianglesToRaster = trianglesToRaster;
            screen.repaint();
            endTime = System.nanoTime();
            renderingTime += (float) (endTime - startTime) / 1000000000.0f;
            tempTime = System.nanoTime();
            deltaTime = (float) (tempTime - LastTickTime) / 1000000000.0f;
            LastTickTime = tempTime;
            ftheta += deltaTime;
            timeElapsed += deltaTime;
            if(timeElapsed > 1){
                System.out.println( "FPS: " + (int) (1.0/deltaTime) + " Calcul: "+ calculationTime+ " Render: "+renderingTime+ " sort: "+sortTime);
                timeElapsed = timeElapsed % 1;
                calculationTime = 0;
                renderingTime = 0;
                sortTime = 0;
            }
        }
    }


    public void addMesh(Mesh mesh){
        meshes.add(mesh);
    }
}