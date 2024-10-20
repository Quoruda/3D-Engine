public class Camera {


    public float[] vCamera, vLookDir;
    float fYaw;


    public Camera(){
        vCamera = new float[]{0,0,0,1};
        vLookDir = new float[]{0,0,0,1};
        fYaw = 0;
    }

    public void setPosition(float x, float y, float z){
        vCamera[0] = x;
        vCamera[1] = y;
        vCamera[2] = z;
    }

    public float[][] getMatCamera(){
        float[] vUp = new float[]{0,1,0,1};
        float[] vTarget = Geometry.VectorAddition(this.vCamera, vLookDir);
        float[][] matCamera = Geometry.Matrix_PointAt(this.vCamera, vTarget, vUp);
        return matCamera;
    }

}
