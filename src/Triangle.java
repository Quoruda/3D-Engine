public class Triangle {

    float[][] p;
    float[][] t;

    public Triangle(float[][] p, float[][] t){
        this.p = p;
        this.t = t;
    }

    public Triangle(){
        this.p = new float[3][3];
        this.t = new float[3][2];
    }
}
