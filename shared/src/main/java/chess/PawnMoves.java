package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoves extends PieceMoveCalculator {

    Collection<ChessMove> p_m = new ArrayList<>() {
    };

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece s_piece = board.getPiece(position);
        ChessGame.TeamColor color = s_piece.getTeamColor();
        if(color == WHITE) {

            if (row + 1 == 8) {
                ChessPosition pos_u = new ChessPosition(row + 1, col);
                if (board.getPiece(pos_u) == null) {
                    p_m.add(new ChessMove(position, pos_u, ChessPiece.PieceType.QUEEN));
                    p_m.add(new ChessMove(position, pos_u, ChessPiece.PieceType.ROOK));
                    p_m.add(new ChessMove(position, pos_u, ChessPiece.PieceType.BISHOP));
                    p_m.add(new ChessMove(position, pos_u, ChessPiece.PieceType.KNIGHT));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
                    ChessPiece piece_ur = board.getPiece(pos_ur);
                    if (piece_ur != null && color != piece_ur.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ur, ChessPiece.PieceType.QUEEN));
                        p_m.add(new ChessMove(position, pos_ur, ChessPiece.PieceType.ROOK));
                        p_m.add(new ChessMove(position, pos_ur, ChessPiece.PieceType.BISHOP));
                        p_m.add(new ChessMove(position, pos_ur, ChessPiece.PieceType.KNIGHT));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
                    ChessPiece piece_ul = board.getPiece(pos_ul);
                    if (piece_ul != null && color != piece_ul.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ul, ChessPiece.PieceType.QUEEN));
                        p_m.add(new ChessMove(position, pos_ul, ChessPiece.PieceType.ROOK));
                        p_m.add(new ChessMove(position, pos_ul, ChessPiece.PieceType.BISHOP));
                        p_m.add(new ChessMove(position, pos_ul, ChessPiece.PieceType.KNIGHT));
                    }
                }

            }
            else if (row == 2) {
                ChessPosition pos_u1 = new ChessPosition(row + 1, col);
                ChessPosition pos_u2 = new ChessPosition(row + 2, col);
                boolean presence = true;
                if (board.getPiece(pos_u1) == null) {
                    p_m.add(new ChessMove(position, pos_u1, null));
                } else {
                    presence = false;
                }
                if (board.getPiece(pos_u2) == null && presence) {
                    p_m.add(new ChessMove(position, pos_u2, null));
                }
                if (col + 1 < 9) {
                    ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
                    ChessPiece piece_ur = board.getPiece(pos_ur);
                    if (piece_ur != null && color != piece_ur.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ur, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
                    ChessPiece piece_ul = board.getPiece(pos_ul);
                    if (piece_ul != null && color != piece_ul.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ul, null));
                    }
                }
            }
            else if (row + 1 < 9) {
                ChessPosition pos_u = new ChessPosition(row + 1, col);
                if (board.getPiece(pos_u) == null) {
                    p_m.add(new ChessMove(position, pos_u, null));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
                    ChessPiece piece_ur = board.getPiece(pos_ur);
                    if (piece_ur != null && color != piece_ur.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ur, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
                    ChessPiece piece_ul = board.getPiece(pos_ul);
                    if (piece_ul != null && color != piece_ul.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_ul, null));
                    }
                }
            }
        }
        else{


            if (row - 1 == 1) {
                ChessPosition pos_d = new ChessPosition(row - 1, col);
                if (board.getPiece(pos_d) == null) {
                    p_m.add(new ChessMove(position, pos_d, ChessPiece.PieceType.QUEEN));
                    p_m.add(new ChessMove(position, pos_d, ChessPiece.PieceType.ROOK));
                    p_m.add(new ChessMove(position, pos_d, ChessPiece.PieceType.BISHOP));
                    p_m.add(new ChessMove(position, pos_d, ChessPiece.PieceType.KNIGHT));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.QUEEN));
                        p_m.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.ROOK));
                        p_m.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.BISHOP));
                        p_m.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.KNIGHT));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.QUEEN));
                        p_m.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.ROOK));
                        p_m.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.BISHOP));
                        p_m.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.KNIGHT));
                    }
                }

            }
            else if (row == 7) {
                ChessPosition pos_d1 = new ChessPosition(row - 1, col);
                ChessPosition pos_d2 = new ChessPosition(row - 2, col);
                boolean presence = true;
                if (board.getPiece(pos_d1) == null) {
                    p_m.add(new ChessMove(position, pos_d1, null));
                } else {
                    presence = false;
                }
                if (board.getPiece(pos_d2) == null && presence) {
                    p_m.add(new ChessMove(position, pos_d2, null));
                }
                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dr, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dl, null));
                    }
                }
            }
            else if (row - 1 > 0) {
                ChessPosition pos_d = new ChessPosition(row - 1, col);
                if (board.getPiece(pos_d) == null) {
                    p_m.add(new ChessMove(position, pos_d, null));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dr, null));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        p_m.add(new ChessMove(position, pos_dl, null));
                    }
                }
            }


        }
        return p_m;

            }


        }




