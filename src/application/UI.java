package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The UI class provides methods to print the chess board and its pieces.
 */
public class UI {

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Reads a chess position from the console input.
     *
     * @param sc the Scanner object used to read the input.
     * @return a ChessPosition object representing the input position.
     * @throws InputMismatchException if the input is not a valid chess position.
     */
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }

    /**
     * Prints the chess match state, including the board, captured pieces, and current game status.
     *
     * @param chessMatch the current state of the chess game.
     * @param captured   a list of captured ChessPieces.
     */
    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        /**
         * Prints the chess board with the given pieces.
         *
         * @param pieces a 2D array of ChessPiece objects representing the chess board.
         */
        printBoard(chessMatch.getPieces());
        System.out.println();

        /**
         * Prints the captured pieces of the given list.
         *
         * @param captured a list of captured ChessPieces.
         */
        printCapturedPieces(captured);
        System.out.println();

        /**
         * Prints the current turn and game status.
         */
        System.out.println("Turn: " + chessMatch.getTurn());

        if (!chessMatch.getCheckMate()) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("CHECK!");
            }
        } else {
            System.out.println("CHECKMATE!");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer());
        }
    }

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
                printPiece(pieces[i][j], false); // Prints the ChessPiece at the specified position.
            }
            System.out.println(); // Prints a new line after each row.
        }
        System.out.println("  a b c d e f g h"); // Prints the alphabetical column headers.
    }

    /**
     * Prints the chess board with the given pieces and possible moves.
     *
     * @param pieces        a 2D array of ChessPiece objects representing the chess board.
     * @param possibleMoves a 2D boolean array indicating the possible moves for each piece.
     */
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Prints the row number in reverse order.
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]); // Prints the ChessPiece at the specified position.
                // with the option to highlight possible moves.
            }
            System.out.println(); // Prints a new line after each row.
        }
        System.out.println("  a b c d e f g h"); // Prints the alphabetical column headers.
    }

    /**
     * Prints a ChessPiece on the console, optionally with a background color.
     *
     * @param piece      the ChessPiece to be printed.
     * @param background if true, the piece will be printed with a background color.
     */
    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    /**
     * Prints the captured pieces of the given list.
     *
     * @param captured a list of captured ChessPieces.
     */
    private static void printCapturedPieces(List<ChessPiece> captured) {
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());

        System.out.println("Captured pieces: ");
        System.out.print("White: ");
        System.out.print(ANSI_WHITE);
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);

        System.out.print("Black: ");
        System.out.print(ANSI_YELLOW);
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    }
}
