package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

 public class BishopMoves extends PieceMoveCalculator{
     Collection<ChessMove> b_m = new ArrayList<>() {
     };
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
         int row = position.getRow();
         int col = position.getColumn();
         ChessPiece s_piece = board.getPiece(position);
         ChessGame.TeamColor ally = s_piece.getTeamColor();
         for (int i = row + 1, j = col + 1; i < 9 && j < 9; i++, j++) {
             ChessPosition pos = new ChessPosition(i, j);
             ChessPiece piece = board.getPiece(pos);
             if (piece == null) {
                 b_m.add(new ChessMove(position, pos, null));
             } else {
                 if (piece.getTeamColor() == ally) {
                     break;
                 } else {
                     b_m.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for (int i = row + 1, j = col - 1; i < 9 && j > 0; i++, j--) {
             ChessPosition pos = new ChessPosition(i, j);
             ChessPiece piece = board.getPiece(pos);
             if (piece == null) {
                 b_m.add(new ChessMove(position, pos, null));
             } else {
                 if (piece.getTeamColor() == ally) {
                     break;
                 } else {
                     b_m.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for(int i = row - 1, j = col + 1; i > 0 && j < 9; i--, j++){
             ChessPosition pos = new ChessPosition(i,j);
             ChessPiece piece = board.getPiece(pos);
             if( piece == null){
                 b_m.add(new ChessMove(position, pos, null));
             }
             else{
                 if(piece.getTeamColor() == ally){
                     break;
                 }
                 else{
                     b_m.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for(int i = row - 1, j = col - 1; i > 0 && j > 0; i--, j--){
             ChessPosition pos = new ChessPosition(i,j);
             ChessPiece piece = board.getPiece(pos);
             if( piece == null){
                 b_m.add(new ChessMove(position, pos, null));
             }
             else{
                 if(piece.getTeamColor() == ally){
                     break;
                 }
                 else{
                     b_m.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }

         return b_m;
     }

 }
