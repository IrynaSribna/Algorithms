import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;


public class KdTree {

    private Node root;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, RectHV rect, Node lb, Node rt) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "p=" + p +
                    ", rect=" + rect +
                    ", lb=" + lb +
                    ", rt=" + rt +
                    '}';
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

        return 1 + size(x.lb) + size(x.rt);
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
        if (node == null) return new KdTree.Node(point, rect, null, null);
        if (currentX) {
            if (point.x() < node.p.x() && !point.equals(node.p)) {
                node.lb = insert(node.lb,
                        point,
                        new RectHV(
                                rect.xmin(), rect.ymin(), node.p.x(), rect.ymax()
                        ),
                        false);
            }
            else if (point.x() >= node.p.x() && !point.equals(node.p)) {
                node.rt = insert(node.rt,
                        point,
                        new RectHV(
                                node.p.x(), rect.ymin(), rect.xmax(), rect.ymax()
                        ),
                        false);
            }

        } else {
            if (point.y() < node.p.y() && !point.equals(node.p)) {
                node.lb = insert(node.lb,
                        point,
                        new RectHV(
                                rect.xmin(), rect.ymin(), rect.xmax(), node.p.y()
                        ),
                        true);
            }
            else if (point.y() >= node.p.y() && !point.equals(node.p)) {
                node.rt = insert(node.rt,
                        point,
                        new RectHV(
                                rect.xmin(), node.p.y(), rect.xmax(), rect.ymax()
                        ),
                        true);
            }
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Argument to contains() is null");

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
                return x.p;
            }
        } else {
            if (point.y() < x.p.y()) {
                return get(x.lb, point, true);
            }
            else if (point.y() > x.p.y()) {
                return get(x.rt, point, true);
            }
            else {
                return x.p;
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

                StdDraw.point(node.p.x(), node.p.y());
                if (vertical) {
                    StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
                } else {
                    StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
                }
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
            throw new IllegalArgumentException("Input argument rect must be not null");
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
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }

        Stack<Node> nodes = new Stack<>();
        Point2D nearest = null;
        double minDistance = Double.POSITIVE_INFINITY;
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
            StdDraw.point(p.x(), p.y());
            System.out.println("In range: " + p);
        }

        System.out.println("Nearest " + tree2.nearest(new Point2D(0.7, 0.7)));

        KdTree tree3 = new KdTree();
        tree3.insert(new Point2D(0.25, 1));
        tree3.insert(new Point2D(0.25, 0.25));
        System.out.println("Tree3 size: " + tree3.size());
        System.out.println(tree3);

        System.out.println("==============");
        KdTree tree4 = new KdTree();
        tree4.insert(new Point2D(1.0, 0));
        tree4.insert(new Point2D(0, 0));
        tree4.insert(new Point2D(1, 0));
        tree4.insert(new Point2D(0, 0));
        tree4.insert(new Point2D(1, 1));
        System.out.println("Tree4 size: " + tree4.size());
        System.out.println(tree4);

    }

    @Override
    public String toString() {
        return "KdTree{" +
                "root=" + root +
                '}';
    }
}
