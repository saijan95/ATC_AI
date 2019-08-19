package Radar_Simulator;

import ATC_Core.Aircraft;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saija on 2017-03-28.
 */
public class AircraftsView extends Pane {
    private Image aircraftImageFile;
    private List<AircraftImageView> aircraftImages;

    public AircraftsView(List<Aircraft> aircrafts) {
        super();
        aircraftImages = new ArrayList<AircraftImageView>();
        aircraftImageFile = new Image("file:" + System.getProperty("user.dir") + "\\Pictures\\airplane_symbol.png");
        drawAirPlanes(aircrafts);
    }

    public void drawAirPlanes(List<Aircraft> aircrafts) {
        if (aircraftImages.size() == 0) {
            for (Aircraft aircraft : aircrafts) {
                AircraftImageView imgView = new AircraftImageView(aircraft, aircraftImageFile);
                imgView.setFitWidth(20);
                imgView.setFitHeight(20);
                aircraftImages.add(imgView);
                getChildren().add(imgView);
            }
        }

        for (int i = 0; i < aircraftImages.size(); i++) {
            aircraftImages.get(i).setX(aircrafts.get(i).getPosition().getX());
            aircraftImages.get(i).setY(aircrafts.get(i).getPosition().getY());
        }
    }

    public void clearAircraftsView() {
        for (AircraftImageView aircraftImageView : aircraftImages) {
            getChildren().remove(aircraftImageView);
        }

        aircraftImages.clear();

    }

    public void removeAircraftImage(Aircraft aircraft) {
        for (int i = 0; i < aircraftImages.size(); i++) {
            AircraftImageView aircraftImageView = aircraftImages.get(i);
            if (aircraftImageView.getAircraft().equals(aircraft)) {
                aircraftImages.remove(i);

                // remove image from view
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        getChildren().remove(aircraftImageView);
                    }
                });

                break;
            }
        }
    }

    public List<AircraftImageView> getAircraftImages() {
        return aircraftImages;
    }
}
