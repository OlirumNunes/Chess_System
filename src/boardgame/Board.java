package boardgame;

import boardgame.exception.BoardException;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Invalid board size");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("Invalid position");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Invalid position on the board");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    /**
     * Places a piece at the specified position on the board.
     *
     * @param piece    The piece to be placed.
     * @param position The position where the piece will be placed.
     * @throws BoardException If there is already a piece at the specified position.
     */
    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("There is already a piece on this position " + position);
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    /**
 * Removes a piece from the specified position on the board.
 *
 * @param position The position of the piece to be removed.
 * @return The removed piece, or null if there was no piece at the specified position.
 * @throws BoardException If the specified position is invalid.
 */
public Piece removePiece(Position position) {
    if (!positionExists(position)) {
        throw new BoardException("Invalid position");
    }
    if (piece(position) == null) {
        return null;
    }
    Piece aux = piece(position);
    aux.position = null;
    pieces[position.getRow()][position.getColumn()] = null;
    return aux;
}

    /**
 * Checks if the specified position exists within the board.
 *
 * @param row   The row of the position.
 * @param column The column of the position.
 * @return True if the position exists within the board, false otherwise.
 */
private boolean positionExists(int row, int column) {
    return row >= 0 && row < rows && column >= 0 && column < columns;
}
    /**
     * Checks if the specified position exists within the board.
     *
     * @param position The position to be checked.
     * @return True if the position exists within the board, false otherwise.
     */
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    /**
     * Checks if the specified position has a piece on it.
     *
     * @param position The position to be checked.
     * @return True if there is a piece at the specified position, false otherwise.
     * @throws BoardException If the specified position is invalid.
     */
    public boolean thereIsAPiece(Position position) {
        if (!positionExists(position)) {
            throw new BoardException("Invalid position");
        }
        return piece(position) != null;
    }

}
