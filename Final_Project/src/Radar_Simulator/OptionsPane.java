package Radar_Simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

/**
 * Created by saija on 2017-04-08.
 */
public class OptionsPane extends HBox {
    private ComboBox<Double> timeComboBox;
    private Button resetButton;
    private Button addAircraftsButton;

    public OptionsPane() {
        super();
        createOptionsPane();
    }

    private void createOptionsPane() {
        setPadding(new Insets(5, 5, 5, 5));

        // create time combo box
        ObservableList<Double> options =
            FXCollections.observableArrayList(
                    0.10,
                    0.25,
                    0.5,
                    0.75,
                    1.0,
                    1.25
            );

        timeComboBox = new ComboBox<Double>(options);
        HBox.setMargin(timeComboBox, new Insets(0, 5, 0, 0));
        timeComboBox.setValue(1.0);


        // create reset button
        resetButton = new Button("Rest");
        HBox.setMargin(resetButton, new Insets(0, 5, 0, 5));

        // create add aircraft button
        addAircraftsButton = new Button("Add Aircrafts");
        HBox.setMargin(addAircraftsButton, new Insets(0, 0, 0, 5));

        this.getChildren().add(timeComboBox);
        this.getChildren().add(resetButton);
        this.getChildren().add(addAircraftsButton);
    }

    public ComboBox<Double> getTimeComboBox() {
        return timeComboBox;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getAddAircraftsButton() {
        return addAircraftsButton;
    }
}
