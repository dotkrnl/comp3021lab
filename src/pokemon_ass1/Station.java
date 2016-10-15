package pokemon_ass1;

import java.awt.Point;


/**
 * Class Station is a special type of cell. Besides recording the location of
 * the cell, it also records the information of a supply station, including
 * the number of the provided balls (from assignment PDF).
 *
 * Also, immutable once constructed.
 */
public class Station extends Cell {

    /**
     * Construct a Station cell with given property.
     * @param loc The location of the pokemon.
     * @param ball_count The count of ball in this station.
     */
    public Station(Point loc, int ball_count) {
        super(loc);
        this.ball_count = ball_count;
    }

    @Override
    public boolean affectPlayer(Player player) {
        player.addBalls(getBallCount());
        return true;
    }

    /**
     * @return The count of ball in this station.
     */
    public int getBallCount() {
        return ball_count;
    }


    private final int ball_count;

}
