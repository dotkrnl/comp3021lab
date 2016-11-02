package pokemon;

import java.awt.Point;


/**
 * Class Pokemon is a special type of cell. Besides recording the location
 * of the cell, it also records the information of the properties of a
 * Pokemon, including the name, type, combat power and required balls to
 * catch it (from assignment PDF).
 *
 * Also, immutable once constructed.
 */
public class Pokemon extends Cell {

    /**
     * Construct a Pokemon cell with given property.
     * @param loc The location of the pokemon.
     * @param name The name of it.
     * @param type The type of it.
     * @param combat The combat of it.
     * @param required_balls The required count of ball to catch it.
     */
    public Pokemon(Point loc, String name, String type,
                   int combat, int required_balls) {
        super(loc);
        this.name = name;
        this.type = type;
        this.combat = combat;
        this.required_balls = required_balls;
    }

    @Override
    public boolean affectPlayer(Player player) {
        try {
            // You will always catch the Pokemon when you pass it by default
            // if you have enough Poke balls.
            player.catchPokemon(this);
            return true;  // affect successfully.
        } catch (Player.BallNotSufficientException e) {
            // Just skip the catching.
        }
        return false;
    }

    /**
     * @return The name of the pokemon.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The type of the pokemon.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The combat value of the pokemon.
     */
    public int getCombat() {
        return combat;
    }

    /**
     * @return The required count of balls to catch the pokemon.
     */
    public int getRequiredBalls() {
        return required_balls;
    }

    private final String name;
    private final String type;
    private final int combat;
    private final int required_balls;

}
