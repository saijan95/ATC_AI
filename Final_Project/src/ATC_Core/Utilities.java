package ATC_Core;

import javafx.geometry.Point2D;

/**
 * Created by saija on 2017-04-11.
 */
public class Utilities {
    public static double kilometersToFeet(double kilometers) {
        return kilometers * 3280.8;
    }

    public static double feetToKilometers(double feet) {
        return feet / 3280.8;
    }

    public static double kphToMps(double kph) {
        return kph * (1000.0/3600.0);
    }

    public static double distance(Point2D point1 , Point2D point2) {
        double x = Math.abs(point1.getX() - point2.getX());
        double y = Math.abs(point1.getY() - point2.getY());

        return (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
    }
}
