package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves extends PieceMoveCalculator{
     Collection<ChessMove> bishopMoveList = new ArrayList<>() {};

     public void bishopMoveUpRight(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
         for (int i = row + 1, j = col + 1; i < 9 && j < 9; i++, j++) {
             ChessPosition pos = new ChessPosition(i, j);
             ChessPiece piece = board.getPiece(pos);
             if (piece == null) {
                 bishopMoveList.add(new ChessMove(position, pos, null));
             } else {
                 if (piece.getTeamColor() == color) {
                     break;
                 } else {
                     bishopMoveList.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
     }
    public void bishopMoveUpLeft(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for (int i = row + 1, j = col - 1; i < 9 && j > 0; i++, j--) {
            ChessPosition pos = new ChessPosition(i, j);
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                bishopMoveList.add(new ChessMove(position, pos, null));
            } else {
                if (piece.getTeamColor() == color) {
                    break;
                } else {
                    bishopMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }
    public void bishopMoveDownRight(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for(int i = row - 1, j = col + 1; i > 0 && j < 9; i--, j++){
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                bishopMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == color){
                    break;
                }
                else{
                    bishopMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }

    public void bishopMoveDownLeft(int row, int col, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        for(int i = row - 1, j = col - 1; i > 0 && j > 0; i--, j--){
            ChessPosition pos = new ChessPosition(i,j);
            ChessPiece piece = board.getPiece(pos);
            if( piece == null){
                bishopMoveList.add(new ChessMove(position, pos, null));
            }
            else{
                if(piece.getTeamColor() == color){
                    break;
                }
                else{
                    bishopMoveList.add(new ChessMove(position, pos, null));
                    break;
                }
            }
        }
    }




     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
         int row = position.getRow();
         int col = position.getColumn();
         ChessPiece pieceAtPosition = board.getPiece(position);
         ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
         bishopMoveUpRight(row, col, board, position, color);
         bishopMoveUpLeft(row, col, board, position, color);
         bishopMoveDownRight(row, col, board, position, color);
         bishopMoveDownLeft(row, col, board, position, color);

         return bishopMoveList;
     }

 }
