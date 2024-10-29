public class Camera {


    public float[] vCamera, vLookDir;
    float fYaw;
    float clipped_distance;


    public Camera(){
        vCamera = new float[]{0,0,0,1};
        vLookDir = new float[]{0,0,0,1};
        fYaw = 0;
        clipped_distance = 0.1f;
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

    public void setYawFromDegrees(int degrees){
        this.fYaw = degrees * (float) Math.PI / 180f;
    }

    public void translateY(float v){
        this.vCamera[1] += v;
    }

    public void translateX(float v){
        this.vCamera[0] += v;
    }

}
