package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;


public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 2;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private final ChessGame game = new ChessGame();




    public void printBoard(String perspective) {
        printHeaders(perspective);
        System.out.println();
        for (int row = 8; row > 0; row--) {
            printRowNumbers(printRow(row, perspective));
            for (int col = 1; col < 9 ; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_BLACK);
                } else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                ChessBoard board = game.getBoard();
                ChessPiece piece = board.getPiece(new ChessPosition(printRow(row, perspective), printCol(col, perspective)));
                if(piece == null){
                    System.out.print("   ");
                }
                else{
                    setPieceColor(piece);
                    System.out.print(" " + piecePrint(piece) + " ");}

            }
            printRowNumbers(printRow(row, perspective));
            System.out.print(RESET_BG_COLOR);
            System.out.println();
        }
        printHeaders(perspective);

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

    public void printHeaders(String perspective){
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        if(perspective.equals("WHITE")){
        List<String> headers = new ArrayList<>(List.of("a", "b", "c", "d", "e", "f", "g", "h"));
        printHeadersHelper(headers);
        }
        else{
            List<String> headers = new ArrayList<>(List.of("h", "g", "f", "e", "d", "c", "b", "a"));
            printHeadersHelper(headers);
        }



    }

    public void printHeadersHelper(List<String> headers){
        System.out.print("   ");
        for(String header : headers){
            System.out.print(" " + header + " ");
        }
        System.out.print("   ");
        System.out.print(RESET_BG_COLOR);
    }


    public void printRowNumbers(int rowNumber){
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.print(" " + rowNumber + " ");
    }


    public int printRow(int row, String perspective){
        if(perspective.equals("BLACK")){
            row = 9 - row;
            return row;
        }
        else{
            return row;
        }

    }
    public int printCol(int col, String perspective){
        if(perspective.equals("BLACK")){
            col = 9 - col;
            return col;
        }
        else{
            return col;
        }
    }

}
