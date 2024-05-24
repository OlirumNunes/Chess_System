package chess;

import boardgame.Position;
import chess.exception.ChessException;

/**
 * Represents a position on a chess board.
 */
public class ChessPosition {

    /**
     * The column of the position on the chess board.
     */
    private char column;

    /**
     * The row of the position on the chess board.
     */
    private int row;

    /**
     * Initializes a new instance of the ChessPosition class.
     *
     * @param column The column of the position on the chess board.
     * @param row    The row of the position on the chess board.
     * @throws ChessException If the provided column or row is not valid.
     */
    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) {
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8.");
        }
        this.column = column;
        this.row = row;
    }

    /**
     * Converts a Position object to a ChessPosition object.
     *
     * @param position The Position object to be converted.
     * @return A new instance of ChessPosition.
     */
    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char) ('a' + position.getColumn()), 8 - position.getRow());
    }

    /**
     * Gets the column of the position on the chess board.
     *
     * @return The column of the position.
     */
    public char getColumn() {
        return column;
    }

    /**
     * Gets the row of the position on the chess board.
     *
     * @return The row of the position.
     */
    public int getRow() {
        return row;
    }

    /**
     * Converts the ChessPosition object to a Position object.
     *
     * @return A new instance of Position.
     */
    protected Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    /**
     * Returns a string representation of the ChessPosition object.
     *
     * @return A string representation of the ChessPosition object.
     */
    @Override
    public String toString() {
        return "" + column + row;
    }

}
