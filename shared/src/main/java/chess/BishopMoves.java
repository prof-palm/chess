package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves extends PieceMoveCalculator{
     Collection<ChessMove> bishopMoveList = new ArrayList<>() {};

    public void slider(int row, int col, int rowDirection, int colDirection, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        int r = row + rowDirection;
        int c = col + colDirection;
        while(r > 0 && r < 9 && c > 0 && c < 9){
            ChessPosition pos = new ChessPosition(r,c);
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
            r+= rowDirection;
            c+=colDirection;

        }
    }



     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
         int row = position.getRow();
         int col = position.getColumn();
         ChessPiece pieceAtPosition = board.getPiece(position);
         ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
         slider(row, col, 1, 1, board, position, color);
         slider(row, col, -1, 1, board, position, color);
         slider(row, col, 1, -1, board, position, color);
         slider(row, col, -1, -1, board, position, color);

         return bishopMoveList;
     }

 }
