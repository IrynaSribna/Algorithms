import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <=0 || trials<=0) {
            throw new IllegalArgumentException("Invalid arguments. " +
                "Positive integers are expected");
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return 0L;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return 0L;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return 0L;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return 0L;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = 0; // grid size
        int t = 0; // number of experiments
        if (args.length == 2) {
            try {
                n = Integer.parseInt(args[0]);
                t = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Arguments should be of Integer type. " +
                    "Provided arguments: " + args[0] + " " + args[1]);
                System.exit(1);
            }
        } else {
            System.err.println("Expected to get 2 arguments. " +
                "Amount of the provided arguments: " + args.length);
            System.exit(1);
        }

        new PercolationStats(n, t);
    }

}
