import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;

/** This is a class that contains all the information for a Tricorn object,
which is an extension of the FractalGenerator class **/

public class Tricorn extends FractalGenerator{

  /** Value to cap our computations for numIterations **/
  public static final int MAX_ITERATIONS = 2000;

  /** This sets the range of a Rectangle2D object so that the fractal is best
  viewed **/
  public void getInitialRange(Rectangle2D.Double range) {
    range.x = -2;
    range.y = -2;
    range.width = 4;
    range.height = 4;
  }

  /** This is a computation that determines the color of each pixel in the
  display accoring to the function z_n = (conj(z_(n-1)))^2 + c
  where c is the location of the pixel **/
  public int numIterations(double re, double im) {
    double z = 0;
    int i = 0;
    double x = re;
    double y = im;
    double nextre;
    double nextim;
    while (i < MAX_ITERATIONS && z <= 4) {
      /** Computing the real and imaginary parts of the next iteration **/
      nextre = ((re * re) - (im * im)) + x;
      nextim = -2 * re * im + y;

      re = nextre;
      im = nextim;

      /** Computing the norm of the next iteration **/
      z = im * im + re * re;
      i++;
    }
    /** Making sure the iterations stop at MAX_ITERATIONS **/
    if (i == MAX_ITERATIONS) {
      return -1;
    }
    /** If max isn't reached, return number of iterations **/
    return i;
  }

  /** Will be used to get the name for items in a JComboBox **/
  @Override
  public String toString() {
    return "Tricorn";
  }
}
