package boardgame;

/**
 * A class representing a position on a board.
 */
public class Position {

    /**
     * The row of the position.
     */
    private int row;

    /**
     * The column of the position.
     */
    private int column;

    /**
     * Constructs a new position with the specified row and column.
     *
     * @param row    the row of the new position
     * @param column the column of the new position
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Gets the row of the position.
     *
     * @return the row of the position
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of the position.
     *
     * @param row the new row of the position
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column of the position.
     *
     * @return the column of the position
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column of the position.
     *
     * @param column the new column of the position
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Sets the row and column of the position.
     *
     * @param row    the new row of the position
     * @param column the new column of the position
     */
    public void setValues(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns a string representation of the position.
     *
     * @return a string representation of the position in the format "row, column"
     */
    @Override
    public String toString() {
        return row + ", " + column;
    }

}
