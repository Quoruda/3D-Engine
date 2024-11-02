package Rendering;

import Objects.Texture;

public class Triangle{
    public float[][] p;
    public float lum;
    public float[][] t;
    public Texture texture;

    public Triangle(){
        this.p = new float[3][4];
        this.t = new float[3][3];
    }

    public Triangle(float[][] p, float[][] t){
        this.p = p;
        this.t = t;
    }

    public Triangle(float[][] p, float lum, float[][] t, Texture texture){
        this.p = p;
        this.lum = lum;
        this.t = t;
        this.texture = texture;
    }
}