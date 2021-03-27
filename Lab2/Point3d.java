/**
 * A three-dimensional point class.
 */
public class Point3d {

    /** X coordinate of the point */
    private double xCoord;

    /** Y coordinate of the point */
    private double yCoord;

    /** Z coordinate of the point */
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. */
    public Point3d(double x, double y, double z) {
      xCoord = x;
      yCoord = y;
      zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. */
    public Point3d() {
      // Call three-argument constructor and specify the origin.
      this(0, 0, 0);
    }

    /** Return the X coordinate of the point. */
    public double getX() {
      return xCoord;
    }

    /** Return the Y coordinate of the point. */
    public double getY() {
      return yCoord;
    }

    /** Return the Z coordinate of the point. */
    public double getZ() {
      return zCoord;
    }

    /** Set the X coordinate of the point. */
    public void setX(double val) {
      xCoord = val;
    }

    /** Set the Y coordinate of the point. */
    public void setY(double val) {
      yCoord = val;
    }

    /** Set the Z coordinate of the point. */
    public void setZ(double val) {
      zCoord = val;
    }

    /** Check whether two points are equal **/
    public boolean equals(Object o) {
      /** Verify that obj is a Point3d **/
      if (o instanceof Point3d) {
        /** Cast obj to other **/
        Point3d other = (Point3d) o;
        /** Check for equality **/
        if (xCoord == other.getX() && yCoord == other.getY() &&
            zCoord == other.getZ()) {
              return true;
            }
      }
      return false;
    }

    /*** Calculates the distance between two points using distance formula ***/
    public double distanceTo(Point3d obj) {
      /** Cast obj to other **/
      Point3d other = (Point3d) obj;
      /** Calculate distance **/
      double dist =  Math.sqrt((xCoord - other.getX()) * (xCoord - other.getX()) +
                              (yCoord - other.getY()) * (yCoord - other.getY()) +
                              (zCoord - other.getZ()) * (zCoord - other.getZ()));
      return dist;
    }
}
