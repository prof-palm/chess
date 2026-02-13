package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;



/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {
    ChessBoard game_board = new ChessBoard();
    TeamColor teamTurn = TeamColor.WHITE;


    public ChessGame() {
        game_board.resetBoard();

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
        ChessPiece piece = game_board.getPiece(startPosition);
        Collection<ChessMove> valid_moves = new ArrayList<>();
        if(piece == null){
            return null;

        }
        else {
            Collection<ChessMove> poss_moves = piece.pieceMoves(game_board, startPosition);
            ChessGame.TeamColor color = piece.getTeamColor();
            for (ChessMove move : poss_moves) {
                ChessBoard clone = game_board.clone();
                shadowMove(move, clone);
                if (!isInCheck(color, clone)) {
                    valid_moves.add(move);
                }

            }
            return valid_moves;

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
        Collection<ChessMove> v_moves = validMoves(start);
        ChessPiece s_piece = game_board.getPiece(start);
        if(s_piece != null && s_piece.getTeamColor() != teamTurn){
            throw new InvalidMoveException();
        }
        if(v_moves == null){
            throw new InvalidMoveException();
        }
        else {
            Collection<ChessMove> actual_move = new ArrayList<>();
            for (ChessMove v_move : v_moves) {
                if (v_move.equals (move)) {
                    actual_move.add(v_move);
                    if(s_piece.getPieceType() == ChessPiece.PieceType.PAWN && (end.getRow() == 8 || end.getRow() == 1 )){
                        ChessPiece.PieceType promo = move.getPromotionPiece();
                        ChessPiece promo_piece = new ChessPiece(teamTurn, promo);
                        game_board.addPiece(start, null);
                        game_board.addPiece(end, null);
                        game_board.addPiece(end, promo_piece);
                    }
                    else{
                        game_board.addPiece(start, null);
                        game_board.addPiece(end, null);
                        game_board.addPiece(end, s_piece);
                    }

                }


            }
            if(actual_move.isEmpty()){
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
        ChessPiece s_piece  = clone.getPiece(start);
        clone.addPiece(start, null);
        clone.addPiece(end, null);
        clone.addPiece(end, s_piece);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, game_board);
        }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        Collection<ChessMove> enemy_mvs  = new ArrayList<>() ;
        ChessPosition k_position = findKing(teamColor, board);
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i,j)) == null || board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor ){
                    doNothing();
                }
                else{
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    enemy_mvs.addAll(piece.pieceMoves(board, new ChessPosition(i,j)));

                }
            }

        }
        for(ChessMove enemy_mv : enemy_mvs){
             if(enemy_mv.getEndPosition().equals(k_position)){
                 return true;
             }
        }
        return false;
    }
    public ChessPosition findKing(TeamColor teamColor) {
        return findKing(teamColor, game_board);
    }

    public ChessPosition findKing(TeamColor teamColor, ChessBoard board){
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(board.getPiece(new ChessPosition(i,j)) == null || board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor ){
                    doNothing();
                }
                else{
                    ChessPiece p_type = board.getPiece(new ChessPosition(i, j));
                    if(p_type.getPieceType() == ChessPiece.PieceType.KING){
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
        Collection<ChessMove> all_poss_m = allValidMoves(teamColor);
        if(isInCheck(teamColor) && all_poss_m.isEmpty()){
            return true;
        }
        return false;
    }

    public Collection<ChessMove> allValidMoves(TeamColor teamColor){
        Collection<ChessMove> all_poss_m = new ArrayList<>();
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++){
                if(game_board.getPiece(new ChessPosition(i,j)) == null || game_board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor ){
                    doNothing();
                }
                else{
                    all_poss_m.addAll(validMoves(new ChessPosition(i,j)));

                }
            }

        }
        return all_poss_m;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> all_poss_m = allValidMoves(teamColor);
        return !isInCheck(teamColor) && all_poss_m.isEmpty();

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        game_board = board;
    }

    public void doNothing(){

    }
    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return game_board;
    }
    public ChessGame clone(){
        try{
        ChessGame clone = (ChessGame) super.clone();
        clone.setBoard(game_board);
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
        return Objects.equals(game_board, chessGame.game_board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(game_board, teamTurn);
    }


}
