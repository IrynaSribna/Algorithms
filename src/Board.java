import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;


public class Board {
    private final int n;
    private final int length;
    private final int[][] boardTiles;
    public final int manhattanDistance;
    private int[] pieces;
    private int zeroPosition;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles[0].length;
        this.length = n * n;
        pieces = new int[length];
        this.boardTiles = tiles.clone();
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
        this.manhattanDistance = manhattan();
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
                int diff = Math.abs(value - i - 1);
                count += diff / n + diff % n;
            }
        }

        return count;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return n == board.n &&
            manhattanDistance == board.manhattanDistance &&
            Arrays.equals(pieces, board.pieces);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Board left = null;
        Board right = null;
        Board top = null;
        Board bottom = null;
        if (shouldAddLeft()) {
            left = swap(zeroPosition, zeroPosition + 1);
        }
        if (shouldAddRight()) {
            right = swap(zeroPosition, zeroPosition - 1);
        }

        if (shouldAddTop()) {
            top = swap(zeroPosition, zeroPosition - n);
        }

        if (shouldAddDown()) {
            bottom = swap(zeroPosition, zeroPosition + n);
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


    public Board swap(int fromPosition, int toPosition) {
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
        do {
            first = StdRandom.uniform(0, length);
            second = StdRandom.uniform(0, length);
        } while (first == second || first == zeroPosition || second == zeroPosition);

        return swap(first, second);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6}

        };

        int[][] tiles2 = {
            {2, 8, 1},
            {0, 7, 6},
            {5, 3, 4}
        };
        Board initial = new Board(tiles);
        System.out.println("Hamming: " + initial.hamming());
        System.out.println("Manhattan: " + initial.manhattan());
        System.out.println("==================");
        initial.neighbors().forEach(System.out::println);
        System.out.println("---------------------");
        System.out.println(initial.twin());


        Board second = new Board(tiles2);
        System.out.println("===============");
        System.out.println("Hamming second: " + second.hamming());
        System.out.println("Manhattan: " + second.manhattan());

        System.out.println("Equal: " + initial.equals(second));
    }

    private int getPosition (int x, int y) {
        return y + n * x;
    }

}
