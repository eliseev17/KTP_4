import sourceFile.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    public int numIterations(double x, double y) {
        double re = x;
        double im = y;
        boolean flag = true;
        int iter = 0;
        while ((iter < MAX_ITERATIONS) && (flag)) {
            iter = iter + 1;
            re = re * re - im * im;
            im = im * re * 2;
            if ((re * re + im * im) < 4) {
                flag = false;
                break;
            }
        }
        if (flag == true) {
            return -1;
        } else return iter;
    }
}