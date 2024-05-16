package application;

import chess.ChessPiece;

/**
 * The UI class provides methods to print the chess board and its pieces.
 */
public class UI {

    /**
     * Prints the chess board with the given pieces.
     *
     * @param pieces a 2D array of ChessPiece objects representing the chess board.
     */
    public static void printBoard(ChessPiece[][] pieces) {
        /**
         * Iterates through the rows of the chess board and prints each row and its pieces.
         *
         * @param pieces a 2D array of ChessPiece objects representing the chess board.
         */
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Prints the row number in reverse order.
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]); // Prints the ChessPiece at the specified position.
            }
            System.out.println(); // Prints a new line after each row.
        }
        System.out.println("  a b c d e f g h"); // Prints the alphabetical column headers.
    }

    /**
     * Prints the ChessPiece at the specified position.
     *
     * @param piece the ChessPiece object to be printed.
     */
    private static void printPiece(ChessPiece piece) {
        /**
         * Checks if the piece is null, and if so, prints a hyphen to indicate an empty space.
         * If the piece is not null, it prints the piece itself.
         *
         * @param piece the ChessPiece object to be printed.
         */
        if (piece == null) {
            // If the piece is null, it represents an empty space on the board.
            // Print a hyphen to indicate an empty space.
            System.out.print("-");
        } else {
            // If the piece is not null, it represents a chess piece on the board.
            // Print the piece itself.
            System.out.print(piece);
        }
        // Print a new line after each piece.
        System.out.print(" ");
    }
}
