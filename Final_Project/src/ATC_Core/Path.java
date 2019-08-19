package ATC_Core;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saija on 2017-03-26.
 */
public class Path {
    private List<Point2D> pathPoints;
    private double direction;

    public Path(List<Point2D> pathPoints, double direction) {
        this.pathPoints = pathPoints;
        this.direction = direction;
    }

    public Path(Path otherPath) {
        pathPoints = new ArrayList<Point2D>();
        for (Point2D p : otherPath.pathPoints) {
            Point2D newPoint = new Point2D(p.getX(), p.getY());
            pathPoints.add(newPoint);
        }

        direction = otherPath.direction;
    }

    public Point2D getIntersection(Path otherPath) {
       Point2D point1A = pathPoints.get(0);
       Point2D point2A = pathPoints.get(pathPoints.size() - 1);

       Point2D point1B = otherPath.pathPoints.get(0);
       Point2D point2B = otherPath.pathPoints.get(pathPoints.size() - 1);

       double A1 = point2A.getY() - point1A.getY();
       double B1 = point1A.getX() - point2A.getX();
       double C1 = (A1*point1A.getX()) + (B1*point1A.getY());

       double A2 = point2B.getY() - point1B.getY();
       double B2 = point1B.getX() - point2B.getX();
       double C2 = (A2*point1B.getX()) + (B2*point1B.getY());

       double delta = (A1 * B2) - (A2 * B1);
       if (delta == 0)
           return null;

       double x = ((B2 * C1) - (B1 * C2)) / delta;
       double y = ((A1 * C2) - (A2 * C1)) / delta;

       return new Point2D(x, y);
    }

    public boolean connected(Path otherPath) {
        if (pathPoints.get(pathPoints.size() - 1).equals(otherPath.pathPoints.get(0))) {
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = true;

        if (other instanceof Path) {
            Path otherPath = (Path) other;
            List<Point2D> otherPathPoints = otherPath.pathPoints;

            if (direction == otherPath.direction) {
                if (pathPoints.size() == otherPathPoints.size()) {
                    for (int i = 0; i < pathPoints.size(); i++) {
                        if (!pathPoints.get(i).equals(otherPathPoints.get(i))) {
                            equal = false;
                            break;
                        }
                    }

                    if (equal)
                        return true;
                }
            }
        }

        return false;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        for (Point2D p : pathPoints) {
            hash += p.hashCode();
        }

        return (int) (hash * direction);
    }

    public List<Point2D> getPathPoints() {
        return pathPoints;
    }

    public double getDirection() {
        return direction;
    }
}
