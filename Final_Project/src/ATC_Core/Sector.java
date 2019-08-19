package ATC_Core;

import javafx.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saija on 2017-03-26.
 */
public class Sector extends BoundingBox {
    private double minAltitude;
    private double maxAltitude;
    private List<Aircraft> aircrafts;
    private List<Path> paths;

    public Sector(double width, double height, double depth, double minAltitude, double maxAltitude, List<Path> paths, List<Aircraft> aircrafts) {
        super(0, 0, 0, width, height, depth);

        this.minAltitude = minAltitude;
        this.maxAltitude = maxAltitude;
        this.aircrafts = aircrafts;
        this.paths = paths;
    }

    public Sector(Sector otherSector) {
        super(otherSector.getMinX(), otherSector.getMinY(), otherSector.getMinZ(), otherSector.getWidth(),
                otherSector.getHeight(), otherSector.getDepth());

        aircrafts = new ArrayList<Aircraft>();
        for (Aircraft aircraft : otherSector.aircrafts) {
            aircrafts.add(new Aircraft(aircraft));
        }

        paths = new ArrayList<Path>();
        for (Path path : otherSector.getPaths()) {
            paths.add(new Path(path));
        }
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = true;

        if (other instanceof Sector) {
            Sector otherSector = (Sector) other;
            List<Aircraft> otherAircrafts = otherSector.aircrafts;
            List<Path> otherPaths = otherSector.paths;
            if (aircrafts.size() == otherAircrafts.size()) {
                for (int i = 0; i < aircrafts.size(); i++) {
                    if (!aircrafts.get(i).equals(otherAircrafts.get(i))) {
                        equal = false;
                        break;
                    }
                }
            }
            else {
                equal = false;
            }

            if (paths.size() == otherPaths.size() && equal) {
                for (int i = 0; i < paths.size(); i++) {
                    if (!paths.get(i).equals(otherPaths.get(i))) {
                        equal = false;
                        break;
                    }
                }
            }
            else {
                equal = false;
            }
        }
        else {
            equal = false;
        }

        return equal;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Aircraft aircraft : aircrafts) {
            hashCode += aircraft.hashCode();
        }

        for (Path path : paths) {
            hashCode += path.hashCode();
        }

        return hashCode;
    }

    public void addAircraft(Aircraft newAircraft) {
        aircrafts.add(newAircraft);
    }

    public void removeAircraft(Aircraft aircraft) {
        int i = 0;
        for (Aircraft a : aircrafts) {
            if (a.equals(aircraft)) {
                aircrafts.remove(i);
                return;
            }

            i++;
        }
    }

    public void clearAircrafts() {
        aircrafts.clear();
    }

    public double getMinAltitude() {
        return minAltitude;
    }

    public double getMaxAltitude() {
        return maxAltitude;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public List<Path> getPaths() {
        return paths;
    }
}
