import sourceFile.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;
    }

    @Override
    public int numIterations(double x, double y) {
        double re = x;
        double im = y;
        int iter = 0;
        while ((iter < MAX_ITERATIONS)) {
            iter = iter + 1;
            double re2 = x * x - y * y + re;
            double im2 = 2 * x * y + im;
            x = re2;
            y = im2;
            if ((x * x + y * y) > 4) {
                break;
            }
        }
        if (iter == MAX_ITERATIONS) {
            return -1;
        }
        return iter;
    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}