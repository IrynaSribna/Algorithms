import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;


public class Board {
    private final int n;
    private final int length;
    private final int[] pieces;
    private int zeroPosition;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles[0].length;
        this.length = n * n;
        pieces = new int[length];
        int[][] copy = tiles.clone();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int position = getPosition(i, j);
                pieces[position] = copy[i][j];
                if (pieces[position] == 0) {
                    this.zeroPosition = position;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n).append("\n");
        for (int i = 0; i < n * n; i = i + n) {
            for (int j = 0; j < n; j++) {
                builder.append(String.format("%2d ", pieces[i + j]));
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
        for (int i = 0; i < length; i++) {
            if (pieces[i] != 0 && pieces[i] != i + 1) {
                count++;
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < length; i++) {
            int value = pieces[i];
            if (value != 0 && value != i + 1) {
                count += Math.abs(expectedCol(value)-actualCol(i))
                    + Math.abs(expectedRow(value) - actualRow(i));
            }
        }

        return count;
    }

    private int expectedCol(int value) {
        int mod = value % n;
        return mod == 0 ? n : mod;
    }

    private int expectedRow(int value) {
        int div = value / n;
        int mod = value % n;
        return mod == 0 ? div : div + 1;
    }

    private int actualCol(int position) {
        return position % n + 1;
    }

    private int actualRow(int position) {
        return position / n + 1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Board board = (Board) other;
        return n == board.n &&
            manhattan() == board.manhattan() &&
            Arrays.deepEquals(new int[][]{pieces}, new int[][]{board.pieces});
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Board left = null;
        Board right = null;
        Board top = null;
        Board bottom = null;
        if (shouldAddTop()) {
            top = swap(zeroPosition, zeroPosition - n);
        }

        if (shouldAddLeft()) {
            left = swap(zeroPosition, zeroPosition + 1);
        }

        if (shouldAddDown()) {
            bottom = swap(zeroPosition, zeroPosition + n);
        }

        if (shouldAddRight()) {
            right = swap(zeroPosition, zeroPosition - 1);
        }

        Queue<Board> neighbors = new Queue<>();
        if (left != null && !left.equals(this)) {
            neighbors.enqueue(left);
        }

        if (right != null && !right.equals(this)) {
            neighbors.enqueue(right);
        }

        if (top != null && !top.equals(this)) {
            neighbors.enqueue(top);
        }

        if (bottom != null && !bottom.equals(this)) {
            neighbors.enqueue(bottom);
        }

        return neighbors;
    }

    private Board swap(int fromPosition, int toPosition) {
        int[][] shiftedPieces2D = new int[n][n];
        int[] shiftedPieces = this.pieces.clone();
        int temp = shiftedPieces[fromPosition];
        shiftedPieces[fromPosition] = shiftedPieces[toPosition];
        shiftedPieces[toPosition] = temp;
        for (int i = 0; i < this.length; i++) {
            shiftedPieces2D[i / n][ i % n] = shiftedPieces[i];
        }
        return new Board(shiftedPieces2D);
    }

    private boolean shouldAddLeft() {
        int leftPosition = zeroPosition + 1;
        return !(leftPosition % n == 0);
    }

    private boolean shouldAddRight() {
        return !(zeroPosition % n == 0);
    }

    private boolean shouldAddTop() {
        return zeroPosition > n - 1;
    }

    private boolean shouldAddDown() {
        return zeroPosition < length - n;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int first, second;
        if (zeroPosition == 0) {
            first = 1;
            second = 2;
        } else if (zeroPosition == 1) {
            first = 0;
            second = 2;
        } else {
            first = 0;
            second = 1;
        }
        return swap(first, second);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6}

        };

        int [][] tiles1 = {
            {1, 0},
            {2, 3}
        };

        int [][] tiles3 = {
            {1, 6, 4},
            {2, 7, 8},
            {5, 0, 3}
        };

        Board board = new Board(tiles);
        Board board1 = new Board(tiles1);
        Board board2 = new Board(tiles3);

        if (board.equals(board1)) {
            if (board1.equals(board2)) {
              board1.neighbors();
            }
        }

    }

    private int getPosition(int x, int y) {
        return y + n * x;
    }

}
