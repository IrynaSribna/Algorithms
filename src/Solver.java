import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;


public class Solver {

    private MinPQ<Board> pQueue;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.pQueue.insert(initial);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return 0;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return new Queue<>();
    }

    private static class SearchNode
        implements Comparable<SearchNode> {

        private int priority;
        private SearchNode previous;

        @Override
        public int compareTo(SearchNode o) {
            return 0;
        }
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}
