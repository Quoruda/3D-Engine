import java.lang.Math;
import java.util.ArrayList;

public class Geometry {

    public static float[][] getMatrixRotationY(float fAngleRad){
        return new float[][]{
                {(float)Math.cos(fAngleRad),0 ,(float) Math.sin(fAngleRad), 0 },
                {0,1,0,0},
                {(float)-Math.sin(fAngleRad),0,(float)Math.cos(fAngleRad),0},
                {0,0,0,1}
        };
    }

    public static  float[][] getMatrixRotationX(float fAngleRad){
        return new float[][]{
                {1,0,0,0},
                {0, (float)Math.cos(fAngleRad),(float)Math.sin(fAngleRad), 0 },
                {0,(float)-Math.sin(fAngleRad),(float)Math.cos(fAngleRad), 0 },
                {0,0,0,1}
        };
    }

    public static float[][] getMatrixRotationZ(float fAngleRad){
        return new float[][]{
                {(float)Math.cos(fAngleRad),(float)Math.sin(fAngleRad),0, 0 },
                {(float)-Math.sin(fAngleRad),(float)Math.cos(fAngleRad), 0, 0 },
                {0,0,1,0},
                {0,0,0,1}
        };
    }

    public static float[][] getMatrixTranslation(float x, float y, float z){
        return new float[][]{
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {x,y,z,1}
        };
    }

    public static float[][] getMatrixIdentity(){
        return new float[][]{
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        };
    }

    public static float[][] getEmptyMatrix(){
        return new float[4][4];
    }

    public static float[] getEmptyVector(){
        return new float[]{0,0,0,1};
    }

    public static float[][] matrix_makeProjection(float fFovDegrees, float fAspectRatio, float fNear, float fFar){
        float fFovRad =(float) (1.0/Math.tan(fFovDegrees*0.5/180.0* Math.PI));
        return new float[][]{
                {fAspectRatio*fFovRad,0,0,0},
                {0,fFovRad,0 ,0},
                {0,0,fFar/(fFar-fNear),1f},
                {0,0,(-fFar*fNear)/(fFar-fNear),0f}
        };
    }

    public static float[] matrix_multiplyVector(float[][] m, float[] i){
        return new float[]{
                i[0] * m[0][0] + i[1] * m[1][0] + i[2] * m[2][0] + i[3] * m[3][0],
                i[0] * m[0][1] + i[1] * m[1][1] + i[2] * m[2][1] + i[3] * m[3][1],
                i[0] * m[0][2] + i[1] * m[1][2] + i[2] * m[2][2] + i[3] * m[3][2],
                i[0] * m[0][3] + i[1] * m[1][3] + i[2] * m[2][3] + i[3] * m[3][3],
        };
    }

    public static float[][] matrix_multiplyTriangle(float[][] m, float[][] t){
        return new float[][]{matrix_multiplyVector(m, t[0]), matrix_multiplyVector(m, t[1]), matrix_multiplyVector(m, t[2])};
    }

    public static float[] VectorAddition(float[] a, float[] b){
        return new float[]{a[0]+b[0],a[1]+b[1],a[2]+b[2], 1};
    }

    public static float[] VectorSubstraction(float[] a, float[] b){
        return new float[]{a[0] - b[0], a[1] - b[1], a[2] - b[2],1};
    }

    public static float[][] triangleTranslate(float[][] t, float[] v){
        return new float[][]{VectorAddition(t[0], v),VectorAddition(t[1], v),VectorAddition(t[2], v)  };
    }

    public static float[] vector_cross_product(float[] v1, float[] v2){
        return new float[]{
                v1[1] * v2[2] - v1[2] * v2[1],
                v1[2] * v2[0] - v1[0] * v2[2],
                v1[0] * v2[1] - v1[1] * v2[0],
                1
        };
    }

    public static float vector_length(float[] v){
        return (float) Math.sqrt(VectorDotProduct(v,v));
    }

    public static float quick_reversed_sqrt(float x) {
        int magic_number =0x5f3759df;
        float xhalf = 0.5f * x;
        int i = Float.floatToRawIntBits(x);
        i = magic_number - (i >> 1);
        x = Float.intBitsToFloat(i);
        x *= (1.5f - xhalf * x * x);
        return x;
    }

    public static float[] vector_normalise(float[] v){
        float rsqrt = quick_reversed_sqrt(VectorDotProduct(v,v));
        return new float[]{
                v[0]*rsqrt,
                v[1]*rsqrt,
                v[2]*rsqrt,
                1
        };
    }
    public static float[] vector_normalise2(float[] v){
        float length = vector_length(v);
        return new float[]{
                v[0]/length,
                v[1]/length,
                v[2]/length,
                1
        };
    }


    public static float VectorDotProduct(float[] v1, float[] v2){
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    public static float[] VectorMultiply(float[] v, float c){
        return new float[]{v[0] * c,  v[1] * c, v[2] * c, 1};
    }

    public static float[] VectorDivision(float[] v, float c){
        return new float[]{v[0] / c,  v[1] / c, v[2] / c, 1};
    }

    public static float[][] Matrix_PointAt(float[] pos, float[] target, float[] up){
        float[] newForward = VectorSubstraction(target, pos);
        newForward = vector_normalise(newForward);

        float[] a = VectorMultiply(newForward, VectorDotProduct(up, newForward));
        float[] newup = VectorSubstraction(up, a);
        newup = vector_normalise(newup);

        float[] newRight = vector_cross_product(newup, newForward);

        return new float[][]{
                {newRight[0], newRight[1], newRight[2], 0},
                {newup[0], newup[1], newup[2], 0},
                {newForward[0], newForward[1], newForward[2], 0},
                {pos[0], pos[1], pos[2], 1}
        };

    }

    public static float[][] Matrix_QuickInverse(float[][] m){
        float[][] result = new float[4][4];
        result[0][0] = m[0][0]; result[0][1] = m[1][0]; result[0][2] = m[2][0]; result[0][3] = 0;
        result[1][0] = m[0][1]; result[1][1] = m[1][1]; result[1][2] = m[2][1]; result[1][3] = 0;
        result[2][0] = m[0][2]; result[2][1] = m[1][2]; result[2][2] = m[2][2]; result[2][3] = 0;

        result[3][0] = -(m[3][0]*result[0][0] + m[3][1]*result[1][0] + m[3][2]*result[2][0] );
        result[3][1] = -(m[3][0]*result[0][1] + m[3][1]*result[1][1] + m[3][2]*result[2][1] );
        result[3][2] = -(m[3][0]*result[0][2] + m[3][1]*result[1][2] + m[3][2]*result[2][2] );
        result[3][3] = 1;

        return result;

    }

    public static float[][] MatrixMultiplyMatrix(float[][] m1, float[][] m2){
        float[][] result = new float[4][4];
        for(int c = 0; c < 4; c++){
            for(int r = 0; r < 4; r++){
                result[r][c] = m1[r][0] * m2[0][c] + m1[r][1] * m2[1][c] + m1[r][2] * m2[2][c] + m1[r][3] * m2[3][c];
            }
        }
        return result;
    }

    public static void print2DArray(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void print1DArray(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + " ");

        }
        System.out.println();
        System.out.println();
    }

    public static float[] Vector_IntersectPlane(float[] plane_p, float[] plane_n, float[] lineStart, float[] lineEnd, MutableValue<Float> t){
        plane_n = vector_normalise(plane_n);
        float plane_d = -VectorDotProduct(plane_n, plane_p);
        float ad = VectorDotProduct(lineStart, plane_n);
        float bd = VectorDotProduct(lineEnd, plane_n);
        t.v = (-plane_d -ad )/(bd - ad);
        float[] lineStartToEnd = VectorSubstraction(lineEnd, lineStart);
        float[] lineToIntersect = VectorMultiply(lineStartToEnd, t.v);
        return VectorAddition(lineStart, lineToIntersect);

    }

    public static float distancePlan(float[] plane_n,float[] plane_p, float[] p){
        return plane_n[0]*p[0]+plane_n[1]*p[1]+plane_n[2]*p[2] -VectorDotProduct(plane_n, plane_p);
    }

    public static ArrayList<Triangle> Triangle_ClipAgainstPlane(float[] plane_p, float[] plane_n , Triangle in_tri) {
        plane_n = vector_normalise(plane_n);

        float[][] inside_points = new float[3][4];
        int nInsidePointCount = 0;
        float[][] outside_points = new float[3][4];
        int nOutsidePointCount = 0;
        float[][] inside_tex = new float[3][2];
        int nInsideTexCount = 0;
        float[][] outside_tex = new float[3][2];
        int nOutsideTexCount = 0;

        float d0 = distancePlan(plane_n,plane_p, in_tri.p[0]);
        float d1 = distancePlan(plane_n,plane_p ,in_tri.p[1]);
        float d2 = distancePlan(plane_n,plane_p ,in_tri.p[2]);

        if (d0 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.p[0];
            inside_tex[nInsideTexCount++] = in_tri.t[0];
        } else {
            outside_points[nOutsidePointCount++] = in_tri.p[0];
            outside_tex[nOutsideTexCount++] = in_tri.t[0];
        }
        if (d1 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.p[1];
        } else {
            outside_points[nOutsidePointCount++] = in_tri.p[1];
        }
        if (d2 >= 0) {
            inside_points[nInsidePointCount++] = in_tri.p[2];
        } else {
            outside_points[nOutsidePointCount++] = in_tri.p[2];
        }

        ArrayList<Triangle> out_triangles = new ArrayList<>();
        Triangle out_tri1 = new Triangle();
        Triangle out_tri2 = new Triangle();

        if (nInsidePointCount == 0) {
            //return out_triangles;
        }
        else if (nInsidePointCount == 3) {
            out_triangles.add(in_tri);
        }
        else if (nInsidePointCount == 1 && nOutsidePointCount == 2) {

            out_tri1 = new Triangle();

            out_tri1.p[0] = inside_points[0];
            out_tri1.t[0] = inside_tex[0];

            MutableValue<Float> t = new MutableValue<>();
            out_tri1.p[1] = Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0], t);
            out_tri1.t[1][0] = t.v * (outside_tex[0][0] - inside_tex[0][0]) + inside_tex[0][0];
            out_tri1.t[1][1] = t.v * (outside_tex[0][1] - inside_tex[0][1]) + inside_tex[0][1];

            out_tri1.p[2] = Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1], t);
            out_tri1.t[2][0] = t.v * (outside_tex[1][0] - inside_tex[0][0]) + inside_tex[0][0];
            out_tri1.t[2][1] = t.v * (outside_tex[1][1] - inside_tex[0][1]) + inside_tex[0][1];

            out_triangles.add(out_tri1);
        }
        else if (nInsidePointCount == 2 && nOutsidePointCount == 1) {
            out_tri1.p = new float[3][4];
            out_tri1.p[0] = inside_points[0];
            out_tri1.p[1] = inside_points[1];
            out_tri1.t[0] = inside_tex[0];
            out_tri1.t[1] = inside_tex[1];

            MutableValue<Float> t = new MutableValue<>();
            out_tri1.p[2] = Vector_IntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0],t);
            out_tri1.t[2][0] = t.v * (outside_tex[0][0] - inside_tex[0][0]) + inside_tex[0][0];
            out_tri1.t[2][1] = t.v * (outside_tex[0][1] - inside_tex[0][1]) + inside_tex[0][1];
            out_triangles.add(out_tri1);

            out_tri2.p = new float[3][4];
            out_tri2.p[0] = inside_points[1];
            out_tri2.t[0] = inside_tex[1];
            out_tri2.p[1] = out_tri1.p[2];
            out_tri2.t[1] = out_tri1.t[2];
            out_tri2.p[2] = Vector_IntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0], t);
            out_tri2.t[2][0] = t.v * (outside_tex[0][0] - inside_tex[1][0]) + inside_tex[1][0];
            out_tri2.t[2][1] = t.v * (outside_tex[0][1] - inside_tex[1][1]) + inside_tex[1][1];

            out_triangles.add(out_tri2);

        }else{
            System.out.println("Bizarre: "+nInsidePointCount + " " + nOutsidePointCount);
        }
        if(out_triangles.size() > 2) {
            System.out.println("bizarre");
        }
        return out_triangles;
    }
}

