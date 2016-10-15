package pokemon_ass1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Used for recording the status of the player, including the current
 * location, the caught Pokemon, the number of balls and the path visited.
 */
public class Player implements Cloneable {

    /**
     * Construct a player instance with given starting point.
     * @param starting The starting point of the player.
     */
    public Player(Point starting) {
        visited = new Route(starting);
        ball_count = 0;
        caught_pokemon = new ArrayList<>();
    }

    /**
     * @return Visited path of the player.
     */
    public Route getVisited() {
        return visited;
    }

    /**
     * @return The count of Pokemon balls the player has.
     */
    public int getBallCount() {
        return ball_count;
    }

    /**
     * @return The Pokemon the player caught.
     */
    public List<Pokemon> getCaughtPokemon() {
        return Collections.unmodifiableList(caught_pokemon);
    }

    /**
     * @return The types the player caught.
     */
    public HashSet<String> getCaughtTypes() {
        HashSet<String> caught_types = new HashSet<>();
        for (Pokemon p : caught_pokemon) {
            caught_types.add(p.getType());
        }
        return caught_types;
    }

    /**
     * @return The max compact value caught.
     */
    public int getMaxCompact() {
        int max_compact = 0;
        for (Pokemon p : caught_pokemon) {
            max_compact = Math.max(max_compact, p.getCombat());
        }
        return max_compact;
    }

    /**
     * @return The score value for the player.
     * The scoring function is defined in assignment PDF.
     */
    public int getScore() {
        return ball_count + 5 * caught_pokemon.size() +
                10 * getCaughtTypes().size() + getMaxCompact() -
                visited.distance();
    }

    /**
     * Add a Pokemon to the list of caught Pokemon. If already caught,
     * ignore the action. Also, reduce the ball count according to the Pokemon.
     * @param p The Pokemon to add into the caught list.
     * @throws BallNotSufficientException If ball not sufficient to catch.
     */
    public void catchPokemon(Pokemon p) throws BallNotSufficientException {
        if (caught_pokemon.contains(p)) return;
        int required_balls = p.getRequiredBalls();
        if (ball_count < required_balls) {
            throw new BallNotSufficientException();
        } else {
            ball_count -= required_balls;
            caught_pokemon.add(p);
        }
    }

    /**
     * Add Pokemon balls
     * @param delta Adding count of balls.
     */
    public void addBalls(int delta) {
        ball_count += delta;
    }

    /**
     * Move the player via the specified route.
     * @param via The route to move the player.
     * @throws Route.PointNotNearException if invalid route to move.
     */
    public void move(Route via) throws Route.PointNotNearException {
        visited.append(via);
    }

    @Override
    public Player clone() {
        try {
            Player ret = (Player) super.clone();
            ret.visited = visited.clone();
            ret.caught_pokemon = new ArrayList<>(caught_pokemon);
            return ret;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }


    /**
     * The balls are not sufficient to catch one pokemon.
     */
    class BallNotSufficientException extends Exception {}


    private Route visited;
    private int ball_count;
    private ArrayList<Pokemon> caught_pokemon;

}
