import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;


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
        root = insert(root, p, new RectHV(0, 0, 1, 1), true);
    }

    // currentX = true means that the insertion is based on x coordinate; or y coordinate otherwise
    private Node insert(Node node, Point2D point, RectHV rect, boolean currentX) {
        if (node == null) return new KdTree.Node(point, rect, null, null, 1);
        if (currentX) {
            if (point.x() < node.p.x()) {
                node.lb = insert(node.lb,
                        point,
                        new RectHV(
                                rect.xmin(), rect.ymin(), node.p.x(), rect.ymax()
                        ),
                        false);
            }
            else if (point.x() > node.p.x()) {
                node.rt = insert(node.rt,
                        point,
                        new RectHV(
                                node.p.x(), rect.ymin(), rect.xmax(), rect.ymax()
                        ),
                        false);
            }
            else {
                node.p = point;
            }
        } else {
            if (point.y() < node.p.y()) {
                node.lb = insert(node.lb,
                        point,
                        new RectHV(
                                rect.xmin(), rect.ymin(), rect.xmax(), node.p.y()
                        ),
                        true);
            }
            else if (point.y() > node.p.y()) {
                node.rt = insert(node.rt,
                        point,
                        new RectHV(
                                rect.xmin(), node.p.y(), rect.xmax(), rect.ymax()
                        ),
                        true);
            }
            else {
                node.p = point;
            }
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
            if (point.x() < x.p.x()) {
                return get(x.lb, point, false);
            }
            else if (point.x() > x.p.x()) {
                return get(x.rt, point, false);
            }
            else {
                return x.p.equals(point) ? x.p : null;
            }
        } else {
            if (point.y() < x.p.y()) {
                return get(x.lb, point, true);
            }
            else if (point.y() > x.p.y()) {
                return get(x.rt, point, true);
            }
            else {
                return x.p.equals(point) ? x.p : null;
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        Stack<Node> nodes = new Stack<>();

        Node current = this.root;

        boolean vertical = true;
        while (!nodes.isEmpty() || current != null) {
            if (current != null) {
                nodes.push(current);
                current = current.lb;
            } else {
                Node node = nodes.pop();

                StdDraw.setPenColor(Color.BLACK);
                StdDraw.point(node.p.x(), node.p.y());
                StdDraw.setPenRadius(0.006);
                if (vertical) {
                    StdDraw.setPenColor(Color.RED);
                    StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                } else {
                    StdDraw.setPenColor(Color.BLUE);
                    StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
                }
                StdDraw.setPenRadius(0.010);
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.point(node.p.x(), node.p.y());

                current = node.rt;
            }

            vertical = !vertical;
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Node> nodes = new Stack<>();
        Queue<Point2D> pointsInRange = new Queue<>();
        if (rect == null) {
            return pointsInRange;
        }
        Node current = this.root;

        while (!nodes.isEmpty() || current != null) {
            if (current != null) {
                nodes.push(current);
                current = current.lb;
                if ((current != null) && (current.rect.intersects(rect))) {
                    if (rect.contains(current.p)) {
                        pointsInRange.enqueue(current.p);
                    }
                }
            } else {
                Node node = nodes.pop();

                current = node.rt;
                if ((current != null) && (current.rect.intersects(rect))) {
                    if (rect.contains(current.p)) {
                        pointsInRange.enqueue(current.p);
                    }
                }
            }
        }

        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty() || p == null) {
            return null;
        }

        Stack<Node> nodes = new Stack<>();
        Point2D nearest = null;
        double minDistance = Double.MAX_VALUE;
        Node current = this.root;

        while (!nodes.isEmpty() || current != null) {
            if (current != null) {
                nodes.push(current);
                current = current.lb;
                if ((current != null) && (current.rect.contains(p))) {
                    double currentDistance = current.p.distanceSquaredTo(p);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        nearest = current.p;
                    }
                }
            } else {
                Node node = nodes.pop();

                current = node.rt;
                if ((current != null) && (current.rect.contains(p))) {
                    double currentDistance = current.p.distanceSquaredTo(p);
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        nearest = current.p;
                    }
                }
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.8, 0.8));
        tree.insert(new Point2D(0.8, 0.4));
        tree.draw();



        System.out.println(tree.contains(new Point2D(0.2, 0.2)));
        System.out.println(tree.contains(new Point2D(0.9, 0.6)));
        System.out.println(tree.contains(new Point2D(0.5, 0.4)));
        System.out.println(tree.contains(new Point2D(0.9, 0.4)));
        System.out.println(tree.contains(new Point2D(0.8, 0.4)));
        System.out.println(tree.contains(new Point2D(0.4, 0.4)));
        System.out.println(tree.contains(new Point2D(0.1, 0.5)));
        System.out.println(tree.size());

        KdTree tree2 = new KdTree();
        tree2.insert(new Point2D(0.0, 0.0));
        tree2.insert(new Point2D(0.1, 0.4));
        tree2.insert(new Point2D(0.6, 0.5));

        StdDraw.setPenRadius(0.005);
        RectHV rect = new RectHV(0.4, 0.3, 0.8, 0.6);
        rect.draw();
        Iterable<Point2D> result = tree2.range(rect);
        for (Point2D p : result) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.point(p.x(), p.y());
            System.out.println("In range: " + p);
        }

        System.out.println("Nearest " + tree2.nearest(new Point2D(0.7, 0.7)));
    }

}
