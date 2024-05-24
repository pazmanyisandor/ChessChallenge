package chess;

import chess.control.ChessApplication;
import chess.model.ChessState;
import javafx.application.Application;
import puzzle.solver.BreadthFirstSearch;

/**
 * The main class for the Chess Game application.
 * This class initializes the initial state of the chess game,
 * solves the game using a breadth-first search algorithm, and then launches the JavaFX application.
 */
public class Main {
    /**
     * The main method serves as the entry point for the application.
     * It initializes the chess game state, solves the game using breadth-first search, and launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        ChessState initialState = new ChessState(2, 1, 2, 2, 0);

        BreadthFirstSearch solver = new BreadthFirstSearch();
        solver.solveAndPrintSolution(initialState);

        Application.launch(ChessApplication.class, args);
    }
}