package ATC_Core;

import java.text.DecimalFormat;

/**
 * Created by saija on 2017-03-26.
 */
public class Vector {
    private double heading;
    private double magnitude;
    private DecimalFormat decimalFormat;

    public Vector(double heading, double magnitude) {
        this.heading = heading;
        this.magnitude = magnitude;
        decimalFormat = new DecimalFormat("#.##");
    }

    public Vector(Vector otherVector) {
        heading = otherVector.heading;
        magnitude = otherVector.magnitude;
        decimalFormat = new DecimalFormat("#.##");
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Vector) {
            Vector otherVector = (Vector) other;
            return heading == otherVector.heading && magnitude == otherVector.magnitude;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int) ((256 * magnitude) + heading * 256);
    }

    public void setHeading(double newHeading) {
        this.heading = newHeading;
    }

    public void setMagnitude(double  newMagnitude) {
        this.magnitude = newMagnitude;
    }

    public double getHeading() {
        return heading;
    }

    public String getHeadingStr() {
        String direction = null;
        if (heading == 90)
            direction = "N";
        else if (heading == 270)
            direction = "S";
        else if (heading == 0)
            direction = "E";
        else if (heading == 180)
            direction = "W";
        else if (heading > 0 && heading < 90)
            direction  = "NE";
        else if (heading > 90 && heading < 180)
            direction = "NW";
        else if (heading > 180 && heading < 270)
            direction = "SW";
        else if (heading > 270 && heading < 360)
            direction = "SE";

        return direction + ": " + heading;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public String getMagnitudeStr() {
        String kmMagnitudeStr = decimalFormat.format(magnitude);

        return kmMagnitudeStr;
    }
}
