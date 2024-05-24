package boardgame;

/**
 * Abstract class representing a game piece.
 */
public abstract class Piece {

    /**
     * The position of the piece on the board.
     */
    protected Position position;

    /**
     * The board on which the piece is placed.
     */
    private Board board;

    /**
     * Constructor for the Piece class.
     *
     * @param board The board on which the piece is placed.
     */
    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    /**
     * Returns the board on which the piece is placed.
     *
     * @return The board on which the piece is placed.
     */
    protected Board getBoard() {
        return board;
    }

    /**
     * Returns an array of possible moves for the piece.
     *
     * @return A 2D boolean array representing the possible moves for the piece.
     */
    public abstract boolean[][] possibleMoves();

    /**
     * Checks if a specific move is possible for the piece.
     *
     * @param position The position to check for a possible move.
     * @return True if the move is possible, false otherwise.
     */
    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    /**
     * Checks if there are any possible moves for the piece.
     *
     * @return True if there are any possible moves, false otherwise.
     */
    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

}
