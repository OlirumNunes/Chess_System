package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.exception.ChessException;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    /**
     * Initializes a new ChessMatch object with a new 8x8 board, turn 1, and the current player as White.
     */
    public ChessMatch() {
        /**
         * Initializes a new 8x8 board.
         * @param rows The number of rows in the board.
         * @param columns The number of columns in the board.
         */
        board = new Board(8, 8);

        /**
         * Sets the initial turn of the game.
         * @param turn The number of the turn.
         */
        turn = 1;

        /**
         * Sets the initial color of the current player.
         * @param color The color of the current player.
         */
        currentPlayer = Color.WHITE;

        /**
         * Initializes the chess board with the standard setup of pieces.
         */
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    /**
     * Get the current state of the chess board, represented as a 2D array of ChessPieces.
     *
     * @return a 2D array of ChessPieces, where each element represents a piece on the board.
     */
    public ChessPiece[][] getPieces() {
        /**
         * Creates a 2D array of ChessPieces, where each element represents a piece on the board.
         *
         * @param rows The number of rows in the board.
         * @param columns The number of columns in the board.
         * @return a 2D array of ChessPieces, where each element represents a piece on the board.
         */
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    /**
     * Performs a chess move from the given source position to the target position.
     *
     * @param sourcePosition The source position of the move.
     * @param targetPosition The target position of the move.
     * @return The captured piece, if any.
     * @throws ChessException If the move is illegal or puts the player in check.
     */
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't move into this position because you'll put yourself in a check situation");
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0 ||
                    movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        check = testCheck(opponent(currentPlayer));

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        if (movedPiece instanceof Pawn &&
                (target.getRow() == source.getRow() - 2 ||
                        target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }
        return (ChessPiece) capturedPiece;
    }

    /**
     * Replaces the promoted piece with the specified type.
     *
     * @param type The type of the new piece to be created. It can be "B" for Bishop, "N" for Knight, "R" for Rook, or "Q" for Queen.
     * @return The newly created piece of the specified type.
     * @throws IllegalStateException If there is no piece to be promoted.
     */
    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!type.equals("B") &&
                !type.equals("N") &&
                !type.equals("R") &&
                !type.equals("Q")) {
            return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = createChessPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    /**
     * Creates a new ChessPiece object based on the provided type and color.
     *
     * @param type  The type of the new piece to be created. It can be "B" for Bishop, "N" for Knight, "R" for Rook, or "Q" for Queen.
     * @param color The color of the new piece to be created.
     * @return The newly created piece of the specified type and color.
     * @throws IllegalArgumentException If the provided type is not a valid piece type.
     */
    private ChessPiece createChessPiece(String type, Color color) {
        switch (type.toUpperCase()) {
            case "B":
                return new Bishop(board, color);
            case "N":
                return new Knight(board, color);
            case "R":
                return new Rook(board, color);
            case "Q":
                return new Queen(board, color);
            default:
                throw new IllegalArgumentException("Invalid piece type");
        }
    }

    /*
     * Makes a move on the chess board from the given source position to the target position.
     *
     * @param sourcePosition The source position of the move.
     * @param targetPosition The target position of the move.
     * @return The captured piece, if any.
     * @throws ChessException           If the move is illegal or puts the player in check.
     * @throws IllegalArgumentException If the provided type is not a valid piece type.
     */
    private Piece makeMove(Position source, Position target) throws ChessException, IllegalArgumentException {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }
        if (p instanceof Pawn) {
            if (source.getColumn() !=
                    target.getColumn() &&
                    capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;
    }

    /**
     * Undoes the last move made on the chess board.
     *
     * @param source        The source position of the move.
     * @param target        The target position of the move.
     * @param capturedPiece The captured piece, if any.
     * @throws ChessException           If the move is illegal or puts the player in check.
     * @throws IllegalArgumentException If the provided type is not a valid piece type.
     */
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        if (p instanceof Pawn) {
            if (source.getColumn() !=
                    target.getColumn() &&
                    capturedPiece == enPassantVulnerable) {

                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    /**
     * Validates the source position of a chess move.
     *
     * @param position The source position of the move.
     * @throws ChessException If the source position does not have a piece, the piece is not owned by the current player, or the piece has no possible moves.
     */
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        } else if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        } else if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    /**
     * Validates the target position of a chess move.
     *
     * @param source The source position of the move.
     * @param target The target position of the move.
     * @throws ChessException If the target position is not reachable by the chosen piece.
     */
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    /**
     * Returns the opponent color of the provided color.
     *
     * @param color The color of the player whose opponent is to be determined.
     * @return The color of the opponent player.
     */
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Returns the king of the specified color.
     *
     * @param color The color of the player whose king is to be determined.
     * @return The king piece of the specified color.
     * @throws IllegalStateException If there is no king of the specified color on the board.
     */
    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    /**
     * Returns whether the player whose color is specified is in check.
     *
     * @param color The color of the player whose check status is to be determined.
     * @return True if the player is in check, false otherwise.
     */
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponent(color)).toList();
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the player whose color is specified is in checkmate.
     *
     * @param color The color of the player whose checkmate status is to be determined.
     * @return True if the player is in checkmate, false otherwise.
     */
    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).toList();
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Places a new piece on the chess board at the specified position.
     *
     * @param column The column of the position where the piece will be placed.
     * @param row    The row of the position where the piece will be placed.
     * @param piece  The piece to be placed on the board.
     */
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        /**
         * Places a new piece on the chess board at the specified position.
         *
         * @param column The column of the position where the piece will be placed.
         * @param row    The row of the position where the piece will be placed.
         * @param piece  The piece to be placed on the board.
         * @return void
         * @throws IllegalArgumentException If the provided piece is not a valid chess piece.
         */
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}

