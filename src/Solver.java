import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;


public class Solver {

    private Queue<Board> solution = new Queue<>();
    private int solutionMoves;
    private boolean isSolvable = true;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) {
            throw new IllegalArgumentException();
        }

        int moves = 0;
        SearchNode initialSearchNode = new SearchNode(initial, moves, null);
        compute(initialSearchNode);
    }

    private void compute(SearchNode searchNode) {
        int moves = 0;
        //SearchNode initialSearchNode = new SearchNode(initial, moves, null);
        MinPQ<SearchNode> pInitQueue = new MinPQ<>();
        MinPQ<SearchNode> pTwinQueue = new MinPQ<>();
        pInitQueue.insert(searchNode);
        pTwinQueue.insert(new SearchNode(searchNode.board.twin(), 0, null));
        SearchNode dequeuedInit = pInitQueue.delMin();
        SearchNode dequeuedTwin = pTwinQueue.delMin();

        while(!dequeuedInit.board.isGoal()) {
            solution.enqueue(dequeuedInit.board);
            moves++;
            for (Board neighbor : dequeuedInit.board.neighbors()) {
                if (dequeuedInit.previous == null ||!neighbor.equals(dequeuedInit.previous.board)) {
                    pInitQueue.insert(new SearchNode(neighbor, moves, dequeuedInit));
                }
            }

            for (Board neighbor : dequeuedTwin.board.neighbors()) {
                if (dequeuedTwin.previous == null ||!neighbor.equals(dequeuedTwin.previous.board)) {
                    pTwinQueue.insert(new SearchNode(neighbor, moves, dequeuedTwin));
                }
            }
            dequeuedInit = pInitQueue.delMin();
            dequeuedTwin = pTwinQueue.delMin();
            if (dequeuedTwin.board.isGoal()) {
                this.isSolvable = false;
                break;
            }
        }

        solution.enqueue(dequeuedInit.board);
        this.solutionMoves = dequeuedInit.moves;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return solutionMoves;
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
//        int[][] tiles1 = {
//            {1, 0, 3},
//            {4, 2, 5},
//            {7, 8, 6}

        int[][] tiles1 = {
            {1, 2, 3},
            {4, 5, 6},
            {8, 7, 0}
        };
        Board board1 = new Board(tiles1);
        Solver solver1 = new Solver(board1);

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles1);

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
