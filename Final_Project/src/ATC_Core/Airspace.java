package ATC_Core;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Created by saija on 2017-04-11.
 */
public class Airspace {
    private double width;
    private double height;
    private double depth;
    private Point3D center;

    public Airspace(double width, double height, double depth, Point3D center) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.center = center;
    }

    public Airspace(Airspace otherAirspace) {
        width = otherAirspace.width;
        height = otherAirspace.height;
        depth = otherAirspace.depth;
        center = otherAirspace.center;
    }

    public boolean intersect(Airspace otherAirspace) {
        double xCenter = center.getX();
        double yCenter = center.getY();
        double zCenter = center.getZ();

        double xCenterOther = otherAirspace.center.getX();
        double yCenterOther = otherAirspace.center.getY();
        double zCenterOther = otherAirspace.center.getZ();

        double airspaceAMinX = xCenter - width/2;
        double airspaceAMaxX = xCenter + width/2;
        double airspaceAMinY = yCenter - height/2;
        double airspaceAMaxY = yCenter + height/2;
        double airspaceAMinZ = zCenter - depth/2;
        double airspaceAMaxZ = zCenter + depth/2;

        double airspaceBMinX = xCenterOther - otherAirspace.width/2;
        double airspaceBMaxX = xCenterOther + otherAirspace.width/2;
        double airspaceBMinY = yCenterOther - otherAirspace.height/2;
        double airspaceBMaxY = yCenterOther + otherAirspace.height/2;
        double airspaceBMinZ = zCenterOther - otherAirspace.depth/2;
        double airspaceBMaxZ = zCenterOther + otherAirspace.depth/2;

        return (airspaceAMinX <= airspaceBMaxX && airspaceAMaxX >= airspaceBMinX) &&
                (airspaceAMinY <= airspaceBMaxY && airspaceAMaxY >= airspaceBMinY) &&
                (airspaceAMinZ <= airspaceBMaxZ && airspaceAMaxZ >= airspaceBMinZ);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Airspace) {
            Airspace otherAirspace = (Airspace) other;
            return width == otherAirspace.width
                    && height == otherAirspace.height
                    && depth == otherAirspace.depth
                    && center.equals(otherAirspace.center);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return center.hashCode();
    }

    public void setCenter(Point3D newCenter) {
        center = newCenter;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDepth() {
        return depth;
    }

    public Point3D getCenter() {
        return center;
    }
}
