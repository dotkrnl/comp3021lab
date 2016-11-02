package pokemon.ui;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//import pokemon.Cell;
//import pokemon.Map;
//import pokemon.Pokemon;
//import pokemon.Station;

public class PokemonScreenLAB9 extends Application {

    /**
     * width of the window
     */
    private static int W = 600;

    /**
     * height of the window
     */
    private static int H = 400;


    // this define the size of one CELL
    private static int STEP_SIZE = 40;

    // this are the urls of the images
    private static String imgFront;
    private static String imgBack;
    private static String imgLeft;
    private static String imgRight;
    private static String imgPika;

    static {
        try {
            imgFront = PokemonScreenLAB9.class.getResource(
                    "/pokemon/resources/icons/front.png")
                    .toURI().toString();
            imgBack = PokemonScreenLAB9.class.getResource(
                    "/pokemon/resources/icons/back.png")
                    .toURI().toString();
            imgLeft = PokemonScreenLAB9.class.getResource(
                    "/pokemon/resources/icons/left.png")
                    .toURI().toString();
            imgRight = PokemonScreenLAB9.class.getResource(
                    "/pokemon/resources/icons/right.png")
                    .toURI().toString();
            imgPika = PokemonScreenLAB9.class.getResource(
                    "/pokemon/resources/icons/25.png")
                    .toURI().toString();
        } catch (Exception err) {
            System.err.println("Failed to load resources:");
            err.printStackTrace();
            System.exit(1);
        }
    }

    private ImageView avatar;
    private Image avatarImage;

    private ImageView pikachu;
    private Image pikachuImage;

    // these booleans correspond to the key pressed by the user
    boolean goUp, goDown, goRight, goLeft;

    // current position of the avatar
    double currentPosx = 0;
    double currentPosy = 0;

    // current position of the pikachu
    double pikachuPosx = 0;
    double pikachuPosy = 0;

    protected boolean stop = false;

    @Override
    public void start(Stage stage) throws Exception {

        // at the beginning lets set the image of the avatar front
        avatarImage = new Image(imgFront);
        avatar = new ImageView(avatarImage);
        avatar.setFitHeight(STEP_SIZE);
        avatar.setFitWidth(STEP_SIZE);
        avatar.setPreserveRatio(true);
        Random generator = new Random();
        currentPosx = STEP_SIZE * generator.nextInt(W / STEP_SIZE);
        currentPosy = STEP_SIZE * generator.nextInt(H / STEP_SIZE);

        // Set pikachu
        pikachuImage = new Image(imgPika);
        pikachu = new ImageView(pikachuImage);
        pikachu.setFitHeight(STEP_SIZE);
        pikachu.setFitWidth(STEP_SIZE);
        pikachu.setPreserveRatio(true);
        pikachu.setVisible(false);
        pikachuPosx = currentPosx;
        pikachuPosy = currentPosy;

        Group mapGroup = new Group();
        avatar.relocate(currentPosx, currentPosy);
        pikachu.relocate(pikachuPosx, pikachuPosy);
        mapGroup.getChildren().add(avatar);
        mapGroup.getChildren().add(pikachu);

        // create scene with W and H and color of background
        Scene scene = new Scene(mapGroup, W, H, Color.SANDYBROWN);

        // add listener on key pressing
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                pikachu.setVisible(true);
                switch (event.getCode()) {
                case UP:
                    goUp = true;
                    avatar.setImage(new Image(imgBack));
                    break;
                case DOWN:
                    goDown = true;
                    avatar.setImage(new Image(imgFront));
                    break;
                case LEFT:
                    goLeft = true;
                    avatar.setImage(new Image(imgLeft));
                    break;
                case RIGHT:
                    goRight = true;
                    avatar.setImage(new Image(imgRight));
                    break;
                default:
                    break;
                }
            }
        });

        // add listener key released
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                case UP:
                    goUp = false;
                    break;
                case DOWN:
                    goDown = false;
                    break;
                case LEFT:
                    goLeft = false;
                    break;
                case RIGHT:
                    goRight = false;
                    break;
                default:
                    break;
                }
                stop = false;
            }
        });

        stage.setScene(scene);
        stage.show();

        // it will execute this periodically
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (stop)
                    return;

                int dx = 0, dy = 0;

                if (goUp) {
                    dy -= (STEP_SIZE);
                } else if (goDown) {
                    dy += (STEP_SIZE);
                } else if (goRight) {
                    dx += (STEP_SIZE);
                } else if (goLeft) {
                    dx -= (STEP_SIZE);
                } else {
                    // no key was pressed return
                    return;
                }
                moveAvatarBy(dx, dy);
            }
        };
        // start the timer
        timer.start();
    }

    private void moveAvatarBy(int dx, int dy) {
        double x = avatar.getLayoutX() + dx;
        double y = avatar.getLayoutY() + dy;
        moveAvatar(x, y, avatar.getLayoutX(), avatar.getLayoutY());
    }

    private void moveAvatar(double x, double y, double px, double py) {
        final double w = avatar.getBoundsInLocal().getWidth();
        final double h = avatar.getBoundsInLocal().getHeight();
        if (x >= 0 && x + w <= W && y >= 0 && y + h <= H) {
            // relocate ImageView avatar
            avatar.relocate(x, y);
            pikachu.relocate(px, py);
            // update position
            currentPosx = x;
            currentPosy = y;

            // I moved the avatar lets set stop at true
            // and wait user release the key :)
            stop = true;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
