/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    /** Check whether two locations are equal **/
    @Override
    public boolean equals(Object obj) {
      /** Verify that obj is a Location**/
      if (obj instanceof Location) {
        /** Cast o to a location and store as new variable other **/
        Location loc = (Location) obj;
        /** Check for equality **/
        if (xCoord == loc.xCoord && yCoord == loc.yCoord) {
              return true;
            }
      }
      /** If this point is reached, the locations are not equal or the passed
      object is not a location **/
      return false;
    }

    /** Compute a hashcode for the location **/
    @Override
    public int hashCode()
    {
      /** Some prime number **/
      int result = 17;

      /** Operation to give pseudo-unique code based on fields **/
      result = 37 * result + xCoord;
      result = 37 * result + yCoord;

      return result;
    }
}
