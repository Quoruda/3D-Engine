package Objects;

import Rendering.Geometry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mesh {

    public float[][] vertices;
    public float[][] transformedVertices;
    public int[][] triangles;
    public float[][][] transformedTriangles;
    public float[][] normals;

    public float[][] verticesTex;
    public int[][] tList;
    public float[][][] tListTransformed;



    private float positionX;
    private float positionY;
    private float positionZ;

    private float rotationX;
    private float rotationY;
    private float rotationZ;

    public boolean position_changed;
    public boolean rotation_changed;
    public boolean scale_changed;

    private float scaleX;
    private float scaleY;
    private float scaleZ;

    private Texture texture;

    public Mesh(){
        triangles = new int[0][3];
        transformedTriangles = new float[0][3][4];
        normals = new float[0][4];
        tList = new int[0][3];
        tListTransformed = new float[0][3][3];
        vertices = new float[0][4];
        transformedVertices = new float[0][3];
        positionX = 0;
        positionY = 0;
        positionZ = 0;
        rotationX = 0;
        rotationY = 0;
        rotationZ = 0;
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        texture = new Texture();
        position_changed = true;
        rotation_changed = true;
        scale_changed = true;
    }

    public void setPositionX(float positionX){
        this.positionX = positionX;
        position_changed = true;
    }
    public void setPositionY(float positionY){
        this.positionY = positionY;
        position_changed = true;
    }
    public void setPositionZ(float positionZ){
        this.positionZ = positionZ;
        position_changed = true;
    }

    public float getPositionX(){
        return positionX;
    }

    public float getPositionY(){
        return positionY;
    }

    public float getPositionZ(){
        return positionZ;
    }

    public void setPosition(float positionX, float positionY, float positionZ){
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        position_changed = true;
    }

    public void setRotationX(float rotationX){
        this.rotationX = rotationX;
        rotation_changed = true;
    }

    public void setRotationY(float rotationY){
        this.rotationY = rotationY;
        rotation_changed = true;
    }

    public void setRotationZ(float rotationZ){
        this.rotationZ = rotationZ;
        rotation_changed = true;
    }

    public float getRotationX(){
        return rotationX;
    }

    public void rotate(float rotationX, float rotationY, float rotationZ){
        this.rotationX += rotationX;
        this.rotationY += rotationY;
        this.rotationZ += rotationZ;
        rotation_changed = true;
    }

    public static Mesh loadFromObjectFile(String sFilename, boolean hasTexture){
        Mesh mesh = new Mesh();
        ArrayList<float[]> vertices = new ArrayList<float[]>();
        ArrayList<float[]> vtexs = new ArrayList<float[]>();
        ArrayList<int[]> faces = new ArrayList<int[]>();
        ArrayList<int[]> ftexs = new ArrayList<int[]>();

        try {
            int n = 0;
            File file = new File(sFilename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                String[] tokens = data.trim().split(" ");
                if(tokens[0].equals("v")){
                    vertices.add(new float[]{Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]), 1});
                }
                if(tokens[0].equals("vt")){
                    vtexs.add(new float[]{Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), 1.0f});
                }

                if (tokens[0].equals("f")) {
                    if(!hasTexture) {
                        int[] face = new int[tokens.length - 1];
                        n += tokens.length - 2;
                        for (int i = 1; i < tokens.length; i++) {
                            face[i - 1] = Integer.parseInt(tokens[i]);
                        }
                        faces.add(face);
                    }else{

                        int[] face = new int[tokens.length - 1];
                        int[] ftext = new int[tokens.length - 1];
                        n += tokens.length - 2;
                        for (int i = 1; i < tokens.length; i++) {
                            String[] parts = tokens[i].split("[/ ]");
                            face[i - 1] = Integer.parseInt(parts[0]);
                            ftext[i - 1] = Integer.parseInt(parts[1]);
                        }
                        faces.add(face);
                        ftexs.add(ftext);

                    }
                }

            }

            if(!hasTexture){
                float minX = vertices.get(0)[0];
                float minY = vertices.get(0)[1];
                float minZ = vertices.get(0)[2];
                float maxX = vertices.get(0)[0];
                float maxY = vertices.get(0)[1];
                float maxZ = vertices.get(0)[2];
                float x, y, z;
                for(float[] vertex: vertices){
                    x = vertex[0];
                    y = vertex[0];
                    z = vertex[0];
                    if(x > maxX){
                        maxX = x;
                    }
                    if(x < minX){
                        minX = x;
                    }
                    if(y > maxY){
                        maxY = y;
                    }
                    if(y < minY){
                        minY = y;
                    }
                    if(z > maxZ){
                        maxZ = z;
                    }
                    if(z < minZ){
                        minZ = z;
                    }
                }
                float u, v;
                for(float[] vertex: vertices){
                    x = vertex[0];
                    y = vertex[1];
                    z = vertex[2];

                    //test
                    //u = (x-minX)/(maxX-minX);
                    //v = (z-minZ)/(maxZ-minZ);

                    //cylindrical
                    //float angle = (float) Math.atan2(z, x);
                    //u = (angle+ (float)Math.PI)/(2*(float)Math.PI);
                    //v = (y-minY)/(maxX-minY);

                    //spherical
                    float theta = (float)Math.atan2(z, x);
                    float phi = (float) Math.acos((y-minY)/(maxX-minY));
                    u = (theta+(float)Math.PI)/(2*(float)Math.PI);
                    v = phi/(float)Math.PI;

                    //add
                    vtexs.add(new float[]{u, v, 1.0f});
                }
                for(int[] face: faces){
                    int[] ftex = new int[face.length];
                    for(int i=0; i<face.length; i++){
                        ftex[i] = face[i];
                    }
                    ftexs.add(ftex);
                }
            }

            mesh.vertices = new float[vertices.size()][4];
            for(int i = 0; i < vertices.size(); i++){
                mesh.vertices[i] = vertices.get(i);
            }

            mesh.verticesTex = new float[vtexs.size()][3];
            for(int i = 0; i < vtexs.size(); i++){
                mesh.verticesTex[i] = vtexs.get(i);
            }

            mesh.triangles = new int[n][3];
            mesh.tList = new int[n][3];

            int iTriangle = 0;
            for (int j = 0; j < faces.size(); j++) {
                int[] face = faces.get(j);
                int[] ftex = ftexs.get(j);
                for(int i=0; i< face.length-2; i++){
                    mesh.triangles[iTriangle] = new int[]{face[0] - 1, face[i+1]-1, face[i+2]-1};
                    mesh.tList[iTriangle] = new int[]{ftex[0] - 1, ftex[i+1]-1, ftex[i+2]-1};
                    iTriangle++;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        mesh.transformedVertices = new float[mesh.vertices.length][3];
        mesh.transformedTriangles = new float[mesh.triangles.length][3][4];
        mesh.tListTransformed = new float[mesh.tList.length][3][3];
        mesh.normals = new float[mesh.triangles.length][4];

        mesh.position_changed = true;
        mesh.rotation_changed = true;
        mesh.scale_changed = true;

        return mesh;
    }

    public float[][][] getTransformedTriangles(){
        return transformedTriangles;
    }

    public void update(float deltaTime){
        texture.update(deltaTime);
        updateTexture();

        if(!(position_changed || rotation_changed || scale_changed)) return;
        position_changed = false;
        rotation_changed = false;
        scale_changed = false;

        transformedVertices = new float[vertices.length][4];
        transformedTriangles = new float[triangles.length][3][4];
        normals = new float[triangles.length][];

        float[][] matTrans = Geometry.getMatrixTranslation(positionX, positionY, positionZ);

        float[][] matWorld = Geometry.getMatrixIdentity();

        float[][] matRotZ = Geometry.getMatrixRotationZ(rotationZ);
        matWorld = Geometry.MatrixMultiplyMatrix(matRotZ, matWorld);
        float[][] matRotY = Geometry.getMatrixRotationY(rotationY);
        matWorld = Geometry.MatrixMultiplyMatrix(matRotY, matWorld);
        float[][] matRotX = Geometry.getMatrixRotationX(rotationX);
        matWorld = Geometry.MatrixMultiplyMatrix(matRotX, matWorld);

        matWorld = Geometry.MatrixMultiplyMatrix(matWorld, matTrans);

        int i;
        for (i = 0; i < vertices.length; i++) {
            transformedVertices[i] = vertices[i];
            transformedVertices[i][0] *= scaleX;
            transformedVertices[i][1] *= scaleY;
            transformedVertices[i][2] *= scaleZ;
            transformedVertices[i] = Geometry.matrix_multiplyVector(matWorld, transformedVertices[i]);
        }

        float[] line1, line2, normal;

        for (i = 0; i < triangles.length; i++) {

            transformedTriangles[i][0] = transformedVertices[triangles[i][0]];
            transformedTriangles[i][1] = transformedVertices[triangles[i][1]];
            transformedTriangles[i][2] = transformedVertices[triangles[i][2]];

            line1 = Geometry.VectorSubstraction(transformedVertices[triangles[i][1]], transformedVertices[triangles[i][0]]);
            line2 = Geometry.VectorSubstraction(transformedVertices[triangles[i][2]], transformedVertices[triangles[i][0]]);

            normal = Geometry.vector_cross_product(line1, line2);
            normal = Geometry.vector_normalise(normal);
            normals[i] = normal;
        }
    }

    private void updateTexture(){
        tListTransformed = new float[tList.length][3][3];
        int i;

        for (i = 0; i < tList.length; i++) {
            tListTransformed[i][0] = verticesTex[tList[i][0]];
            tListTransformed[i][1] = verticesTex[tList[i][1]];
            tListTransformed[i][2] = verticesTex[tList[i][2]];
        }
    }

    public float[][] getTransformedTriangle(int i){
        return transformedTriangles[i];
    }

    public float[] getNormal(int i){
        return normals[i];
    }

    public float[][] getT(int i){
        return tListTransformed[i];
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
        this.scaleZ = scale;
        scale_changed = true;
    }

    public void setScaleX(float scale){
        this.scaleX = scale;
        scale_changed = true;
    }

    public void setScaleY(float scale){
        this.scaleY = scale;
        scale_changed = true;
    }

    public void setScaleZ(float scale){
        scale_changed = true;
        this.scaleZ = scale;
    }

    public void setTexture(Texture texture){
        this.texture = texture;
    }

    public Texture getTexture(){
        return texture;
    }
}
