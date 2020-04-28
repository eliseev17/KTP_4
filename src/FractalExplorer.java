import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.text.Position;

public class FractalExplorer {
    private int displaySize;
    private JImageDisplay image;
    private FractalGenerator fracGen;
    private Rectangle2D.Double range;

    public FractalExplorer(int dispSize) {
        displaySize = dispSize;
        fracGen = new Mandelbrot();
        range = new Rectangle2D.Double(0, 0, 0, 0);
        fracGen.getInitialRange(range);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal");
        JButton rButton = new JButton("Reset");
        image = new JImageDisplay(displaySize, displaySize);
        rButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fracGen.getInitialRange(range);
				drawFractal();
			}
		});
        image.addMouseListener(new MouseHandler());
        frame.add(image, BorderLayout.CENTER);
        frame.add(rButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        for (int x = 0; x < displaySize; x++) {
            for (int y = 0; y < displaySize; y++) {
                double xCoord = fracGen.getCoord(range.x, range.x + range.width, displaySize, x);
                double yCoord = fracGen.getCoord(range.y, range.y + range.width, displaySize, y);
                double nIter = fracGen.numIterations(xCoord, yCoord);
                if (nIter == -1) {
                    image.drawPixel(x, y, 0);
                } else {
                    double hue = 0.7f / (yCoord / xCoord + (0.2 * xCoord / nIter)) + (float) (nIter / 200f) + (float) ((xCoord * yCoord - nIter / ((xCoord / yCoord) * (xCoord - yCoord))) / yCoord);
                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
                    image.drawPixel(x, y, rgbColor);
                }
            }
        }
        image.repaint();
    }

    /**
     * Simple handler to zoom in on the clicked pixel.
     **/
    public class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            double xCoord = fracGen.getCoord(range.x, range.x + range.width, displaySize, x);
            int y = e.getY();
            double yCoord = fracGen.getCoord(range.y, range.y + range.height, displaySize, y);
            fracGen.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
            drawFractal();
        }
    }

    /**
     * Run the application.
     **/
    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer(500);
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }
}