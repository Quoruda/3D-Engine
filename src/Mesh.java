import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mesh {

    private int[][] triangles;
    private float[][][] transformedTriangles;
    private float[][] normals;

    private float[][] vertices;
    private float[][] transformedVertices;

    private float positionX;
    private float positionY;
    private float positionZ;

    private float rotationX = 0;
    private float rotationY = 0;
    private float rotationZ = 0;

    private boolean position_changed = false;
    private boolean rotation_changed = false;
    private boolean scale_changed = false;

    private float scale = 1;

    public Mesh(){

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

    public void rotate(float rotationX, float rotationY, float rotationZ){
        this.rotationX += rotationX;
        this.rotationY += rotationY;
        this.rotationZ += rotationZ;
        rotation_changed = true;
    }

    public static Mesh loadFromObjectFile(String sFilename){
        Mesh mesh = new Mesh();
        ArrayList<float[]> vertices = new ArrayList<float[]>();
        ArrayList<int[]> faces = new ArrayList<int[]>();

        try {
            int n = 0;
            File file = new File(sFilename);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();

                String[] token = data.trim().split(" ");
                if(token[0].equals("v")){
                    vertices.add(new float[]{Float.parseFloat(token[1]), Float.parseFloat(token[2]), Float.parseFloat(token[3]), 1});
                }else if(token[0].equals("f")){
                    int[] face = new int[token.length-1];
                    if(face.length == 3){
                        n += 1;
                    }else if(face.length == 4){
                        n += 2;
                    }
                    for(int i = 1; i < token.length; i++){
                        face[i-1] = Integer.parseInt(token[i]);
                    }
                    faces.add(face);
                }

            }

            mesh.vertices = new float[vertices.size()][4];
            for(int i = 0; i < vertices.size(); i++){
                mesh.vertices[i] = vertices.get(i);
            }


            mesh.triangles = new int[n][3];
            int i = 0;
            for (int[] face: faces) {
                mesh.triangles[i] = new int[]{face[0] - 1, face[1]-1, face[2]-1};
                i++;
                if (face.length == 4 ) {
                    mesh.triangles[i] = new int[]{ face[2]-1, face[3]-1,face[0]-1};
                    i++;
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        mesh.transformedVertices = new float[mesh.vertices.length][3];
        mesh.transformedTriangles = new float[mesh.triangles.length][3][4];
        mesh.normals = new float[mesh.triangles.length][4];

        mesh.position_changed = true;
        mesh.rotation_changed = true;
        return mesh;
    }

    public float[][][] getTransformedTriangles(){
        return transformedTriangles;
    }

    public void update(){

        if(!(position_changed || rotation_changed || scale_changed)) return;
        //System.out.println(true);

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
            transformedVertices[i] = Geometry.VectorMultiply(transformedVertices[i], scale);
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

        position_changed = false;
        rotation_changed = false;

    }

    public float[][] getTransformedTriangle(int i){
        return transformedTriangles[i];
    }

    public float[] getNormal(int i){
        return normals[i];
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        scale_changed = true;
    }
}
