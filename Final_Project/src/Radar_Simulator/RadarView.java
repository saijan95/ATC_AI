package Radar_Simulator;

import ATC_Core.Sector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/**
 * Created by saija on 2017-03-26.
 */
public class RadarView extends GridPane {
    public static final double DEFAULT_WIDTH = 500;
    public static final double DEFAULT_HEIGHT = 500;

    private AeronauticalPathsView pathsView;
    private AircraftsView aircraftsView;
    private RadarBackgroundView backgroundView;

    public RadarView(Sector sector) {
        super();
        createRadarView(sector);
    }

    private void createRadarView(Sector sector) {
        // create AeronauticalPathsView
        pathsView = new AeronauticalPathsView(sector.getPaths(), RadarView.DEFAULT_WIDTH, RadarView.DEFAULT_HEIGHT);
        pathsView.widthProperty().bind(heightProperty());
        pathsView.heightProperty().bind(heightProperty());

        // create AircraftsView
        aircraftsView = new AircraftsView(sector.getAircrafts());
        aircraftsView.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        aircraftsView.prefWidthProperty().bind(heightProperty());
        aircraftsView.prefHeightProperty().bind(heightProperty());

        Image backgroundImage = new Image("file:" + System.getProperty("user.dir") + "\\Pictures\\radar_background.png");
        backgroundView = new RadarBackgroundView(backgroundImage);
        backgroundView.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        backgroundView.prefWidthProperty().bind(heightProperty());
        backgroundView.prefHeightProperty().bind(heightProperty());

        add(backgroundView, 0, 0);
        add(pathsView, 0, 0);
        add(aircraftsView, 0, 0);
    }

    public AeronauticalPathsView getAeronauticalPathsView() {
        return pathsView;
    }

    public AircraftsView getAircraftsView() {
        return aircraftsView;
    }
}
