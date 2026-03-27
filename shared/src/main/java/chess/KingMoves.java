package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves extends PieceMoveCalculator{
    Collection<ChessMove> kingMoveList = new ArrayList<>() {
    };
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position ){
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor ally = pieceAtPosition.getTeamColor();
        if(row + 1 < 9){
        ChessPosition positionUp = new ChessPosition(row + 1, col);
        ChessPiece pieceUp = board.getPiece(positionUp);
            if(board.getPiece(positionUp)== null){
                kingMoveList.add(new ChessMove(position, positionUp, null));
            }
            else{
                if (ally != pieceUp.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionUp, null));}
            }
        }
        if(row - 1 > 0){
            ChessPosition positionDown = new ChessPosition(row - 1, col);
            ChessPiece pieceDown = board.getPiece(positionDown);
            if(board.getPiece(positionDown)== null){
                kingMoveList.add(new ChessMove(position, positionDown, null));
            }
            else{
                if (ally != pieceDown.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionDown, null));}
            }
        }
        if( col - 1 > 0){
            ChessPosition positionLeft = new ChessPosition(row, col - 1);
            ChessPiece pieceLeft = board.getPiece(positionLeft);
            if(board.getPiece(positionLeft)== null){
                kingMoveList.add(new ChessMove(position, positionLeft, null));
            }
            else{
                if (ally != pieceLeft.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionLeft, null));}
            }
        }
        if(col + 1 < 9){
            ChessPosition positionRight = new ChessPosition(row, col + 1);
            ChessPiece pieceRight = board.getPiece(positionRight);
            if(board.getPiece(positionRight)== null){
                kingMoveList.add(new ChessMove(position, positionRight, null));
            }
            else{
                if (ally != pieceRight.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionRight, null));}
            }
        }
        if ( row + 1 < 9 && col + 1 < 9){
            ChessPosition positionUpRight = new ChessPosition(row + 1, col + 1);
            ChessPiece pieceUpRight = board.getPiece(positionUpRight);
            if(board.getPiece(positionUpRight)== null){
                kingMoveList.add(new ChessMove(position, positionUpRight, null));
            }
            else{
                if (ally != pieceUpRight.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionUpRight, null));
                }
            }
        }
        if( row - 1 > 0 && col + 1 < 9){
            ChessPosition positionDownRight = new ChessPosition(row - 1, col + 1);
            ChessPiece pieceDownRight = board.getPiece(positionDownRight);
            if(board.getPiece(positionDownRight)== null){
                kingMoveList.add(new ChessMove(position, positionDownRight, null));
            }
            else{
                if (ally != pieceDownRight.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionDownRight, null));
                }
            }
        }
        if( row + 1 < 9 && col - 1 > 0){
            ChessPosition positionUpLeft = new ChessPosition(row + 1, col - 1);
            ChessPiece pieceUpLeft = board.getPiece(positionUpLeft);
            if(board.getPiece(positionUpLeft)== null){
                kingMoveList.add(new ChessMove(position, positionUpLeft, null));
            }
            else{
                if (ally != pieceUpLeft.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionUpLeft, null));
                }
            }
        }
        if(row - 1 > 0 && col - 1 > 0){
        ChessPosition positionDownLeft = new ChessPosition(row - 1, col - 1);
        ChessPiece pieceDownLeft = board.getPiece(positionDownLeft);
            if(board.getPiece(positionDownLeft)== null && positionDownLeft.getColumn() > 0 && positionDownLeft.getRow() > 0 ){
                kingMoveList.add(new ChessMove(position, positionDownLeft, null));
            }
            else{
                if (ally != pieceDownLeft.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, positionDownLeft, null));
                }
            }
        }
        return kingMoveList;
    }
}
