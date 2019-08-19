package AI_ATC;

import ATC_Core.Aircraft;
import ATC_Core.Sector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by saija on 2017-04-17.
 */
public class ArrivalTimeCritic implements Critic {

    @Override
    public List<Aircraft> getConflictAircrafts(Node node) {
        List<Aircraft> conflictAircrafts = new ArrayList<Aircraft>();

        PriorityQueue<Aircraft> priorityQueue = new PriorityQueue<Aircraft>(new Comparator<Aircraft>() {
            @Override
            public int compare(Aircraft o1, Aircraft o2) {
                if (o1.getPriority() < o2.getPriority())
                    return -1;
                else if (o1.getPriority() > o2.getPriority())
                    return 1;
                else
                    return 0;
            }
        });

        for (Aircraft aircraft : node.getSector().getAircrafts()) {
            priorityQueue.add(aircraft);
        }

        double last_speed = -1;
        for (Aircraft aircraft : priorityQueue) {
            double speed = aircraft.getVector().getMagnitude();
            if (last_speed == -1) {
                last_speed = speed;
            }
            else {

                if (last_speed < speed && speed < aircraft.getMaxCruiseSpeed())
                    conflictAircrafts.add(aircraft);

                last_speed = speed;
            }
        }

        return conflictAircrafts;
    }

    public int evaluateSate(Node node) {
        return getConflictAircrafts(node).size() * 2;
    }
}
