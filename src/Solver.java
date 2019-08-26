import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class Solver {

    private MinPQ<SearchNode> pQueue = new MinPQ<>();
    private Queue<Board> solution = new Queue<>();
    private int solutionMoves = 0;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        int moves = 0;
        SearchNode initialSearchNode = new SearchNode(initial, moves, null);
        this.pQueue.insert(initialSearchNode);
        SearchNode dequeued =this.pQueue.delMin();

        while(!dequeued.board.isGoal()) {
            solution.enqueue(dequeued.board);
            moves++;
            for (Board neighbor : dequeued.board.neighbors()) {
                this.pQueue.insert(new SearchNode(neighbor, moves, dequeued));
            }
            dequeued = this.pQueue.delMin();
        }

        dequeued = this.pQueue.delMin();
        this.solutionMoves = dequeued.moves;

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.solutionMoves = 0;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
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
            int current = moves + board.manhattanDistance;
            int that = o.moves + o.board.manhattanDistance;

            if (current == that) return 0;
            return current < that ? -1 : 1;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles1 = {
            {1, 0, 3},
            {4, 2, 5},
            {7, 8, 6}
        };
        Board board1 = new Board(tiles1);
        Solver solver1 = new Solver(board1);

        // create initial board from file
//        In in = new In(args[0]);
//        int n = in.readInt();
//        int[][] tiles = new int[n][n];
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles1);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
//        }

    }

}
