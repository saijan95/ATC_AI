package Radar_Simulator;

import ATC_Core.Aircraft;
import ATC_Core.Globals;
import ATC_Core.Utilities;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;

/**
 * Created by saija on 2017-03-29.
 */
public class AircraftInformationPane extends GridPane {
    public final static double DEFAULT_IMAGE_WIDTH = 300;
    public final static double DEFAULT_IMAGE_HEIGHT = 200;

    private ImageView aircraftImage;
    private Label aircraftName;
    private Label aircraftModel;
    private Label speed;
    private Label heading;
    private Label altitude;

    private ATCCommunication atcCommunication;

    HashMap<String, Image> aircraftImageFiles = new HashMap<String, Image>();

    public AircraftInformationPane() {
        loadImages();
        createInformationPane();
    }

    private void createInformationPane() {
        setPrefSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        setPadding(new Insets(5, 5, 5, 5));
        setVgap(5);
        setHgap(5);

        aircraftImage = new ImageView();
        aircraftImage.setFitWidth(300);
        aircraftImage.setFitHeight(200);
        aircraftImage.setPreserveRatio(true);

        aircraftName = new Label();
        aircraftName.setTextAlignment(TextAlignment.LEFT);
        aircraftName.setStyle("-fx-font-size: 32px;");
        GridPane.setHgrow(aircraftName, Priority.ALWAYS);

        aircraftModel = new Label();
        aircraftModel.setTextAlignment(TextAlignment.LEFT);
        aircraftModel.setStyle("-fx-font-size: 14px");
        GridPane.setHgrow(aircraftModel, Priority.ALWAYS);

        speed = new Label();
        speed.setTextAlignment(TextAlignment.LEFT);
        speed.setStyle("-fx-font-size: 14px");
        GridPane.setHgrow(speed, Priority.ALWAYS);

        heading = new Label();
        heading.setTextAlignment(TextAlignment.LEFT);
        heading.setStyle("-fx-font-size: 14px");
        GridPane.setHgrow(heading, Priority.ALWAYS);

        altitude = new Label();
        altitude.setTextAlignment(TextAlignment.LEFT);
        altitude.setStyle("-fx-font-size: 14px");
        GridPane.setHgrow(altitude, Priority.ALWAYS);

        atcCommunication = new ATCCommunication();
        atcCommunication.setEditable(false);
        GridPane.setVgrow(atcCommunication, Priority.ALWAYS);

        add(aircraftImage, 0, 0, 1, 5);
        add(aircraftName, 1, 0);
        add(aircraftModel, 1, 1);
        add(speed, 1, 2);
        add(heading, 1, 3);
        add(altitude, 1, 4);
        add(atcCommunication, 0, 5, 2, 1);
    }

    public void loadImages() {
        String workingDir = System.getProperty("user.dir");

        for (int i = 0; i < RadarSimulationApp.imageFileNames.length; i++) {
            Image img = new Image("file:" + workingDir + "\\Pictures\\" + RadarSimulationApp.imageFileNames[i]);
            aircraftImageFiles.put(Globals.AIRCRAFT_MODELS[i], img);
        }
    }

    public void update(Aircraft aircraft) {
        aircraftName.setText(aircraft.getAircraftNumber());
        aircraftModel.setText(aircraft.getAircraftModel());
        speed.setText("Speed: " + aircraft.getVector().getMagnitudeStr());
        heading.setText("Heading: " + aircraft.getVector().getHeadingStr());
        altitude.setText("Altitude: " + Utilities.kilometersToFeet(aircraft.getAltitude()));

        Image aircraftImg = null;
        if (aircraft.getAircraftModel().equals(Globals.AIRCRAFT_MODELS[0]))
            aircraftImg = aircraftImageFiles.get(Globals.AIRCRAFT_MODELS[0]);
        else if (aircraft.getAircraftModel().equals(Globals.AIRCRAFT_MODELS[1]))
            aircraftImg = aircraftImageFiles.get(Globals.AIRCRAFT_MODELS[1]);
        else if (aircraft.getAircraftModel().equals(Globals.AIRCRAFT_MODELS[2]))
            aircraftImg = aircraftImageFiles.get(Globals.AIRCRAFT_MODELS[2]);

        aircraftImage.setImage(aircraftImg);
    }

    public void clear() {
        aircraftImage.setImage(null);
        aircraftName.setText("Aircraft Name");
        aircraftModel.setText("Aircraft Model");
        speed.setText("Speed");
        heading.setText("Heading");
        altitude.setText("Altitude");
        atcCommunication.clear();
    }
}
