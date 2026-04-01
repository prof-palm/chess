package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;

import static ui.EscapeSequences.*;


public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private final ChessGame game = new ChessGame();




    public void printBoard() {

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    System.out.print(SET_BG_COLOR_BLACK);
                }
                ChessBoard board = game.getBoard();
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if(piece == null){
                    System.out.print("   ");
                }
                else{
                    setPieceColor(piece);
                    System.out.print(" " + piecePrint(piece) + " ");}

            }
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }

    }

    public String piecePrint(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case PAWN -> "P";
            case ROOK -> "R";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
            case KNIGHT -> "N";
        };
    }

    public void setPieceColor(ChessPiece piece){
        if(piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            System.out.print(SET_TEXT_COLOR_RED);
        }
        else{
            System.out.print(SET_TEXT_COLOR_BLUE);

        }
    }




    private static void setWhiteSquare(PrintStream out) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setBlackSquare(PrintStream out) {
        System.out.print(SET_BG_COLOR_BLACK);
    }



}
