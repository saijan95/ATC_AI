package Radar_Simulator;

import ATC_Core.Path;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Created by saija on 2017-03-26.
 */
public class AeronauticalPathsView extends Canvas {
    private List<Path> paths;
    private GraphicsContext gc;

    private double prefWidth;
    private double prefHeight;

    public AeronauticalPathsView(List<Path> paths, double prefWidth, double prefHeight) {
        super(prefWidth, prefHeight);

        this.paths = paths;
        gc = getGraphicsContext2D();

        this.prefWidth = prefWidth;
        this.prefHeight = prefHeight;
    }

    public void drawAeronauticalPaths() {
        double width = getWidth();
        double height = getHeight();

        // scales the scale factor
        // Width and height can be used because they are equal.
        double scaleFactor = height / prefHeight;

        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        for (Path path : paths) {
            List<Point2D> pathPoints = path.getPathPoints();

            if (pathPoints.size() > 1) {
                for (int i = 0; i < pathPoints.size()-1; i++) {
                    gc.strokeLine(pathPoints.get(i).getX() * scaleFactor, pathPoints.get(i).getY() * scaleFactor,
                            pathPoints.get(i + 1).getX() * scaleFactor, pathPoints.get(i + 1).getY() * scaleFactor);
                }
            }
        }
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return prefWidth;
    }

    @Override
    public double prefHeight(double width) {
        return prefHeight;
    }
}
