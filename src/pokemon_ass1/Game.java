package pokemon_ass1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * This the entry class of the whole program, which contains the main
 * function. It should contain a Map and a Player (from assignment PDF).
 *
 * The input and output functionality is moved into GameMapFile.java and
 * GameOutputFile.java
 */
public class Game {

    /**
     * The main entry of this assignment.
     * @param args Argument of this program. [0] should be input file and [1]
     *             should be output file.
     * @throws Exception Not handling any exception.
     */
    public static void main(String[] args) throws Exception {
        GameMapFile input_file = new GameMapFile("./sampleIn.txt");
        GameOutputFile output_file = new GameOutputFile("./sampleOut.txt");

        if (args.length > 0) {
            input_file = new GameMapFile(args[0]);
        }

        if (args.length > 1) {
            output_file = new GameOutputFile(args[1]);
        }

        Game game = new Game(input_file.read());
        output_file.save(game.getBestPlayer());
    }

    /**
     * Construct a Game with map provided.
     * @param map The map of this game.
     */
    public Game(Map map) {
        game_map = map;
        best_player = null;  // lazy calculated
        memo_best_player = new HashMap<>();
    }

    /**
     * Get the best player of the game.
     * @return The best player with best score.
     * @throws NotAccessibleException If there's no solution at all.
     */
    public Player getBestPlayer() throws NotAccessibleException {
        // If not calculated:
        if (best_player == null) {
            // Calculate the best player.

            // Enumerate all possibilities of visiting cells.
            for (Set<Cell> to_visit : getAllToVisit()) {
                try {
                    Player player = getBestPlayerTill(
                            game_map.getDestination(), to_visit);
                    if (best_player == null ||
                            best_player.getScore() < player.getScore()) {
                        // Got a better player.
                        best_player = player;
                    }
                } catch (NotAccessibleException e) {
                    // There's cells impossible to visit.
                    // Just ignore this case.
                }
            }
        }

        if (best_player != null) {
            return best_player;
        } else {
            // Impossible to go from starting to destination.
            throw new NotAccessibleException();
        }
    }

    /**
     * @return All possibilities of visiting cells. For example, {a, b, c} we
     * have [{}, {a}, {b}, {c}, {a, b}, {a, c}, {b, c}, {a, b, c}]
     */
    private ArrayList<Set<Cell> > getAllToVisit() {
        ArrayList<Cell> all = new ArrayList<>(game_map.getSpecialCells());

        ArrayList<Set<Cell> > list = new ArrayList<>();

        // Enumerate all possibilities with bits.
        for (long ind = (1L << all.size()) - 1; ind >= 0; ind--) {
            HashSet<Cell> current = new HashSet<>();
            for (int i = 0; i < all.size(); i++) {
                // If one bit set, the corresponding cell to visit:
                if ((ind & (1L << i)) != 0) {
                    current.add(all.get(i));
                }
            }
            list.add(current);
        }

        return list;
    }

    /**
     * Get the best player from starting cell to the cell indicate by till.
     * On the way to till, player must visit all cells in to_visit.
     * The calculation is memo-pad-ized.
     * @param till The destination for the player to get.
     * @param to_visit The cells the player have to visit.
     * @return The best player with the conditions. Best means maximized score.
     * @throws NotAccessibleException It's not possible to visit all cells,
     * or we can't get to destination from the starting point.
     */
    private Player getBestPlayerTill(Cell till, Set<Cell> to_visit)
            throws NotAccessibleException {
        if (!memo_best_player.containsKey(till)) {
            // If no memo pad for till, create it
            memo_best_player.put(till, new HashMap<Set<Cell>, Player>());
        }
        HashMap<Set<Cell>, Player> memo_till = memo_best_player.get(till);

        // If no memo to to_visit:
        if (!memo_till.containsKey(to_visit)) {
            // Calculate the best player.
            Player sol = to_visit.isEmpty() ?
                    searchBestPlayerFromStarting(till) :
                    searchBestPlayerNonEmpty(till, to_visit);

            // And store to the memo pad.
            HashSet<Cell> key = new HashSet<>(to_visit);
            memo_till.put(key, sol);
        }

        Player sol = memo_till.get(to_visit);
        if (sol != null) {
            // Clone to avoid changing.
            return sol.clone();
        } else {
            // Impossible to finish.
            throw new NotAccessibleException();
        }
    }

    /**
     * Get the best player from starting to the cell indicated by till directly.
     * "Directly" means no via cells.
     * @param till The destination for the player to get.
     * @return The best player. Null if impossible.
     */
    private Player searchBestPlayerFromStarting(Cell till) {
        try {
            // Directly come from the starting
            Cell starting = game_map.getStarting();
            Player p = new Player(starting.getLocation());

            p.move(game_map.shortestPath(starting, till));
            till.affectPlayer(p);

            return p;
        } catch (Route.PointNotNearException e) {
            throw new Error("Code ensured near.");
        } catch (Map.NoPathException e) {
            // Impossible to get to this cell.
            return null;
        }
    }

    /**
     * Get the best player from starting to the cell indicated by till with
     * searching. Player must visit all cells in to_visit.
     * @param till The destination for the player to get.
     * @param to_visit The cells the player have to visit.
     * @return The best player with the conditions. Null if impossible.
     */
    private Player searchBestPlayerNonEmpty(Cell till, Set<Cell> to_visit) {
        Player sol = null;

        try {
            for (Cell prev_hop : to_visit) {
                // Come from previous hop.
                HashSet<Cell> new_to_visit = new HashSet<>(to_visit);
                new_to_visit.remove(prev_hop);

                Player p = getBestPlayerTill(prev_hop, new_to_visit);

                p.move(game_map.shortestPath(prev_hop, till));
                till.affectPlayer(p);

                if (sol == null || sol.getScore() < p.getScore()) {
                    // Got a better solution.
                    sol = p;
                }
            }
        } catch (Route.PointNotNearException e) {
            throw new Error("Code ensured near.");
        } catch (Map.NoPathException e) {
            return null;
        } catch (NotAccessibleException e) {
            return null;
        }

        return sol;
    }


    /**
     * This exception indicates that a gold to be impossible. Like impossible
     * to get from starting to destination. Or impossible to access all cells
     * required.
     */
    public class NotAccessibleException extends Exception {
        private static final long serialVersionUID = 1L;
    }


    private final Map game_map;

    private Player best_player;
    private HashMap<Cell, HashMap<Set<Cell>, Player> > memo_best_player;

}
