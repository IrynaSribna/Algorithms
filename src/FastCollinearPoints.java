import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
    private final Point[] pointsToCheck;
    private final LineSegment[] segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Null array");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Null element");
            }
        }

        validateForDuplicates(points);

        Point[] copy = points.clone(); // defensive copy
        Arrays.sort(copy);
        this.pointsToCheck = copy;

        for (int i = 0; i < this.pointsToCheck.length; i++) {
            Point origin = pointsToCheck[i];
            for (int j = i + 1; j < this.pointsToCheck.length - 1; j++) {

            }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.clone();
    }

    private void validateForDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) { // got the duplicate element
                    throw new IllegalArgumentException("Duplicate point");
                }
            }
        }
    }

    public static void main(String[] args) {
        Point[] points2 = new Point[] {
                new Point(6, 8),
                new Point(6, 4),
                new Point(7, 8),
                new Point(10, 0),
                new Point(3, 2),
                new Point(6, 34),
                new Point(2, 11),
                new Point(4, 4),
                new Point(5, 6),
                new Point(8, 2)
        };

        FastCollinearPoints collinearPoints = new FastCollinearPoints(points2);

        for (Point p : collinearPoints.points) {
            System.out.println(p);
        }

        for (LineSegment s : collinearPoints.segments()) {
            if (s != null) {
                System.out.println(s);
            }
        }

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
