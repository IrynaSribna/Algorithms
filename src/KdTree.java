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
        private Node lb;              // the left/bottom subtree
        private Node rt;              // the right/top subtree
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
        return x.size;
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
            if (point.x() < node.p.x() && !point.equals(node.p)) {
                node.lb = insert(node.lb,
                        point,
                        node.lb != null
                                ? node.lb.rect
                                : new RectHV(
                                    rect.xmin(), rect.ymin(), node.p.x(), rect.ymax()
                                    ),
                        false);
            }
            else if (point.x() >= node.p.x() && !point.equals(node.p)) {
                node.rt = insert(node.rt,
                        point,
                        node.rt != null
                            ? node.rt.rect
                            : new RectHV(
                                node.p.x(), rect.ymin(), rect.xmax(), rect.ymax()
                                ),
                        false);
            }

        } else {
            if (point.y() < node.p.y() && !point.equals(node.p)) {
                node.lb = insert(node.lb,
                        point,
                        node.lb != null
                                ? node.lb.rect
                                : new RectHV(
                                    rect.xmin(), rect.ymin(), rect.xmax(), node.p.y()
                                    ),
                        true);
            }
            else if (point.y() >= node.p.y() && !point.equals(node.p)) {
                node.rt = insert(node.rt,
                        point,
                        node.rt != null
                                ? node.rt.rect
                                : new RectHV(
                                    rect.xmin(), node.p.y(), rect.xmax(), rect.ymax()
                                    ),
                        true);
            }
        }
        node.size = 1 + size(node.lb) + size(node.rt);
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

        if (x.p.equals(point)) {
            return x.p;
        }
        if (currentX) {
            if (point.x() < x.p.x()) {
                return get(x.lb, point, false);
            }
            else if (point.x() >= x.p.x()) {
                return get(x.rt, point, false);
            }
        } else {
            if (point.y() < x.p.y()) {
                return get(x.lb, point, true);
            }
            else if (point.y() >= x.p.y()) {
                return get(x.rt, point, true);
            }
        }

        return null;
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

        if (this.root.rect.intersects(rect)) {
            if (rect.contains(this.root.p)) {
                pointsInRange.enqueue(this.root.p);
            }
        }

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

//        KdTree tree = new KdTree();
//        tree.insert(new Point2D(0.7, 0.2));
//        tree.insert(new Point2D(0.5, 0.4));
//        tree.insert(new Point2D(0.9, 0.6));
//        tree.insert(new Point2D(0.2, 0.3));
//        tree.insert(new Point2D(0.4, 0.7));
//        tree.insert(new Point2D(0.9, 0.6));
//        tree.insert(new Point2D(0.4, 0.7));
//        tree.insert(new Point2D(0.8, 0.8));
//        tree.insert(new Point2D(0.8, 0.4));
//        tree.draw();
//
//        KdTree tree2 = new KdTree();
//        tree2.insert(new Point2D(0.0, 0.0));
//        tree2.insert(new Point2D(0.1, 0.4));
//        tree2.insert(new Point2D(0.6, 0.5));
//
//        StdDraw.setPenRadius(0.005);
//        RectHV rect = new RectHV(0.4, 0.3, 0.8, 0.6);
//        rect.draw();
//        Iterable<Point2D> result = tree2.range(rect);
//        for (Point2D p : result) {
//            StdDraw.point(p.x(), p.y());
//        }
//
//        KdTree tree3 = new KdTree();
//        tree3.insert(new Point2D(0.25, 1));
//        tree3.insert(new Point2D(0.25, 0.25));
//
//        KdTree tree4 = new KdTree();
//        tree4.insert(new Point2D(1.0, 0));
//        tree4.insert(new Point2D(0, 0));
//        tree4.insert(new Point2D(1, 0));
//        tree4.insert(new Point2D(0, 0));
//        tree4.insert(new Point2D(1, 1));
//
//        KdTree kdTree = new KdTree();
//        kdTree.insert(new Point2D(0.372, 0.497));
//        kdTree.insert(new Point2D(0.564, 0.413));
//        kdTree.insert(new Point2D(0.226, 0.577));
//        kdTree.insert(new Point2D(0.144, 0.179));
//        kdTree.insert(new Point2D(0.083, 0.51));
//        kdTree.insert(new Point2D(0.32, 0.708));
//        kdTree.insert(new Point2D(0.417, 0.362));
//        kdTree.insert(new Point2D(0.862, 0.825));
//        kdTree.insert(new Point2D(0.785, 0.725));
//        kdTree.insert(new Point2D(0.499, 0.208));
//        System.out.println(kdTree.contains(new Point2D(0.32, 0.99)));

//        KdTree kdTree = new KdTree();
//        kdTree.insert(new Point2D(0.125, 0.25));
//        kdTree.insert(new Point2D(0.25, 0.25));
//        kdTree.insert(new Point2D(0.75, 0.125));
//        kdTree.insert(new Point2D(0.75, 1.0));
//        kdTree.insert(new Point2D(1.0, 0.25));
//        kdTree.insert(new Point2D(0.375, 0.5));
//        kdTree.insert(new Point2D(0.0, 0.125));
//        kdTree.insert(new Point2D(0.875, 0.125));
//        kdTree.insert(new Point2D(0.25, 0.375));
//        kdTree.insert(new Point2D(0.75, 0.25));
//        kdTree.insert(new Point2D(0.375, 0.25));
//        kdTree.insert(new Point2D(0.0, 0.5));
//        kdTree.insert(new Point2D(0.5, 0.125));
//        kdTree.insert(new Point2D(0.875, 0.625));
//        kdTree.insert(new Point2D(0.75, 0.0));
//        kdTree.insert(new Point2D(0.625, 0.0));
//        kdTree.insert(new Point2D(1.0, 1.0));
//        kdTree.insert(new Point2D(0.0, 0.375));
//        kdTree.insert(new Point2D(0.75, 0.5));
//        kdTree.insert(new Point2D(1.0, 0.875));
//        System.out.println(kdTree.contains(new Point2D(0.125, 0.25)) &&
//
//        kdTree.contains(new Point2D(0.25, 0.25)) &&
//        kdTree.contains(new Point2D(0.75, 0.125)) &&
//        kdTree.contains(new Point2D(0.75, 1.0)) &&
//        kdTree.contains(new Point2D(1.0, 0.25)) &&
//        kdTree.contains(new Point2D(0.375, 0.5)) &&
//        kdTree.contains(new Point2D(0.0, 0.125)) &&
//        kdTree.contains(new Point2D(0.875, 0.125)) &&
//        kdTree.contains(new Point2D(0.25, 0.375)) &&
//        kdTree.contains(new Point2D(0.75, 0.25)) &&
//        kdTree.contains(new Point2D(0.375, 0.25)) &&
//        kdTree.contains(new Point2D(0.0, 0.5)) &&
//        kdTree.contains(new Point2D(0.5, 0.125)) &&
//        kdTree.contains(new Point2D(0.875, 0.625)) &&
//        kdTree.contains(new Point2D(0.75, 0.0)) &&
//        kdTree.contains(new Point2D(0.625, 0.0)) &&
//        kdTree.contains(new Point2D(1.0, 1.0)) &&
//        kdTree.contains(new Point2D(0.0, 0.375)) &&
//        kdTree.contains(new Point2D(0.75, 0.15)) &&
//        kdTree.contains(new Point2D(1.0, 0.875)));

       RectHV queryRect = new RectHV(0.28, 0.089, 0.953, 0.269);
       KdTree kdTree = new KdTree();
       kdTree.insert(new Point2D(0.7, 0.2));
       kdTree.insert(new Point2D(0.5, 0.4));
       kdTree.insert(new Point2D(0.2, 0.3));
       kdTree.insert(new Point2D(0.4,0.7));
       kdTree.insert(new Point2D(0.9, 0.6));
        Iterable<Point2D> result = kdTree.range(queryRect);
        for (Point2D p : result) {
            System.out.print(p + ", ");
        }

        System.out.println();
        System.out.println("========================");

        PointSET set = new PointSET();
        set.insert(new Point2D(0.7, 0.2));
        set.insert(new Point2D(0.5, 0.4));
        set.insert(new Point2D(0.2, 0.3));
        set.insert(new Point2D(0.4,0.7));
        set.insert(new Point2D(0.9, 0.6));

        Iterable<Point2D> setResult = set.range(queryRect);
        for (Point2D p : setResult) {
            System.out.print(p + ", ");
        }
    }
}
