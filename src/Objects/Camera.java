package Objects;

import Rendering.Geometry;
import Rendering.Triangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Camera {


    public float[] vCamera, vLookDir,vUp;
    private float fYaw;
    private float pitch;
    private float clipped_distance;
    private int width;
    private float ratio;
    private boolean active;

    private BufferedImage frame;

    public Camera(){
        vCamera = new float[]{0,0,0,1};
        vLookDir = new float[]{0,0,0,1};
        fYaw = 0;
        pitch = 0;
        clipped_distance = 0.1f;
        width = 300;
        ratio = 600f/700f;
        active = false;
        calculate_vLookDir_vUp();
        takePhoto(new ArrayList<>());
    }

    public void setPosition(float x, float y, float z){
        vCamera[0] = x;
        vCamera[1] = y;
        vCamera[2] = z;
    }

    public float getPositionX(){
        return vCamera[0];
    }

    public float getPositionY(){
        return vCamera[1];
    }

    public float getPositionZ(){
        return vCamera[2];
    }

    public float[][] getMatCamera(){
        calculate_vLookDir_vUp();
        float[] vTarget = Geometry.VectorAddition(this.vCamera, vLookDir);
        float[][] matCamera = Geometry.Matrix_PointAt(this.vCamera, vTarget, vUp);
        return matCamera;
    }

    public float getYaw(){
        return fYaw;
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

    public float getPitch(){
        return pitch;
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


    public float getClipped_distance() {
        return clipped_distance;
    }

    public void setClipped_distance(float clipped_distance) {
        this.clipped_distance = clipped_distance;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight(){
        return (int) (width*ratio);
    }

    public float getRatio(){
        return ratio;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setRatio(float ratio){
        this.ratio = ratio;
    }

    public void takePhoto(ArrayList<Triangle> triangles){
        int width = getWidth();
        int height = getHeight();
        float[][] pDepthBuffer = new float[height][width];
        BufferedImage photo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(Triangle t: triangles){
            TexturedTriangle(
                    (int) t.p[0][0], (int) t.p[0][1], t.t[0][0], t.t[0][1], t.t[0][2],
                    (int) t.p[1][0], (int) t.p[1][1], t.t[1][0], t.t[1][1], t.t[1][2],
                    (int) t.p[2][0], (int) t.p[2][1], t.t[2][0], t.t[2][1], t.t[2][2],
                    t.texture, t.lum, pDepthBuffer, photo );
        }

        frame = photo;

    }

    public static void setPixel(int x, int y, int r, int g, int b, BufferedImage frame){
        frame.setRGB(x,y,(r << 16) | (g << 8) | b);
    }

    public void TexturedTriangle(int x1, int y1, float u1, float v1, float w1,
                                 int x2, int y2, float u2, float v2, float w2,
                                 int x3, int y3, float u3, float v3, float w3,
                                 Texture texture, float lum, float[][] pDepthBuffer, BufferedImage frame){

        int width = getWidth();
        int height = getHeight();

        int cR, cG, cB, color;

        int tempI;
        float tempF;


        if (y2 < y1){
            //swap y1, y2
            tempI = y1;y1 = y2;y2 = tempI;
            //swap x1, x2
            tempI = x1;x1 = x2;x2 = tempI;
            //swap u1, u2
            tempF = u1;u1 = u2;u2 = tempF;
            //swap v1, v2
            tempF = v1;v1 = v2; v2 = tempF;
            //swap w1, w2
            tempF = w1;w1 = w2;w2 = tempF;
        }
        if (y3 < y1){
            //swap y1 y3
            tempI = y1;y1 = y3;y3 = tempI;
            //swap x1 x3
            tempI = x1;x1 = x3;x3 = tempI;
            //swap u1 u3
            tempF = u1;u1 = u3;u3 = tempF;
            //swap v1 v3
            tempF = v1;v1 = v3;v3 = tempF;
            //swap w1 w3
            tempF = w1;w1 = w3;w3 = tempF;
        }

        if (y3 < y2){
            //swap y2 y3
            tempI = y2;y2 = y3;y3 = tempI;
            //swap x2 x3
            tempI = x2;x2 = x3;x3 = tempI;
            //swap u2 u3
            tempF = u2;u2 = u3;u3 = tempF;
            //swap v2 v3
            tempF = v2;v2 = v3;v3 = tempF;
            //swap w2 w3
            tempF = w2;w2 = w3;w3 = tempF;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        float dv1 = v2 - v1;
        float du1 = u2 - u1;
        float dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        float dv2 = v3 - v1;
        float du2 = u3 - u1;
        float dw2 = w3 - w1;

        float dax_step = 0, dbx_step = 0,
                du1_step = 0, dv1_step = 0,
                du2_step = 0, dv2_step = 0,
                dw1_step = 0, dw2_step = 0;

        if (dy1 != 0) dax_step = dx1 / (float)Math.abs(dy1);
        if (dy2 != 0) dbx_step = dx2 / (float)Math.abs(dy2);

        if (dy1 != 0) du1_step = du1 / (float)Math.abs(dy1);
        if (dy1 != 0) dv1_step = dv1 / (float)Math.abs(dy1);
        if (dy1 != 0) dw1_step = dw1 / (float)Math.abs(dy1);

        if (dy2 != 0) du2_step = du2 / (float)Math.abs(dy2);
        if (dy2 != 0) dv2_step = dv2 / (float)Math.abs(dy2);
        if (dy2 != 0) dw2_step = dw2 / (float)Math.abs(dy2);

        float tex_u, tex_v, tex_w;
        float tex_su, tex_sv, tex_sw;
        float tex_eu, tex_ev, tex_ew;
        int ax, bx;

        float tstep, t;

        if (dy1 != 0){

            for (int i = Math.max(y1,0); i <= Math.min(y2, height-1); i++){
                ax = (int)(x1 + (i - y1) * dax_step);
                bx = (int)(x1 + (i - y1) * dbx_step);

                tex_su = u1 + (i - y1) * du1_step;
                tex_sv = v1 + (i - y1) * dv1_step;
                tex_sw = w1 + (i - y1) * dw1_step;

                tex_eu = u1 + (i - y1) * du2_step;
                tex_ev = v1 + (i - y1) * dv2_step;
                tex_ew = w1 + (i - y1) * dw2_step;

                if (ax > bx){
                    //swap ax bx
                    tempI = ax;ax = bx;bx = tempI;
                    //swap tex_su tex_eu
                    tempF = tex_su;tex_su = tex_eu;tex_eu = tempF;
                    //swap tex_sv tex_ev
                    tempF = tex_sv;tex_sv = tex_ev;tex_ev = tempF;
                    //swap tex_sw tex_ew
                    tempF = tex_sw;tex_sw = tex_ew;tex_ew = tempF;
                }

                tstep = 1.0f / (float)(bx - ax);
                t = 0.0f;
                if(bx > width-1) bx = width-1;
                if(ax < 0){
                    t += tstep*ax*-1;
                    ax = 0;
                }
                for (int j = ax; j <= bx; j++){
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if(tex_w > pDepthBuffer[i][j]) {
                        tex_u = (1.0f - t) * tex_su + t * tex_eu;
                        tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                        color = texture.getPixel(tex_u / tex_w, tex_v / tex_w);
                        cR = (color >> 16) & 0xFF;
                        cG = (color >> 8) & 0xFF;
                        cB = color & 0xFF;

                        setPixel(j,i,(int) (cR*lum),(int) (cG*lum),(int) (cB*lum), frame );
                        pDepthBuffer[i][j] = tex_w;
                    }
                    t += tstep;
                }
            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if (dy1 != 0) dax_step = dx1 / (float)Math.abs(dy1);
        if (dy2 != 0) dbx_step = dx2 / (float)Math.abs(dy2);

        du1_step = 0; dv1_step = 0;

        if (dy1 != 0) du1_step = du1 / (float)Math.abs(dy1);
        if (dy1 != 0) dv1_step = dv1 / (float)Math.abs(dy1);
        if (dy1 != 0) dw1_step = dw1 / (float)Math.abs(dy1);

        if (dy1 != 0){
            for (int i = Math.max(y2,0); i <= Math.min(y3, height-1); i++) {
                ax = (int) (x2 + (float)(i - y2) * dax_step);
                bx = (int) (x1 + (float)(i - y1) * dbx_step);

                tex_su = u2 + (float)(i - y2) * du1_step;
                tex_sv = v2 + (float)(i - y2) * dv1_step;
                tex_sw = w2 + (float)(i - y2) * dw1_step;
                tex_eu = u1 + (float)(i - y1) * du2_step;
                tex_ev = v1 + (float)(i - y1) * dv2_step;
                tex_ew = w1 + (float)(i - y1) * dw2_step;

                if (ax > bx) {
                    //swap ax bx
                    tempI = ax;
                    ax = bx;
                    bx = tempI;
                    //swap tex_su tex_eu
                    tempF = tex_su;
                    tex_su = tex_eu;
                    tex_eu = tempF;
                    //swap tex_sv tex_ev
                    tempF = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = tempF;
                    //swap tex_sw tex_ew
                    tempF = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = tempF;
                }

                tstep = 1.0f / (float)(bx - ax);
                t = 0.0f;
                if(bx > width-1) bx = width-1;
                if(ax < 0){
                    t += tstep*ax*-1;
                    ax = 0;
                }
                for (int j = ax; j <= bx; j++) {
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if(tex_w > pDepthBuffer[i][j]) {
                        tex_u = (1.0f - t) * tex_su + t * tex_eu;
                        tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                        color = texture.getPixel(tex_u / tex_w, tex_v / tex_w);
                        cR = (color >> 16) & 0xFF;
                        cG = (color >> 8) & 0xFF;
                        cB = color & 0xFF;
                        setPixel(j,i,(int) (cR*lum),(int) (cG*lum),(int) (cB*lum), frame );
                        pDepthBuffer[i][j] = tex_w;
                    }
                    t += tstep;
                }
            }
        }
    };

    public BufferedImage getFrame() {
        return frame;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
