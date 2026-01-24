package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves extends PieceMoveCalculator {
    Collection<ChessMove> kn_m = new ArrayList<>() {
    };

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece s_piece = board.getPiece(position);
        ChessGame.TeamColor color = s_piece.getTeamColor();

        //up right
        if (row + 2 < 9 && col + 1 < 9) {
                ChessPosition pos_ur = new ChessPosition(row + 2, col + 1);
                ChessPiece piece_ur = board.getPiece(pos_ur);
                if (piece_ur == null) {
                    kn_m.add(new ChessMove(position, pos_ur, null));

                }
                else if (color != piece_ur.getTeamColor()){
                    kn_m.add(new ChessMove(position, pos_ur, null));
                }

        }
        //up left
        if (row + 2 < 9 && col - 1 > 0) {
            ChessPosition pos_ul = new ChessPosition(row + 2, col - 1);
            ChessPiece piece_ul = board.getPiece(pos_ul);
            if (piece_ul == null) {
                kn_m.add(new ChessMove(position, pos_ul, null));

            }
            else if (color != piece_ul.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_ul, null));
            }

        }
        //down right
        if (row - 2 > 0  && col + 1 < 9) {
            ChessPosition pos_dr = new ChessPosition(row - 2, col + 1);
            ChessPiece piece_dr = board.getPiece(pos_dr);
            if (piece_dr == null) {
                kn_m.add(new ChessMove(position, pos_dr, null));

            }
            else if (color != piece_dr.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_dr, null));
            }

        }
        //down left
        if (row - 2 > 0 && col - 1 > 0) {
            ChessPosition pos_dl = new ChessPosition(row - 2, col - 1);
            ChessPiece piece_dl = board.getPiece(pos_dl);
            if (piece_dl == null) {
                kn_m.add(new ChessMove(position, pos_dl, null));

            }
            else if (color != piece_dl.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_dl, null));
            }

        }
        //right up
        if (row + 1 < 9 && col + 2 < 9) {
            ChessPosition pos_ru = new ChessPosition(row + 1, col + 2);
            ChessPiece piece_lu = board.getPiece(pos_ru);
            if (piece_lu == null) {
                kn_m.add(new ChessMove(position, pos_ru, null));

            }
            else if (color != piece_lu.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_ru, null));
            }

        }
        //right down
        if (row - 1 > 0 && col + 2 < 9) {
            ChessPosition pos_rd = new ChessPosition(row - 1, col + 2);
            ChessPiece piece_rd = board.getPiece(pos_rd);
            if (piece_rd == null) {
                kn_m.add(new ChessMove(position, pos_rd, null));

            }
            else if (color != piece_rd.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_rd, null));
            }

        }
        //left up
        if (row + 1 < 9 && col - 2 > 0 ) {
            ChessPosition pos_lu = new ChessPosition(row + 1, col - 2);
            ChessPiece piece_lu = board.getPiece(pos_lu);
            if (piece_lu == null) {
                kn_m.add(new ChessMove(position, pos_lu, null));

            }
            else if (color != piece_lu.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_lu, null));
            }

        }
        // left down
        if (row -  1 > 0 && col - 2 > 0 ) {
            ChessPosition pos_ld = new ChessPosition(row - 1, col - 2);
            ChessPiece piece_ld = board.getPiece(pos_ld);
            if (piece_ld == null) {
                kn_m.add(new ChessMove(position, pos_ld, null));

            }
            else if (color != piece_ld.getTeamColor()){
                kn_m.add(new ChessMove(position, pos_ld, null));
            }

        }
    return kn_m;
    }
}
