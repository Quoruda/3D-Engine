package Display;

import Inputs.KeyMap;
import Inputs.KeyPressedListener;
import Objects.Camera;
import Objects.Texture;
import Rendering.Engine;
import Rendering.Triangle;



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        jpanel.setBackground(Color.black);


        jframe = new JFrame();
        int width = 700;
        int height = 600;
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(width, height);
        jframe.add(jpanel);
        jframe.setVisible(true);
        jframe.setFocusable(true);
        jframe.requestFocusInWindow();

        KeyListener keyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                keyMap.pressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyMap.released(e.getKeyCode());
            }
        };
        jframe.addKeyListener(keyListener);
    }

    @Override
    public void update() {
        jpanel.repaint();
    }

    public void setPixel(int x, int y, int r, int g, int b){
        frame.setRGB(x,y,(r << 16) | (g << 8) | b);
    }

    public void paintTriangle(Graphics g) {
        BufferedImage frame = this.frame;
        if(frame == null){
            frame = new BufferedImage(jframe.getWidth(), jframe.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        int width, height;
        float ratio = (float)frame.getHeight()/(float)frame.getWidth();
        width = jframe.getWidth();
        height = (int)(width*ratio);
        if(height > jframe.getHeight()){
            height = jframe.getHeight();
            width = (int)(height/ratio);
        }

        int x = (jframe.getWidth()-width)/2;
        int y = (jframe.getHeight()-height)/2;
        g.drawImage(frame, x,y, width, height, jpanel );

    }

    public void setResolution(int width, int height){
        jframe.setSize(width, height);
    }

}
