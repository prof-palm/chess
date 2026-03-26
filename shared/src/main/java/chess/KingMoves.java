package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves extends PieceMoveCalculator{
    Collection<ChessMove> kingMoveList = new ArrayList<>() {
    };
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position ){
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece s_piece = board.getPiece(position);
        ChessGame.TeamColor ally = s_piece.getTeamColor();
        if(row + 1 < 9){
        ChessPosition pos_u = new ChessPosition(row + 1, col);
        ChessPiece piece_u = board.getPiece(pos_u);
            if(board.getPiece(pos_u)== null){
                kingMoveList.add(new ChessMove(position, pos_u, null));
            }
            else{
                if (ally != piece_u.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_u, null));



                }
            }
        }
        if(row - 1 > 0){
            ChessPosition pos_d = new ChessPosition(row - 1, col);
            ChessPiece piece_d = board.getPiece(pos_d);
            if(board.getPiece(pos_d)== null){
                kingMoveList.add(new ChessMove(position, pos_d, null));
            }
            else{
                if (ally != piece_d.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_d, null));



                }
            }
        }
        if( col - 1 > 0){
            ChessPosition pos_l = new ChessPosition(row, col - 1);
            ChessPiece piece_l = board.getPiece(pos_l);
            if(board.getPiece(pos_l)== null){
                kingMoveList.add(new ChessMove(position, pos_l, null));
            }
            else{
                if (ally != piece_l.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_l, null));



                }
            }
        }
        if(col + 1 < 9){
            ChessPosition pos_r = new ChessPosition(row, col + 1);
            ChessPiece piece_r = board.getPiece(pos_r);
            if(board.getPiece(pos_r)== null){
                kingMoveList.add(new ChessMove(position, pos_r, null));
            }
            else{
                if (ally != piece_r.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_r, null));



                }
            }
        }
        if ( row + 1 < 9 && col + 1 < 9){
            ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
            ChessPiece piece_ur = board.getPiece(pos_ur);
            if(board.getPiece(pos_ur)== null){
                kingMoveList.add(new ChessMove(position, pos_ur, null));
            }
            else{
                if (ally != piece_ur.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_ur, null));



                }
            }

        }
        if( row - 1 > 0 && col + 1 < 9){
            ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
            ChessPiece piece_dr = board.getPiece(pos_dr);
            if(board.getPiece(pos_dr)== null){
                kingMoveList.add(new ChessMove(position, pos_dr, null));
            }
            else{
                if (ally != piece_dr.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_dr, null));



                }
            }

        }
        if( row + 1 < 9 && col - 1 > 0){
            ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
            ChessPiece piece_ul = board.getPiece(pos_ul);
            if(board.getPiece(pos_ul)== null){
                kingMoveList.add(new ChessMove(position, pos_ul, null));
            }
            else{
                if (ally != piece_ul.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_ul, null));



                }
            }

        }
        if(row - 1 > 0 && col - 1 > 0){
        ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
        ChessPiece piece_dl = board.getPiece(pos_dl);
            if(board.getPiece(pos_dl)== null && pos_dl.getColumn() > 0 && pos_dl.getRow() > 0 ){
                kingMoveList.add(new ChessMove(position, pos_dl, null));
            }
            else{
                if (ally != piece_dl.getTeamColor()) {
                    kingMoveList.add(new ChessMove(position, pos_dl, null));



                }
            }
        }
        return kingMoveList;
    }
}
