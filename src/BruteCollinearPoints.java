import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/*
 Finds all line segments containing 4 points
 */
public class BruteCollinearPoints {
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        List<LineSegment> list = new LinkedList<>();
        if (points == null) {
            throw new IllegalArgumentException("Null array");
        }

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Null element");
            }
        }

        validateForDuplicates(points);
        Arrays.sort(points);
        Point[] copy = points.clone();

        for (int i = 0; i < copy.length - 3; i++) {
            Point a = copy[i];
            for (int j = i + 1; j < copy.length - 2; j++) {
                Point b = copy[j];
                double slopeAB = a.slopeTo(b);

                for (int k = j + 1; k < copy.length - 1; k++) {
                    Point c = copy[k];
                    double slopeAC = a.slopeTo(c);
                    if (slopeAB == slopeAC) {
                        for (int g = k + 1; g < points.length; g++) {
                            Point d = copy[g];

                            double slopeAD = a.slopeTo(d);
                            if (slopeAB == slopeAD) {
                                list.add(new LineSegment(a, d));
                            }

                        }
                    }
                }
            }
        }

        lineSegments = list.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments()     {
        return lineSegments.clone();
    }


    public static void main(String[] args) {
        Point[] points = null;

        Point[] points2 = {
                new Point(2, 8),
                new Point(6, 4),
                new Point(4, 6),
                new Point(8, 2),
                new Point(1, 15),
                new Point(6, 9)
        };


        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points2);
        for (LineSegment s : bruteCollinearPoints.segments()) {
            System.out.println(s);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points2) {
            p.draw();
        }
        StdDraw.show();

        for (LineSegment s : bruteCollinearPoints.segments()) {
            StdOut.println(s);
            s.draw();
        }
        StdDraw.show();
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
}
