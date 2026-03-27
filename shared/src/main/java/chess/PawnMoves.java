package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoves extends PieceMoveCalculator {

    Collection<ChessMove> pawnMoveList = new ArrayList<>() {
    };

    public void possiblePromotionsWhite(ChessPosition startPosition, ChessPosition endPosition){
        pawnMoveList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        pawnMoveList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        pawnMoveList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        pawnMoveList.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }

    public void possiblePromotionsBlack(ChessPosition position, ChessPosition endPosition){
        pawnMoveList.add(new ChessMove(position, endPosition, ChessPiece.PieceType.QUEEN));
        pawnMoveList.add(new ChessMove(position, endPosition, ChessPiece.PieceType.ROOK));
        pawnMoveList.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
        pawnMoveList.add(new ChessMove(position, endPosition, ChessPiece.PieceType.KNIGHT));
    }

    public void pawnDiagonalMovesWhite(int col, int row, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
        if (col + 1 < 9) {
            ChessPosition positionUpRight = new ChessPosition(row + 1, col + 1);
            ChessPiece pieceUpRight = board.getPiece(positionUpRight);
            if (pieceUpRight != null && color != pieceUpRight.getTeamColor()) {
                pawnMoveList.add(new ChessMove(position, positionUpRight, null));
            }
        }
        if (col - 1 > 0) {
            ChessPosition positionUpLeft = new ChessPosition(row + 1, col - 1);
            ChessPiece pieceUpLeft = board.getPiece(positionUpLeft);
            if (pieceUpLeft != null && color != pieceUpLeft.getTeamColor()) {
                pawnMoveList.add(new ChessMove(position, positionUpLeft, null));
            }
        }
    }
    public void pawnDiagonalMovesBlack(int col, int row, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
        if (col + 1 < 9) {
            ChessPosition positionDownRight = new ChessPosition(row - 1, col + 1);
            ChessPiece pieceDownRight = board.getPiece(positionDownRight);
            if (pieceDownRight != null && color != pieceDownRight.getTeamColor()) {
                pawnMoveList.add(new ChessMove(position, positionDownRight, null));
            }
        }
        if (col - 1 > 0) {
            ChessPosition positionDownLeft = new ChessPosition(row - 1, col - 1);
            ChessPiece pieceDownLeft = board.getPiece(positionDownLeft);
            if (pieceDownLeft != null && color != pieceDownLeft.getTeamColor()) {
                pawnMoveList.add(new ChessMove(position, positionDownLeft, null));
            }
        }
    }

    public void pawnPromotesWhite(int row, int col, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
        ChessPosition positionUp = new ChessPosition(row + 1, col);
        if (board.getPiece(positionUp) == null) {
            possiblePromotionsWhite(position, positionUp);
        }

        if (col + 1 < 9) {
            ChessPosition positionUpRight = new ChessPosition(row + 1, col + 1);
            ChessPiece pieceUpRight = board.getPiece(positionUpRight);
            if (pieceUpRight != null && color != pieceUpRight.getTeamColor()) {
                possiblePromotionsWhite(position, positionUpRight);
            }
        }

        if (col - 1 > 0) {
            ChessPosition positionUpLeft = new ChessPosition(row + 1, col - 1);
            ChessPiece pieceUpLeft = board.getPiece(positionUpLeft);
            if (pieceUpLeft != null && color != pieceUpLeft.getTeamColor()) {
                possiblePromotionsWhite(position, positionUpLeft);
            }
        }
    }
    public void pawnPromotesBlack(int row, int col, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
        ChessPosition positionDown = new ChessPosition(row - 1, col);
        if (board.getPiece(positionDown) == null) {
            possiblePromotionsBlack(position,  positionDown);
        }

        if (col + 1 < 9) {
            ChessPosition positionDownRight = new ChessPosition(row - 1, col + 1);
            ChessPiece pieceDownRight = board.getPiece(positionDownRight);
            if (pieceDownRight != null && color != pieceDownRight.getTeamColor()) {
                possiblePromotionsBlack(position,  positionDownRight);
            }
        }

        if (col - 1 > 0) {
            ChessPosition positionDownLeft = new ChessPosition(row - 1, col - 1);
            ChessPiece pieceDownLeft = board.getPiece(positionDownLeft);
            if (pieceDownLeft != null && color != pieceDownLeft.getTeamColor()) {
                possiblePromotionsBlack(position,  positionDownLeft);
            }
        }

    }
    public void pawnStartMovesWhite(int row, int col, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){
            ChessPosition positionUp = new ChessPosition(row + 1, col);
            ChessPosition positionUpTwo = new ChessPosition(row + 2, col);
            boolean presence = true;
            if (board.getPiece(positionUp) == null) {
                pawnMoveList.add(new ChessMove(position, positionUp, null));
            } else {
                presence = false;
            }
            if (board.getPiece(positionUpTwo) == null && presence) {
                pawnMoveList.add(new ChessMove(position, positionUpTwo, null));
            }
            pawnDiagonalMovesWhite(col, row, board, color, position);

    }

    public void pawnStartMovesBlack(int row, int col, ChessBoard board, ChessGame.TeamColor color, ChessPosition position){

        ChessPosition positionDown = new ChessPosition(row - 1, col);
        ChessPosition positionDownTwo = new ChessPosition(row - 2, col);
        boolean presence = true;
        if (board.getPiece(positionDown) == null) {
            pawnMoveList.add(new ChessMove(position, positionDown, null));
        } else {
            presence = false;
        }
        if (board.getPiece(positionDownTwo) == null && presence) {
            pawnMoveList.add(new ChessMove(position, positionDownTwo, null));
        }
        pawnDiagonalMovesBlack(col, row, board, color, position);


    }




    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
        if(color == WHITE) {
            if(row + 1 == 8){
            pawnPromotesWhite(row, col, board, color, position);
            }
            else if(row == 2){
            pawnStartMovesWhite(row, col, board, color, position);
            }
            else if (row + 1 < 9) {
                ChessPosition positionUp = new ChessPosition(row + 1, col);
                if (board.getPiece(positionUp) == null) {
                    pawnMoveList.add(new ChessMove(position, positionUp, null));
                }
                pawnDiagonalMovesWhite(col, row, board, color, position);
            }
        }
        else{
            if (row - 1 == 1) {
                pawnPromotesBlack(row, col, board, color, position);
            }
            else if (row == 7) {
                pawnStartMovesBlack(row, col, board, color, position);
            }
            else if (row - 1 > 0) {
                ChessPosition positionDown = new ChessPosition(row - 1, col);
                if (board.getPiece(positionDown) == null) {
                    pawnMoveList.add(new ChessMove(position, positionDown, null));
                }
                pawnDiagonalMovesBlack(col, row, board, color, position);
            }
        }
        return pawnMoveList;
    }


        }




