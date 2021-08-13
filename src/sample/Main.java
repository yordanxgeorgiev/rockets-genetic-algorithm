package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    public final static double SCENE_WIDTH = 800;
    public final static double SCENE_HEIGHT = 600;
    public final static double TARGET_X = SCENE_WIDTH/2;
    public final static double TARGET_Y = 100;
    public final static double STARTING_X = SCENE_WIDTH/2;
    public final static double STARTING_Y = SCENE_HEIGHT - 50;
    public final static int ROCKET_WIDTH = 10;
    public final static int ROCKET_HEIGHT = 30;

    public final static int velocity = 30;

    private ParallelTransition parallelTransition = new ParallelTransition();

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setTitle("Rockets!");

        Population rockets = new Population();

        createTarget(root);

        addObstacles(root);

        for(int i = 0; i < 1; i++)
        {
            naturalSelection(root, rockets);
        }

        parallelTransition.play();

        primaryStage.setScene(new Scene(root, SCENE_WIDTH, SCENE_HEIGHT));
        primaryStage.show();

    }

    private void createTarget(Group g)
    {
        Circle target = new Circle();
        target.setCenterX(TARGET_X);
        target.setCenterY(TARGET_Y);
        target.setRadius(10);
        target.setFill(Color.RED);
        g.getChildren().add(target);
    }

    private void addObstacles(Group g)
    {
        Rectangle obstacle = new Rectangle(200, 350, 400, 10); // Obstacle
        g.getChildren().add(obstacle);

        Rectangle obstacle1 = new Rectangle(300,5,10,200);
        g.getChildren().add(obstacle1);

        Rectangle obstacle2 = new Rectangle(450,5,10,200);
        g.getChildren().add(obstacle2);
    }

    private void animate(Group root, DNA dna)
    {
        double[][] trace = dna.getTrace();

        Rectangle rect = new Rectangle(); // Representation of the rocket
        rect.setWidth(ROCKET_HEIGHT);
        rect.setHeight(ROCKET_WIDTH);
        rect.setFill(Color.BLACK);
        rect.setOpacity(0.5);

        Path path = new Path();
        path.getElements().add(new MoveTo(STARTING_X, STARTING_Y));

        for(int k = 1; k < trace.length; k++)
        {
            if(trace[k] == null || trace[k][0] == 0.0) break;
            path.getElements().add(new LineTo(trace[k][0], trace[k][1]));

            /*
            Visualization of the points of the path

            Circle circle = new Circle();
            circle.setCenterX(trace[k][0]);
            circle.setCenterY(trace[k][1]);
            circle.setRadius(1);
            circle.setFill(Color.BLUE);
            root.getChildren().add(circle);*/
        }

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setPath(path);
        pathTransition.setNode(rect);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        parallelTransition.getChildren().add(pathTransition);


        root.getChildren().add(rect);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void naturalSelection(Group root, Population rockets)
    {
        for(int i = 1; i < 10000; i++)
        {
            rockets.getFitnessStat();
            if(i%999 == 0) animate(root, rockets.getDNA()[rockets.getFitnessMaxIndex()]); // Adding the best rocket of each generation to the animation
            rockets.breed();

        }
    }
}
