package ATC_Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saija on 2017-03-26.
 */
public class FlightRoute {
    List<Path> route;

    public FlightRoute() {
        route = new ArrayList<Path>();
    }

    public FlightRoute(List<Path> route) {
        this.route = route;
    }

    public FlightRoute(FlightRoute otherFlighRoute) {
        route = new ArrayList<Path>();
        for (Path path : otherFlighRoute.route) {
            route.add(new Path(path));
        }
    }

    public void addPath(Path newPath) {
        route.add(newPath);
    }

    public void removePath(Path path) {
        int i = 0;
        for (Path p : route) {
            if (p.equals(path)) {
                route.remove(i);
            }

            i++;
        }
    }

    @Override
    public boolean equals(Object other) {
        boolean equal = true;
        if (other instanceof FlightRoute) {
            FlightRoute otherFlightRoute = (FlightRoute) other;
            List<Path> otherRoute = otherFlightRoute.route;

            if (route.size() == otherRoute.size()) {
                for (int i = 0; i < route.size(); i++){
                    if (!route.get(i).equals(otherRoute.get(i))) {
                        equal = false;
                        break;
                    }
                }

                if (equal)
                    return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Path p : route) {
            hash += p.hashCode();
        }

        return hash;
    }

    public int indexOf(Path path) {
        for (int i = 0; i < route.size(); i++) {
            if (route.get(i).equals(path)) {
                return i;
            }
        }

        return -1;
    }

    public Path getPath(int index) {
        if (index >= 0 && index < route.size())
            return route.get(index);

        return null;
    }

    public int getSize() {
        return route.size();
    }
}
