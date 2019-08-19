package AI_ATC;

import ATC_Core.Aircraft;
import ATC_Core.Airspace;
import ATC_Core.Globals;
import ATC_Core.Utilities;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SeparationCritic implements Critic {

    public SeparationCritic() {

    }

    private double timeRequired(double magnitude, double distanceInPixels, double distancePerPixel) {
        double distance = distanceInPixels * distancePerPixel;
        double mps = Utilities.kphToMps(magnitude);

        return distance / mps;
    }

    /**
     *
     * @param startingPosition
     * @param heading
     * @param distanceInPixels
     * @return
     */
    private Point2D extrapolatePosition(Point2D startingPosition, double heading, double distanceInPixels) {
        double x = startingPosition.getX();
        double y = startingPosition.getY();

        if (heading == 0 || heading == 360) {
            x += distanceInPixels;
        }
        else if (heading == 90) {
            y -= distanceInPixels;
        }
        else if (heading == 180) {
            x -= distanceInPixels;
        }
        else if (heading == 270) {
            y += distanceInPixels;
        }
        else if (heading > 0 && heading < 90) {
            x += distanceInPixels;
            x -= distanceInPixels;
        }
        else if (heading > 90 && heading < 180) {
            x -= distanceInPixels;
            y -= distanceInPixels;
        }
        else if (heading > 180 && heading < 270) {
            x -= distanceInPixels;
            y += distanceInPixels;
        }
        else {
            x += distanceInPixels;
            y += distanceInPixels;
        }

        return new Point2D(x, y);
    }

    public boolean canCollide(Aircraft aircraft1, Aircraft aircraft2, Point2D intersection) {
        // get 2 dimensional position of aircraft
        Point2D pointA = new Point2D(aircraft1.getPosition().getX(), aircraft1.getPosition().getY());
        Point2D pointB = new Point2D(aircraft2.getPosition().getX(), aircraft2.getPosition().getY());

        // distance needed to travel by each aircraft to get to the intersection point
        double distanceA = Utilities.distance(pointA, intersection);
        double distanceB = Utilities.distance(pointB, intersection);

        // time required for each aircraft to travel to the intersection point
        double timeA = timeRequired(aircraft1.getVector().getMagnitude(), distanceA, Globals.METERS_PER_PIXEL);
        double timeB = timeRequired(aircraft2.getVector().getMagnitude(), distanceB, Globals.METERS_PER_PIXEL);

        double longestTime = timeA > timeB ? timeA : timeB;

        double magnitude, heading;
        Point2D startingPoint;
        if (longestTime == timeA) {
            magnitude = Utilities.kphToMps(aircraft2.getVector().getMagnitude());
            heading = aircraft2.getVector().getHeading();
            startingPoint = new Point2D(aircraft2.getPosition().getX(), aircraft2.getPosition().getY());
        }
        else {
            magnitude = Utilities.kphToMps(aircraft1.getVector().getMagnitude());
            heading = aircraft1.getVector().getHeading();
            startingPoint = new Point2D(aircraft1.getPosition().getX(), aircraft1.getPosition().getY());
        }

        double distanceInPixels = (magnitude * longestTime) / Globals.METERS_PER_PIXEL;
        Point2D endPoint = extrapolatePosition(startingPoint, heading, distanceInPixels);

        Point3D extrapolatedPosition;
        Airspace currentAirspace, futureAirspace, intersectionAirspace;
        if (longestTime == timeA) {
            extrapolatedPosition = new Point3D(endPoint.getX(), endPoint.getY(), aircraft2.getPosition().getZ());

            currentAirspace = aircraft2.getPersonalAirspace();

            futureAirspace = new Airspace(currentAirspace.getWidth(), currentAirspace.getHeight(),
                    currentAirspace.getDepth(), extrapolatedPosition);

            intersectionAirspace = new Airspace(
                    aircraft1.getPersonalAirspace().getWidth(),
                    aircraft1.getPersonalAirspace().getHeight(),
                    aircraft1.getPersonalAirspace().getDepth(),
                    new Point3D(intersection.getX(), intersection.getY(), aircraft1.getPosition().getZ()));

        }
        else {
            extrapolatedPosition = new Point3D(endPoint.getX(), endPoint.getY(), aircraft1.getPosition().getZ());

            currentAirspace = aircraft1.getPersonalAirspace();

            futureAirspace = new Airspace(currentAirspace.getWidth(), currentAirspace.getHeight(),
                    currentAirspace.getDepth(), extrapolatedPosition);

            intersectionAirspace = new Airspace(
                    aircraft2.getPersonalAirspace().getWidth(),
                    aircraft2.getPersonalAirspace().getHeight(),
                    aircraft2.getPersonalAirspace().getDepth(),
                    new Point3D(intersection.getX(), intersection.getY(), aircraft2.getPosition().getZ()));

        }

        return futureAirspace.intersect(intersectionAirspace);
    }

    public List<Aircraft> getConflictAircrafts(Node node) {
        List<Aircraft> aircrafts = node.getSector().getAircrafts();

        List<Aircraft> conflictAircrafts = new ArrayList<Aircraft>();
        for (int i1 = 0; i1 < aircrafts.size(); i1++) {
            Aircraft aircraft1 = aircrafts.get(i1);

            // create evaluation airspace around the position of the aircraft
            Airspace areaOfEvaluation = new Airspace(
                    Globals.EVAL_WIDTH,
                    Globals.EVAL_HEIGHT,
                    Globals.EVAL_DEPTH,
                    aircraft1.getPosition());

            for (int i2 = 0; i2 < aircrafts.size(); i2++) {
                if (i1 != i2) {
                    Aircraft aircraft2 = aircrafts.get(i2);
                    if (areaOfEvaluation.intersect(aircraft2.getPersonalAirspace())) {
                        // get intersection of two paths of the aircrafts
                        Point2D pathsIntersection = aircraft1.getCurrentPath().getIntersection(aircraft2.getCurrentPath());
                        if (pathsIntersection != null) {
                            if (canCollide(aircraft1, aircraft2, pathsIntersection)) {
                                if (!conflictAircrafts.contains(aircraft1))
                                    conflictAircrafts.add(aircraft1);

                                if (!conflictAircrafts.contains(aircraft2))
                                    conflictAircrafts.add(aircraft2);
                            }
                        }
                    }
                }
            }
        }

        return conflictAircrafts;
    }

    @Override
    public int evaluateSate(Node node) {
        return getConflictAircrafts(node).size() * 10;
    }
}
