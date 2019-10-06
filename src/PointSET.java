import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;


public class PointSET {

    private final SET<Point2D> pointsSet;

    // construct an empty set of points
    public PointSET() {
        pointsSet = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointsSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointsSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointsSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointsSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : pointsSet) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<Point2D> pointsInsideRect = new Queue<>();
        if (isEmpty()) {
            return  pointsInsideRect;
        }
        for (Point2D point : pointsSet) {
            if (rect.contains(point)) {
                pointsInsideRect.enqueue(point);
            }
        }

        return pointsInsideRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D nearestPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : pointsSet) {
            double currentDistance = p.distanceSquaredTo(point);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        PointSET set = new PointSET();
        set.insert(new Point2D(0.0, 0.0));
        set.insert(new Point2D(0.1, 0.4));
        set.insert(new Point2D(0.6, 0.5));

        StdDraw.setPenRadius(0.005);
        set.draw();
        RectHV rect = new RectHV(0.4, 0.3, 0.8, 0.6);
        rect.draw();
        Iterable<Point2D> result = set.range(rect);
        for (Point2D p : result) {
            StdDraw.point(p.x(), p.y());
            System.out.println("In range: " + p);
        }

        System.out.println("Nearest point: " + set.nearest(new Point2D(0.7, 0.7)));

    }

}
