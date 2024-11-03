package Objects;

import Rendering.Geometry;

public class Camera {


    public float[] vCamera, vLookDir,vUp;
    public float fYaw;
    public float pitch;
    public float clipped_distance;
    public int[] resolution;


    public Camera(){
        vCamera = new float[]{0,0,0,1};
        vLookDir = new float[]{0,0,0,1};
        fYaw = 0;
        pitch = 0;
        clipped_distance = 0.1f;
        resolution = new int[]{700, 600};
        calculate_vLookDir_vUp();
    }

    public void setPosition(float x, float y, float z){
        vCamera[0] = x;
        vCamera[1] = y;
        vCamera[2] = z;
    }

    public float[][] getMatCamera(){
        calculate_vLookDir_vUp();
        float[] vTarget = Geometry.VectorAddition(this.vCamera, vLookDir);
        float[][] matCamera = Geometry.Matrix_PointAt(this.vCamera, vTarget, vUp);
        return matCamera;
    }

    public void setYaw(float yaw){
        fYaw = yaw;
    }

    public void setYawFromDegrees(float degrees){
        this.fYaw = degrees * (float) Math.PI / 180f;
    }

    public void rotateYaw(float yaw){
        this.fYaw += yaw;
    }

    public void rotateYawFromDegrees(float degrees){
        this.fYaw += degrees * (float) Math.PI / 180f;
    }

    public void setPitch(float pitch){
        this.pitch = pitch;
    }

    public void setPitchFromDegrees(float degrees){
        this.pitch = degrees * (float) Math.PI / 180f;
    }

    public void rotatePitch(float pitch){
        this.pitch += pitch;
    }

    public void rotatePitchFromDegrees(float degrees){
        this.pitch += degrees * (float) Math.PI / 180f;
    }

    public void moveForward(float v){
        calculate_vLookDir_vUp();
        float[] vForward = Geometry.VectorMultiply(this.vLookDir, v);
        vCamera = Geometry.VectorAddition(vCamera, vForward);
    }

    public void moveRight(float v) {
        calculate_vLookDir_vUp();

        float[] vRight = Geometry.vector_cross_product(this.vLookDir, this.vUp);
        vRight = Geometry.vector_normalise(vRight);

        float[] vLateralMove = Geometry.VectorMultiply(vRight, v);

        vCamera = Geometry.VectorAddition(vCamera, vLateralMove);
    }

    public void translateY(float v){
        this.vCamera[1] += v;
    }

    public void translateX(float v){
        this.vCamera[0] += v;
    }

    public void update(){
    }

    public void calculate_vLookDir_vUp(){
        float[] vTarget = new float[]{0,0,1,1};

        // ! gimbal lock
        this.pitch = (float) Math.max(-Math.PI / 2 + 0.01f, Math.min(Math.PI / 2 - 0.01f, this.pitch));

        float[][] matCameraRot = Geometry.getMatrixIdentity();
        float[][] matCameraRotY = Geometry.getMatrixRotationY(this.fYaw);
        float[][] matCameraRotX = Geometry.getMatrixRotationX(-this.pitch);

        matCameraRot = Geometry.MatrixMultiplyMatrix(matCameraRotY, matCameraRot);
        matCameraRot = Geometry.MatrixMultiplyMatrix(matCameraRotX, matCameraRot);
        this.vLookDir = Geometry.matrix_multiplyVector(matCameraRot, vTarget);

        float[] vUp = new float[]{0, 1, 0,1};
        float[] vRight = Geometry.vector_cross_product(vUp, this.vLookDir);

        this.vUp = Geometry.vector_cross_product(this.vLookDir, vRight);
    }

}
