package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static java.lang.System.out;
import static ui.EscapeSequences.*;


public class chessboardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private final ChessGame game = new ChessGame();




    private void printBoard(PrintStream out) {

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    setWhiteSquare(out);
                } else {
                    setBlackSquare(out);
                }
                ChessBoard board = game.getBoard();
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece == null){
                    out.print(" ");
                }
                else{
                    setPieceColor(out, piece);
                    out.print(piecePrint(piece));}
            }
            System.out.println();
        }

    }

    public String piecePrint(ChessPiece piece){
        if(piece.getPieceType().equals(ChessPiece.PieceType.PAWN)){
            return "P";
        } else if (piece.getPieceType().equals(ChessPiece.PieceType.ROOK)) {
            return "R";
        } else if (piece.getPieceType().equals(ChessPiece.PieceType.BISHOP)) {
            return "B";
        } else if (piece.getPieceType().equals(ChessPiece.PieceType.QUEEN)) {
            return "Q";
        } else if (piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
            return "K";
        } else if (piece.getPieceType().equals(ChessPiece.PieceType.KNIGHT)) {
            return "N";
        }
        return " ";
    }

    public void setPieceColor(PrintStream out, ChessPiece piece){
        if(piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            out.print(SET_TEXT_COLOR_RED);
        }
        else{
            out.print(SET_TEXT_COLOR_BLUE);

        }
    }




    private static void setWhiteSquare(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlackSquare(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhiteSquare(out);
    }


}
