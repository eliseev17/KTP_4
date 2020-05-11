import sourceFile.FractalGenerator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;

public class FractalExplorer {
    private final JImageDisplay imageDisplay;
    private FractalGenerator fractal;
    private Rectangle2D.Double range;
    private int rowsRemaining;
    JComboBox comboBox;
    JButton resetButton;
    JButton saveButton;

    public FractalExplorer() {
        imageDisplay = new JImageDisplay();
        fractal = new Mandelbrot();
        range = new Rectangle2D.Double();
        fractal.getInitialRange(range);
    }

    private class FractalWorker extends SwingWorker<Object, Object> {
        int yCoordinate;
        int[] rgbValue;

        public FractalWorker(int yCoordinate) {
            this.yCoordinate = yCoordinate;
        }

        @Override
        protected Object doInBackground() {
            rgbValue = new int[1000000];
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, JImageDisplay.HEIGHT, yCoordinate);
            for (int x = 0; x < JImageDisplay.WIDTH; x++) {
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, JImageDisplay.WIDTH, x);
                int nIter = fractal.numIterations(xCoord, yCoord);
                if (nIter == -1) {
                    rgbValue[x] = 0;
                } else {
                    double hue = 0.7f + (float) nIter / 200f;
                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
                    rgbValue[x] = rgbColor;
                }
            }
            return null;
        }

        @Override
        protected void done() {
            for (int x = 0; x < JImageDisplay.WIDTH; x++) {
                imageDisplay.drawPixel(x, yCoordinate, rgbValue[x]);
            }
            imageDisplay.repaint(0, yCoordinate, JImageDisplay.WIDTH, 1);
            rowsRemaining--;
            if (rowsRemaining == 0) {
                enableUI(true);
            }
        }
    }

    public void enableUI(boolean flag) {
        saveButton.setEnabled(flag);
        resetButton.setEnabled(flag);
        comboBox.setEnabled(flag);
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Fractal Explorer");
        resetButton = new JButton("Reset Display");
        comboBox = new JComboBox();
        FractalGenerator mandelbrot = new Mandelbrot();
        comboBox.addItem(mandelbrot);
        FractalGenerator tricorn = new Tricorn();
        comboBox.addItem(tricorn);
        FractalGenerator burningShip = new BurningShip();
        comboBox.addItem(burningShip);
        //ButtonHandler fractalChooser = new ButtonHandler();
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JComboBox) {
                    JComboBox mySource = (JComboBox) e.getSource();
                    fractal = (FractalGenerator) mySource.getSelectedItem();
                    fractal.getInitialRange(range);
                    drawFractal();
                }
            }
        });
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Fractal:");
        panel.add(label);
        panel.add(comboBox);
        frame.add(panel, BorderLayout.NORTH);
        saveButton = new JButton("Save");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveButton);
        bottomPanel.add(resetButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser myFileChooser = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                myFileChooser.setFileFilter(filter);
                myFileChooser.setAcceptAllFileFilterUsed(false);
                int userSelection = myFileChooser.showSaveDialog(imageDisplay);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File file = myFileChooser.getSelectedFile();
                    try {
                        BufferedImage displayImage = imageDisplay.getImage();
                        ImageIO.write(displayImage, "png", file);
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(imageDisplay,
                                exception.getMessage(), "Cannot Save Image",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.getInitialRange(range);
                drawFractal();
            }
        });
        imageDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (rowsRemaining != 0) return;
                int x = e.getX();
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, JImageDisplay.WIDTH, x);
                int y = e.getY();
                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, JImageDisplay.HEIGHT, y);
                fractal.recenterAndZoomRange(range, xCoord, yCoord, 0.5);
                drawFractal();
            }
        });
        frame.add(imageDisplay, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal() {
        enableUI(false);
        rowsRemaining = JImageDisplay.HEIGHT;
        for (int y = 0; y < JImageDisplay.HEIGHT; y++) {
            FractalWorker worker = new FractalWorker(y);
            worker.execute();
        }
//        for (int x = 0; x < JImageDisplay.WIDTH; x++) {
//            for (int y = 0; y < JImageDisplay.HEIGHT; y++) {
//                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, JImageDisplay.WIDTH, x);
//                double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, JImageDisplay.HEIGHT, y);
//                int nIter = fractal.numIterations(xCoord, yCoord);
//                if (nIter == -1) {
//                    imageDisplay.drawPixel(x, y, 0);
//                } else {
//                    double hue = 0.7f + (float) nIter / 200f;
//                    int rgbColor = Color.HSBtoRGB((float) hue, 1f, 1f);
//                    imageDisplay.drawPixel(x, y, rgbColor);
//                }
////                другой способ
////                imageDisplay.drawPixel(x, y, calculatePoint(xCoord, yCoord));
//            }
//        }
//        imageDisplay.repaint();
    }

    public static void main(String[] args) {
        FractalExplorer fracExp = new FractalExplorer();
        fracExp.createAndShowGUI();
        fracExp.drawFractal();
    }

    // метод для красивого изображения
//    public static int calculatePoint(double x, double y) {
//        double cx = x;
//        double cy = y;
//        int i = 0;
//        for (; i < Mandelbrot.MAX_ITERATIONS; i++) {
//            double nx = x * x - y * y + cx;
//            double ny = 2 * x * y + cy;
//            x = nx;
//            y = ny;
//            if (x * x + y * y > 4) break;
//        }
//        if (i == Mandelbrot.MAX_ITERATIONS) return 0x00000000;
//        return Color.HSBtoRGB((float) i / (float) Mandelbrot.MAX_ITERATIONS, 0.5f, 1f);
//    }
}