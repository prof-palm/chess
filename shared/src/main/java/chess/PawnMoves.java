package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMoves extends PieceMoveCalculator {

    Collection<ChessMove> pawnMoveList = new ArrayList<>() {
    };

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
        if(color == WHITE) {

            if (row + 1 == 8) {
                ChessPosition positionUp = new ChessPosition(row + 1, col);
                if (board.getPiece(positionUp) == null) {
                    pawnMoveList.add(new ChessMove(position, positionUp, ChessPiece.PieceType.QUEEN));
                    pawnMoveList.add(new ChessMove(position, positionUp, ChessPiece.PieceType.ROOK));
                    pawnMoveList.add(new ChessMove(position, positionUp, ChessPiece.PieceType.BISHOP));
                    pawnMoveList.add(new ChessMove(position, positionUp, ChessPiece.PieceType.KNIGHT));
                }

                if (col + 1 < 9) {
                    ChessPosition positionUpRight = new ChessPosition(row + 1, col + 1);
                    ChessPiece pieceUpRight = board.getPiece(positionUpRight);
                    if (pieceUpRight != null && color != pieceUpRight.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, positionUpRight, ChessPiece.PieceType.QUEEN));
                        pawnMoveList.add(new ChessMove(position, positionUpRight, ChessPiece.PieceType.ROOK));
                        pawnMoveList.add(new ChessMove(position, positionUpRight, ChessPiece.PieceType.BISHOP));
                        pawnMoveList.add(new ChessMove(position, positionUpRight, ChessPiece.PieceType.KNIGHT));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition positionUpLeft = new ChessPosition(row + 1, col - 1);
                    ChessPiece pieceUpLeft = board.getPiece(positionUpLeft);
                    if (pieceUpLeft != null && color != pieceUpLeft.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, positionUpLeft, ChessPiece.PieceType.QUEEN));
                        pawnMoveList.add(new ChessMove(position, positionUpLeft, ChessPiece.PieceType.ROOK));
                        pawnMoveList.add(new ChessMove(position, positionUpLeft, ChessPiece.PieceType.BISHOP));
                        pawnMoveList.add(new ChessMove(position, positionUpLeft, ChessPiece.PieceType.KNIGHT));
                    }
                }

            }
            else if (row == 2) {
                ChessPosition positionUpTwo = new ChessPosition(row + 1, col);
                ChessPosition pieceUpTwo = new ChessPosition(row + 2, col);
                boolean presence = true;
                if (board.getPiece(positionUpTwo) == null) {
                    pawnMoveList.add(new ChessMove(position, positionUpTwo, null));
                } else {
                    presence = false;
                }
                if (board.getPiece(pieceUpTwo) == null && presence) {
                    pawnMoveList.add(new ChessMove(position, pieceUpTwo, null));
                }
                if (col + 1 < 9) {
                    ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
                    ChessPiece piece_ur = board.getPiece(pos_ur);
                    if (piece_ur != null && color != piece_ur.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_ur, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
                    ChessPiece piece_ul = board.getPiece(pos_ul);
                    if (piece_ul != null && color != piece_ul.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_ul, null));
                    }
                }
            }
            else if (row + 1 < 9) {
                ChessPosition pos_u = new ChessPosition(row + 1, col);
                if (board.getPiece(pos_u) == null) {
                    pawnMoveList.add(new ChessMove(position, pos_u, null));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_ur = new ChessPosition(row + 1, col + 1);
                    ChessPiece piece_ur = board.getPiece(pos_ur);
                    if (piece_ur != null && color != piece_ur.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_ur, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_ul = new ChessPosition(row + 1, col - 1);
                    ChessPiece piece_ul = board.getPiece(pos_ul);
                    if (piece_ul != null && color != piece_ul.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_ul, null));
                    }
                }
            }
        }
        else{


            if (row - 1 == 1) {
                ChessPosition pos_d = new ChessPosition(row - 1, col);
                if (board.getPiece(pos_d) == null) {
                    pawnMoveList.add(new ChessMove(position, pos_d, ChessPiece.PieceType.QUEEN));
                    pawnMoveList.add(new ChessMove(position, pos_d, ChessPiece.PieceType.ROOK));
                    pawnMoveList.add(new ChessMove(position, pos_d, ChessPiece.PieceType.BISHOP));
                    pawnMoveList.add(new ChessMove(position, pos_d, ChessPiece.PieceType.KNIGHT));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.QUEEN));
                        pawnMoveList.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.ROOK));
                        pawnMoveList.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.BISHOP));
                        pawnMoveList.add(new ChessMove(position, pos_dr, ChessPiece.PieceType.KNIGHT));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.QUEEN));
                        pawnMoveList.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.ROOK));
                        pawnMoveList.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.BISHOP));
                        pawnMoveList.add(new ChessMove(position, pos_dl, ChessPiece.PieceType.KNIGHT));
                    }
                }

            }
            else if (row == 7) {
                ChessPosition pos_d1 = new ChessPosition(row - 1, col);
                ChessPosition pos_d2 = new ChessPosition(row - 2, col);
                boolean presence = true;
                if (board.getPiece(pos_d1) == null) {
                    pawnMoveList.add(new ChessMove(position, pos_d1, null));
                } else {
                    presence = false;
                }
                if (board.getPiece(pos_d2) == null && presence) {
                    pawnMoveList.add(new ChessMove(position, pos_d2, null));
                }
                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dr, null));
                    }
                }
                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dl, null));
                    }
                }
            }
            else if (row - 1 > 0) {
                ChessPosition pos_d = new ChessPosition(row - 1, col);
                if (board.getPiece(pos_d) == null) {
                    pawnMoveList.add(new ChessMove(position, pos_d, null));
                }

                if (col + 1 < 9) {
                    ChessPosition pos_dr = new ChessPosition(row - 1, col + 1);
                    ChessPiece piece_dr = board.getPiece(pos_dr);
                    if (piece_dr != null && color != piece_dr.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dr, null));
                    }
                }

                if (col - 1 > 0) {
                    ChessPosition pos_dl = new ChessPosition(row - 1, col - 1);
                    ChessPiece piece_dl = board.getPiece(pos_dl);
                    if (piece_dl != null && color != piece_dl.getTeamColor()) {
                        pawnMoveList.add(new ChessMove(position, pos_dl, null));
                    }
                }
            }


        }
        return pawnMoveList;

            }


        }




