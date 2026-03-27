package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends PieceMoveCalculator {
        Collection<ChessMove> rookMoveList = new ArrayList<>() {
        };

    public void rookMoveUp(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for (int i = row + 1; i < 9; i++) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                rookMoveList.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == color) {
                    break;
                } else {
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }
    public void rookMoveDown(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for (int i = row - 1; i > 0; i--) {
            ChessPosition pos = new ChessPosition(i, col);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                rookMoveList.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == color) {
                    break;
                } else {
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }
    public void rookMoveRight(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for(int j = col + 1; j < 9; j++){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                rookMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == color){
                    break;
                }
                else{
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }
    public void rookMoveLeft(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for(int j = col - 1; j > 0; j--){
            ChessPosition pos = new ChessPosition(row,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                rookMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == color){
                    break;
                }
                else{
                    rookMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
        rookMoveUp(row, col, board, position, color);
        rookMoveDown(row, col, board, position, color);
        rookMoveRight(row, col, board, position, color);
        rookMoveLeft(row, col, board, position, color);
        return rookMoveList;
    }

}



