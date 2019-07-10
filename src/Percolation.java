import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private int[][] grid;
    WeightedQuickUnionUF union;
    private int size;
    private int n;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <=0) {
            throw new IllegalArgumentException("Invalid grid size");
        }
        grid = new int[n][n];
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }

        union = new WeightedQuickUnionUF(n);
        size = n * n;
        this.n = n;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        //union.find()
        return grid[row][col] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return 0;
    }

    // does the system percolate?
    public boolean percolates() {
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        System.out.println(percolation.xyTo1D(2, 0));
    }

    private int xyTo1D(int x, int y) {
        return x + n * y;
    }


}
