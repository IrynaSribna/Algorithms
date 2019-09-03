import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {

    private final Stack<Board> solution = new Stack<>();
    private int solutionMoves;
    private boolean isSolvable = false;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        int moves = 0;
        MinPQ<SearchNode> pInitQueue = new MinPQ<>();
        SearchNode nodeToSolve = new SearchNode(initial, moves, null);
        pInitQueue.insert(nodeToSolve);
        Board twin = initial.twin();
        SearchNode twinNode = new SearchNode(twin, moves, null);
        pInitQueue.insert(twinNode);
        SearchNode deletedNode = pInitQueue.delMin();

        while (!deletedNode.board.isGoal()) {
            moves++;
            insertNeighbors(moves, pInitQueue, deletedNode);
            deletedNode = pInitQueue.delMin();
        }

        SearchNode goal = deletedNode;
        while (!(goal.previous == null)) {
            solution.push(goal.board);
            goal = goal.previous;
        }
        solution.push(goal.board);

        if (solution.peek().equals(initial)) {
            isSolvable = true;
        }

        solutionMoves = solution.size() - 1;
    }

    private void insertNeighbors(int moves, MinPQ<SearchNode> pInitQueue, SearchNode dequeued) {
        for (Board neighbor : dequeued.board.neighbors()) {
            if (dequeued.previous == null || !neighbor.equals(dequeued.previous.board)) {
                pInitQueue.insert(new SearchNode(neighbor, moves, dequeued));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (!isSolvable) return -1;
        return solutionMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return !isSolvable ? null : solution;
    }

    private static class SearchNode
        implements Comparable<SearchNode> {

        private final Board board;
        private final int moves;
        private final int manhattan;
        private final SearchNode previous;

        private SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.manhattan = board.manhattan();
        }

        @Override
        public int compareTo(SearchNode that) {
            int currentPriority = this.moves + this.manhattan;
            int otherPriority = that.moves + that.manhattan;

            return currentPriority < otherPriority
                ? -1
                : currentPriority > otherPriority
                    ? 1
                        : this.manhattan < that.manhattan
                            ? -1
                            : this.manhattan > that.manhattan
                                ? -1
                                : 0;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = {
            {1, 3, 5},
            {4, 0, 8},
            {7, 6, 2}
        };
        // create initial board from file
//        In in = new In(args[0]);
//        int n = in.readInt();
//        int[][] tiles = new int[n][n];
//        for (int i = 0; i < n; i++)
//            for (int j = 0; j < n; j++)
//                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}
