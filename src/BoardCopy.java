import edu.princeton.cs.algs4.Queue;


public class BoardCopy {
    private final int n;
    private final int[][] boardTiles;
    private int manhattenDistance;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public BoardCopy(int[][] tiles) {
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
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = this.boardTiles[i][j];
                int pos = position(i, j);
                if (value != 0 && value != pos) {

                    int diff = Math.abs(pos - value);
                    count += diff / n + diff % n;
                    System.out.println("Value: " + value + " --- " + diff / n + diff % n);
                }
            }
        }
        return count;
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
    public Iterable<BoardCopy> neighbors() {
        return new Queue<>();
    }

    // a board that is obtained by exchanging any pair of tiles
    public BoardCopy twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5}

        };
        BoardCopy initial = new BoardCopy(tiles);
        System.out.println("Hamming: " + initial.hamming());
        System.out.println("Manhattan " + initial.manhattan());

    }

    private boolean check(int x, int y, int value) {
        int position = position(x, y);
        System.out.println("x: " + x + " y: " + y + " Position: " + position + " value: " + value);
        if (value == 0) {
            return true;
        }
        return position == value;
    }

    private int position(int x, int y) {
        return y + n * x + 1;
    }

}
