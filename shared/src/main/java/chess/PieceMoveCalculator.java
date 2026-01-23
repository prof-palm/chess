package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class PieceMoveCalculator {

    abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position );

}