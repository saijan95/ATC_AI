package Radar_Simulator;

import ATC_Core.*;
import com.sun.prism.paint.Paint;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.BoundingBox;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sun.awt.Mutex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by saija on 2017-03-26.
 */
public class RadarSimulationApp extends Application {
    public static String[] imageFileNames = {"b777-300.jpg", "b747-8.jpg", "b787-10.jpg"};

    private Radar radar;

    public void start(Stage primaryStage) {
        Sector sector = readSectorData();

        GridPane pane = new GridPane();
        pane.setAlignment(Pos.TOP_LEFT);
        pane.setPrefSize(500, 500);
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        radar = new Radar(sector, pane);

        primaryStage.setTitle("Artificial Intelligence Air Traffic Controller");

        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        cleanup();
    }

    public Sector readSectorData() {
        List<Path> aeronauticalPaths = new ArrayList<Path>();
        List<Aircraft> aircrafts = new ArrayList<Aircraft>();

        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(System.getProperty("user.dir") + "\\sector_paths.txt");
            bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArr = line.split(",");
                aeronauticalPaths.add(readPath(lineArr));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }

        Sector sector =  new Sector(
                500,
                500,
                500,
                Globals.SECTOR_MIN_ALTITUDE,
                Globals.SECTOR_MAX_ALTITUDE,
                aeronauticalPaths,
                aircrafts);

        return sector;
    }

    private Path readPath(String[] pathData) {
        Point2D start = new Point2D(Double.parseDouble(pathData[0]), Double.parseDouble(pathData[1]));
        Point2D end = new Point2D(Double.parseDouble(pathData[2]), Double.parseDouble(pathData[3]));
        double direction = Double.parseDouble(pathData[4]);

        List<Point2D> pathPoints = new ArrayList<Point2D>();
        pathPoints.add(start);
        pathPoints.add(end);

        return new Path(pathPoints, direction);
    }

    private void cleanup() {
        radar.cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
