package chess.model;

import com.google.gson.Gson;
import org.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * A class that handles saving and loading the game state of a chess game to and from a JSON file.
 * This class uses the Gson library for JSON serialization and deserialization.
 */
public class GameSaverLoader {
    /**
     * The file path where the game state will be saved to and loaded from.
     * The default path is "gameState.json".
     */
    public static String FILE_PATH = "gameState.json";
    private static final Gson gson = new Gson();

    /**
     * Saves the current game state to a JSON file.
     * This includes positions of the king and knight, the goal position, and the move count.
     * @param kingPosition Current position of the king on the board.
     * @param knightPosition Current position of the knight on the board.
     * @param goalPosition Target position to achieve on the board.
     * @param moveCount Number of moves made so far.
     */
    public void saveGame(int[] kingPosition, int[] knightPosition, int[] goalPosition, int moveCount) {
        GameState gameData = new GameState(kingPosition, knightPosition, goalPosition, moveCount);
        String json = gson.toJson(gameData);

        Path path = Paths.get(FILE_PATH);
        try (OutputStream outputStream = Files.newOutputStream(path);
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            writer.write(json);
            Logger.info("Game saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Error while saving game: " + e);
        }
    }

    /**
     * Loads a game state from a JSON file.
     * Reads the game data file specified by `FILE_PATH` and converts it back into a GameState object.
     * @return GameState object if successful, null if an error occurs during file reading or JSON parsing.
     */
    public GameState loadGame() {
        Path path = Paths.get(FILE_PATH);
        try (InputStream inputStream = Files.newInputStream(path);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {

            GameState gameState = gson.fromJson(reader, GameState.class);
            Logger.info("Game loaded successfully.");
            return gameState;
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error("Error while loading game: " + e);
            return null;
        }
    }

    /**
     * Nested class within GameSaverLoader to represent the state of a chess game.
     * This class encapsulates all relevant game data including the positions of the king and knight pieces,
     * the goal position, and the total number of moves made. It provides methods to access this data,
     * and a static method to sort a list of GameState instances by move count.
     * This class is utilized primarily for serialization and deserialization of game states to and from JSON.
     */
    public static class GameState {
        private int[] kingPosition;
        private int[] knightPosition;
        private int[] goalPosition;
        private int moveCount;

        /**
         * Constructor that initializes a new GameState object with specified positions and move count.
         * Logs the creation of a new GameState object.
         * @param kingPosition Initial position of the king on the chess board.
         * @param knightPosition Initial position of the knight on the chess board.
         * @param goalPosition Target position on the chess board.
         * @param moveCount Initial number of moves made in the game.
         */
        public GameState(int[] kingPosition, int[] knightPosition, int[] goalPosition, int moveCount) {
            this.kingPosition = kingPosition;
            this.knightPosition = knightPosition;
            this.goalPosition = goalPosition;
            this.moveCount = moveCount;

            Logger.info("GameState object created.");
        }

        /**
         * Returns the current position of the king.
         * @return Array representing the king's position on the board.
         */
        public int[] getKingPosition() {
            return kingPosition;
        }

        /**
         * Returns the current position of the knight.
         * @return Array representing the knight's position on the board.
         */
        public int[] getKnightPosition() {
            return knightPosition;
        }

        /**
         * Returns the goal position on the board.
         * @return Array representing the target position to win the game.
         */
        public int[] getGoalPosition() {
            return goalPosition;
        }

        /**
         * Returns the number of moves made so far in the game.
         * @return The total move count.
         */
        public int getMoveCount() {
            return moveCount;
        }

        /**
         * Sorts a list of GameState objects by the number of moves, in ascending order.
         * Useful for comparing and sorting game states based on game progress.
         * @param gameStates List of GameState objects to sort.
         */
        public static void sortGameStatesByMoveCount(List<GameState> gameStates) {
            Collections.sort(gameStates, (gs1, gs2) -> Integer.compare(gs1.getMoveCount(), gs2.getMoveCount()));
            Logger.info("Game State collection sorted.");
        }
    }
}
