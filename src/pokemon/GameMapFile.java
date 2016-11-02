package pokemon;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Game map input related functions for Game.
 */
public class GameMapFile extends File {

    private static final long serialVersionUID = 1L;

    /**
     * Construct a file to read game map.
     * @param file_name The file name of this file.
     */
    public GameMapFile(String file_name) {
        super(file_name);
    }

    /**
     * Read the game map in the file specified. Format specified by assignment.
     * @return The map of the game.
     * @throws Exception If file damaged or format incorrect.
     */
    public Map read() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(this));

        // Read the height and width of the input file
        String[] line_components = br.readLine().split(" ");
        int M = Integer.parseInt(line_components[0]);
        int N = Integer.parseInt(line_components[1]);

        Point starting = new Point(0, 0), destination = new Point(0, 0);
        boolean[][] is_wall = new boolean[M][N];
        int station_count = 0, pokemon_count = 0;

        for (int i = 0; i < M; i++) {
            String line = br.readLine();
            for (int j = 0; j < N; j++) {
                is_wall[i][j] = false;
                switch (line.charAt(j)) {
                    case CHAR_WALL: is_wall[i][j] = true; break;
                    case CHAR_POKEMON: pokemon_count += 1; break;
                    case CHAR_STATION: station_count += 1; break;
                    case CHAR_STARTING: starting = new Point(i, j); break;
                    case CHAR_DESTINATION: destination = new Point(i, j); break;
                }
            }
        }

        ArrayList<Cell> cells = new ArrayList<>();

        // Read all pokemon
        for (int i = 0; i < pokemon_count; i++) {
            cells.add(parsePokemon(br.readLine()));
        }
        // Read all stations
        for (int i = 0; i < station_count; i++) {
            cells.add(parseStation(br.readLine()));
        }

        br.close();

        return new Map(is_wall, cells,
                new Cell(starting), new Cell(destination));
    }

    /**
     * Parse a pokemon configuration from the string.
     * @param in Pokemon configuration string.
     * @return The pokemon cell specified by the string.
     * @throws Exception If pokemon format is invalid.
     */
    private static Pokemon parsePokemon(String in) throws Exception {
        Matcher s = POKEMON_FORMAT.matcher(in);
        if (!s.matches()) {
            throw new Exception("Invalid Pokemon format.");
        }
        Point location = new Point(
                Integer.parseInt(s.group(1).trim()),
                Integer.parseInt(s.group(2).trim())
        );
        return new Pokemon(location,
                s.group(3).trim(), s.group(4).trim(),
                Integer.parseInt(s.group(5).trim()),
                Integer.parseInt(s.group(6).trim()));
    }

    /**
     * Parse a station configuration from the string.
     * @param in Station configuration string.
     * @return The station cell specified by the string.
     * @throws Exception If station format is invalid.
     */
    private static Station parseStation(String in) throws Exception {
        Matcher s = STATION_FORMAT.matcher(in);
        if (!s.matches()) {
            throw new Exception("Invalid station format.");
        }
        Point location = new Point(
                Integer.parseInt(s.group(1).trim()),
                Integer.parseInt(s.group(2).trim())
        );
        return new Station(location,
                Integer.parseInt(s.group(3).trim()));
    }


    private static final char CHAR_WALL = '#';
    private static final char CHAR_POKEMON = 'P';
    private static final char CHAR_STATION = 'S';
    private static final char CHAR_STARTING = 'B';
    private static final char CHAR_DESTINATION = 'D';

    private static final Pattern POKEMON_FORMAT = Pattern.compile(
            "<(.*),(.*)>,(.*),(.*),(.*),(.*)$");
    private static final Pattern STATION_FORMAT = Pattern.compile(
            "<(.*),(.*)>,(.*)$");

}

