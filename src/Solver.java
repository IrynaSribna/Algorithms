import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;


public class Solver {

    private MinPQ<SearchNode> pQueue;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        SearchNode initialSearchNode = new SearchNode(initial, 0, null);
        this.pQueue.insert(initialSearchNode);


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

        private Board board;
        private int moves;
        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        @Override
        public int compareTo(SearchNode o) {
            return 0;
        }
    }

    // test client (see below)
    public static void main(String[] args) {

    }

}
