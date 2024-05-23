package chess;

import chess.control.ChessApplication;
import chess.model.ChessState;
import javafx.application.Application;
import puzzle.solver.BreadthFirstSearch;

public class Main {
    public static void main(String[] args) {
        ChessState initialState = new ChessState(2, 1, 2, 2, 0);

        BreadthFirstSearch solver = new BreadthFirstSearch();
        solver.solveAndPrintSolution(initialState);

        Application.launch(ChessApplication.class, args);
    }
}