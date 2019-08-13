import edu.princeton.cs.algs4.Queue;


public class Board {
    private final int n;
    private final int[][] boardTiles;
    private int manhattenDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles[0].length;
        this.boardTiles = tiles.clone();
    }

    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(String.format("%2d ", boardTiles[i][j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!check(i, j, this.boardTiles[i][j])) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return 0;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Queue<>();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
            {1, 2, 3},
            {4, 0, 5},
            {7, 8, 6}

        };
        Board initial = new Board(tiles);
        System.out.println("Hamming: " + initial.hamming());

    }

    private boolean check(int x, int y, int value) {
        int position = y + n * x + 1;
        System.out.println("x: " + x + " y: " + y + " Position: " + position + " value: " + value);
        if (value == 0) {
            return true;
        }
        return position == value;
    }

}
