package pokemon;

import java.awt.Point;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;


/**
 * An instance of Map will store the essential information of a map of the
 * pokemon game. Some essential methods are provided.
 *
 * Note that we can reduce cells to only the starting, destination and all
 * special cells like Station and Pokemon. Between the cells we always go with
 * the shortest path. This can improve performance.
 *
 * Once the map constructed, it will be immutable.
 */
public class Map {

    /**
     * Construct the immutable map, with starting and destination point.
     * Also cells should be specified, and is_wall array should be a
     * height * width array that is_wall[i][j] states (i,j) is a wall.
     * @param is_wall A height * width array indicates the walls.
     * @param sp_cells Array of special cells only Station and Pokemon.
     * @param starting The starting point for the player.
     * @param destination The destination point for the player.
     */
    public Map(boolean[][] is_wall, List<Cell> sp_cells,
               Cell starting, Cell destination) {
        this.height = is_wall.length;
        this.width = height > 0 ? is_wall[0].length : 0;
        this.is_wall = is_wall;

        this.sp_cells = new HashSet<>();
        for (Cell cell : sp_cells) {
            this.sp_cells.add(cell);
        }

        this.starting = starting;
        this.destination = destination;
        this.memo_route = new HashMap<>();
    }

    /**
     * Return the shortest route go from Cell a to Cell b.
     * @param a The starting cell.
     * @param b The destination cell.
     * @return The shortest path from a to b.
     * @throws NoPathException If no path between a and b.
     */
    public Route shortestPath(Cell a, Cell b) throws NoPathException {
        if (!memo_route.containsKey(a)) {
            // if no memo pad for source: a, create it
            memo_route.put(a, new HashMap<Cell, Route>());
        }
        HashMap<Cell, Route> memo_a = memo_route.get(a);

        // if no memo for a to b, calculate and store it.
        if (!memo_a.containsKey(b)) {
            try {
                // BFS to get the shortest path.
                memo_a.put(b, bfsSearch(a.getLocation(), b.getLocation()));
            } catch (NoPathException e) {
                memo_a.put(b, null);
            }
        }

        Route path = memo_a.get(b);
        if (path == null) {
            throw new NoPathException();
        } else {
            return path;
        }
    }

    /**
     * Return the shortest route using bfs go from Point a to Point b.
     * @param a The starting point.
     * @param b The ending point.
     * @return The shortest path from a to b.
     * @throws NoPathException If no path from a to b
     */
    private Route bfsSearch(Point a, Point b) throws NoPathException {
        HashMap<Point, Route> path_to = new HashMap<>();
        LinkedList<Point> bfs_queue = new LinkedList<>();

        path_to.put(new Point(a), new Route(a));
        bfs_queue.add(new Point(a));

        while (!bfs_queue.isEmpty()) {
            Point now = bfs_queue.remove();
            Route path = path_to.get(now);

            if (now.equals(b)) { // Got to destination.
                return path;
            }

            for (Point next : path.possibleNext()) {
                if (path_to.containsKey(next) || // already calculated
                        !isPassable(next)) {     // or point not accessible.
                    continue;
                }
                if (!destination.getLocation().equals(b) &&
                        destination.getLocation().equals(next)) {
                    // According to Valerio, The player cannot arrive
                    // at the destination more times in the same path.
                    continue;
                }
                Route next_path = path.clone();
                try {
                    next_path.append(next);
                } catch (Route.PointNotNearException e) {
                    throw new Error("Code ensured near.");
                }
                path_to.put(next, next_path);
                bfs_queue.add(next);
            }
        }

        // Cannot get to be from a
        throw new NoPathException();
    }

    /**
     * @param p A point.
     * @return True if p is passable in the map.
     */
    public boolean isPassable(Point p) {
        return p.x >= 0 && p.y >= 0 &&
                p.x < height && p.y < width &&
                !is_wall[p.x][p.y];
    }

    /**
     * @return Starting cell of the map.
     */
    public Cell getStarting() {
        return starting;
    }

    /**
     * @return Destination cell of the map.
     */
    public Cell getDestination() {
        return destination;
    }

    /**
     * @return Set of all special cells.
     */
    public Set<Cell> getSpecialCells() {
        return Collections.unmodifiableSet(sp_cells);
    }


    /**
     * There's not path between two cells.
     */
    public class NoPathException extends Exception {
        private static final long serialVersionUID = 1L;
    }


    private final int height, width;
    private final boolean[][] is_wall;

    private final HashSet<Cell> sp_cells;

    private final Cell starting, destination;

    // Internal memo pad for best route searching.
    private HashMap<Cell, HashMap<Cell, Route> > memo_route;

}
