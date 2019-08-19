package AI_ATC;

import ATC_Core.Sector;

import java.util.List;

/**
 * Created by saija on 2017-04-11.
 */
public class Node {
    private Sector sector;
    private int score;
    private int depth;

    private Node parent;
    private List<Node> children;

    public Node(Sector sector, int score, int depth, Node parent, List<Node> children) {
        this.sector = sector;
        this.score = score;
        this.depth = depth;

        this.parent = parent;
        this.children = children;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Node) {
            Node otherNode = (Node) other;

            return sector.equals(otherNode.sector);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return sector.hashCode();
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setParent(Node newParent) {
        parent = newParent;
    }

    public Sector getSector() {
        return sector;
    }

    public int getScore() {
        return score;
    }

    public int getDepth() {
        return depth;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }
}
