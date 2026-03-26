package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoves extends PieceMoveCalculator{
     Collection<ChessMove> bishopMoveList = new ArrayList<>() {};

     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
         int row = position.getRow();
         int col = position.getColumn();
         ChessPiece pieceAtPosition = board.getPiece(position);
         ChessGame.TeamColor ally = pieceAtPosition.getTeamColor();
         for (int i = row + 1, j = col + 1; i < 9 && j < 9; i++, j++) {
             ChessPosition pos = new ChessPosition(i, j);
             ChessPiece piece = board.getPiece(pos);
             if (piece == null) {
                 bishopMoveList.add(new ChessMove(position, pos, null));
             } else {
                 if (piece.getTeamColor() == ally) {
                     break;
                 } else {
                     bishopMoveList.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for (int i = row + 1, j = col - 1; i < 9 && j > 0; i++, j--) {
             ChessPosition pos = new ChessPosition(i, j);
             ChessPiece piece = board.getPiece(pos);
             if (piece == null) {
                 bishopMoveList.add(new ChessMove(position, pos, null));
             } else {
                 if (piece.getTeamColor() == ally) {
                     break;
                 } else {
                     bishopMoveList.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for(int i = row - 1, j = col + 1; i > 0 && j < 9; i--, j++){
             ChessPosition pos = new ChessPosition(i,j);
             ChessPiece piece = board.getPiece(pos);
             if( piece == null){
                 bishopMoveList.add(new ChessMove(position, pos, null));
             }
             else{
                 if(piece.getTeamColor() == ally){
                     break;
                 }
                 else{
                     bishopMoveList.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }
         for(int i = row - 1, j = col - 1; i > 0 && j > 0; i--, j--){
             ChessPosition pos = new ChessPosition(i,j);
             ChessPiece piece = board.getPiece(pos);
             if( piece == null){
                 bishopMoveList.add(new ChessMove(position, pos, null));
             }
             else{
                 if(piece.getTeamColor() == ally){
                     break;
                 }
                 else{
                     bishopMoveList.add(new ChessMove(position, pos, null));
                     break;
                 }
             }
         }

         return bishopMoveList;
     }

 }
