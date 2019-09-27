import java.util.TreeSet;

import edu.princeton.cs.algs4.BST;


public class KdTree {

    private Node root;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;

        public Node(Point2D p, RectHV rect, Node lb, Node rt, int size) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.size = size;
        }
    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return size(root) == 0;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Insert was called with null Point");
        }
        root = insert(root, p, true);
    }

    private Node insert(Node node, Point2D point, boolean currentX) {
        if (node == null) return new KdTree.Node(point, null, null, null, 1);
        if (currentX) {
            if (point.x() < node.p.x()) node.lb = insert(node.lb, point, false);
            else if (point.x() > node.p.x()) node.rt = insert(node.rt, point, false);
            else node.p = point;
        } else {
            if (point.y() < node.p.y()) node.lb = insert(node.lb, point, true);
            else if (point.y() > node.p.y()) node.rt = insert(node.rt, point, true);
            else node.p = point;
        }

        node.size = 1 + size(node.lb) + size(node.rt);
        return node;

    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Argument to contains() is null");

        System.out.println("Get returns: " + get(point));
        return get(point) != null;
    }

    private Point2D get(Point2D point) {
        return get(root, point, true);
    }

    private Point2D get(Node x, Point2D point, boolean currentX) {
        if (point == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;

        if (currentX) {
            if (point.x() < x.p.x()) return get(x.lb, point, false);
            else if (point.x() > x.p.x()) return get(x.rt, point, false);
            else return x.p;
        } else {
            if (point.y() < x.p.y()) return get(x.lb, point, true);
            else if (point.y() < x.p.y()) return get(x.rt, point, true);
            else return x.p;
        }
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return null;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.8, 0.8));



        System.out.println(tree.contains(new Point2D(0.2, 0.2)));
        System.out.println(tree.contains(new Point2D(0.9, 0.6)));
        System.out.println(tree.contains(new Point2D(0.5, 0.4)));
        System.out.println(tree.contains(new Point2D(0.9, 0.4)));
//        System.out.println(tree.contains(new Point2D(0.4, 0.4)));
//        System.out.println(tree.contains(new Point2D(0.1, 0.5)));
        System.out.println(tree.size());
    }

}
