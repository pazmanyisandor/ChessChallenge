package chess.model;

import puzzle.TwoPhaseMoveState;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class ChessState implements TwoPhaseMoveState<String> {
    private int kingX, kingY;
    private int knightX, knightY;
    private static final int TARGET_X = 0, TARGET_Y = 6; // Goal position

    public ChessState(int kingX, int kingY, int knightX, int knightY) {
        this.kingX = kingX;
        this.kingY = kingY;
        this.knightX = knightX;
        this.knightY = knightY;
    }

    @Override
    public boolean isLegalToMoveFrom(String piece) {
        if (piece.equals("King")) {
            return isAttackedByKnight(kingX, kingY);
        } else if (piece.equals("Knight")) {
            return isAttackedByKing(knightX, knightY);
        }
        return false;
    }

    private boolean isAttackedByKnight(int x, int y) {
        int[][] knightMoves = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
        for (int[] move : knightMoves) {
            if (knightX + move[0] == x && knightY + move[1] == y) {
                return true;
            }
        }
        return false;
    }

    private boolean isAttackedByKing(int x, int y) {
        int[][] kingMoves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] move : kingMoves) {
            if (kingX + move[0] == x && kingY + move[1] == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSolved() {
        return (kingX == TARGET_X && kingY == TARGET_Y) || (knightX == TARGET_X && knightY == TARGET_Y);
    }

    @Override
    public boolean isLegalMove(TwoPhaseMove<String> move) {
        String piece = move.from();
        String[] parts = move.to().split(" ");
        int newX = Integer.parseInt(parts[1]);
        int newY = Integer.parseInt(parts[2]);
        if (piece.equals("King")) {
            return isLegalKingMove(newX, newY);
        } else if (piece.equals("Knight")) {
            return isLegalKnightMove(newX, newY);
        }
        return false;
    }

    private boolean isLegalKingMove(int newX, int newY) {
        return Math.abs(newX - kingX) <= 1 && Math.abs(newY - kingY) <= 1;
    }

    private boolean isLegalKnightMove(int newX, int newY) {
        int dx = Math.abs(newX - knightX);
        int dy = Math.abs(newY - knightY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public void makeMove(TwoPhaseMove<String> move) {
        if (isLegalMove(move)) {
            String piece = move.from();
            String[] parts = move.to().split(" ");
            int newX = Integer.parseInt(parts[1]);
            int newY = Integer.parseInt(parts[2]);
            if (piece.equals("King")) {
                kingX = newX;
                kingY = newY;
            } else if (piece.equals("Knight")) {
                knightX = newX;
                knightY = newY;
            }
        }
    }

    @Override
    public Set<TwoPhaseMove<String>> getLegalMoves() {
        Set<TwoPhaseMove<String>> legalMoves = new HashSet<>();
        if (isLegalToMoveFrom("King")) {
            int[][] kingMoves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
            for (int[] move : kingMoves) {
                int newX = kingX + move[0];
                int newY = kingY + move[1];
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                    legalMoves.add(new TwoPhaseMove<>("King", "King " + newX + " " + newY));
                }
            }
        } else if (isLegalToMoveFrom("Knight")) {
            int[][] knightMoves = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
            for (int[] move : knightMoves) {
                int newX = knightX + move[0];
                int newY = knightY + move[1];
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                    legalMoves.add(new TwoPhaseMove<>("Knight", "Knight " + newX + " " + newY));
                }
            }
        }
        return legalMoves;
    }

    @Override
    public TwoPhaseMoveState<String> clone() {
        return new ChessState(kingX, kingY, knightX, knightY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessState that = (ChessState) o;
        return kingX == that.kingX && kingY == that.kingY && knightX == that.knightX && knightY == that.knightY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kingX, kingY, knightX, knightY);
    }

    @Override
    public String toString() {
        return String.format("King: (%d, %d), Knight: (%d, %d)", kingX, kingY, knightX, knightY);
    }
}
