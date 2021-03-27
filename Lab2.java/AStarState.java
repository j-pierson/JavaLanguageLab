/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/

import java.util.*;

public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    /** This is a HashMap that will store the available
        waypoints for pathmaking **/
    private HashMap<Location, Waypoint> OpenWaypoints
            = new HashMap<Location, Waypoint>();

    /** This is a HashMap that will store the waypoints
        eliminated during pathmaking **/
    private HashMap<Location, Waypoint> ClosedWaypoints
            = new HashMap<Location, Waypoint>();

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");
        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()
    {
      /** Checking if there are any open waypoints **/
      if (numOpenWaypoints() == 0) {
        return null;
      }
      /** Creating an iterator to cycle through the open waypoints **/
      Iterator<Waypoint> iter = OpenWaypoints.values().iterator();
      Waypoint min = iter.next();
      /** Looping through the iterator and storing the current waypoint as min
      if the total cost is less than the current minimum **/
      while (iter.hasNext()) {
        Waypoint wp = iter.next();
        if (wp.getTotalCost() < min.getTotalCost()) {
          min = wp;
        }
      }
      /** Return the waypoint with the least total cost **/
      return min;
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)
    {
        /** Check whether the open waypoints map contains a waypoint at the same
        location as the one that was passed **/
        if (! (OpenWaypoints.containsKey(newWP.getLocation()))) {
          /** Adding the waypoint to the HashMap **/
          OpenWaypoints.put(newWP.getLocation(), newWP);
          return true;
        }
        /** Check whether the waypoint currently in the map at the same location
        has a higher previous cost **/
        else if (OpenWaypoints.get(newWP.getLocation()).getPreviousCost() >
                 newWP.getPreviousCost()) {
                   /** Adding the waypoint to the HashMap **/
                   OpenWaypoints.put(newWP.getLocation(), newWP);
                   return true;
                 }
        /** If this is reached, the waypoint does not need to be added **/
        return false;
    }


    /** Returns the current number of open waypoints. **/
    public int numOpenWaypoints()
    {
        return OpenWaypoints.size();
    }


    /**
     * This method moves the waypoint at the specified location from the
     * open HashMap to the closed HashMap.
     **/
    public void closeWaypoint(Location loc)
    {
      /** Create a waypoint for the passed location **/
      Waypoint wp = OpenWaypoints.get(loc);
      /** Remove from the open HashMap **/
      OpenWaypoints.remove(loc);
      /** Add to the cloased HashMap **/
      ClosedWaypoints.put(loc, wp);
    }

    /**
     * Returns true if the closed waypoints HashMap contains a waypoint
     * for the specified location, and false if it does not.
     **/
    public boolean isLocationClosed(Location loc)
    {
      return ClosedWaypoints.containsKey(loc);
    }
}
