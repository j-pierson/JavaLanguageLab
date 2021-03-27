import java.awt.geom.Rectangle2D;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/** This is a class that holds the main function for the app, and it also
contains methods for user interation with the app, and the structure of the
user interface **/

public class FractalExplorer {

  private int display_size;
  private JImageDisplay display;
  private FractalGenerator fractal;
  private Rectangle2D.Double range;
  private int rows_remaining;
  private JButton button;
  private JButton button2;
  private JComboBox box;

  /** Constuctor for the FractalExplorer object **/
  public FractalExplorer(int size) {
    /** Initializing fields for FractalExplorer **/
    display_size = size;
    fractal = new Mandelbrot();
    Rectangle2D.Double rect = new Rectangle2D.Double();
    fractal.getInitialRange(rect);
    range = rect;
  }

  /** Method to construct the GUI using Swing objects and display the interface
  in the app **/
  public void createAndShowGUI() {
    /** Creating new JFrame and JImageDisplay items. The display is used to
    initialize the display field **/
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JImageDisplay disp = new JImageDisplay(display_size, display_size);
    display = disp;
    /** Adding a mouse listener to interact with clicks on the display **/
    display.addMouseListener(new MouseHandler());
    /** New JPanel objects to hold other objects together in display **/
    JPanel p = new JPanel();
    JPanel p2 = new JPanel();
    /** JComboBox to be added to a panel and allow fractal selection **/
    box = new JComboBox();
    /** Adding the various fractal options to the combo box **/
    box.addItem(new Mandelbrot());
    box.addItem(new Tricorn());
    box.addItem(new BurningShip());
    /** Adding an ActionHandler to do event when user interacts with box **/
    box.addActionListener(new ActionHandler());
    /** Adding a label to indicate what the box is for **/
    JLabel l = new JLabel("Fractal:");
    /** Adding a new Reset button **/
    button = new JButton("Reset");
    /** ActionHandler for interaction with Reset button **/
    button.addActionListener(new ActionHandler());
    /** Adding a new Save button **/
    button2 = new JButton("Save");
    /** ActionHandler for interaction with Save button **/
    button2.addActionListener(new ActionHandler());
    /** Adding all the above elements to the proper panels **/
    p.add(l);
    p.add(box);
    p2.add(button);
    p2.add(button2);
    /** Setting the frame to have border layout style **/
    frame.setLayout(new BorderLayout());
    /** Placing the objecting in the correct locations on the frame **/
    frame.add(display, BorderLayout.CENTER);
    frame.add(p, BorderLayout.NORTH);
    frame.add(p2, BorderLayout.SOUTH);
    frame.pack();
    /** Making the frame visible **/
    frame.setVisible(true);
    /** Not allowing the user to change the size of the frame **/
    frame.setResizable(false);
  }

  /** Method for drawing the fractal to the display **/
  private void displayFractal() {
    /** Temporarily disabling buttons and combo box **/
    enableUI(false);
    /** Value to keep track of progress in displaying fractal **/
    rows_remaining = display_size;
    /** Calling FractalWorker to compute and display the new image one row at a
    time **/
    for (int y = 0; y < display_size; y++) {
      FractalWorker worker = new FractalWorker(y);
      worker.execute();
    }

  }

  private class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      /** Getting what triggered the ActionHandler **/
      String cmd = e.getActionCommand();

      /** Checking if the action came from the combo box **/
      if (e.getSource() instanceof JComboBox) {
        /** Casting the source to a JComboBox **/
        JComboBox b = (JComboBox) e.getSource();
        /** Casting the selected item to a fractal object */
        fractal = (FractalGenerator) b.getSelectedItem();
        /** Readjusting the range of view for the frame **/
        fractal.getInitialRange(range);
        /** Displaying the newly selected fractal **/
        displayFractal();
      }
      /** Checking if the action came from the reset button **/
      else if (cmd.equals("Reset")) {
        /** Resetting the range and displaying the fractal **/
        fractal.getInitialRange(range);
        displayFractal();
      }
      /** Checking if the action came from the save button **/
      else if (cmd.equals("Save")) {
        /** Creating a new JFileChooser object **/
        JFileChooser chooser = new JFileChooser();
        /** Casting the source to a JButton **/
        JButton b = (JButton) e.getSource();
        /** Creating a new FileFilter object **/
        FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        /** Setting file filter and accept criteria for chooser **/
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        /** Showing the save diaglog to the JImageDisplay **/
        int temp = chooser.showSaveDialog(b.getParent().getParent());
        if (temp == JFileChooser.APPROVE_OPTION) {
          try {
            /** Try writing the file to desired file **/
            ImageIO.write(display.image, "png", chooser.getSelectedFile());
          }
          catch (IOException x) {
            /** Catch all IOExceptions **/
            JOptionPane.showMessageDialog(b.getParent().getParent(),
                                          x.getMessage(),
                                          "Cannot Save Image",
                                          JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    }
  }

  /** Class to hand all mouse click events **/
  private class MouseHandler extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      /** If in progess of display, do nothing **/
      if (rows_remaining != 0) {
        return;
      }
      /** Calculate complex values for coordinate **/
      double x = fractal.getCoord(range.x, range.x + range.width,
                                  display_size, e.getX());
      double y = fractal.getCoord(range.y, range.y + range.height,
                                  display_size, e.getY());
      /** Change the range to center around click and zoom in **/
      fractal.recenterAndZoomRange(range, x, y, 0.5);
      /** Display image at new range **/
      displayFractal();
    }
  }

  /** Class to perform display operations in background thread **/
  private class FractalWorker extends SwingWorker<Object, Object> {

    /** Field to hold row being calculated and an array to hold all x values in
    that row **/
    private int y;
    private int[] values;

    /** Constructor for FractalWorker initializing the row **/
    public FractalWorker(int val) {
      y = val;
    }

    /** Method to calculate the color of each pixel in the row **/
    public Object doInBackground() {
      /** Initializing the length of the array **/
      values = new int[display_size];
      /** Complexifying the row coordinate **/
      double newY = FractalGenerator.getCoord(range.y, range.y +
                                                range.height, display_size, y);
      /** Loop through row and compute color of each pixel **/
      for (int x = 0; x < display_size; x++) {
        /** Complexigy column coordinate **/
        double xCoord = FractalGenerator.getCoord(range.x, range.x +
                                                  range.width, display_size, x);
        /** Calculate number of iterations by fractal function **/
        int iter = fractal.numIterations(xCoord, newY);
        int rgbColor;
        /** Interations exceeded max, so it gets no color **/
        if (iter == -1) {
          rgbColor = 0;
        }
        else {
          /** Calculate the gue from the iterations **/
          float hue = 0.7f + (float) iter / 200f;
          /** Get rbg value from the hue **/
          rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
        }
        /** Place rbg color into appropriate location in array **/
        values[x] = rgbColor;
      }
      return null;
    }

    /** Method to function after doInBackground has been completed **/
    public void done() {
      /** Loop through row **/
      for (int x = 0; x < display_size; x++) {
        /** Draw the correct color in the correct pixel location **/
        display.drawPixel(x, y, values[x]);
      }
      /** Repaint the line of the display that has been drawn **/
      display.repaint(0, 0, y, display_size, 1);
      /** Reduce remaining rows to be painted **/
      rows_remaining--;
      /** If the painint is complete, reactivate the buttons and combo box **/
      if (rows_remaining == 0) {
        enableUI(true);
      }
    }
  }

  /** Method to either enable or disable the buttons and combo box **/
  public void enableUI(boolean val) {
    box.setEnabled(val);
    button.setEnabled(val);
    button2.setEnabled(val);
  }

  /** Main method to create the explorer instance, show the UI, and display the
  initial fractal **/
  public static void main(String[] args) {
    FractalExplorer explorer = new FractalExplorer(800);
    explorer.createAndShowGUI();
    explorer.displayFractal();
  }
}
