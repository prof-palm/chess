package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoves extends PieceMoveCalculator {
        Collection<ChessMove> rookMoveList = new ArrayList<>() {
        };

    public void slider(int row, int col, int rowDirection, int colDirection, ChessBoard board, ChessPosition position, ChessGame.TeamColor color){
        int r = row + rowDirection;
        int c = col + colDirection;
        while(r > 0 && r < 9 && c > 0 && c < 9){
            ChessPosition pos = new ChessPosition(r,c);
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
            r+= rowDirection;
            c+=colDirection;

        }
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece pieceAtPosition = board.getPiece(position);
        ChessGame.TeamColor color = pieceAtPosition.getTeamColor();
        slider(row, col, 0, 1, board, position, color);
        slider(row, col, 1, 0, board, position, color);
        slider(row, col, 0, -1, board, position, color);
        slider(row, col, -1, 0, board, position, color);
        return rookMoveList;
    }

}



