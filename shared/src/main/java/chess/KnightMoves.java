package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoves extends PieceMoveCalculator {
    Collection<ChessMove> knightMoveList = new ArrayList<>() {
    };

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor color = pieceAtPosition.getTeamColor();

        //up right
        if (row + 2 < 9 && col + 1 < 9) {
                ChessPosition positionUpperRight = new ChessPosition(row + 2, col + 1);
                ChessPiece pieceUpperRight = board.getPiece(positionUpperRight);
                if (pieceUpperRight == null) {
                    knightMoveList.add(new ChessMove(position, positionUpperRight, null));

                }
                else if (color != pieceUpperRight.getTeamColor()){
                    knightMoveList.add(new ChessMove(position, positionUpperRight, null));
                }

        }
        //up left
        if (row + 2 < 9 && col - 1 > 0) {
            ChessPosition positionUpperLeft = new ChessPosition(row + 2, col - 1);
            ChessPiece pieceUpperLeft = board.getPiece(positionUpperLeft);
            if (pieceUpperLeft == null) {
                knightMoveList.add(new ChessMove(position, positionUpperLeft, null));

            }
            else if (color != pieceUpperLeft.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionUpperLeft, null));
            }

        }
        //down right
        if (row - 2 > 0  && col + 1 < 9) {
            ChessPosition positionLowerRight = new ChessPosition(row - 2, col + 1);
            ChessPiece pieceLowerRight = board.getPiece(positionLowerRight);
            if (pieceLowerRight == null) {
                knightMoveList.add(new ChessMove(position, positionLowerRight, null));

            }
            else if (color != pieceLowerRight.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionLowerRight, null));
            }

        }
        //down left
        if (row - 2 > 0 && col - 1 > 0) {
            ChessPosition positionLowerLeft = new ChessPosition(row - 2, col - 1);
            ChessPiece pieceLowerLeft = board.getPiece(positionLowerLeft);
            if (pieceLowerLeft == null) {
                knightMoveList.add(new ChessMove(position, positionLowerLeft, null));

            }
            else if (color != pieceLowerLeft.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionLowerLeft, null));
            }

        }
        //right up
        if (row + 1 < 9 && col + 2 < 9) {
            ChessPosition positionRightUp = new ChessPosition(row + 1, col + 2);
            ChessPiece pieceRightUp = board.getPiece(positionRightUp);
            if (pieceRightUp == null) {
                knightMoveList.add(new ChessMove(position, positionRightUp, null));

            }
            else if (color != pieceRightUp.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionRightUp, null));
            }

        }
        //right down
        if (row - 1 > 0 && col + 2 < 9) {
            ChessPosition positionRightDown = new ChessPosition(row - 1, col + 2);
            ChessPiece pieceRightDown = board.getPiece(positionRightDown);
            if (pieceRightDown == null) {
                knightMoveList.add(new ChessMove(position, positionRightDown, null));

            }
            else if (color != pieceRightDown.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionRightDown, null));
            }

        }
        //left up
        if (row + 1 < 9 && col - 2 > 0 ) {
            ChessPosition positionLeftUp = new ChessPosition(row + 1, col - 2);
            ChessPiece pieceLeftUp = board.getPiece(positionLeftUp);
            if (pieceLeftUp == null) {
                knightMoveList.add(new ChessMove(position, positionLeftUp, null));

            }
            else if (color != pieceLeftUp.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionLeftUp, null));
            }

        }
        // left down
        if (row -  1 > 0 && col - 2 > 0 ) {
            ChessPosition positionLeftDown = new ChessPosition(row - 1, col - 2);
            ChessPiece pieceLeftDown = board.getPiece(positionLeftDown);
            if (pieceLeftDown == null) {
                knightMoveList.add(new ChessMove(position, positionLeftDown, null));

            }
            else if (color != pieceLeftDown.getTeamColor()){
                knightMoveList.add(new ChessMove(position, positionLeftDown, null));
            }

        }
    return knightMoveList;
    }
}
