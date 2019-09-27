import edu.princeton.cs.algs4.StdDraw;


public class Point2D implements Comparable<Point2D> {

    private final double x;
    private final double y;

    // construct the point (x, y)
    public Point2D(double x, double y) {
        if (x == 0.0) {
            this.x = 0.0;  // convert -0.0 to +0.0
        } else {
            this.x = x;
        }

        if (y == 0.0) {
            this.y = 0.0;  // convert -0.0 to +0.0
        }
        else {
            this.y = y;
        }
    }

    // x-coordinate
    public double x() {
        return this.x;
    }

    // y-coordinate
    public double y() {
        return this.y;
    }

    // Euclidean distance between two points
    public double distanceTo(Point2D that) {
        if (that == null) {
            throw new IllegalArgumentException();
        }
        return Math.sqrt(distanceSquaredTo(that));
    }

    // square of Euclidean distance between two points
    public double distanceSquaredTo(Point2D that) {
        if (that == null) {
            throw new IllegalArgumentException();
        }

        double dx = that.x - this.x;
        double dy = that.y - this.y;

        return dx*dx + dy*dy;
    }

    @Override
    public int compareTo(Point2D o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }

        if (this.y < o.y) return -1;
        if (this.y > o.y) return +1;
        if (this.x < o.x) return -1;
        if (this.x > o.x) return +1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        Point2D point2D = (Point2D) o;
        return Double.compare(point2D.x, x) == 0 &&
            Double.compare(point2D.y, y) == 0;
    }

    // draw to standard draw
    public    void draw()  {
        StdDraw.point(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Point2D{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
