package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

/**
 * Abstract class representing a chess piece.
 */
public abstract class ChessPiece extends Piece {

    /**
     * The color of the chess piece.
     */
    private Color color;

    /**
     * The number of moves the piece has made.
     */
    private int moveCount;

    /**
     * Constructs a new chess piece with the specified color and board.
     *
     * @param board the board the piece belongs to
     * @param color the color of the piece
     */
    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    /**
     * Returns the color of the chess piece.
     *
     * @return the color of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the number of moves the piece has made.
     *
     * @return the number of moves
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Increases the move count of the piece by one.
     */
    protected void increaseMoveCount() {
        moveCount++;
    }

    /**
     * Decreases the move count of the piece by one.
     */
    protected void decreaseMoveCount() {
        moveCount--;
    }

    /**
     * Returns the chess position of the piece.
     *
     * @return the chess position of the piece
     */
    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    /**
     * Checks if there is an opponent piece at the specified position.
     *
     * @param position the position to check
     * @return true if there is an opponent piece, false otherwise
     */
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }
}
