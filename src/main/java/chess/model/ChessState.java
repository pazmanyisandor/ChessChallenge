package chess.model;

import puzzle.TwoPhaseMoveState;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Represents the state of a chess game with a king and a knight, including their positions and move count.
 * Implements the {@link puzzle.TwoPhaseMoveState} interface.
 */
public class ChessState implements TwoPhaseMoveState<String> {
    private int kingX, kingY;
    private int knightX, knightY;
    private static final int TARGET_X = 0, TARGET_Y = 6; // Goal position
    private int moveCount = 0;

    /**
     * Constructs a {@code ChessState} with the specified positions and move count.
     *
     * @param kingX The x-coordinate of the king.
     * @param kingY The y-coordinate of the king.
     * @param knightX The x-coordinate of the knight.
     * @param knightY The y-coordinate of the knight.
     * @param moveCount The number of moves made so far.
     */
    public ChessState(int kingX, int kingY, int knightX, int knightY, int moveCount) {
        this.kingX = kingX;
        this.kingY = kingY;
        this.knightX = knightX;
        this.knightY = knightY;
        this.moveCount = moveCount;
    }

    /**
     * @return The x-coordinate of the king.
     */
    public int getKingX() {
        return kingX;
    }

    /**
     * @return The y-coordinate of the king.
     */
    public int getKingY() {
        return kingY;
    }

    /**
     * @return The x-coordinate of the knight.
     */
    public int getKnightX() {
        return knightX;
    }

    /**
     * @return The y-coordinate of the knight.
     */
    public int getKnightY() {
        return knightY;
    }

    /**
     * @return The x-coordinate of the goal position.
     */
    public int getGoalX() {
        return TARGET_X;
    }

    /**
     * @return The y-coordinate of the goal position.
     */
    public int getGoalY() {
        return TARGET_Y;
    }

    /**
     * @return The number of moves made so far.
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     * Determines if it is legal to move from the current position for the specified piece.
     *
     * @param piece The piece to check legality for ("King" or "Knight").
     * @return {@code true} if it is legal to move from the current position, {@code false} otherwise.
     */
    @Override
    public boolean isLegalToMoveFrom(String piece) {
        if (piece.equals("King")) {
            return isAttackedByKnight(kingX, kingY);
        } else if (piece.equals("Knight")) {
            return isAttackedByKing(knightX, knightY);
        }
        return false;
    }

    /**
     * Determines if the specified position is attacked by the knight.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return {@code true} if the position is attacked by the knight, {@code false} otherwise.
     */
    private boolean isAttackedByKnight(int x, int y) {
        int[][] knightMoves = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
        for (int[] move : knightMoves) {
            if (knightX + move[0] == x && knightY + move[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the specified position is attacked by the king.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @return {@code true} if the position is attacked by the king, {@code false} otherwise.
     */
    private boolean isAttackedByKing(int x, int y) {
        int[][] kingMoves = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] move : kingMoves) {
            if (kingX + move[0] == x && kingY + move[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the current state is the goal state.
     *
     * @return {@code true} if the current state is the goal state, {@code false} otherwise.
     */
    @Override
    public boolean isSolved() {
        return (kingX == TARGET_X && kingY == TARGET_Y) || (knightX == TARGET_X && knightY == TARGET_Y);
    }

    /**
     * Determines if the specified move is legal.
     *
     * @param move The move to check legality for.
     * @return {@code true} if the move is legal, {@code false} otherwise.
     */
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

    /**
     * Determines if the specified position is a legal move for the king.
     *
     * @param newX The new x-coordinate for the king.
     * @param newY The new y-coordinate for the king.
     * @return {@code true} if the move is legal for the king, {@code false} otherwise.
     */
    private boolean isLegalKingMove(int newX, int newY) {
        return Math.abs(newX - kingX) <= 1 && Math.abs(newY - kingY) <= 1;
    }

    /**
     * Determines if the specified position is a legal move for the knight.
     *
     * @param newX The new x-coordinate for the knight.
     * @param newY The new y-coordinate for the knight.
     * @return {@code true} if the move is legal for the knight, {@code false} otherwise.
     */
    private boolean isLegalKnightMove(int newX, int newY) {
        int dx = Math.abs(newX - knightX);
        int dy = Math.abs(newY - knightY);
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    /**
     * Makes the specified move if it is legal.
     *
     * @param move The move to make.
     */
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

            moveCount = moveCount + 1;
        }
    }

    /**
     * Gets the set of legal moves from the current state.
     *
     * @return A set of legal moves.
     */
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

    /**
     * @return A clone of the current state.
     */
    @Override
    public TwoPhaseMoveState<String> clone() {
        return new ChessState(kingX, kingY, knightX, knightY, moveCount);
    }

    /**
     * Checks if the current state is equal to the specified object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the current state is equal to the specified object, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessState that = (ChessState) o;
        return kingX == that.kingX && kingY == that.kingY && knightX == that.knightX && knightY == that.knightY;
    }

    /**
     * @return The hash code of the current state.
     */
    @Override
    public int hashCode() {
        return Objects.hash(kingX, kingY, knightX, knightY);
    }

    /**
     * @return A string representation of the current state.
     */
    @Override
    public String toString() {
        return String.format("King: (%d, %d), Knight: (%d, %d)", kingX, kingY, knightX, knightY);
    }
}