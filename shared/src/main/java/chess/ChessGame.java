package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;



/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to
 * this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {
    ChessBoard gameBoard = new ChessBoard();
    TeamColor teamTurn = TeamColor.WHITE;


    public ChessGame() {
        gameBoard.resetBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> validMoveList = new ArrayList<>();
        if(piece == null){
            return null;

        }
        else {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, startPosition);
            ChessGame.TeamColor color = piece.getTeamColor();
            for (ChessMove move : possibleMoves) {
                ChessBoard clone = gameBoard.clone();
                shadowMove(move, clone);
                if (!isInCheck(color, clone)) {
                    validMoveList.add(move);
                }

            }
            return validMoveList;

        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //need to see if moves is within valid moves collection.
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        Collection<ChessMove> validMoveList = validMoves(start);
        ChessPiece pieceAtPosition = gameBoard.getPiece(start);
        if(pieceAtPosition != null && pieceAtPosition.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }
        if(validMoveList == null){
            throw new InvalidMoveException();
        }
        else {
            Collection<ChessMove> actualMoveList = new ArrayList<>();
            for (ChessMove validMove : validMoveList) {
                if (validMove.equals (move)) {
                    actualMoveList.add(validMove);
                    if(pieceAtPosition.getPieceType() == ChessPiece.PieceType.PAWN && (end.getRow() == 8 || end.getRow() == 1 )){
                        ChessPiece.PieceType promo = move.getPromotionPiece();
                        ChessPiece promotionPiece = new ChessPiece(teamTurn, promo);
                        gameBoard.addPiece(start, null);
                        gameBoard.addPiece(end, null);
                        gameBoard.addPiece(end, promotionPiece);
                    }
                    else{
                        gameBoard.addPiece(start, null);
                        gameBoard.addPiece(end, null);
                        gameBoard.addPiece(end, pieceAtPosition);
                    }

                }


            }
            if(actualMoveList.isEmpty()){
                throw new InvalidMoveException();
            }

        }

        if(teamTurn == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
            }
        else{
            setTeamTurn(TeamColor.WHITE);
        }

        }
    /**
     * does the hypothetical moving of piece, does not check for valid move
     * does not set the team turn
     */

    public void shadowMove(ChessMove move, ChessBoard clone){
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceAtPosition  = clone.getPiece(start);
        clone.addPiece(start, null);
        clone.addPiece(end, null);
        clone.addPiece(end, pieceAtPosition);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, gameBoard);
        }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        Collection<ChessMove> enemyMoves  = new ArrayList<>() ;
        ChessPosition kingPosition = findKing(teamColor, board);
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i,j)) == null || board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor ){
                    doNothing();
                }
                else{
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    enemyMoves.addAll(piece.pieceMoves(board, new ChessPosition(i,j)));

                }
            }

        }
        for(ChessMove enemyMove : enemyMoves){
             if(enemyMove.getEndPosition().equals(kingPosition)){
                 return true;
             }
        }
        return false;
    }
    public ChessPosition findKing(TeamColor teamColor) {
        return findKing(teamColor, gameBoard);
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard board){
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i,j)) == null || board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor ){
                    doNothing();
                }
                else{
                    ChessPiece pieceType = board.getPiece(new ChessPosition(i, j));
                    if(pieceType.getPieceType() == ChessPiece.PieceType.KING){
                        return new ChessPosition(i, j);

                    }
                }
            }
        }

        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> allPossibleMoves = allValidMoves(teamColor);
        if(isInCheck(teamColor) && allPossibleMoves.isEmpty()){
            return true;
        }
        return false;
    }

    public Collection<ChessMove> allValidMoves(TeamColor teamColor){
        Collection<ChessMove> allPossibleMoves = new ArrayList<>();
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(gameBoard.getPiece(new ChessPosition(i,j)) == null || gameBoard.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor ){
                    doNothing();
                }
                else{
                    allPossibleMoves.addAll(validMoves(new ChessPosition(i,j)));

                }
            }

        }
        return allPossibleMoves;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allPossibleMoves = allValidMoves(teamColor);
        return !isInCheck(teamColor) && allPossibleMoves.isEmpty();

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    public void doNothing(){

    }
    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
    public ChessGame clone(){
        try{
        ChessGame clone = (ChessGame) super.clone();
        clone.setBoard(gameBoard);
        return clone;


        }
        catch(CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }


}
