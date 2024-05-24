/**
 * A custom exception for the chess game.
 * This exception extends the {@link boardgame.exception.BoardException}
 * and is used to handle specific chess-related errors.
 */
package chess.exception;

import boardgame.exception.BoardException;

/**
 * A custom exception for the chess game.
 * This exception extends the {@link boardgame.exception.BoardException}
 * and is used to handle specific chess-related errors.
 */
public class ChessException extends BoardException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of the ChessException with the specified error message.
     *
     * @param msg the error message
     */
    public ChessException(String msg) {
        super(msg);
    }
}
