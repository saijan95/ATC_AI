package ATC_Core;

import com.sun.deploy.uitoolkit.UIToolkit;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import javax.rmi.CORBA.Util;

/**
 * Created by saija on 2017-03-26.
 */
public class Aircraft {
    private String aircraftNumber;
    private String aircraftModel;
    private FlightRoute flightRoute;
    private Path currentPath;
    private Point3D position;
    private Airspace personalAirspace;
    private Vector vector;
    private double fuelLevel;
    private double maxCruiseSpeed;
    private double maxCruiseAltitude;
    private double lastKiloCheckPoint;
    public int priority;

    public Aircraft(String aircraftNumber, String aircraftModel, FlightRoute flightRoute, Path currentPath,
                    Point3D position, Airspace personalAirspace, Vector vector, double fuelLevel,
                    double maxCruiseSpeed, double maxCruiseAltitude, int priority) {

        this.aircraftNumber = aircraftNumber;
        this.aircraftModel = aircraftModel;
        this.flightRoute = flightRoute;
        this.currentPath = currentPath;
        this.position = position;
        this.personalAirspace = personalAirspace;
        this.vector = vector;
        this.fuelLevel = fuelLevel;
        this.maxCruiseSpeed = maxCruiseSpeed;
        this.maxCruiseAltitude = maxCruiseAltitude;
        this.priority = priority;
    }

    public Aircraft(Aircraft otherAircraft) {
        aircraftNumber = otherAircraft.aircraftNumber;
        aircraftModel = otherAircraft.aircraftModel;
        flightRoute = new FlightRoute(otherAircraft.flightRoute);
        currentPath = new Path(otherAircraft.currentPath);
        position = new Point3D(
                otherAircraft.position.getX(),
                otherAircraft.position.getY(),
                otherAircraft.position.getZ());
        personalAirspace = new Airspace(otherAircraft.personalAirspace);
        vector = new Vector(otherAircraft.vector);
        fuelLevel = otherAircraft.fuelLevel;
        maxCruiseSpeed = otherAircraft.maxCruiseSpeed;
        maxCruiseAltitude = otherAircraft.maxCruiseAltitude;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Aircraft) {
            Aircraft otherAirPlane = (Aircraft) other;
            return (aircraftNumber.equals(otherAirPlane.aircraftNumber)
                    && aircraftModel.equals(otherAirPlane.aircraftModel)
                    && vector.equals(otherAirPlane.vector)
                    && personalAirspace.equals(otherAirPlane.personalAirspace)
                    && flightRoute.equals(otherAirPlane.flightRoute)
                    && currentPath.equals(otherAirPlane.currentPath)
                    && position.equals(otherAirPlane.position)
                    && fuelLevel == otherAirPlane.fuelLevel
                    && maxCruiseSpeed == otherAirPlane.maxCruiseSpeed
                    && maxCruiseAltitude == otherAirPlane.maxCruiseAltitude);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += aircraftNumber.hashCode();
        hash += aircraftModel.hashCode();
        hash += vector.hashCode();
        hash += personalAirspace.hashCode();
        hash += flightRoute.hashCode();
        hash += currentPath.hashCode();
        hash += position.hashCode();
        hash += fuelLevel + maxCruiseSpeed + maxCruiseAltitude;

        return hash;
    }

    public void moveAircraft(Point3D newPosition) {
        position = newPosition;
        personalAirspace.setCenter(newPosition);
    }

    public void setVector(Vector newVector) {
        vector = newVector;
    }

    public void setPersonalAirspace(Airspace newAirSpace) {
        personalAirspace = newAirSpace;
    }

    public void setFlightRoute(FlightRoute newFlightRoute) {
        flightRoute = newFlightRoute;
    }

    public void setCurrentPath(Path newPath) {
        currentPath = newPath;
    }

    public void setPosition(Point3D newPosition) {
        position = newPosition;
    }

    public void setLastKiloCheckPoint(double distance) {
        lastKiloCheckPoint = distance;
    }

    public String getAircraftNumber() {
        return aircraftNumber;
    }

    public String getAircraftModel() {
        return aircraftModel;
    }

    public Vector getVector() {
        return vector;
    }

    public Airspace getPersonalAirspace() {
        return personalAirspace;
    }

    public FlightRoute getFlightRoute() {
        return flightRoute;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public Point3D getPosition() {
        return position;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public double getMaxCruiseSpeed() {
        return maxCruiseSpeed;
    }

    public double getMaxCruiseAltitude() {
        return maxCruiseAltitude;
    }

    public double getLastKiloCheckPoint() {
        return lastKiloCheckPoint;
    }

    public double getAltitude() {
        return position.getZ();
    }

    public int getPriority() {
        return priority;
    }
}
