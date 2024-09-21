import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Screen extends JPanel {
    public ArrayList<Engine.TriangleMesh> TrianglesToRaster;

    public Screen() {
        TrianglesToRaster = new ArrayList<>();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        ArrayList<Engine.TriangleMesh> trianglesToRaster = TrianglesToRaster;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight()); // Effacer le contenu de la fenêtre

        int x0;
        int y0;
        int x1;
        int y1;
        int x2;
        int y2;

        for( Engine.TriangleMesh triangleMesh : trianglesToRaster){
            float[][] triangle = triangleMesh.t;
            x0 = (int) triangle[0][0];
            y0 = (int) triangle[0][1];
            x1 = (int) triangle[1][0];
            y1 = (int) triangle[1][1];
            x2 = (int) triangle[2][0];
            y2 = (int) triangle[2][1];
            int[] triangleColor = triangleMesh.color;
            g.setColor(new Color(triangleColor[0], triangleColor[1], triangleColor[2]));

            g.fillPolygon(new int[]{x0, x1, x2},new int[]{y0, y1, y2},3);
            g.setColor(Color.BLACK);
            g.setColor(Color.WHITE);

        }
    }

}
