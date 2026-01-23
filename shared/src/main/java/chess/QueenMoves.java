package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves extends PieceMoveCalculator {
    Collection<ChessMove> q_m = new ArrayList<>() {
    };
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position ){

        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece s_piece = board.getPiece(position);
        ChessGame.TeamColor ally = s_piece.getTeamColor();
        for (int i = row + 1, j = col + 1; i < 9 && j < 9; i++, j++) {
            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                q_m.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        for (int i = row + 1, j = col - 1; i < 9 && j > 0; i++, j--) {
            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                q_m.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        for(int i = row - 1, j = col + 1; i > 0 && j < 9; i--, j++){
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                q_m.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        for(int i = row - 1, j = col - 1; i > 0 && j > 0; i--, j--){
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                q_m.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        for (int i = row + 1; i < 9; i++) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                q_m.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes down
        for (int i = row - 1; i > 0; i--) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                q_m.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == ally) {
                    break;
                } else {
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes right
        for(int j = col + 1; j < 9; j++){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                q_m.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
        //rook goes left
        for(int j = col - 1; j > 0; j--){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                q_m.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == ally){
                    break;
                }
                else{
                    q_m.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }

        return q_m; }

}










