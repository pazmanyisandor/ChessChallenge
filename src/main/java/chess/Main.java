package chess;

import chess.model.ChessState;
import puzzle.solver.BreadthFirstSearch;

public class Main {
    public static void main(String[] args) {
        ChessState initialState = new ChessState(2, 1, 2, 2);

        BreadthFirstSearch solver = new BreadthFirstSearch();
        solver.solveAndPrintSolution(initialState);
    }
}