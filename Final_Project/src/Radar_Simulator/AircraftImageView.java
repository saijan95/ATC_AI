package Radar_Simulator;

import ATC_Core.Aircraft;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by saija on 2017-04-04.
 */
public class AircraftImageView extends ImageView {
    private Aircraft aircraft;

    public AircraftImageView(Aircraft aircraft, Image img) {
        super();
        setImage(img);

        this.aircraft = aircraft;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }
}
