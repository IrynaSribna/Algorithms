import edu.princeton.cs.algs4.StdDraw;


public class RectHV {

    private final double xmin;
    private final double ymin;
    private final double xmax;
    private final double ymax;

    // construct the rectangle [xmin, xmax] x [ymin, ymax]
    // // throw a java.lang.IllegalArgumentException if (xmin > xmax) or (ymin > ymax)
    public RectHV(double xmin, double ymin,
                     double xmax, double ymax) {

        if ((xmin > xmax) || (ymin > ymax)) {
            throw new IllegalArgumentException();
        }

        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    // minimum x-coordinate of rectangle
    public double xmin() {
        return this.xmin;
    }

    // minimum y-coordinate of rectangle
    public double ymin() {
        return this.ymin;
    }

    // maximum x-coordinate of rectangle
    public double xmax() {
        return this.xmax;
    }

    // maximum y-coordinate of rectangle
    public double ymax() {
        return this.ymax;
    }

    // does this rectangle contain the point p (either inside or on boundary)?
    public boolean contains(Point2D p) {
        return  (p.x() >= this.xmin) && (p.x() <= this.xmax)
            && (p.y() >= this.ymin) && (p.y() < this.ymax());
    }

    // does this rectangle intersect that rectangle (at one or more points)?
    public boolean intersects(RectHV that) {
        return this.xmax >= that.ymin && this.ymax >= that.ymin
            && that.xmax >= this.xmin && that.ymax >= this.ymin;
    }

    // Euclidean distance from point p to closest point in rectangle
    public double distanceTo(Point2D p) {
        return Math.sqrt(distanceSquaredTo(p));
    }

    // square of Euclidean distance from point p to closest point in rectangle
    public double distanceSquaredTo(Point2D p) {
        double dx = 0.0;
        double dy = 0.0;
        if (p.x() < xmin) {
            dx = xmin - p.x();
        } else if (p.x() >= xmax) {
            dx = p.x() - xmax;
        }

        if (p.y() >= ymax) {
            dy = p.y() - ymax;
        } else if (p.y() <= ymin) {
            dy = ymin - p.y();
        }
        return dx*dx + dy*dy;
    }

    // does this rectangle equal that object?
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass().equals(o.getClass())) {
            return false;
        }
        RectHV rectHV = (RectHV) o;
        return Double.compare(rectHV.xmin, xmin) == 0 &&
            Double.compare(rectHV.ymin, ymin) == 0 &&
            Double.compare(rectHV.xmax, xmax) == 0 &&
            Double.compare(rectHV.ymax, ymax) == 0;
    }

    // draw to standard draw
    public void draw() {
        StdDraw.line(xmin, ymin, xmax, ymin);
        StdDraw.line(xmin, ymax, xmax, ymax);
        StdDraw.line(xmin, ymin, xmin, ymax);
        StdDraw.line(xmax, ymin, xmax, ymax);

    }

    // string representation

    @Override
    public String toString() {
        return "RectHV{" +
            "xmin=" + xmin +
            ", ymin=" + ymin +
            ", xmax=" + xmax +
            ", ymax=" + ymax +
            '}';
    }

    public static void main(String... args) {
        Point2D p = new Point2D(0.1, 0.4);
        RectHV r = new RectHV(0.4, 0.3, 0.8, 0.6);
        RectHV r2 = new RectHV(0.7, 0.4, 0.9, 0.5);
//        r.draw();
//        p.draw();
//        r2.draw();
        System.out.println(r.distanceTo(p));
        System.out.println(r2.intersects(r));

        RectHV rec1 = new RectHV(0.0, 0.0, 0.6, 0.3);
        RectHV rec2 = new RectHV(0.4, 0.2, 0.8, 0.8);
        rec1.draw();
        rec2.draw();
        System.out.println(rec1.intersects(rec2));
    }
}
