import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

/** Class to create instance of a displaying containing a buffered image, and
various painting operations to chance and modify the image **/

public class JImageDisplay extends JComponent {

  public BufferedImage image;

  /** Constructor the initiaze the display with a buffered image **/
  public JImageDisplay(int width, int height) {
    /** Initialize the image **/
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    /** Set display dimensions from parameters **/
    Dimension dim = new Dimension(width, height);
    super.setPreferredSize(dim);
  }

  /** Method to paing an image to the display **/
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
  }

  /** Method to clear the dispay, replacing the image with all pixels of no
  color **/
  public void clearImage(){
    /** Getting height and width of the displayed image **/
    int w = image.getWidth();
    int h = image.getHeight();
    /** Calculate total pixels **/
    int total_pixels = w * h;
    /** Create an array to hold all the pixels **/
    int[] colors = new int[total_pixels];
    int i = 0;
    /** Loop through each coordinate in the image and set the correct location
    in the array to rgb value of 0 **/
    for (int x = 0; x < w; x++) {
      for (int y = 0; y < h; y++) {
        colors[i] = 0;
        i++;
      }
    }
    /** Use the array to set the color of each pixel **/
    image.setRGB(image.getMinX(), image.getMinY(), w, h, colors, 0, w);
  }

  /** Method to draw a color at a certain pixel **/
  public void drawPixel(int x, int y, int rgbColor) {
    image.setRGB(x, y, rgbColor);
  }
}
