package Radar_Simulator;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * Created by saija on 2017-04-08.
 */
public class RadarBackgroundView extends Pane {
    public RadarBackgroundView(Image bacgroundImage) {
        BackgroundImage backgroundImage = new BackgroundImage(bacgroundImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(RadarView.DEFAULT_WIDTH, RadarView.DEFAULT_HEIGHT, false, false, true, true));

        setBackground(new Background(backgroundImage));
    }
}
