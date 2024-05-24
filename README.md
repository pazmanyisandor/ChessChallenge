# Chess Game - Assignment
Developer: Sandor Pazmanyi
This project was made in the first half of 2024, for the Software Development subject
at the University of Debrecen.

## The task
"There is a chessboard on which a knight and a king are placed as shown in Figure 1.
The task is to move any piece to the square marked with a 'C'.
You can only move the piece that is currently able to capture the other piece according to the rules of chess."

## Specifications given by the professor
- The software should follow the MVC principle
- It should be able to run tests on it, with JUnit5
- It should have the capability to save and load its state, using Gson
- it should have a leaderboard

## Short description by the developer
The program has two UI components, the main and the leaderboard.
The core parts of the software are the 'ChessState.java' file, which models the game; and the
'ChessController.java' file, which handles the interaction between the User and the ChessState class.

## Possible Solution
START   King: (2, 1) | Knight: (2, 2) | Goal: (0, 6)

1. King: (2, 1) | Knight: (1, 0) | Goal: (0, 6) | **Move: Knight(-1, -2)**
2. King: (2, 1) | Knight: (0, 2) | Goal: (0, 6) | **Move: Knight(-1,  2)**
3. King: (1, 2) | Knight: (0, 2) | Goal: (0, 6) | **Move: King  (-1,  1)**
4. King: (1, 2) | Knight: (2, 1) | Goal: (0, 6) | **Move: Knight( 2, -1)**
5. King: (1, 2) | Knight: (3, 3) | Goal: (0, 6) | **Move: Knight( 1,  2)**
6. King: (2, 3) | Knight: (3, 3) | Gaal: (0, 6) | **Move: King  ( 1,  1)**
7. King: (2, 3) | Knight: (1, 4) | Goal: (0, 6) | **Move: Knight(-2,  1)**
8. King: (2, 3) | Knight: (0, 6) | Goal: (0, 6) | **Move: Knight(-1,  2)**

SOLVED  