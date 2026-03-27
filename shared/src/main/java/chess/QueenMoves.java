package chess;

import java.util.ArrayList;
import java.util.Collection;


//Literally just create instances of both my Bishops moves class and RookMoves class, and then add them together
public class QueenMoves extends PieceMoveCalculator {
    Collection<ChessMove> queenMoveList = new ArrayList<>() {
    };
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position ){
        Collection<ChessMove> bishopsList =  bishopMovesList(board, position);
        Collection<ChessMove> rooksList = rookMovesList(board, position);
        queenMoveList.addAll(bishopsList);
        queenMoveList.addAll(rooksList);
        return queenMoveList;



}

public Collection<ChessMove> bishopMovesList(ChessBoard board, ChessPosition myPosition){
    BishopMoves bishopMoves = new BishopMoves();
    return bishopMoves.pieceMoves(board, myPosition);
}

public Collection<ChessMove> rookMovesList(ChessBoard board, ChessPosition myPosition){
    RookMoves rookMoves = new RookMoves();
    return rookMoves.pieceMoves(board, myPosition);
    }





}










