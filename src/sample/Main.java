package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    //Variablen
    static int speed = 5;
    static List<Body> snake = new ArrayList<>();
    static Dir direction = Dir.left; //Startrichtung geht nach links
    static boolean gameOver = false;

    //4 verschiedene Richtungen, in welche sich die Schlange bewegen kann
    public enum Dir{
        left, right, up, down
    }

    //Anhand dieser 3 Variablen kann die grösse des Spielfeldes angegeben werden
    static int width = 20;
    static int height = 20;
    static int bodysize = 25;

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        Canvas canvas = new Canvas(width * bodysize, height * bodysize);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        /*Man kann den AnimationTimer mit einem while-Loop vergleichen, welcher eine Methode handle hat,
        welche bei jedem Durchgnag ausgeführt wird. Die Methode enthällt zusätzlich noch die Variable now,
        welche die Anzahl Milisekunden seit dem 01.01.1970 angibt*/
        new AnimationTimer() {
            long lastTick = 0;

            public void handle(long now) {

                /*Am Anfang ist die Variable lastTick auf 0 gesetzt und deshalb muss die Funktion tick das erste Mal manuell
                ausgeführt werden*/
                if (lastTick == 0) {
                    lastTick = now;
                    tick(graphicsContext);
                    return;
                }

                /*Sorgt dafür, dass screen nur in gewissen Zeitabständen refreshed wird. Je höher die Variable Speed, desto
                höher die refreshrates und desto schneller bewegt sich die Schlange*/
                if (now - lastTick > 1000000000 / speed) {
                    lastTick = now;
                    tick(graphicsContext);
                }

            }
        }.start();


        Scene scene = new Scene(root, width * bodysize, height * bodysize);

        //Code wird ausgeführt, wenn eine Taste gedrückt wird
        //Schlange kann sowohl mit WASD als auch mit den Pfeiltasten kontrolliert werden
        //Die Schlange kann nicht in die entgegengesetzte Richtung laufen
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if ((keyEvent.getCode() == KeyCode.W || keyEvent.getCode() == KeyCode.UP) && direction != Dir.down){
                    direction = Dir.up;
            }
            if ((keyEvent.getCode() == KeyCode.D || keyEvent.getCode() == KeyCode.RIGHT) && direction != Dir.left){
                direction = Dir.right;
            }
            if ((keyEvent.getCode() == KeyCode.S || keyEvent.getCode() == KeyCode.DOWN) && direction != Dir.up){
                direction = Dir.down;
            }
            if ((keyEvent.getCode() == KeyCode.A || keyEvent.getCode() == KeyCode.LEFT) && direction != Dir.right){
                direction = Dir.left;
            }
            if (keyEvent.getCode() == KeyCode.ESCAPE){
                System.exit(0);
            }
        });

        //Am Anfang des Spiels werden manuell 6 Schlangen-Elemente kreiert
        snake.add(new Body(width / 2, height / 2));
        snake.add(new Body(width / 2, height / 2));
        snake.add(new Body(width / 2, height / 2));
        snake.add(new Body(width / 2, height / 2));
        snake.add(new Body(width / 2, height / 2));
        snake.add(new Body(width / 2, height / 2));

        primaryStage.setScene(scene);
        primaryStage.setTitle("SNAKE GAME");
        primaryStage.show();
    }


    //tick

    public static void tick(GraphicsContext graphicsContext) {

        //Alle Schlangen-Elemente rücken (von hintern her) auf die Position des vorherigen Schlangen-Elements nach
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }

        //Der Kopf der Schlange bewegt sich in die entsprechende Richtung
        switch (direction){
            case up:
                snake.get(0).y--;
                if(snake.get(0).y<=0){
                    gameOver = true;
                }
                break;
            case down:
                snake.get(0).y++;
                if(snake.get(0).y>=height){
                    gameOver = true;
                }
                break;
            case left:
                snake.get(0).x--;
                if(snake.get(0).x<=0){
                    gameOver = true;
                }
                break;
            case right:
                snake.get(0).x++;
                if(snake.get(0).x>=width){
                    gameOver = true;
                }
                break;
        }


        /*Hintergrunf wird schwarz ausgefüllt. Konkret wird ein schwarzes Quadrat mit der grösse des
        Spielfeldes generiert*/
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, width * bodysize, height * bodysize);

        //Jedes Körper-Element der Schlange wird gefärbt. Danach ein kleineres Feld hineingezeichnet, um einen Schatten zu simulieren
        for (Body body : snake) {
            graphicsContext.setFill(Color.LIGHTGREEN);
            graphicsContext.fillRect(body.x * bodysize, body.y * bodysize, bodysize - 1, bodysize - 1);
            graphicsContext.setFill(Color.GREEN);
            graphicsContext.fillRect(body.x * bodysize, body.y * bodysize, bodysize - 2, bodysize - 2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
