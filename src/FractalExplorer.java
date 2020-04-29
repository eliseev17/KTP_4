import sourceFile.FractalGenerator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class FractalExplorer {
    private JImageDisplay image;
    private FractalGenerator fracGen;
    private Rectangle2D.Double range;

    public FractalExplorer() {
        fracGen = new Mandelbrot();
        range = new Rectangle2D.Double(0, 0, 0, 0);
        fracGen.getInitialRange(range);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal");
        JButton rButton = new JButton("Reset");
        image = new JImageDisplay();
        rButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fracGen.getInitialRange(range);
                drawFractal();
            }
        });
        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                double xCoord = fracGen.getCoord(range.x, range.x + range.width, JImageDisplay.WIDTH, x);
                int y = e.getY();
                double yCoord = fracGen.getCoord(range.y, range.y + range.height, JImageDisplay.HEIGHT, y);
                fracGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
                drawFractal();
            }
        });
        frame.add(image, BorderLayout.CENTER);
        frame.add(rButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public int calculatePoint(double x, double y) {
        double cx = x;
        double cy = y;
        int i = 0;
        for (; i < JImageDisplay.ITERATIONS; i++) {
            double nx = x * x - y * y + cx;
            double ny = 2 * x * y + cy;
            x = nx;
            y = ny;
            if (x * x + y * y > 4) break;
        }
        if (i == JImageDisplay.ITERATIONS) return 0x00000000;
        return Color.HSBtoRGB((float) i / (float) JImageDisplay.ITERATIONS, 0.5f, 1f);
    }

    private void drawFractal() {
        for (int x = 0; x < JImageDisplay.WIDTH; x++) {
            for (int y = 0; y < JImageDisplay.HEIGHT; y++) {
                double xCoord = fracGen.getCoord(range.x, range.x + range.width, JImageDisplay.WIDTH, x);
                double yCoord = fracGen.getCoord(range.y, range.y + range.width, JImageDisplay.HEIGHT, y);
                int color = calculatePoint(xCoord, yCoord);
                image.drawPixel(x, y, color);
            }
        }
        image.repaint();
    }

    /**
     * Run the application.
     **/

    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer();
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
}