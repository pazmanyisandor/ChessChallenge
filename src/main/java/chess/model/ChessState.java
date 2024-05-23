package chess.model;

import puzzle.TwoPhaseMoveState;

import java.util.Set;

public class ChessState implements puzzle.TwoPhaseMoveState{
    @Override
    public boolean isLegalToMoveFrom(Object o) {
        return false;
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(Object o) {
        return false;
    }

    @Override
    public void makeMove(Object o) {

    }

    @Override
    public Set getLegalMoves() {
        return null;
    }

    @Override
    public TwoPhaseMoveState clone() {
        return null;
    }
}
