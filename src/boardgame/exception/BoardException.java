/**
 * Represents an exception that occurs in the context of a board game.
 */
package boardgame.exception;

/**
 * A custom exception for board game related issues.
 */
public class BoardException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance of {@code BoardException} with the specified detail message.
     *
     * @param msg the detail message
     */
    public BoardException(String msg) {
        super(msg);
    }

}
