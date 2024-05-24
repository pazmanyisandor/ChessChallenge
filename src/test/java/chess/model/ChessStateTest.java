package chess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.TwoPhaseMoveState.TwoPhaseMove;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChessStateTest {
    private ChessState state;

    @BeforeEach
    void setUp() {
        state = new ChessState(2, 1, 2, 2, 0);
    }

    @Test
    void testGetKingX() {
        assertEquals(2, state.getKingX());
    }

    @Test
    void testGetKingY() {
        assertEquals(1, state.getKingY());
    }

    @Test
    void testGetKnightX() {
        assertEquals(2, state.getKnightX());
    }

    @Test
    void testGetKnightY() {
        assertEquals(2, state.getKnightY());
    }

    @Test
    void testGetGoalX() {
        assertEquals(0, state.getGoalX());
    }

    @Test
    void testGetGoalY() {
        assertEquals(6, state.getGoalY());
    }

    @Test
    void testGetMoveCount() {
        assertEquals(0, state.getMoveCount());
    }

    @Test
    void testIsAttackedByKing() {
        ChessState attackingState = new ChessState(2, 2, 3, 3, 0);
        assertTrue(attackingState.isLegalToMoveFrom("Knight"));
        assertFalse(attackingState.isLegalToMoveFrom("King"));
    }

    @Test
    void testIsSolved() {
        // Goal not reached
        assertFalse(state.isSolved());

        // King at goal position
        ChessState kingAtGoal = new ChessState(0, 6, 2, 2, 0);
        assertTrue(kingAtGoal.isSolved());

        // Knight at goal position
        ChessState knightAtGoal = new ChessState(2, 2, 0, 6, 0);
        assertTrue(knightAtGoal.isSolved());
    }

    @Test
    void testIsLegalMove() {
        // Legal King move
        TwoPhaseMove<String> kingMove = new TwoPhaseMove<>("King", "King 3 1");
        assertTrue(state.isLegalMove(kingMove));

        // Illegal King move
        TwoPhaseMove<String> illegalKingMove = new TwoPhaseMove<>("King", "King 4 4");
        assertFalse(state.isLegalMove(illegalKingMove));

        // Legal Knight move
        TwoPhaseMove<String> knightMove = new TwoPhaseMove<>("Knight", "Knight 4 3");
        assertTrue(state.isLegalMove(knightMove));

        // Illegal Knight move
        TwoPhaseMove<String> illegalKnightMove = new TwoPhaseMove<>("Knight", "Knight 5 5");
        assertFalse(state.isLegalMove(illegalKnightMove));
    }

    @Test
    void testMakeMove() {
        // Making a legal King move
        TwoPhaseMove<String> kingMove = new TwoPhaseMove<>("King", "King 3 1");
        state.makeMove(kingMove);
        assertEquals(new ChessState(3, 1, 2, 2, 1), state);

        // Making a legal Knight move
        state = new ChessState(1, 0, 2, 2, 1);
        TwoPhaseMove<String> knightMove = new TwoPhaseMove<>("Knight", "Knight 4 3");
        state.makeMove(knightMove);
        assertEquals(new ChessState(1, 0, 4, 3, 2), state);
    }

    @Test
    void testClone() {
        ChessState clonedState = (ChessState) state.clone();
        assertEquals(state, clonedState);
        assertNotSame(state, clonedState);
    }

    @Test
    void testEquals() {
        ChessState sameState = new ChessState(2, 1, 2, 2, 0);
        ChessState differentState = new ChessState(0, 0, 0, 0, 0);
        assertEquals(state, sameState);
        assertNotEquals(state, differentState);
    }

    @Test
    void testHashCode() {
        ChessState sameState = new ChessState(2, 1, 2, 2, 0);
        ChessState differentState = new ChessState(0, 0, 0, 0, 0);
        assertEquals(state.hashCode(), sameState.hashCode());
        assertNotEquals(state.hashCode(), differentState.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "King: (2, 1), Knight: (2, 2)";
        assertEquals(expectedString, state.toString());
    }
}