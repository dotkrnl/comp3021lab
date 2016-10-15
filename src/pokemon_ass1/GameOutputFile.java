package pokemon_ass1;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * Game information output related functions.
 */
class GameOutputFile extends File {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a file to write game player information.
     * @param file_name The file name of this file.
     */
    public GameOutputFile(String file_name) {
        super(file_name);
    }

    /**
     * Save player information to this file.
     * @param p The player to save.
     * @throws IOException If writing failed.
     */
    public void save(Player p) throws IOException {
        this.createNewFile();

        FileWriter fw = new FileWriter(this.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(String.valueOf(p.getScore()));
        bw.write('\n');

        bw.write(String.valueOf(p.getBallCount()));
        bw.write(':');
        bw.write(String.valueOf(p.getCaughtPokemon().size()));
        bw.write(':');
        bw.write(String.valueOf(p.getCaughtTypes().size()));
        bw.write(':');
        bw.write(String.valueOf(p.getMaxCompact()));
        bw.write('\n');

        Route path = p.getVisited();
        List<Point> points = path.getPoints();
        bw.write(formatPoint(points.get(0)));
        for (int i = 1; i < points.size(); i++) {
            bw.write("->");
            bw.write(formatPoint(points.get(i)));
        }

        bw.close();
    }

    /**
     * Get the formatted string of a Point.
     * @param p The point to format.
     * @return The formatted string.
     */
    public static String formatPoint(Point p) {
        return "<" + p.x + ',' + p.y + '>';
    }

}