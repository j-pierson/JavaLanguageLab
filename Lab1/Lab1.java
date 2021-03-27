import java.io.*;

/***
This is a class that will prompt three 3d points from a user, and then compute
and print the area contained within the triange formed by the three points.
***/
public class Lab1 {
  public static void main(String[] args) {
    /*** double x, y, and z : these are fields that will temporarily hold the
                              components of a 3d point
         int i : iterator
         Point3d p1, p2, and p3 : Point3d objects that will be created by the
                                  three inputted doubles
    ***/
    double x;
    double y;
    double z;
    int i;
    Point3d p1 = new Point3d();
    Point3d p2 = new Point3d();
    Point3d p3 = new Point3d();
    for (i = 0; i < 3; i++) {
      System.out.println("Enter the x component for point " + (i + 1) + ":");
      x = getDouble();
      System.out.println("Enter the y component for point " + (i + 1) + ":");
      y = getDouble();
      System.out.println("Enter the z component for point " + (i + 1) + ":");
      z = getDouble();
      if (i == 0) {
        p1 = new Point3d(x, y, z);
      }
      if (i == 1) {
        p2 = new Point3d(x, y, z);
      }
      if (i == 2) {
        p3 = new Point3d(x, y, z);
      }
    }
    if (p1.equals(p2) || p2.equals(p3) || p3.equals(p1)) {
      System.out.println("You have entered duplicate points. Area can not be"
                          + " computed.");
    }
    else {
    double area = getArea(p1, p2, p3);
    System.out.println(area);
    }
  }

  /**
    * Obtains one double-precision floating point number from
    * standard input.
    *
    * @return return the inputted double, or 0 on error.
    */
   public static double getDouble() {

       // There's potential for the input operation to "fail"; hard with a
       // keyboard, though!
       try {
           // Set up a reader tied to standard input.
           BufferedReader br =
               new BufferedReader(new InputStreamReader(System.in));

           // Read in a whole line of text.
           String s = br.readLine();

           // Conversion is more likely to fail, of course, if there's a typo.
           try {
               double d = Double.parseDouble(s);

               //Return the inputted double.
               return d;
           }
           catch (NumberFormatException e) {
               // Bail with a 0.  (Simple solution for now.)
               return 0.0;
           }
       }
       catch (IOException e) {
           // This should never happen with the keyboard, but we'll
           // conform to the other exception case and return 0 here,
           // too.
           return 0.0;
       }
   }

    /***
    Uses Herron's formula to compute the area contained within a triangle
    ***/
    public static double getArea(Point3d p1, Point3d p2, Point3d p3) {
      double side1 = p1.distanceTo(p2);
      double side2 = p2.distanceTo(p3);
      double side3 = p3.distanceTo(p1);
      double s = (side1 + side2 + side3) / 2;
      double area = Math.sqrt(s * (s - side1) * (s - side2) * (s - side3));
      return area;
    }
}
