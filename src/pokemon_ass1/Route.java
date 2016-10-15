package pokemon_ass1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


/**
 * Store route from one point to another.
 */
public class Route implements Cloneable {

    /**
     * Construct a route with starting as the start point.
     * @param starting The starting point of the route.
     */
    public Route(Point starting) {
        route = new ArrayList<>();
        route.add(starting);
    }

    /**
     * @return An immutable list with he route between two points.
     */
    public List<Point> getPoints() {
        return Collections.unmodifiableList(route);
    }

    /**
     * @return Return the distance travelled with this route.
     */
    public int distance() {
        // Only changing is called distance.
        return route.size() - 1;
    }

    /**
     * Append a point to the last of the route.
     * @param next_point the next point of the route.
     * @throws PointNotNearException The next point provided is not next to
     * the last point of the route.
     */
    public void append(Point next_point) throws PointNotNearException {
        append(new Route(next_point));
    }

    /**
     * Append a route to the last of the route.
     * @param another Another route to append.
     * @throws PointNotNearException The next point provided is not equal to
     * or next to the last point of the route.
     */
    public void append(Route another) throws PointNotNearException {
        Point last_point = route.get(route.size() - 1);
        Point connection_point = another.route.get(0);
        int dx = connection_point.x - last_point.x;
        int dy = connection_point.y - last_point.y;
        int m_distance = Math.abs(dx) + Math.abs(dy);

        if (m_distance == 0) {
            // The starting of another route is the ending of this route.
            route.addAll(another.route.subList(1, another.route.size()));
        } else if (m_distance == 1) {
            // The starting of another route is next to this route.
            route.addAll(another.route);
        } else {
            // Invalid another route.
            throw new PointNotNearException();
        }
    }

    /**
     * @return All possible next points of this route.
     */
    public Point[] possibleNext() {
        Point last_point = route.get(route.size() - 1);
        return new Point[]{
                new Point(last_point.x + 1, last_point.y),
                new Point(last_point.x, last_point.y + 1),
                new Point(last_point.x - 1, last_point.y),
                new Point(last_point.x, last_point.y - 1),
        };
    }

    @Override
    public Route clone() {
        try {
            Route ret = (Route) super.clone();
            ret.route = new ArrayList<>(route);
            return ret;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }


    /**
     * This exception will be thrown if user tried to append a point that is
     * not next to the last point in the route.
     */
    class PointNotNearException extends Exception { }


    private ArrayList<Point> route;

}