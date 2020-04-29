import sourceFile.FractalGenerator;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    public int numIterations(double x, double y) {
        int iter = 0;
        double re = 0;
        double im = 0;
        while (iter < MAX_ITERATIONS && re * re + im * im < 4) {
            double reUpdated = re * re - im * im + x;
            double imUpdated = -2 * re * im + y;
            re = reUpdated;
            im = imUpdated;
            iter++;
        }
        if (iter == MAX_ITERATIONS) {
            return -1;
        }
        return iter;
    }

    public String toString() {
        return "Tricorn";
    }
}