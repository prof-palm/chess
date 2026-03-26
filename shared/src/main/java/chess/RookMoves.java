package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends PieceMoveCalculator {
        Collection<ChessMove> rookMoveList = new ArrayList<>() {
        };
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor ally = pieceAtPosition.getTeamColor();
        //Go up for rook
        for (int i = row + 1; i < 9; i++) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                rookMoveList.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes down
        for (int i = row - 1; i > 0; i--) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                rookMoveList.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes right
        for(int j = col + 1; j < 9; j++){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                rookMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes left
        for(int j = col - 1; j > 0; j--){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                rookMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }

        return rookMoveList;
    }

}



