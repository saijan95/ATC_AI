package ATC_Core;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ATC_Core.Globals.*;

/**
 * Created by saija on 2017-04-09.
 */
public class AircraftBuilder {
    public AircraftBuilder() {

    }

    public List<Aircraft> getAircrafts(List<Path> paths, int numberOfAircrafts) {
        Random rand = new Random();
        List<Aircraft> aircrafts = new ArrayList<Aircraft>();
        List<Path> initialPaths = new ArrayList<Path>(paths);

        for (int i = 0; i < numberOfAircrafts; i++) {
            int index = rand.nextInt(AIRCRAFT_NAMES.length);
            String aircraftName = AIRCRAFT_NAMES[index];
            String aircraftModel = AIRCRAFT_MODELS[index];

            // After constructing the flight route, remove the start path from the path list
            // to prevent more than one aircraft starting from the same position
            FlightRoute flightRoute = constructFlightRoute(paths, initialPaths);
            initialPaths.remove(flightRoute.getPath(0));

            Path currentPath = flightRoute.getPath(0);
            Vector vector = getVector(index, flightRoute);

            Point2D current2DPosition = currentPath.getPathPoints().get(0);
            Point3D currentPosition = new Point3D(
                    current2DPosition.getX(),
                    current2DPosition.getY(),
                    Utilities.feetToKilometers(MAX_CRUISING_ALTITUDE[index]));

            double fuelCapacity = FUEL_CAPACITY[index];
            double maxCruisingSpeed = MAX_CRUISING_SPEEDS[index];
            double maxCruisingAltitude = MAX_CRUISING_ALTITUDE[index];

            Airspace airspace = new Airspace(
                    Utilities.feetToKilometers(Globals.MINIMUM_HORIZONTAL_SEPARATION) * 2,
                    Utilities.feetToKilometers(MINIMUM_HORIZONTAL_SEPARATION) * 2,
                    Utilities.feetToKilometers(Globals.MINIMUM_VERTICAL_SEPARATION) * 2,
                    currentPosition);

            Aircraft aircraft = new Aircraft(aircraftName, aircraftModel, flightRoute, currentPath, currentPosition,
                    airspace, vector, fuelCapacity, maxCruisingSpeed, maxCruisingAltitude, i);

            aircrafts.add(aircraft);
        }

        return aircrafts;
    }

    private Vector getVector(int index, FlightRoute flightRoute) {
        Vector vector = null;
        Path initialPath = flightRoute.getPath(0);

        double heading = initialPath.getDirection();
        double magnitude = MAX_CRUISING_SPEEDS[index];
        vector = new Vector(heading, magnitude);

        return vector;
    }

    private FlightRoute constructFlightRoute(List<Path> paths, List<Path> initialPaths) {
        List<Path> route = new ArrayList<Path>();
        List<Path> pathsCopy = new ArrayList<Path>(paths);

        Random rand = new Random();

        // pick an initial path from the list of initial paths
        // then remove the selected path from list of possible paths
        int index = rand.nextInt(initialPaths.size());
        Path initialPath = initialPaths.get(index);
        route.add(initialPath);
        pathsCopy.remove(initialPath);

        // get paths connected to the initial path
        List<Path> connectedPaths = getConnectedPaths(pathsCopy, initialPath);
        while (connectedPaths.size() > 0) {
            index = rand.nextInt(connectedPaths.size());
            initialPath = connectedPaths.get(index);
            route.add(initialPath);
            pathsCopy.remove(initialPath);
            connectedPaths = getConnectedPaths(pathsCopy, initialPath);
        }

        return new FlightRoute(route);
    }

    private List<Path> getConnectedPaths(List<Path> paths, Path path) {
        List<Path> connectedPaths = new ArrayList<Path>();

        for (Path p : paths) {
            if (path.connected(p)) {
                connectedPaths.add(p);
            }
        }

        return connectedPaths;
    }
}
