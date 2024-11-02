package Display;

import Inputs.KeyPressedListener;
import Objects.Texture;
import Rendering.Engine;
import Rendering.Triangle;



import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Screen extends Display{

    public JPanel jpanel;
    public JFrame jframe;

    public Screen() {
        jpanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                paintTriangle(g);
            }
        };
        jframe = new JFrame();

        int width = 700;
        int height = 600;
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(width, height);
        jframe.add(jpanel);
        jframe.setVisible(true);
        jframe.setFocusable(true);
        jframe.setResizable(false);
        jframe.requestFocusInWindow();
        TrianglesToRaster = new ArrayList<>();
    }

    @Override
    public void update() {
        jpanel.repaint();
    }

    public void setPixel(int x, int y, int r, int g, int b){
        frame.setRGB(x,y,(r << 16) | (g << 8) | b);
    }

    public void paintTriangle(Graphics g) {

        int width = jpanel.getWidth();
        int height = jpanel.getHeight();

        pDepthBuffer = new float[height][width];
        frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ArrayList<Triangle> trianglesToRaster = TrianglesToRaster;

        for( Triangle t : trianglesToRaster){
            TexturedTriangle(
                    (int) t.p[0][0], (int) t.p[0][1], t.t[0][0], t.t[0][1], t.t[0][2],
                    (int) t.p[1][0], (int) t.p[1][1], t.t[1][0], t.t[1][1], t.t[1][2],
                    (int) t.p[2][0], (int) t.p[2][1], t.t[2][0], t.t[2][1], t.t[2][2],
                    t.texture, t.lum );
        }

        g.drawImage(frame, 0,0, width, height, jpanel );

        /*
        if(drawLines){
            g.setColor(Color.PINK);
            for( Triangle t : trianglesToRaster) {
                float[][] triangle = t.p;
                x0 = (int) triangle[0][0];
                y0 = (int) triangle[0][1];
                x1 = (int) triangle[1][0];
                y1 = (int) triangle[1][1];
                x2 = (int) triangle[2][0];
                y2 = (int) triangle[2][1];
                g.drawPolyline(new int[]{x0, x1, x2}, new int[]{y0, y1, y2}, 3);

                //g.drawLine(x0, y0, x1, y1);
                //g.drawLine(x1, y1, x2, y2);
                //g.drawLine(x2, y2, x0, y0);
            }


        }
        */
    }




    public void TexturedTriangle(int x1, int y1, float u1, float v1, float w1,
                                 int x2, int y2, float u2, float v2, float w2,
                                 int x3, int y3, float u3, float v3, float w3,
                                 Texture texture, float lum){

        int width = jpanel.getWidth();
        int height = jpanel.getHeight();

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
                for (int j = ax; j < bx; j++){
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if(tex_w > pDepthBuffer[i][j]) {
                        tex_u = (1.0f - t) * tex_su + t * tex_eu;
                        tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                        color = texture.getPixel(tex_u / tex_w, tex_v / tex_w);
                        cR = (color >> 16) & 0xFF;
                        cG = (color >> 8) & 0xFF;
                        cB = color & 0xFF;

                        setPixel(j,i,(int) (cR*lum),(int) (cG*lum),(int) (cB*lum) );
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
            for (int i = Math.max(y2,0); i < Math.min(y3, height); i++) {
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
                for (int j = ax; j < bx; j++) {
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if(tex_w > pDepthBuffer[i][j]) {
                        tex_u = (1.0f - t) * tex_su + t * tex_eu;
                        tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                        color = texture.getPixel(tex_u / tex_w, tex_v / tex_w);
                        cR = (color >> 16) & 0xFF;
                        cG = (color >> 8) & 0xFF;
                        cB = color & 0xFF;
                        setPixel(j,i,(int) (cR*lum),(int) (cG*lum),(int) (cB*lum) );
                        pDepthBuffer[i][j] = tex_w;
                    }
                    t += tstep;
                }

            }

        }
    };
}
