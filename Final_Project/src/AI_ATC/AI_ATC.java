package AI_ATC;

import ATC_Core.Aircraft;
import ATC_Core.Globals;
import ATC_Core.Sector;
import ATC_Core.Utilities;
import javafx.geometry.Point3D;

import java.util.*;

/**
 * Created by saija on 2017-04-11.
 */
public class AI_ATC {
    private List<Critic> critics;

    public AI_ATC() {
        critics = new ArrayList<Critic>();
        createCritics();
    }

    /**
     * Create and add critics to the critics list
     */
    private void createCritics() {
        critics.add(new SeparationCritic());
        critics.add(new ArrivalTimeCritic());
    }

    /**
     * Uses all the critics to evaluate a sector and outputs
     * the summation of all the scores from each critic
     *
     * @param sector    sector to evaluate
     * @return          summation of all the scores from each critic
     */
    public int evaluateSector(Sector sector) {
        int score = 0;

        Node rootNode = new Node(sector, 0, 0, null, new ArrayList<Node>());
        for (Critic critic : critics) {
            score += critic.evaluateSate(rootNode);
        }

        return score;
    }

    private List<Node> expand(Node node) {
        List<Node> newNodes = new ArrayList<Node>();

        for (Critic critic : critics) {
            List<Aircraft> conflictAircrafts = critic.getConflictAircrafts(node);

            for (Aircraft cAircraft : conflictAircrafts) {

                // increase altitude
                if (Utilities.kilometersToFeet(cAircraft.getAltitude()) < cAircraft.getMaxCruiseAltitude()
                        && Utilities.kilometersToFeet(cAircraft.getAltitude()) <= cAircraft.getMaxCruiseAltitude()) {
                    double newAltitude = Utilities.kilometersToFeet(cAircraft.getAltitude())
                            + (Globals.MINIMUM_VERTICAL_SEPARATION * 2);
                    if (newAltitude < node.getSector().getMaxAltitude() && newAltitude <= cAircraft.getMaxCruiseAltitude()) {
                        // create new sector
                        Sector newSector = new Sector(node.getSector());

                        // find conflict aircraft in new sector
                        List<Aircraft> aircrafts = newSector.getAircrafts();
                        Aircraft aircraft = aircrafts.get(aircrafts.indexOf(cAircraft));

                        // change the conflict aircraft's altitude;
                        Point3D position = aircraft.getPosition();
                        Point3D newPosition = new Point3D(
                                position.getX(),
                                position.getY(),
                                Utilities.feetToKilometers(newAltitude));

                        aircraft.setPosition(newPosition);

                        Node newNode = new Node(
                                newSector,
                                0,
                                node.getDepth() + 1,
                                node,
                                new ArrayList<Node>());

                        int score = evaluateSector(newSector);
                        newNode.setScore(score);

                        newNodes.add(newNode);
                    }
                }

                // decrease altitude
                if (Utilities.kilometersToFeet(cAircraft.getAltitude()) > node.getSector().getMinAltitude()) {
                    double newAltitude = Utilities.kilometersToFeet(cAircraft.getAltitude())
                            - (Globals.MINIMUM_VERTICAL_SEPARATION * 2);
                    if (newAltitude > node.getSector().getMinAltitude()) {
                        // create new sector
                        Sector newSector = new Sector(node.getSector());

                        // find conflict aircraft in new sector
                        List<Aircraft> aircrafts = newSector.getAircrafts();
                        Aircraft aircraft = aircrafts.get(aircrafts.indexOf(cAircraft));

                        // change the conflict aircraft's altitude;
                        Point3D position = aircraft.getPosition();
                        Point3D newPosition = new Point3D(
                                position.getX(),
                                position.getY(),
                                Utilities.feetToKilometers(newAltitude));

                        aircraft.setPosition(newPosition);

                        Node newNode = new Node(
                                newSector,
                                0,
                                node.getDepth() + 1,
                                node,
                                new ArrayList<Node>());

                        int score = evaluateSector(newSector);
                        newNode.setScore(score);

                        newNodes.add(newNode);
                    }
                }

                // increase speed
                if (cAircraft.getVector().getMagnitude() < cAircraft.getMaxCruiseSpeed()) {
                    double newSpeed = cAircraft.getVector().getMagnitude() + 100;
                    if (newSpeed <= cAircraft.getMaxCruiseSpeed()) {
                        // create new sector
                        Sector newSector = new Sector(node.getSector());

                        // find conflict aircraft in new sector
                        List<Aircraft> aircrafts = newSector.getAircrafts();
                        Aircraft aircraft = aircrafts.get(aircrafts.indexOf(cAircraft));

                        aircraft.getVector().setMagnitude(newSpeed);

                        Node newNode = new Node(
                                newSector,
                                0,
                                node.getDepth() + 1,
                                node,
                                new ArrayList<Node>());

                        int score = evaluateSector(newSector);
                        newNode.setScore(score);

                        newNodes.add(newNode);
                    }
                }

                // decrease speed
                if (cAircraft.getVector().getMagnitude() <= cAircraft.getMaxCruiseSpeed()) {
                    double newSpeed = cAircraft.getVector().getMagnitude() - 100;
                    if (newSpeed <= cAircraft.getMaxCruiseSpeed()) {
                        // create new sector
                        Sector newSector = new Sector(node.getSector());

                        // find conflict aircraft in new sector
                        List<Aircraft> aircrafts = newSector.getAircrafts();
                        Aircraft aircraft = aircrafts.get(aircrafts.indexOf(cAircraft));

                        aircraft.getVector().setMagnitude(newSpeed);

                        Node newNode = new Node(
                                newSector,
                                0,
                                node.getDepth() + 1,
                                node,
                                new ArrayList<Node>());

                        int score = evaluateSector(newSector);
                        newNode.setScore(score);

                        newNodes.add(newNode);
                    }
                }
            }
        }

        return newNodes;
    }

    public void relocatedSubTree(Node node, Node newParent) {
        Node oldParent = node.getParent();

        // remove node from the old parent's children list
        List<Node> oldParentChildren = oldParent.getChildren();
        for (Node n : oldParentChildren) {
            if (node.equals(n)) {
                oldParentChildren.remove(n);
                break;
            }
        }

        node.setParent(newParent);
        newParent.getChildren().add(node);

        // change depth of all the children nodes
        Queue<Node> fringe = new LinkedList<Node>();
        fringe.add(node);

        while (!fringe.isEmpty()) {
            Node n = fringe.poll();
            n.setDepth(n.getParent().getDepth() + 1);
            fringe.addAll(n.getChildren());
        }
    }

    public Sector getBestSector(Sector sector) {
        Sector bestSector = null;

        Node rootNode = new Node(sector, 0, 0, null, new ArrayList<Node>());
        rootNode.setScore(evaluateSector(rootNode.getSector()));

        // open
        Comparator<Node> cmp = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getScore() + o1.getDepth() < o2.getScore() + o2.getDepth())
                    return -1;
                else if (o1.getScore() + o1.getDepth() > o2.getScore() + o2.getDepth())
                    return 1;
                else
                    return 0;
            }
        };
        PriorityQueue<Node> fringe = new PriorityQueue<Node>(cmp);

        // closed
        HashMap<Node, Node> visitedNodes = new HashMap<Node, Node>();

        fringe.add(rootNode);
        while (!fringe.isEmpty()) {
            Node node = fringe.poll();

            if (node.getScore() == 0) {
                bestSector = node.getSector();
                break;
            }

            List<Node> newNodes = expand(node);
            for (Node n : newNodes) {
                if (!visitedNodes.containsKey(n)) {
                    fringe.add(n);
                }
                else {
                    Node visitedNode = visitedNodes.get(node);
                    if (cmp.compare(node, visitedNode) == -1)
                        relocatedSubTree(visitedNode, node);
                }
            }

            visitedNodes.put(node, node);
        }


        return bestSector;
    }
}
