package pokemon_ass1;

import java.awt.Point;


/**
 * The base class for cells. A cell will affect the stats of a Player once
 * the Player get on to the cell.
 *
 * Immutable once constructed.
 */
public class Cell {

    /**
     * Construct a cell at location specified.
     * @param loc Specified location of the cell.
     */
    public Cell(Point loc) {
        location = loc;
    }

    /**
     * @return The location of this cell.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Affect the status of the player.
     * @param player Player to affect.
     * @return True if the affecting succeeded.
     */
    public boolean affectPlayer(Player player) {
        // Doing nothing for a normal cell.
        // May be override by subclasses.
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return location.equals(cell.location);
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    private final Point location;

}
