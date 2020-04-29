import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class JImageDisplay extends JComponent {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private BufferedImage image;

    JImageDisplay() {
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public BufferedImage getImage() {
        return image;
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
    }

    public void drawPixel(int x, int y, int rgbColor) {
        image.setRGB(x, y, rgbColor);
    }
}